package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.enums.NotificationType;
import com.metaverse.workflow.common.fileservice.StorageService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.enums.BillRemarksStatus;
import com.metaverse.workflow.enums.RemarkBy;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.login.repository.LoginRepository;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.nontrainingExpenditures.Dto.BenchmarkingStudyRequest;
import com.metaverse.workflow.nontrainingExpenditures.repository.BenchmarkingStudyRepository;
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

@RequiredArgsConstructor
@Service
public class BenchmarkingStudyServiceAdepter implements BenchmarkingStudyService {
    private final BenchmarkingStudyRepository benchmarkingStudyRepository;
    private final NonTrainingSubActivityRepository subActivityRepository;
    private final ProgramSessionFileRepository programSessionFileRepository;
    private final StorageService storageService;
    private final LoginRepository loginRepository;
    private final NotificationServiceImpl notificationService;

    @Override
    public WorkflowResponse createBenchmarkingStudy(BenchmarkingStudyRequest request, MultipartFile file) throws DataException {
        NonTrainingSubActivity subActivity = subActivityRepository.findById(request.getNonTrainingSubActivityId())
                .orElseThrow(() -> new DataException("SubActivity not found", "SUB_ACTIVITY_NOT_FOUND", 400));
        BenchmarkingStudy entity = BenchmarkingStudyMapper.mapToThrustSectorVisitReq(request, subActivity);
        BenchmarkingStudy savedEntity = benchmarkingStudyRepository.save(entity);
        if (file != null && !file.isEmpty()) {
            String filePath = this.storageFiles(file, savedEntity.getBenchmarkingStudyId(), "BenchmarkingStudy");
            savedEntity.setUploadBillUrl(filePath);
            benchmarkingStudyRepository.save(savedEntity);
            programSessionFileRepository.save(ProgramSessionFile.builder()
                    .fileType("File")
                    .filePath(filePath)
                    .benchmarkingStudy(savedEntity)
                    .build());
        }
        return WorkflowResponse.builder()
                .status(200)
                .message("Benchmarking Study saved successfully")
                .data(BenchmarkingStudyMapper.mapToThrustSectorVisitRes(savedEntity))
                .build();
    }

    @Override
    @Transactional
    public WorkflowResponse updateBenchmarkingStudy(Long benchmarkingStudyId, BenchmarkingStudyRequest request) throws DataException {
        BenchmarkingStudy entity = benchmarkingStudyRepository.findById(benchmarkingStudyId)
                .orElseThrow(() -> new DataException("Benchmarking Study not found", "BENCHMARKING_STUDY_NOT_FOUND", 400));

        BenchmarkingStudy benchmarkingStudy = BenchmarkingStudyMapper.mapToUpdateBenchmarkingStudy(entity, request);
        BenchmarkingStudy updatedEntity = benchmarkingStudyRepository.save(benchmarkingStudy);

        return WorkflowResponse.builder()
                .status(200)
                .message("Benchmarking Study updated successfully")
                .data(BenchmarkingStudyMapper.mapToThrustSectorVisitRes(updatedEntity))
                .build();
    }

    @Override
    public WorkflowResponse getBenchmarkingStudyBySubActivityId(Long subActivityId) throws DataException {
        List<BenchmarkingStudy> studies = benchmarkingStudyRepository
                .findByNonTrainingSubActivity_SubActivityId(subActivityId)
                .orElseThrow(() -> new DataException("SubActivity not found", "SUB_ACTIVITY_NOT_FOUND", 400));

        return WorkflowResponse.builder()
                .status(200)
                .message("Benchmarking Studies fetched successfully")
                .data(studies.stream()
                        .map(BenchmarkingStudyMapper::mapToThrustSectorVisitRes)
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    @Override
    public WorkflowResponse deleteBenchmarkingStudy(Long benchmarkingStudyId) throws DataException {
        BenchmarkingStudy entity = benchmarkingStudyRepository.findById(benchmarkingStudyId)
                .orElseThrow(() -> new DataException("Benchmarking Study not found", "BENCHMARKING_STUDY_NOT_FOUND", 400));
        programSessionFileRepository.deleteByBenchmarkingStudy_BenchmarkingStudyId(benchmarkingStudyId);
        benchmarkingStudyRepository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Benchmarking Study deleted successfully")
                .build();
    }

    @Override
    public WorkflowResponse getBenchmarkingStudyById(Long benchmarkingStudyId) throws DataException {
        BenchmarkingStudy entity = benchmarkingStudyRepository.findById(benchmarkingStudyId)
                .orElseThrow(() -> new DataException("Benchmarking Study not found", "BENCHMARKING_STUDY_NOT_FOUND", 400));

        return WorkflowResponse.builder()
                .status(200)
                .message("Benchmarking Study fetched successfully")
                .data(BenchmarkingStudyMapper.mapToThrustSectorVisitRes(entity))
                .build();
    }

    public String storageFiles(MultipartFile file, Long TravelAndTransportId, String folderName) {
        String filePath = storageService.store(file, TravelAndTransportId, folderName);
        return filePath;
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

        BenchmarkingStudy expenditure = benchmarkingStudyRepository.findById(remarks.getNonTrainingExpenditureId())
                .orElseThrow(() -> new DataException("Non Training Expenditure not found", "EXPENDITURE_NOT_FOUND", 400));

        // 2. Create and associate SPIU Comment
        NonTrainingSpiuComments spiuComment = NonTrainingExpenditureMapper.mapToEntitySpiuComments(remarks, user);
        spiuComment.setBenchmarkingStudy(expenditure);
        expenditure.getSpiuComments().add(spiuComment);

        // 3. Create and associate Agency Comment
        NonTrainingAgencyComments agencyComment = NonTrainingExpenditureMapper.mapToEntityAgencyComments(remarks, user);
        agencyComment.setBenchmarkingStudy(expenditure);
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
            expenditure.setStatus(status);
        }

        // 5. Save changes
        benchmarkingStudyRepository.save(expenditure);

        return WorkflowResponse.builder()
                .message("Remark or Response added successfully.")
                .status(200)
                .build();
    }

}
