package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.model.NonTrainingActivity;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.nontraining.dto.NonTrainingActivityDto;
import com.metaverse.workflow.nontraining.dto.NonTrainingSubActivityDto;
import com.metaverse.workflow.nontraining.repository.NonTrainingActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NonTrainingActivityServiceImpl implements NonTrainingActivityService{


    private final NonTrainingActivityRepository nonTrainingActivityRepository;

    @Override
    public List<NonTrainingActivityDto> getAllActivitiesByAgency(Long agencyId) {
        List<NonTrainingActivity> byAgencyAgencyId = nonTrainingActivityRepository.findByAgency_AgencyId(agencyId);
        if (byAgencyAgencyId.isEmpty()) {
            return null;
        }
        return byAgencyAgencyId.stream().map((activityEntity)-> NonTrainingActivityDto.builder().activityId(activityEntity.getActivityId())
                .activityName(activityEntity.getActivityName())
                .agency(activityEntity.getAgency().getAgencyName()).build()).toList();
    }

    @Override
    public List<NonTrainingSubActivityDto> getAllSubActivitiesList(Long activityId) {
        NonTrainingActivity activity = nonTrainingActivityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found with id: " + activityId));

        return activity.getSubActivities().stream()
                .map(entity -> {
                    NonTrainingSubActivityDto dto = new NonTrainingSubActivityDto();
                    dto.setActivityId(activityId);
                    dto.setSubActivityId(entity.getSubActivityId());
                    dto.setSubActivityName(entity.getSubActivityName());
                    return dto;
                })
                .toList();
    }

}