package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.enums.NotificationType;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.enums.BillRemarksStatus;
import com.metaverse.workflow.enums.RemarkBy;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.login.repository.LoginRepository;
import com.metaverse.workflow.login.service.LoginService;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.nontrainingExpenditures.Dto.NIMSMEContentDetailsDto;
import com.metaverse.workflow.nontrainingExpenditures.repository.NIMSMEContentDetailsRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import com.metaverse.workflow.notifications.dto.GlobalNotificationRequest;
import com.metaverse.workflow.notifications.service.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NIMSMEContentDetailsService {

    @Autowired
    private NIMSMEContentDetailsRepository repository;

    @Autowired
    private NonTrainingSubActivityRepository subActivityRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private NotificationServiceImpl notificationService;

    public NIMSMEContentDetailsDto saveContent(NIMSMEContentDetailsDto dto) {
        NonTrainingSubActivity subActivity = subActivityRepository.findById(dto.getSubActivityId())
                .orElseThrow(() -> new RuntimeException("SubActivity not found with id " + dto.getSubActivityId()));

        NIMSMEContentDetails content = new NIMSMEContentDetails();
        content.setContentType(dto.getContentType());
        content.setContentName(dto.getContentName());
        content.setDurationOrPages(dto.getDurationOrPages());
        content.setTopic(dto.getTopic());
        content.setStatus(dto.getStatus());
        content.setDateOfUpload(dto.getDateOfUpload());
        content.setUrl(dto.getUrl());
        content.setNonTrainingSubActivity(subActivity);

        return entityToDto(repository.save(content));
    }


    public List<NIMSMEContentDetailsDto> getAllContent() {
        return repository.findAll().stream().map(c -> entityToDto(c)).toList();
    }

    public NIMSMEContentDetailsDto getContentById(Long id) {
        NIMSMEContentDetails entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content not found with id " + id));
        return entityToDto(entity);
    }


    public NIMSMEContentDetailsDto updateContent(Long id, NIMSMEContentDetailsDto
            dto) {
        return repository.findById(id).map(existing -> {
            existing.setContentType(dto.getContentType());
            existing.setContentName(dto.getContentName());
            existing.setDurationOrPages(dto.getDurationOrPages());
            existing.setTopic(dto.getTopic());
            existing.setStatus(dto.getStatus());
            existing.setDateOfUpload(dto.getDateOfUpload());
            existing.setUrl(dto.getUrl());

            if (dto.getSubActivityId() != null) {
                NonTrainingSubActivity subActivity = subActivityRepository.findById(dto.getSubActivityId())
                        .orElseThrow(() -> new RuntimeException("SubActivity not found with id " + dto.getSubActivityId()));
                existing.setNonTrainingSubActivity(subActivity);
            }

            return entityToDto(repository.save(existing));
        }).orElseThrow(() -> new RuntimeException("Content not found with id " + id));
    }

    public void deleteContent(Long id) {
        repository.deleteById(id);
    }

    public void deleteBySubActivityId(Long subActivityId) {
        repository.deleteByNonTrainingSubActivity_SubActivityId(subActivityId);
    }

    public List<NIMSMEContentDetailsDto> getContentByNonTrainingSubActivityId(Long nonTrainingSubActivityId) {
        List<NIMSMEContentDetails> entity = repository.findByNonTrainingSubActivity_SubActivityId(nonTrainingSubActivityId);
        return entity.stream().map(e -> entityToDto(e)).toList();
    }


    public static NIMSMEContentDetailsDto entityToDto(NIMSMEContentDetails nimsmeContentDetails) {
        NIMSMEContentDetailsDto content = new NIMSMEContentDetailsDto();
        content.setId(nimsmeContentDetails.getId());
        content.setContentType(nimsmeContentDetails.getContentType());
        content.setContentName(nimsmeContentDetails.getContentName());
        content.setDurationOrPages(nimsmeContentDetails.getDurationOrPages());
        content.setTopic(nimsmeContentDetails.getTopic());
        content.setStatus(nimsmeContentDetails.getStatus());
        content.setDateOfUpload(nimsmeContentDetails.getDateOfUpload());
        content.setUrl(nimsmeContentDetails.getUrl());
        content.setBillStatus(nimsmeContentDetails.getBillStatus());
        content.setSubActivityId(nimsmeContentDetails.getNonTrainingSubActivity().getSubActivityId());
        content.setAgencyComments(
                Optional.ofNullable(nimsmeContentDetails.getAgencyComments())
                        .orElse(List.of())
                        .stream()
                        .map(NonTrainingAgencyComments::getFormattedRemark)
                        .toList()
        );
        content.setSpiuComments(
                Optional.ofNullable(nimsmeContentDetails.getSpiuComments())
                        .orElse(List.of())
                        .stream()
                        .map(NonTrainingSpiuComments::getFormattedRemark)
                        .toList()
        );
        content.setBillStatus(nimsmeContentDetails.getBillStatus());
        return content;
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
        return loginRepository.findFirstByUserRoleIgnoreCase("ADMIN")
                .orElseThrow(() -> new DataException("Admin user not found", "ADMIN_NOT_FOUND", 400));
    }

    public WorkflowResponse addRemarkOrResponse(NonTrainingExpenditureRemarksDTO remarks, BillRemarksStatus status) throws DataException {

        // 1. Retrieve user and expenditure
        User user = loginRepository.findById(remarks.getUserId())
                .orElseThrow(() -> new DataException("User not found for ID: " + remarks.getUserId(), "USER_NOT_FOUND", 400));

        NIMSMEContentDetails expenditure = repository.findById(remarks.getNonTrainingExpenditureId())
                .orElseThrow(() -> new DataException("Non Training Expenditure not found", "EXPENDITURE_NOT_FOUND", 400));

        // 2. Create and associate SPIU Comment
        NonTrainingSpiuComments spiuComment = NonTrainingExpenditureMapper.mapToEntitySpiuComments(remarks, user);
        spiuComment.setNimsmeContentDetails(expenditure);
        expenditure.getSpiuComments().add(spiuComment);

        // 3. Create and associate Agency Comment
        NonTrainingAgencyComments agencyComment = NonTrainingExpenditureMapper.mapToEntityAgencyComments(remarks, user);
        agencyComment.setNimsmeContentDetails(expenditure);
        expenditure.getAgencyComments().add(agencyComment);

        // ======== NOTIFICATION LOGIC (UPDATED) ========

        if (user.getUserRole().equalsIgnoreCase("ADMIN")) {

            // ---- ADMIN → AGENCY ADMIN ----
            Agency agency = expenditure.getNonTrainingSubActivity().getNonTrainingActivity().getAgency();
            User agencyAdmin = getAgencyAdminOrFallback(agency);

            GlobalNotificationRequest req = GlobalNotificationRequest.builder()
                    .userId(agencyAdmin.getUserId())
                    .sentBy(RemarkBy.ADMIN)
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
            User adminUser = loginRepository.findFirstByUserRoleIgnoreCase("ADMIN")
                    .orElseThrow(() -> new DataException("Admin user not found", "ADMIN_NOT_FOUND", 400));

            Agency agency = expenditure.getNonTrainingSubActivity().getNonTrainingActivity().getAgency();

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
            expenditure.setBillStatus(status);
        }

        // 5. Save changes
        repository.save(expenditure);

        return WorkflowResponse.builder()
                .message("Remark or Response added successfully.")
                .status(200)
                .build();
    }
}
