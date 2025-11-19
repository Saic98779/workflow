package com.metaverse.workflow.nontrainingExpenditures.service;

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
import com.metaverse.workflow.nontrainingExpenditures.Dto.NIMSMEVendorDetailsDto;
import com.metaverse.workflow.nontrainingExpenditures.repository.NIMSMEVendorDetailsRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import com.metaverse.workflow.notifications.dto.GlobalNotificationRequest;
import com.metaverse.workflow.notifications.repository.NotificationRepository;
import com.metaverse.workflow.notifications.service.NotificationServiceImpl;
import com.metaverse.workflow.program.repository.ProgramSessionFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class NIMSMEVendorDetailsService {

    @Autowired
    private NIMSMEVendorDetailsRepository repository;

    @Autowired
    private NonTrainingSubActivityRepository subActivityRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private ProgramSessionFileRepository programSessionFileRepository;

    @Autowired
    private LoginRepository userRepo;

    private NotificationServiceImpl notificationService;
    private NotificationRepository notificationRepository;


    public List<NIMSMEVendorDetailsDto> getAllVendors() {
        return repository.findAll().stream().map(nimsmeVendorDetails ->
                entityToDto(nimsmeVendorDetails)).toList();}

    public NIMSMEVendorDetailsDto getVendorById(Long vendorId) {
        Optional<NIMSMEVendorDetails> byId = repository.findById(vendorId);
        if (byId.isPresent()){
            NIMSMEVendorDetails nimsmeVendorDetails = byId.get();
            entityToDto(nimsmeVendorDetails);
            return entityToDto(nimsmeVendorDetails);
        }
        return null;
    }

    public List<NIMSMEVendorDetailsDto> getSubActivityIdById(Long subActivityId) {
        return repository.findByNonTrainingSubActivity_SubActivityId(subActivityId).stream().map(s -> entityToDto(s)).toList();
    }

    public NIMSMEVendorDetailsDto saveVendor(NIMSMEVendorDetailsDto dto, MultipartFile file) {

        NIMSMEVendorDetails vendor = new NIMSMEVendorDetails();
        vendor.setVendorCompanyName(dto.getVendorCompanyName());
        vendor.setDateOfOrder(DateUtil.covertStringToDate(dto.getDateOfOrder()));
        vendor.setOrderDetails(dto.getOrderDetails());

        NIMSMEVendorDetailsDto finalDto = dto;
        NonTrainingSubActivity subActivity = subActivityRepository.findById(dto.getSubActivityId())
                .orElseThrow(() -> new RuntimeException("SubActivity not found with id " + finalDto.getSubActivityId()));
        vendor.setNonTrainingSubActivity(subActivity);


        NIMSMEVendorDetails save = repository.save(vendor);
        if (file != null && !file.isEmpty()) {
            String filePath = this.storageFiles(file, save.getId(), "NIMSMEVendorDetails");
            save.setOrderUpload(filePath);
            repository.save(save);
            programSessionFileRepository.save(ProgramSessionFile.builder()
                    .fileType("File")
                    .filePath(filePath)
                    .nimsmeVendorDetails(save)
                    .build());
        }

        dto = new NIMSMEVendorDetailsDto();
        dto.setVendorId(save.getId());
        dto.setVendorCompanyName(save.getVendorCompanyName());
        dto.setOrderDetails(save.getOrderDetails());
        dto.setDateOfOrder(DateUtil.dateToString(save.getDateOfOrder(),"dd-MM-yyyy"));
        dto.setSubActivityId(subActivity.getSubActivityId());
        return dto;
    }

    public NIMSMEVendorDetailsDto updateVendor(Long vendorId, NIMSMEVendorDetailsDto updatedVendorDto, MultipartFile file) {
        return repository.findById(vendorId)
                .map(existing -> {
                    existing.setVendorCompanyName(updatedVendorDto.getVendorCompanyName());
                    existing.setDateOfOrder(DateUtil.covertStringToDate( updatedVendorDto.getDateOfOrder()));
                    existing.setOrderDetails(updatedVendorDto.getOrderDetails());

                    if (updatedVendorDto.getSubActivityId() != null) {
                        NonTrainingSubActivity subActivity = subActivityRepository.findById(updatedVendorDto.getSubActivityId())
                                .orElseThrow(() -> new RuntimeException("SubActivity not found with id " + updatedVendorDto.getSubActivityId()));
                        existing.setNonTrainingSubActivity(subActivity);
                    }

                    String newPath = FileUpdateUtil.replaceFile(
                            file,
                            existing.getOrderUpload(),
                            (uploadedFile) -> this.storageFiles(file, existing.getId(), "NIMSMEVendorDetails"),
                            () -> {
                                // Runs *after* saving file successfully → DB update logic
                                existing.setOrderUpload(existing.getOrderUpload());
                                repository.save(existing);
                            }
                    );
                    programSessionFileRepository.updateFilePathByNonTrainingExpenditureId(
                            newPath,
                            existing.getId()
                    );
                    existing.setOrderUpload(newPath);
                    NIMSMEVendorDetails save = repository.save(existing);

                    return entityToDto(save);
                })
                .orElseThrow(() -> new RuntimeException("Vendor not found with id " + vendorId));
    }


    @Transactional
    public WorkflowResponse deleteVendor(Long vendorId) {
        if (!repository.existsById(vendorId)) {
            throw new RuntimeException("Vendor not found with id " + vendorId);
        }
        programSessionFileRepository.deleteByNimsmeVendorDetails_Id(vendorId);
        repository.deleteById(vendorId);
        return WorkflowResponse.builder()
                .message("Vendor Deleted Successfully")
                .status(200)
                .build();
    }

    public void deleteBySubActivityId(Long subActivityId) {
        repository.deleteByNonTrainingSubActivity_SubActivityId(subActivityId);
    }


    public static NIMSMEVendorDetailsDto entityToDto(NIMSMEVendorDetails nimsmeVendorDetails){
        NIMSMEVendorDetailsDto dto;
        dto = new NIMSMEVendorDetailsDto();
        dto.setVendorId(nimsmeVendorDetails.getId());
        dto.setVendorCompanyName(nimsmeVendorDetails.getVendorCompanyName());
        dto.setOrderDetails(nimsmeVendorDetails.getOrderDetails());
        dto.setDateOfOrder(DateUtil.dateToString(nimsmeVendorDetails.getDateOfOrder(),"dd-MM-yyyy"));
        dto.setOrderUpload(nimsmeVendorDetails.getOrderUpload());
        dto.setSubActivityId(nimsmeVendorDetails.getNonTrainingSubActivity().getSubActivityId());
        dto.setAgencyComments(
                Optional.ofNullable(nimsmeVendorDetails.getAgencyComments())
                        .orElse(List.of())
                        .stream()
                        .map(NonTrainingAgencyComments::getFormattedRemark)
                        .toList()
        );
        dto.setSpiuComments(
                Optional.ofNullable(nimsmeVendorDetails.getSpiuComments())
                        .orElse(List.of())
                        .stream()
                        .map(NonTrainingSpiuComments::getFormattedRemark)
                        .toList()
        );
        dto.setStatus(nimsmeVendorDetails.getStatus());
        return dto;
    }

    public String storageFiles(MultipartFile file, Long vendorId, String folderName) {
        return storageService.store(file, vendorId, folderName);
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

        NIMSMEVendorDetails expenditure = repository.findById(remarks.getNonTrainingExpenditureId())
                .orElseThrow(() -> new DataException("Non Training Expenditure not found", "EXPENDITURE_NOT_FOUND", 400));

        // 2. Create and associate SPIU Comment
        NonTrainingSpiuComments spiuComment = NonTrainingExpenditureMapper.mapToEntitySpiuComments(remarks, user);
        spiuComment.setNimsmeVendorDetails(expenditure);
        expenditure.getSpiuComments().add(spiuComment);

        // 3. Create and associate Agency Comment
        NonTrainingAgencyComments agencyComment = NonTrainingExpenditureMapper.mapToEntityAgencyComments(remarks, user);
        agencyComment.setNimsmeVendorDetails(expenditure);
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
            User adminUser = userRepo.findFirstByUserRoleIgnoreCase("ADMIN")
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
        repository.save(expenditure);

        return WorkflowResponse.builder()
                .message("Remark or Response added successfully.")
                .status(200)
                .build();
    }

}

