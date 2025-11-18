package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.common.enums.NotificationType;
import com.metaverse.workflow.common.fileservice.FileUpdateUtil;
import com.metaverse.workflow.common.fileservice.StorageService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.enums.BillRemarksStatus;
import com.metaverse.workflow.enums.RemarkBy;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.login.repository.LoginRepository;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.nontraining.repository.NonTrainingActivityRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingExpenditureRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingResourceExpenditureRepo;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.ResourceRepo;
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

@Service
@RequiredArgsConstructor
public class NonTrainingExpenditureService {

    private final NonTrainingExpenditureRepository nonTrainingExpenditureRepository;
    private final LoginRepository userRepo;
    private final NotificationServiceImpl notificationService;
    private final NonTrainingExpenditureRepository repository;
    private final AgencyRepository agencyRepository;
    private final NonTrainingSubActivityRepository nonTrainingSubActivityRepository;
    private final ResourceRepo resourceRepo;
    private final NonTrainingResourceExpenditureRepo resourceExpenditureRepo;
    private final StorageService storageService;
    private final ProgramSessionFileRepository programSessionFileRepository;
    private final NonTrainingActivityRepository nonTrainingActivityRepository;

    public WorkflowResponse create(NonTrainingExpenditureDTO dto, MultipartFile file) throws DataException {
        Agency agency = agencyRepository.findById(dto.getAgencyId())
                .orElseThrow(() -> new DataException("Agency not found", "AGENCY_NOT_FOUND", 400));
        NonTrainingSubActivity subActivity = nonTrainingSubActivityRepository.findById(dto.getNonTrainingSubActivityId())
                .orElseThrow(() -> new DataException("Activity not found", "ACTIVITY_NOT_FOUND", 400));

        NonTrainingActivity activity = nonTrainingActivityRepository.findById(dto.getNonTrainingActivityId())
                .orElseThrow(() -> new DataException("Activity not found", "ACTIVITY_NOT_FOUND", 400));


        NonTrainingExpenditure entity = NonTrainingExpenditureMapper.toEntity(dto, agency, activity, subActivity);
        NonTrainingExpenditure save = repository.save(entity);

        if (file != null && !file.isEmpty()) {
            String filePath = this.storageFiles(file, save.getId(), "NonTrainingExpenditure");
            save.setUploadBillUrl(filePath);
            repository.save(save);
            programSessionFileRepository.save(ProgramSessionFile.builder()
                    .fileType("File")
                    .filePath(filePath)
                    .nonTrainingExpenditure(save)
                    .build());
        }

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(NonTrainingExpenditureMapper.toDTO(save)).build();
    }

    public List<NonTrainingExpenditureDTO> getAll() {
        return repository.findAll().stream()
                .map(NonTrainingExpenditureMapper::toDTO)
                .collect(Collectors.toList());
    }

    public NonTrainingExpenditureDTO getById(Long id) throws DataException {
        return repository.findById(id)
                .map(NonTrainingExpenditureMapper::toDTO)
                .orElseThrow(() -> new DataException("Expenditure not found", "EXPENDITURE_NOT_FOUND", 400));
    }


    public NonTrainingExpenditureDTO update(Long id, NonTrainingExpenditureDTO dto, MultipartFile file) throws DataException {
        NonTrainingExpenditure existing = repository.findById(id)
                .orElseThrow(() -> new DataException("Expenditure not found", "EXPENDITURE_NOT_FOUND", 400));

        Agency agency = agencyRepository.findById(dto.getAgencyId())
                .orElseThrow(() -> new DataException("Agency not found", "AGENCY_NOT_FOUND", 400));
        NonTrainingSubActivity subActivity = nonTrainingSubActivityRepository.findById(dto.getNonTrainingSubActivityId())
                .orElseThrow(() -> new DataException("Activity not found", "ACTIVITY_NOT_FOUND", 400));

        NonTrainingActivity activity = nonTrainingActivityRepository.findById(dto.getNonTrainingActivityId())
                .orElseThrow(() -> new DataException("Activity not found", "ACTIVITY_NOT_FOUND", 400));


        NonTrainingExpenditure updated = NonTrainingExpenditureMapper.toEntity(dto, agency, activity, subActivity);
        updated.setId(existing.getId());

        String newPath = FileUpdateUtil.replaceFile(
                file,
                updated.getUploadBillUrl(),
                (uploadedFile) -> this.storageFiles(file, updated.getId(), "NonTrainingExpenditure"),
                () -> repository.save(updated)
        );
        updated.setUploadBillUrl(newPath);
        programSessionFileRepository.updateFilePathByNonTrainingExpenditureId(
                newPath,
                updated.getId()
        );
        return NonTrainingExpenditureMapper.toDTO(repository.save(updated));
    }

