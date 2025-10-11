package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.NIMSMEVendorDetails;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.nontrainingExpenditures.Dto.NIMSMEVendorDetailsDto;
import com.metaverse.workflow.nontrainingExpenditures.repository.NIMSMEVendorDetailsRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NIMSMEVendorDetailsService {

    @Autowired
    private NIMSMEVendorDetailsRepository repository;

    @Autowired
    private NonTrainingSubActivityRepository subActivityRepository;

    public List<NIMSMEVendorDetailsDto> getAllVendors() {
        return repository.findAll().stream().map(nimsmeVendorDetails -> {
            NIMSMEVendorDetailsDto dto =  new NIMSMEVendorDetailsDto();
            dto.setVendorId(nimsmeVendorDetails.getId());
            dto.setVendorCompanyName(nimsmeVendorDetails.getVendorCompanyName());
            dto.setOrderDetails(nimsmeVendorDetails.getOrderDetails());
            dto.setDateOfOrder(DateUtil.dateToString(nimsmeVendorDetails.getDateOfOrder(),"dd-MM-yyyy"));
            dto.setSubActivityId(nimsmeVendorDetails.getNonTrainingSubActivity().getSubActivityId());
            return dto;
        }).toList();


    }

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

    public NIMSMEVendorDetailsDto saveVendor(NIMSMEVendorDetailsDto dto) {

        NIMSMEVendorDetails vendor = new NIMSMEVendorDetails();
        vendor.setVendorCompanyName(dto.getVendorCompanyName());
        vendor.setDateOfOrder(DateUtil.covertStringToDate(dto.getDateOfOrder()));
        vendor.setOrderDetails(dto.getOrderDetails());

        NIMSMEVendorDetailsDto finalDto = dto;
        NonTrainingSubActivity subActivity = subActivityRepository.findById(dto.getSubActivityId())
                .orElseThrow(() -> new RuntimeException("SubActivity not found with id " + finalDto.getSubActivityId()));
        vendor.setNonTrainingSubActivity(subActivity);

        NIMSMEVendorDetails save = repository.save(vendor);

        dto = new NIMSMEVendorDetailsDto();
        dto.setVendorId(save.getId());
        dto.setVendorCompanyName(save.getVendorCompanyName());
        dto.setOrderDetails(save.getOrderDetails());
        dto.setDateOfOrder(DateUtil.dateToString(save.getDateOfOrder(),"dd-MM-yyyy"));
        dto.setSubActivityId(subActivity.getSubActivityId());
        return dto;
    }

    public NIMSMEVendorDetailsDto updateVendor(Long vendorId, NIMSMEVendorDetailsDto updatedVendorDto) {
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
                    NIMSMEVendorDetails save = repository.save(existing);

                    return entityToDto(save);
                })
                .orElseThrow(() -> new RuntimeException("Vendor not found with id " + vendorId));
    }


    public void deleteVendor(Long vendorId) {
        repository.deleteById(vendorId);
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
        dto.setSubActivityId(nimsmeVendorDetails.getNonTrainingSubActivity().getSubActivityId());
        return dto;
    }
}

