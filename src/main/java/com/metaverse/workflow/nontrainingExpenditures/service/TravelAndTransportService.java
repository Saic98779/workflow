package com.metaverse.workflow.nontrainingExpenditures.service;


import com.metaverse.workflow.common.enums.NotificationType;
import com.metaverse.workflow.common.enums.PaymentType;
import com.metaverse.workflow.common.fileservice.FileUpdateUtil;
import com.metaverse.workflow.common.fileservice.StorageService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.enums.BillRemarksStatus;
import com.metaverse.workflow.enums.RemarkBy;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.login.repository.LoginRepository;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.nontrainingExpenditures.Dto.TravelAndTransportDto;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.TravelAndTransportRepository;
import com.metaverse.workflow.notifications.dto.GlobalNotificationRequest;
import com.metaverse.workflow.notifications.repository.NotificationRepository;
import com.metaverse.workflow.notifications.service.NotificationServiceImpl;
import com.metaverse.workflow.program.repository.ProgramSessionFileRepository;
import com.metaverse.workflow.program.service.ProgramSessionFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TravelAndTransportService {

    private final TravelAndTransportRepository travelRepo;
    private final NonTrainingSubActivityRepository subActivityRepo;
    private final StorageService storageService;
    private final ProgramSessionFileRepository programSessionFileRepository;
    private final LoginRepository userRepo;
    private final NotificationServiceImpl notificationService;

    public WorkflowResponse saveTravel(TravelAndTransportDto dto, MultipartFile file) {

        try {
            NonTrainingSubActivity subActivity = subActivityRepo.findById(dto.getNonTrainingSubActivityId())
                    .orElseThrow(() -> new RuntimeException("NonTrainingSubActivity not found with id " + dto.getNonTrainingSubActivityId()));

            TravelAndTransport entity = convertToEntity(dto);
            entity.setNonTrainingSubActivity(subActivity);
            TravelAndTransport saved = travelRepo.save(entity);

            if (file != null && !file.isEmpty()) {
                String filePath = this.storageTravelAndTransportFiles(file, saved.getTravelTransportId(), "TravelAndTransport");
                saved.setBillInvoicePath(filePath);
                travelRepo.save(saved);
                programSessionFileRepository.save(ProgramSessionFile.builder()
                        .fileType("File")
                        .filePath(filePath)
                        .travelAndTransport(saved)
                        .build());
            }
            return WorkflowResponse.builder().data(convertToDto(saved)).message("Saved").status(200).build();

        } catch (RuntimeException e) {
            return WorkflowResponse.builder().message(e.getMessage()).status(400).build();
        }

    }

    public List<TravelAndTransportDto> getBySubActivityId(Long subActivityId) {
        return travelRepo.findByNonTrainingSubActivity_SubActivityId(subActivityId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public WorkflowResponse deleteById(Long travelTransportId) throws IOException {
        if (!travelRepo.existsById(travelTransportId)) {
            throw new RuntimeException("TravelAndTransport not found with id " + travelTransportId);
        }
        Optional<TravelAndTransport> byId = travelRepo.findById(travelTransportId);
        if (byId.isPresent()) {
            boolean b = Files.deleteIfExists(Path.of(byId.get().getBillInvoicePath()));
            System.err.println(b);
        }
        travelRepo.deleteById(travelTransportId);
        return WorkflowResponse.builder()
                .message("TravelAndTransport Deleted Successfully")
                .status(200)
                .build();
    }

    @Transactional
    public TravelAndTransportDto updateTravel(Long id, TravelAndTransportDto dto, MultipartFile file) throws IOException {

        TravelAndTransport existing = travelRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("TravelAndTransport not found with id " + id));

        existing.setDateOfTravel(LocalDate.parse(dto.getDateOfTravel()));
        existing.setPurposeOfTravel(dto.getPurposeOfTravel());
        existing.setModeOfTravel(dto.getModeOfTravel());
        existing.setDestination(dto.getDestination());
        existing.setNoOfPersonsTraveled(dto.getNoOfPersonsTraveled());
        existing.setAmount(dto.getAmount());
        existing.setBillNo(dto.getBillNo());
        existing.setBillDate(LocalDate.parse(dto.getBillDate()));
        existing.setModeOfPayment(PaymentType.valueOf(dto.getModeOfPayment()));
        existing.setPayeeName(dto.getPayeeName());
        existing.setTransactionId(dto.getTransactionId());
        existing.setBank(dto.getBank());
        existing.setIfscCode(dto.getIfscCode());
        existing.setPurpose(dto.getPurpose());


        if (dto.getNonTrainingSubActivityId() != null) {
            NonTrainingSubActivity subActivity = subActivityRepo.findById(dto.getNonTrainingSubActivityId())
                    .orElseThrow(() -> new RuntimeException("NonTrainingSubActivity not found with id " + dto.getNonTrainingSubActivityId()));
            existing.setNonTrainingSubActivity(subActivity);
        }

        String newPath = FileUpdateUtil.replaceFile(
                file,
                existing.getBillInvoicePath(),
                (uploadedFile) -> this.storageTravelAndTransportFiles(uploadedFile, existing.getTravelTransportId(), "TravelAndTransport"),
                () -> {
                    // Runs *after* saving file successfully → DB update logic
                    existing.setBillInvoicePath(existing.getBillInvoicePath());
                    travelRepo.save(existing);
                }
        );
        programSessionFileRepository.updateFilePathByTravelTransportId(
                newPath,
                existing.getTravelTransportId()
        );
        existing.setBillInvoicePath(newPath);
        TravelAndTransport updated = travelRepo.save(existing);
        return convertToDto(updated);
    }


    private TravelAndTransportDto convertToDto(TravelAndTransport entity) {
        TravelAndTransportDto dto = new TravelAndTransportDto();
        dto.setTravelTransportId(entity.getTravelTransportId());
        dto.setDateOfTravel(String.valueOf(entity.getDateOfTravel()));
        dto.setPurposeOfTravel(entity.getPurposeOfTravel());
        dto.setModeOfTravel(entity.getModeOfTravel());
        dto.setDestination(entity.getDestination());
        dto.setNoOfPersonsTraveled(entity.getNoOfPersonsTraveled());
        dto.setAmount(entity.getAmount());
        dto.setBillNo(entity.getBillNo());
        dto.setBillDate(String.valueOf(entity.getBillDate()));
        dto.setModeOfPayment(String.valueOf(entity.getModeOfPayment()));
        dto.setPayeeName(entity.getPayeeName());
        dto.setTransactionId(entity.getTransactionId());
        dto.setBank(entity.getBank());
        dto.setIfscCode(entity.getIfscCode());
        dto.setPurpose(entity.getPurpose());
        dto.setBillInvoicePath(entity.getBillInvoicePath());
        dto.setCheckNo(entity.getCheckNo());
        dto.setCheckDate(DateUtil.dateToString(entity.getCheckDate(),"dd-MM-yyyy"));
        dto.setNonTrainingSubActivityId(entity.getNonTrainingSubActivity() != null
                ? entity.getNonTrainingSubActivity().getSubActivityId() : null);
        dto.setAgencyComments(
                Optional.ofNullable(entity.getAgencyComments())
                        .orElse(List.of())
                        .stream()
                        .map(NonTrainingAgencyComments::getFormattedRemark)
                        .toList()
        );
        dto.setSpiuComments(
                Optional.ofNullable(entity.getSpiuComments())
                        .orElse(List.of())
                        .stream()
                        .map(NonTrainingSpiuComments::getFormattedRemark)
                        .toList()
        );
        dto.setStatus(entity.getStatus());

        return dto;
    }

    private TravelAndTransport convertToEntity(TravelAndTransportDto dto) {
        TravelAndTransport entity = new TravelAndTransport();
        entity.setTravelTransportId(dto.getTravelTransportId());
        entity.setDateOfTravel(LocalDate.parse(dto.getDateOfTravel()));
        entity.setPurposeOfTravel(dto.getPurposeOfTravel());
        entity.setModeOfTravel(dto.getModeOfTravel());
        entity.setDestination(dto.getDestination());
        entity.setNoOfPersonsTraveled(dto.getNoOfPersonsTraveled());
        entity.setAmount(dto.getAmount());
        entity.setBillNo(dto.getBillNo());
        entity.setBillDate(LocalDate.parse(dto.getBillDate()));
        entity.setModeOfPayment(PaymentType.valueOf(dto.getModeOfPayment()));
        entity.setPayeeName(dto.getPayeeName());
        entity.setTransactionId(dto.getTransactionId());
        entity.setBank(dto.getBank());
        entity.setIfscCode(dto.getIfscCode());
        entity.setPurpose(dto.getPurpose());
        entity.setBillInvoicePath(dto.getBillInvoicePath());
        entity.setCheckNo(dto.getCheckNo());
        entity.setCheckDate(DateUtil.covertStringToDate(dto.getCheckDate()));

        return entity;
    }

    public String storageTravelAndTransportFiles(MultipartFile file, Long TravelAndTransportId, String folderName) {
        String filePath = storageService.travelAndTransportStore(file, TravelAndTransportId, folderName);
        return filePath;
    }

    private  User getAgencyAdminOrFallback(Agency agency) throws DataException {
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

        TravelAndTransport expenditure = travelRepo.findById(remarks.getNonTrainingExpenditureId())
                .orElseThrow(() -> new DataException("Non Training Expenditure not found", "EXPENDITURE_NOT_FOUND", 400));

        // 2. Create and associate SPIU Comment
        NonTrainingSpiuComments spiuComment = NonTrainingExpenditureMapper.mapToEntitySpiuComments(remarks, user);
        spiuComment.setTravelAndTransport(expenditure);
        expenditure.getSpiuComments().add(spiuComment);

        // 3. Create and associate Agency Comment
        NonTrainingAgencyComments agencyComment = NonTrainingExpenditureMapper.mapToEntityAgencyComments(remarks, user);
        agencyComment.setTravelAndTransport(expenditure);
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
        travelRepo.save(expenditure);

        return WorkflowResponse.builder()
                .message("Remark or Response added successfully.")
                .status(200)
                .build();
    }
}