    @Transactional
    public void delete(Long id) throws DataException {

        NonTrainingExpenditure existing = repository.findById(id)
                .orElseThrow(() -> new DataException("Expenditure not found", "EXPENDITURE_NOT_FOUND", 400));
        programSessionFileRepository.deleteByNonTrainingExpenditure_Id(id);
        repository.deleteById(id);
    }

    public WorkflowResponse saveResource(NonTrainingResourceDTO resource) throws DataException {
        NonTrainingSubActivity subActivity = nonTrainingSubActivityRepository.findById(resource.getNonTrainingSubActivityId())
                .orElseThrow(() -> new DataException("Sub Activity not found", "SUB_ACTIVITY_NOT_FOUND", 400));
        NonTrainingActivity activity = nonTrainingActivityRepository.findById(resource.getNonTrainingActivityId())
                .orElseThrow(() -> new DataException("Activity not found", "ACTIVITY_NOT_FOUND", 400));

        NonTrainingResource nonTrainingResource = NonTrainingExpenditureMapper.mapToResource(resource, subActivity, activity);
        return WorkflowResponse.builder()
                .message("success")
                .status(200)
                .data(NonTrainingExpenditureMapper.mapToResourceRes(resourceRepo.save(nonTrainingResource)))
                .build();
    }

    public WorkflowResponse updateResource(Long id, NonTrainingResourceDTO resourceDto) {
        if (resourceDto == null) {
            return WorkflowResponse.builder()
                    .status(400).message("Invalid request: Resource data is null").data(null).build();
        }
        NonTrainingResource existing = resourceRepo.findById(id).orElse(null);
        if (existing == null) {
            return WorkflowResponse.builder().status(404).message("Resource not found with id " + id).data(null).build();
        }

        existing.setName(resourceDto.getName() != null ? resourceDto.getName() : existing.getName());
        existing.setDesignation(resourceDto.getDesignation() != null ? resourceDto.getDesignation() : existing.getDesignation());
        existing.setRelevantExperience(resourceDto.getRelevantExperience() != null ? resourceDto.getRelevantExperience() : existing.getRelevantExperience());
        existing.setEducationalQualifications(resourceDto.getEducationalQualification() != null ? resourceDto.getEducationalQualification() : existing.getEducationalQualifications());
        existing.setDateOfJoining(
                resourceDto.getDateOfJoining() != null
                        ? DateUtil.stringToDate(resourceDto.getDateOfJoining(), "dd-MM-yyyy")
                        : existing.getDateOfJoining());
        existing.setMonthlySal(resourceDto.getMonthlySal() != null ? resourceDto.getMonthlySal() : existing.getMonthlySal());
        existing.setBankName(resourceDto.getBankName() != null ? resourceDto.getBankName() : existing.getBankName());
        existing.setIfscCode(resourceDto.getIfscCode() != null ? resourceDto.getIfscCode() : existing.getIfscCode());
        existing.setAccountNo(resourceDto.getAccountNo() != null ? resourceDto.getAccountNo() : existing.getAccountNo());

        NonTrainingResource updated = resourceRepo.save(existing);

        return WorkflowResponse.builder()
                .status(200)
                .message("Resource updated successfully")
                .data(NonTrainingExpenditureMapper.mapToResourceRes(updated))
                .build();
    }

    @Transactional
    public WorkflowResponse deleteResource(Long id) throws DataException {
        NonTrainingResource resource = resourceRepo.findById(id)
                .orElseThrow(() -> new DataException("Resource not found with id " + id, "RESOURCE_NOT_FOUND", 400));
        for (NonTrainingResourceExpenditure exp : resource.getNonTrainingResourceExpenditures()) {
            programSessionFileRepository.deleteByNonTrainingResourceExpenditure_NonTrainingResourceExpenditureId(exp.getNonTrainingResourceExpenditureId());
        }
        resourceRepo.delete(resource);
        return WorkflowResponse.builder()
                .status(200)
                .message("Resource deleted successfully")
                .build();
    }

    public WorkflowResponse saveResourceExpenditure(NonTrainingResourceExpenditureDTO resourceExpenditureDTO, MultipartFile file) throws DataException {
        NonTrainingResource nonTrainingResource = resourceRepo.findById(resourceExpenditureDTO.getResourceId())
                .orElseThrow(() -> new DataException("Resource not found with id " + resourceExpenditureDTO.getResourceId(), "RESOURCE_NOT_FOUND", 400));
        NonTrainingResourceExpenditure expenditure = NonTrainingExpenditureMapper.mapToResourceExpenditure(resourceExpenditureDTO, nonTrainingResource);
        NonTrainingResourceExpenditure save = resourceExpenditureRepo.save(expenditure);
        if (file != null && !file.isEmpty()) {
            String filePath = this.storageFiles(file, save.getNonTrainingResourceExpenditureId(), "NotTrainingResourceExpenditure");
            save.setUploadBillUrl(filePath);
            resourceExpenditureRepo.save(save);
            programSessionFileRepository.save(ProgramSessionFile.builder()
                    .fileType("File")
                    .filePath(filePath)
                    .nonTrainingResourceExpenditure(save)
                    .build());
        }

        return WorkflowResponse.builder()
                .message("success")
                .status(200)
                .data(NonTrainingExpenditureMapper.mapToResourceExpenditureResponse(save))
                .build();
    }


