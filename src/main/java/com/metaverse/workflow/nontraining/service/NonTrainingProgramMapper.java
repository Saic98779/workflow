package com.metaverse.workflow.nontraining.service;


import com.metaverse.workflow.model.NonTrainingAchievement;
import com.metaverse.workflow.model.NonTrainingActivity;
import com.metaverse.workflow.nontraining.dto.NonTrainingProgramDto;

public class NonTrainingProgramMapper {
    public static NonTrainingProgramDto trainingProgramDtoMapper(NonTrainingActivity nonTrainingActivity) {
        if (nonTrainingActivity == null || nonTrainingActivity.getAchievements().isEmpty()) {
            return null;
        }

        NonTrainingAchievement ach = nonTrainingActivity.getAchievements().get(0);

        Double percentage = null;
        if (ach.getFinancialTarget() != null && ach.getFinancialTarget() > 0
                && ach.getFinancialTargetAchievement() != null) {

            double targetInRupees = ach.getFinancialTarget() * 10000000;
            double expenditureInRupees = ach.getFinancialTargetAchievement();
            percentage = (expenditureInRupees / targetInRupees) * 100;
        }

        return NonTrainingProgramDto.builder()
                .nonTrainingActivity(nonTrainingActivity.getActivityName())
                .physicalTarget(ach.getPhysicalTarget())
                .applicable("Not Initiated".equalsIgnoreCase(ach.getPhysicalTargetAchievement()) ? "Not Applicable" : "100.00%")
                .physicalAchievement(ach.getPhysicalTargetAchievement())
                .financialTarget(ach.getFinancialTarget())
                .financialExpenditure(ach.getFinancialTargetAchievement())
                .percentage(percentage)
                .build();
    }



}
