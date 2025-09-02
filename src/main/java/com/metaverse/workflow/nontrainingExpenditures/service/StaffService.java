package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.NonTrainingResource;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.nontrainingExpenditures.Dto.StaffDto;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingResourceRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final NonTrainingResourceRepository resourceRepo;
    private final NonTrainingSubActivityRepository subActivityRepo;

    // Save
    public NonTrainingResourceDTO save(StaffDto dto) {
        NonTrainingSubActivity subActivity = subActivityRepo.findById(dto.getNonTrainingSubActivityId())
                .orElseThrow(() -> new RuntimeException("SubActivity not found with id " + dto.getNonTrainingSubActivityId()));

        NonTrainingResource entity = convertToEntity(dto);
//        entity.setNonTrainingSubActivity(subActivity);

        NonTrainingResource saved = resourceRepo.save(entity);
        return convertToDto(saved);
    }

    private NonTrainingResourceDTO convertToDto(NonTrainingResource entity) {
        return NonTrainingResourceDTO.builder()
                .resourceId(entity.getResourceId())
                .name(entity.getName())
                .designation(entity.getDesignation())
                .relevantExperience(entity.getRelevantExperience())
                .educationalQualification(entity.getEducationalQualifications())
                .dateOfJoining(String.valueOf(entity.getDateOfJoining()))
                .monthlySal(entity.getMonthlySal())
                .bankName(entity.getBankName())
                .ifscCode(entity.getIfscCode())
                .accountNo(entity.getAccountNo())
                /*.nonTrainingSubActivityId(
                        entity.getNonTrainingSubActivity() != null ? entity.getNonTrainingSubActivity().getSubActivityId() : null
                )*/
                .build();
    }

    private NonTrainingResource convertToEntity(StaffDto dto) {
        return NonTrainingResource.builder()
                .resourceId(dto.getResourceId())
                .name(dto.getNameOfTheStaff())
                .designation(dto.getDesignation())
                .relevantExperience(dto.getRelevantExperience())
                .educationalQualifications(dto.getEducationalQualification())
                .dateOfJoining(DateUtil.stringToDate(dto.getDateOfJoining(), "dd-MM-yyyy"))
                .monthlySal(dto.getMonthlySal())
                .bankName(dto.getBankName())
                .ifscCode(dto.getIfscCode())
                .accountNo(dto.getAccountNo())
                .build();
    }
}