    public WorkflowResponse updateResourceExpenditure(Long expenditureId, NonTrainingResourceExpenditureDTO resourceExpenditureDTO, MultipartFile file) throws DataException {
        NonTrainingResourceExpenditure existingExpenditure = resourceExpenditureRepo.findById(expenditureId)
                .orElseThrow(() -> new DataException("Expenditure not found with id " + expenditureId, "EXPENDITURE_NOT_FOUND", 400));

        existingExpenditure.setAmount(
                resourceExpenditureDTO.getAmount() != null ? resourceExpenditureDTO.getAmount() : existingExpenditure.getAmount()
        );

        existingExpenditure.setPaymentForMonth(
                resourceExpenditureDTO.getPaymentForMonth() != null && !resourceExpenditureDTO.getPaymentForMonth().isEmpty()
                        ? resourceExpenditureDTO.getPaymentForMonth()
                        : existingExpenditure.getPaymentForMonth()
        );

        existingExpenditure.setDateOfPayment(
                resourceExpenditureDTO.getDateOfPayment() != null && !resourceExpenditureDTO.getDateOfPayment().isEmpty()
                        ? DateUtil.stringToDate(resourceExpenditureDTO.getDateOfPayment(), "dd-MM-yyyy")
                        : existingExpenditure.getDateOfPayment()
        );

        String newPath = FileUpdateUtil.replaceFile(
                file,
                existingExpenditure.getUploadBillUrl(),
                (uploadedFile) -> this.storageFiles(uploadedFile, existingExpenditure.getNonTrainingResourceExpenditureId(), "TravelAndTransport"),
                () -> {
                    resourceExpenditureRepo.save(existingExpenditure);
                });
        programSessionFileRepository.updateFilePathByNonTrainingResourceExpenditureId(
                newPath,
                existingExpenditure.getNonTrainingResourceExpenditureId()
        );
        existingExpenditure.setUploadBillUrl(newPath);
        NonTrainingResourceExpenditure updatedExpenditure = resourceExpenditureRepo.save(existingExpenditure);

        return WorkflowResponse.builder()
                .status(200)
                .message("Expenditure updated successfully")
                .data(NonTrainingExpenditureMapper.mapToResourceExpenditureResponse(updatedExpenditure))
                .build();
    }

    @Transactional
    public WorkflowResponse deleteResourceExpenditure(Long expenditureId) throws DataException {
        NonTrainingResourceExpenditure existingExpenditure = resourceExpenditureRepo.findById(expenditureId)
                .orElseThrow(() -> new DataException("Expenditure not found with id " + expenditureId, "EXPENDITURE_NOT_FOUND", 400));
        programSessionFileRepository.deleteByNonTrainingResourceExpenditure_NonTrainingResourceExpenditureId(expenditureId);
        resourceExpenditureRepo.delete(existingExpenditure);

        return WorkflowResponse.builder()
                .status(200)
                .message("Expenditure deleted successfully with id " + expenditureId)
                .build();
    }

    public WorkflowResponse getResourceByNonTrainingSubActivity(Long nonTrainingActivityId) throws DataException {
        List<NonTrainingResource> resourceList;
        resourceList = resourceRepo.findByNonTrainingSubActivity_SubActivityId(nonTrainingActivityId)
                .orElseThrow(() -> new DataException("Resource not found with this activity id " + nonTrainingActivityId, "RESOURCE_NOT_FOUND", 400));
        return WorkflowResponse.builder().status(200)
                .message("success")
                .data(resourceList.stream().map(NonTrainingExpenditureMapper::mapToResourceResForDropdown).toList())
                .build();
    }

    public WorkflowResponse getAllResourceByNonTrainingActivityId(Long nonTrainingActivityId) throws DataException {
        List<NonTrainingResource> resourceList = resourceRepo.findByNonTrainingSubActivity_SubActivityId(nonTrainingActivityId)
                .orElseThrow(() -> new DataException("Resource not found with this activity id " + nonTrainingActivityId, "RESOURCE_NOT_FOUND", 400));
        return WorkflowResponse.builder().status(200)
                .message("success")
                .data(resourceList.stream().map(NonTrainingExpenditureMapper::mapToResourceRes).toList())
                .build();
    }

