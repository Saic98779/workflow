package com.metaverse.workflow.trainingtarget.dtos;

import lombok.Data;

@Data
public class TargetsAndAchievementsResponseDto {
    private String activityName;
    private String financialYear;
    private Integer trainingTargetQ1;
    private Integer trainingTargetQ2;
    private Integer trainingTargetQ3;
    private Integer trainingTargetQ4;
    private Integer achievedQ1;
    private Integer achievedQ2;
    private Integer achievedQ3;
    private Integer achievedQ4;
    private Integer totalTarget;
    private Integer totalAchieved;
}
