package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.fileservice.FileUpdateUtil;
import com.metaverse.workflow.common.fileservice.StorageService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.NIMSMEVendorDetails;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.ProgramSessionFile;
import com.metaverse.workflow.nontrainingExpenditures.Dto.NIMSMEVendorDetailsDto;
import com.metaverse.workflow.nontrainingExpenditures.repository.NIMSMEVendorDetailsRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
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
        return dto;
    }

    public String storageFiles(MultipartFile file, Long vendorId, String folderName) {
        return storageService.store(file, vendorId, folderName);
    }
}