    public WorkflowResponse getAllExpenditureByNonTrainingActivityId(Long nonTrainingSubActivityId) throws DataException {
        List<NonTrainingExpenditure> expenditureList = repository.findByNonTrainingSubActivity_SubActivityId(nonTrainingSubActivityId)
                .orElseThrow(() -> new DataException("Expenditure not found with this activity id " + nonTrainingSubActivityId, "EXPENDITURE_NOT_FOUND", 400));
        return WorkflowResponse.builder().status(200)
                .message("success")
                .data(expenditureList.stream().map(NonTrainingExpenditureMapper::toDTO).toList())
                .build();
    }

    public WorkflowResponse getAllResourceExpenditureByNonTrainingActivityId(Long nonTrainingSubActivityId) throws DataException {

        List<NonTrainingResource> resourceList = resourceRepo.findByNonTrainingSubActivity_SubActivityId(nonTrainingSubActivityId)
                .orElseThrow(() -> new DataException(
                        "Resource not found with this activity id " + nonTrainingSubActivityId,
                        "RESOURCE_NOT_FOUND",
                        400));

        List<NonTrainingResourceExpenditureDTO> allExpendituresDto =
                resourceList.stream()
                        .flatMap(resource -> resource.getNonTrainingResourceExpenditures().stream())
                        .map(NonTrainingExpenditureMapper::mapToResourceExpenditureResponse) // <-- convert entity → DTO
                        .collect(Collectors.toList());

        return WorkflowResponse.builder()
                .status(200)
                .message("success")
                .data(allExpendituresDto)
                .build();
    }

    public String storageFiles(MultipartFile file, Long TravelAndTransportId, String folderName) {
        String filePath = storageService.store(file, TravelAndTransportId, folderName);
        return filePath;
    }

    public WorkflowResponse addRemarkOrResponse(NonTrainingExpenditureRemarksDTO remarks, BillRemarksStatus status) throws DataException {

        // 1. Retrieve user and expenditure
        User user = userRepo.findById(remarks.getUserId())
                .orElseThrow(() -> new DataException("User not found for ID: " + remarks.getUserId(), "USER_NOT_FOUND", 400));

        NonTrainingExpenditure expenditure = nonTrainingExpenditureRepository.findById(remarks.getNonTrainingExpenditureId())
                .orElseThrow(() -> new DataException("Non Training Expenditure not found", "EXPENDITURE_NOT_FOUND", 400));

        // 2. Create and associate SPIU Comment
        NonTrainingSpiuComments spiuComment = NonTrainingExpenditureMapper.mapToEntitySpiuComments(remarks, user);
        spiuComment.setNonTrainingExpenditure(expenditure);
        expenditure.getSpiuComments().add(spiuComment);

        // 3. Create and associate Agency Comment
        NonTrainingAgencyComments agencyComment = NonTrainingExpenditureMapper.mapToEntityAgencyComments(remarks, user);
        agencyComment.setNonTrainingExpenditure(expenditure);
        expenditure.getAgencyComments().add(agencyComment);

        // ======== NOTIFICATION LOGIC (UPDATED) ========

        if (user.getUserRole().equalsIgnoreCase("ADMIN")) {

            // ---- ADMIN → AGENCY ADMIN ----
            User agencyAdmin = getAgencyAdminOrFallback(expenditure.getAgency());

            GlobalNotificationRequest req = GlobalNotificationRequest.builder()
                    .userId(agencyAdmin.getUserId())
                    .sentBy(RemarkBy.ADMIN)
                    .notificationType(NotificationType.NON_TRAINING_EXPENDITURE)
                    .message(remarks.getSpiuComments())
                    .agencyId(expenditure.getAgency() != null ? expenditure.getAgency().getAgencyId() : -1L)
                    .programId(-1L)
                    .isRead(false)
                    .participantId(-1L)
                    .build();

            notificationService.saveNotification(req);
        }
        else {

            // ---- AGENCY → SYSTEM ADMIN ----
            User adminUser = userRepo.findFirstByUserRoleIgnoreCase("ADMIN")
                    .orElseThrow(() -> new DataException("Admin user not found", "ADMIN_NOT_FOUND", 400));

            GlobalNotificationRequest req = GlobalNotificationRequest.builder()
                    .userId(adminUser.getUserId())
                    .sentBy(RemarkBy.AGENCY)
                    .notificationType(NotificationType.NON_TRAINING_EXPENDITURE)
                    .message(remarks.getAgencyComments())
                    .agencyId(expenditure.getAgency() != null ? expenditure.getAgency().getAgencyId() : -1L)
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
        nonTrainingExpenditureRepository.save(expenditure);

        return WorkflowResponse.builder()
                .message("Remark or Response added successfully.")
                .status(200)
                .build();
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

}

