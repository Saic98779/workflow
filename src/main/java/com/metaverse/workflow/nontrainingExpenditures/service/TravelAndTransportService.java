package com.metaverse.workflow.nontrainingExpenditures.service;


import com.metaverse.workflow.common.enums.PaymentType;
import com.metaverse.workflow.common.fileservice.StorageService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.ProgramSessionFile;
import com.metaverse.workflow.model.TravelAndTransport;
import com.metaverse.workflow.nontrainingExpenditures.Dto.TravelAndTransportDto;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.TravelAndTransportRepository;
import com.metaverse.workflow.program.repository.ProgramSessionFileRepository;
import com.metaverse.workflow.program.service.ProgramSessionFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TravelAndTransportService {

    private final TravelAndTransportRepository travelRepo;
    private final NonTrainingSubActivityRepository subActivityRepo;
    private final StorageService storageService;
    private final ProgramSessionFileRepository programSessionFileRepository;

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


    public void deleteById(Long travelTransportId) {
        if (!travelRepo.existsById(travelTransportId)) {
            throw new RuntimeException("TravelAndTransport not found with id " + travelTransportId);
        }
        travelRepo.deleteById(travelTransportId);
        programSessionFileRepository.deleteByTravelTransportId(travelTransportId);
    }

    public TravelAndTransportDto updateTravel(Long id, TravelAndTransportDto dto) {
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
        existing.setBillInvoicePath(dto.getBillInvoicePath());



        if (dto.getNonTrainingSubActivityId() != null) {
            NonTrainingSubActivity subActivity = subActivityRepo.findById(dto.getNonTrainingSubActivityId())
                    .orElseThrow(() -> new RuntimeException("NonTrainingSubActivity not found with id " + dto.getNonTrainingSubActivityId()));
            existing.setNonTrainingSubActivity(subActivity);
        }

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
        dto.setNonTrainingSubActivityId(entity.getNonTrainingSubActivity() != null
                ? entity.getNonTrainingSubActivity().getSubActivityId() : null);
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

        return entity;
    }

    public String storageTravelAndTransportFiles(MultipartFile file, Long TravelAndTransportId, String folderName) {
            String filePath = storageService.travelAndTransportStore(file, TravelAndTransportId, folderName);
        return  filePath;
    }
}
