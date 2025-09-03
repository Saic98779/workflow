package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.model.NonTrainingAchievement;
import com.metaverse.workflow.model.NonTrainingActivity;
import com.metaverse.workflow.model.NonTrainingTargets;
import com.metaverse.workflow.nontraining.dto.NonTrainingProgramDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NonTrainingProgramMapper {

    /**
     * Maps NonTrainingTargets entity to NonTrainingProgramDto for API response.
     *
     * @param nonTrainingTargets     the target entity
     * @param totalTargets           total physical targets
     * @param totalBudget            total financial targets
     * @param financialExpenditure   actual financial expenditure
     * @return NonTrainingProgramDto populated with calculated values
     */
    public NonTrainingProgramDto nonTrainingProgramDtoMapper(NonTrainingTargets nonTrainingTargets, Long totalTargets, Double totalBudget, double financialExpenditure) {

        NonTrainingActivity subActivity = nonTrainingTargets.getNonTrainingSubActivity().getNonTrainingActivity();
        String activityName = subActivity != null ? subActivity.getActivityName() : "Unknown";

        // Get the achievement for the related activity, if available
        NonTrainingAchievement achievement = null;
        if (subActivity != null && subActivity.getAchievements() != null) {
            achievement = subActivity.getAchievements().stream()
                    .filter(ach -> ach.getNonTrainingActivity().getActivityId()
                            .equals(subActivity.getActivityId()))
                    .findFirst()
                    .orElse(null);
        }

        int physicalAchievement = 0;
        if (achievement != null && achievement.getPhysicalTargetAchievement() != null) {
                physicalAchievement = Integer.parseInt(achievement.getPhysicalTargetAchievement());
        }

        double percentage = 0.0;
        if (totalBudget != null && totalBudget > 0) {
            percentage = Math.round((financialExpenditure / totalBudget) * 100 * 1000.0) / 1000.0;
        }

        return NonTrainingProgramDto.builder()
                .nonTrainingActivity(activityName)
                .nonTrainingSubActivity(subActivity != null ? subActivity.getActivityName() : null)
                .physicalTarget(totalTargets != null ? totalTargets.intValue() : 0)
                .physicalAchievement(String.valueOf(physicalAchievement))
                .financialTarget(totalBudget != null ? totalBudget : 0.0)
                .financialExpenditure(financialExpenditure)
                .percentage(percentage)
                .build();
    }
}
