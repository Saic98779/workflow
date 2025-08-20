package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.model.TrainingBudgetAllocated;
import com.metaverse.workflow.nontraining.dto.TrainingProgramDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TrainingProgramMapper {

    public TrainingProgramDto trainingProgramDtoMapper(
            TrainingBudgetAllocated trainingBudgetAllocated,
            Map<Long, Long> activityToCount, Map<Long, Double> activityToExpenditure) {

        Long activityId = trainingBudgetAllocated.getActivityId().getActivityId();
        long achievedCount = activityToCount.getOrDefault(activityId, 0L);

        Long target = trainingBudgetAllocated.getTrainingTarget();
        Double percentage = (target != null && target > 0)
                ? (achievedCount * 100.0) / target
                : 0.0;

        Double expenditure = activityToExpenditure.getOrDefault(activityId, 0.0);
        Double budget = trainingBudgetAllocated.getBudgetAllocated();
        Double expenditurePercentage = (budget != null && budget > 0)
                ? (expenditure * 100.0) / budget
                : 0.0;

        return TrainingProgramDto.builder()
                .trainingBudgetAllocatedId(trainingBudgetAllocated.getTrainingBudgetAllocatedId())
                .activity(trainingBudgetAllocated.getActivityId().getActivityName())
                .agency(trainingBudgetAllocated.getAgency().getAgencyName())
                .budgetAllocated(trainingBudgetAllocated.getBudgetAllocated())
                .trainingTarget(trainingBudgetAllocated.getTrainingTarget())
                .subActivityId(trainingBudgetAllocated.getSubActivityId().getSubActivityName())
                .trainingAchievement(achievedCount)
                .trainingPercentage(percentage)
                .expenditure(expenditure.longValue())
                .expenditurePercentage(expenditurePercentage)
                .build();
    }
}

