package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.enums.NotificationType;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.enums.BillRemarksStatus;
import com.metaverse.workflow.enums.RemarkBy;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.login.repository.LoginRepository;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.nontrainingExpenditures.Dto.NonTrainingResourceExpenditureDto;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingResourceExpenditureRepo;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingResourceRepository;
import com.metaverse.workflow.notifications.dto.GlobalNotificationRequest;
import com.metaverse.workflow.notifications.service.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NonTrainingResourceExpenditureService {

    private final NonTrainingResourceExpenditureRepo expenditureRepo;
    private final NonTrainingResourceRepository resourceRepo;
    private final LoginRepository userRepo;
    private final NotificationServiceImpl notificationService;

    public NonTrainingResourceExpenditureDto save(NonTrainingResourceExpenditureDto dto) {
        NonTrainingResource resource = resourceRepo.findById(dto.getResourceId())
                .orElseThrow(() -> new RuntimeException("Resource not found with id " + dto.getResourceId()));

        NonTrainingResourceExpenditure entity = convertToEntity(dto);
        entity.setNonTrainingResource(resource);

        NonTrainingResourceExpenditure saved = expenditureRepo.save(entity);
        return convertToDto(saved);
    }

    private NonTrainingResourceExpenditureDto convertToDto(NonTrainingResourceExpenditure entity) {
        return NonTrainingResourceExpenditureDto.builder()
                .nonTrainingResourceExpenditureId(entity.getNonTrainingResourceExpenditureId())
                .resourceId(entity.getNonTrainingResource().getResourceId())
                .amount(entity.getAmount())
                .paymentForMonth(entity.getPaymentForMonth())
                .dateOfPayment(String.valueOf(entity.getDateOfPayment()))
                .agencyComments(
                Optional.ofNullable(entity.getAgencyComments())
                        .orElse(List.of())
                        .stream()
                        .map(NonTrainingAgencyComments::getFormattedRemark)
                        .toList())
                .spiuComments(
                Optional.ofNullable(entity.getSpiuComments())
                        .orElse(List.of())
                        .stream()
                        .map(NonTrainingSpiuComments::getFormattedRemark)
                        .toList())
                .status(entity.getStatus())
                .build();
    }

    private NonTrainingResourceExpenditure convertToEntity(NonTrainingResourceExpenditureDto dto) {
        return NonTrainingResourceExpenditure.builder()
                .nonTrainingResourceExpenditureId(dto.getNonTrainingResourceExpenditureId())
                .amount(dto.getAmount())
                .paymentForMonth(dto.getPaymentForMonth())
                .dateOfPayment(DateUtil.covertStringToDate(dto.getDateOfPayment())).build();
    }


    private User getAgencyAdminOrFallback(Agency agency) throws DataException {
        if (agency != null && agency.getUsers() != null) {
            Optional<User> agencyAdmin = agency.getUsers()
                    .stream()
                    .filter(u -> u.getUserRole() != null &&
                            u.getUserRole().equalsIgnoreCase("AGENCY_ADMIN"))
                    .findFirst();

            if (agencyAdmin.isPresent()) return agencyAdmin.get();
        }

        // fallback to system admin
        return userRepo.findFirstByUserRoleIgnoreCase("ADMIN")
                .orElseThrow(() -> new DataException("Admin user not found", "ADMIN_NOT_FOUND", 400));
    }

    public WorkflowResponse addRemarkOrResponse(NonTrainingExpenditureRemarksDTO remarks, BillRemarksStatus status) throws DataException {

        // 1. Retrieve user and expenditure
        User user = userRepo.findById(remarks.getUserId())
                .orElseThrow(() -> new DataException("User not found for ID: " + remarks.getUserId(), "USER_NOT_FOUND", 400));

        NonTrainingResourceExpenditure expenditure = expenditureRepo.findById(remarks.getNonTrainingExpenditureId())
                .orElseThrow(() -> new DataException("Non Training Expenditure not found", "EXPENDITURE_NOT_FOUND", 400));

        // 2. Create and associate SPIU Comment
        NonTrainingSpiuComments spiuComment = NonTrainingExpenditureMapper.mapToEntitySpiuComments(remarks, user);
        spiuComment.setNonTrainingResourceExpenditure(expenditure);
        expenditure.getSpiuComments().add(spiuComment);

        // 3. Create and associate Agency Comment
        NonTrainingAgencyComments agencyComment = NonTrainingExpenditureMapper.mapToEntityAgencyComments(remarks, user);
        agencyComment.setNonTrainingResourceExpenditure(expenditure);
        expenditure.getAgencyComments().add(agencyComment);

        // ======== NOTIFICATION LOGIC (UPDATED) ========

        String userRole = user.getUserRole();
        if (userRole.equalsIgnoreCase("ADMIN")  || userRole.equalsIgnoreCase("FINANCE") ) {

            // ---- ADMIN → AGENCY ADMIN ----
            Agency agency = expenditure.getNonTrainingResource().getNonTrainingActivity().getAgency();
            User agencyAdmin = getAgencyAdminOrFallback(agency);

            GlobalNotificationRequest req = GlobalNotificationRequest.builder()
                    .userId(agencyAdmin.getUserId())
                    .sentBy(userRole.equals(RemarkBy.ADMIN.name()) ? RemarkBy.ADMIN : RemarkBy.FINANCE)
                    .notificationType(NotificationType.NON_TRAINING_EXPENDITURE)
                    .message(remarks.getSpiuComments())
                    .agencyId(agency != null ? agency.getAgencyId() : -1L)
                    .programId(-1L)
                    .isRead(false)
                    .participantId(-1L)
                    .build();

            notificationService.saveNotification(req);
        } else {

            // ---- AGENCY → SYSTEM ADMIN ----
            User adminUser = userRepo.findFirstByUserRoleIgnoreCase("ADMIN")
                    .orElseThrow(() -> new DataException("Admin user not found", "ADMIN_NOT_FOUND", 400));

            Agency agency = expenditure.getNonTrainingResource().getNonTrainingActivity().getAgency();

            GlobalNotificationRequest req = GlobalNotificationRequest.builder()
                    .userId(adminUser.getUserId())
                    .sentBy(RemarkBy.AGENCY)
                    .notificationType(NotificationType.NON_TRAINING_EXPENDITURE)
                    .message(remarks.getAgencyComments())
                    .agencyId(agency != null ? agency.getAgencyId() : -1L)
                    .programId(-1L)
                    .isRead(false)
                    .participantId(-1L)
                    .build();

            notificationService.saveNotification(req);
        }

        // 4. Update status if provided
        if (status != null) {
            expenditure.setStatus(status);
        }

        // 5. Save changes
        expenditureRepo.save(expenditure);

        return WorkflowResponse.builder()
                .message("Remark or Response added successfully.")
                .status(200)
                .build();
    }

}