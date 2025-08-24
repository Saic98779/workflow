package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.model.NonTrainingActivity;
import com.metaverse.workflow.nontraining.dto.NonTrainingActivityDto;
import com.metaverse.workflow.nontraining.repository.NonTrainingActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}