package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.model.NonTrainingAchievement;
import com.metaverse.workflow.model.NonTrainingActivity;
import com.metaverse.workflow.nontraining.dto.NonTrainingProgramDto;
import com.metaverse.workflow.nontraining.dto.TrainingAndNonTrainingDto;
import com.metaverse.workflow.nontraining.repository.NonTrainingActivityRepository;
import com.metaverse.workflow.nontraining.repository.TrainingBudgetAllocatedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramMonitoringImpl implements  ProgramMonitoringService{

    @Autowired
    private NonTrainingActivityRepository nonTrainingActivityRepository;

    @Autowired
    private TrainingBudgetAllocatedRepository trainingBudgetAllocatedRepository;

    @Override
    public TrainingAndNonTrainingDto getAllTrainingAndNonTrainings(Long agencyId) {
        List<NonTrainingActivity> byAgencyAgencyId = nonTrainingActivityRepository.findByAgency_AgencyId(agencyId);
         byAgencyAgencyId.stream().map(NonTrainingProgramMapper::trainingProgramDtoMapper).toList();
        trainingBudgetAllocatedRepository.findByAgency_AgencyId(agencyId);
     return TrainingAndNonTrainingDto.builder().nonTrainingProgramDtos(byAgencyAgencyId.stream().map(NonTrainingProgramMapper::trainingProgramDtoMapper).toList()).build();
    }
}
