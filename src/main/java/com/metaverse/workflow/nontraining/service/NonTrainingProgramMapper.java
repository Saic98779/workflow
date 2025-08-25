package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.model.NonTrainingAchievement;
import com.metaverse.workflow.model.NonTrainingActivity;
import com.metaverse.workflow.model.NonTrainingTargets;
import com.metaverse.workflow.nontraining.dto.NonTrainingProgramDto;
import com.metaverse.workflow.nontraining.repository.NonTrainingActivityRepository;
import com.metaverse.workflow.trainingandnontrainingtarget.repository.OutcomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NonTrainingProgramMapper {

    private final OutcomeRepository outcomeRepository;

    private final NonTrainingActivityRepository nonTrainingActivityRepository;

    public NonTrainingProgramDto nonTrainingProgramDtoMapper(NonTrainingTargets nonTrainingTargets, Long totalTargets, Double totalBudget, double financialExpenditure) {

        NonTrainingActivity nonTrainingActivity = nonTrainingTargets.getNonTrainingActivity();

        NonTrainingAchievement nonTrainingAchievement = nonTrainingActivity.getAchievements().stream()
                .filter(ach -> ach.getNonTrainingActivity().getActivityId()
                        .equals(nonTrainingActivity.getActivityId()))
                .findFirst()
                .orElse(null);

        return NonTrainingProgramDto.builder()
                .nonTrainingActivity(nonTrainingActivity.getActivityName())
                .physicalTarget(totalTargets != null ? totalTargets.intValue() : 0)
                .physicalAchievement((nonTrainingAchievement != null ? nonTrainingAchievement.getPhysicalTargetAchievement() : null) != null ?
                        nonTrainingAchievement.getPhysicalTargetAchievement() : "0")
                .financialTarget(totalBudget)
                .financialExpenditure(financialExpenditure)
                .percentage(Math.round(financialExpenditure / totalBudget * 100 * 1000.0) / 1000.0)
                .build();
    }
}
