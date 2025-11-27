package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.enums.NotificationType;
import com.metaverse.workflow.common.fileservice.FileUpdateUtil;
import com.metaverse.workflow.common.fileservice.StorageService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.enums.BillRemarksStatus;
import com.metaverse.workflow.enums.RemarkBy;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.login.repository.LoginRepository;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.nontrainingExpenditures.Dto.CentralDataRequest;
import com.metaverse.workflow.nontrainingExpenditures.repository.CentralDataRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import com.metaverse.workflow.notifications.dto.GlobalNotificationRequest;
import com.metaverse.workflow.notifications.service.NotificationServiceImpl;
import com.metaverse.workflow.program.repository.ProgramSessionFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service@RequiredArgsConstructor
public class NIMSMECentralDataService {
    private final CentralDataRepository centralDataRepository;
    private final NonTrainingSubActivityRepository subActivityRepository;
    private final ProgramSessionFileRepository programSessionFileRepository;
    private final StorageService storageService;
    private final LoginRepository loginRepository;
    private final NotificationServiceImpl notificationService;

    public WorkflowResponse createCentralData(CentralDataRequest request, MultipartFile file) throws DataException {
        NonTrainingSubActivity subActivity = subActivityRepository.findById(request.getSubActivityId())
                .orElseThrow(() -> new DataException("SubActivity not found", "SUB_ACTIVITY_NOT_FOUND", 400));

        NIMSMECentralData entity = NIMSMECentralDataMapper.mapToCentralDataReq(request, subActivity);
        NIMSMECentralData savedEntity = centralDataRepository.save(entity);

        if (file != null && !file.isEmpty()) {
            String filePath = this.storageFiles(file, savedEntity.getCentralDataId(), "NIMSMECentralData");
            savedEntity.setUploadBillUrl(filePath);
            centralDataRepository.save(savedEntity);

            programSessionFileRepository.save(ProgramSessionFile.builder()
                    .fileType("File")
                    .filePath(filePath)
                    .nimsmeCentralData(savedEntity)
                    .build());
        }

        return WorkflowResponse.builder()
                .status(200)
                .message("Central Data saved successfully")
                .data(NIMSMECentralDataMapper.mapToCentralDataRes(savedEntity))
                .build();
    }


    @Transactional
    public WorkflowResponse updateCentralData(Long centralDataId, CentralDataRequest request, MultipartFile file) throws DataException {
        NIMSMECentralData entity = centralDataRepository.findById(centralDataId)
                .orElseThrow(() -> new DataException("Central Data not found", "CENTRAL_DATA_NOT_FOUND", 400));


        NIMSMECentralData updatedEntity = NIMSMECentralDataMapper.mapToCentralDataReq(request, entity.getNonTrainingSubActivity());
        updatedEntity.setCentralDataId(centralDataId);

        String newPath = FileUpdateUtil.replaceFile(
                file,
                updatedEntity.getUploadBillUrl(),
                (uploadedFile) -> this.storageFiles(file, updatedEntity.getCentralDataId(), "NonTrainingExpenditure"),
                () -> centralDataRepository.save(updatedEntity)
        );
        updatedEntity.setUploadBillUrl(newPath);
        programSessionFileRepository.updateFilePathByCentralDataId(
                newPath,
                updatedEntity.getCentralDataId());
        NIMSMECentralData saved = centralDataRepository.save(updatedEntity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Central Data updated successfully")
                .data(NIMSMECentralDataMapper.mapToCentralDataRes(saved))
                .build();
    }


    public WorkflowResponse getCentralDataBySubActivityId(Long subActivityId) throws DataException {
        List<NIMSMECentralData> dataList = centralDataRepository
                .findByNonTrainingSubActivity_SubActivityId(subActivityId)
                .orElseThrow(() -> new DataException("SubActivity not found", "SUB_ACTIVITY_NOT_FOUND", 400));

        return WorkflowResponse.builder()
                .status(200)
                .message("Central Data fetched successfully")
                .data(dataList.stream()
                        .map(NIMSMECentralDataMapper::mapToCentralDataRes)
                        .collect(Collectors.toList()))
                .build();
    }


    @Transactional
    public WorkflowResponse deleteCentralData(Long centralDataId) throws DataException {
        NIMSMECentralData entity = centralDataRepository.findById(centralDataId)
                .orElseThrow(() -> new DataException("Central Data not found", "CENTRAL_DATA_NOT_FOUND", 400));

        programSessionFileRepository.deleteByNimsmeCentralData_CentralDataId(centralDataId);
        centralDataRepository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Central Data deleted successfully")
                .build();
    }


    public WorkflowResponse getCentralDataById(Long centralDataId) throws DataException {
        NIMSMECentralData entity = centralDataRepository.findById(centralDataId)
                .orElseThrow(() -> new DataException("Central Data not found", "CENTRAL_DATA_NOT_FOUND", 400));

        return WorkflowResponse.builder()
                .status(200)
                .message("Central Data fetched successfully")
                .data(NIMSMECentralDataMapper.mapToCentralDataRes(entity))
                .build();
    }

    public String storageFiles(MultipartFile file, Long centralDataId, String folderName) {
        return storageService.store(file, centralDataId, folderName);
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

        NIMSMECentralData expenditure = centralDataRepository.findById(remarks.getNonTrainingExpenditureId())
                .orElseThrow(() -> new DataException("Non Training Expenditure not found", "EXPENDITURE_NOT_FOUND", 400));

        // 2. Create and associate SPIU Comment
        NonTrainingSpiuComments spiuComment = NonTrainingExpenditureMapper.mapToEntitySpiuComments(remarks, user);
        spiuComment.setNimsmeCentralData(expenditure);
        expenditure.getSpiuComments().add(spiuComment);

        // 3. Create and associate Agency Comment
        NonTrainingAgencyComments agencyComment = NonTrainingExpenditureMapper.mapToEntityAgencyComments(remarks, user);
        agencyComment.setNimsmeCentralData(expenditure);
        expenditure.getAgencyComments().add(agencyComment);

        // ======== NOTIFICATION LOGIC (UPDATED) ========

        String userRole = user.getUserRole();
        if (userRole.equalsIgnoreCase("ADMIN")  || userRole.equalsIgnoreCase("FINANCE") ) {

            // ---- ADMIN → AGENCY ADMIN ----
            Agency agency = expenditure.getNonTrainingSubActivity().getNonTrainingActivity().getAgency();
            User agencyAdmin = getAgencyAdminOrFallback(agency);

            GlobalNotificationRequest req = GlobalNotificationRequest.builder()
                    .userId(agencyAdmin.getUserId())
                    .sentBy(agency.getAgencyName())
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
                    .sentBy(agency.getAgencyName())
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
        centralDataRepository.save(expenditure);

        return WorkflowResponse.builder()
                .message("Remark or Response added successfully.")
                .status(200)
                .build();
    }

}
