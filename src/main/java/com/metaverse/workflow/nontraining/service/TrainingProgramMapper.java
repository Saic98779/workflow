package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.model.TrainingBudgetAllocated;
import com.metaverse.workflow.nontraining.dto.TrainingProgramDto;

public class TrainingProgramMapper {

    public static TrainingProgramDto trainingProgramDtoMapper(TrainingBudgetAllocated trainingBudgetAllocated) {
        return TrainingProgramDto.builder().trainingBudgetAllocatedId(trainingBudgetAllocated.getTrainingBudgetAllocatedId())
                .activity(trainingBudgetAllocated.getActivityId().getActivityName())
                .agency(trainingBudgetAllocated.getAgency().getAgencyName())
                .budgetAllocated(trainingBudgetAllocated.getBudgetAllocated())
                .trainingTarget(trainingBudgetAllocated.getTrainingTarget())
                .subActivityId(trainingBudgetAllocated.getSubActivityId().getSubActivityName()).build();
    }
}
