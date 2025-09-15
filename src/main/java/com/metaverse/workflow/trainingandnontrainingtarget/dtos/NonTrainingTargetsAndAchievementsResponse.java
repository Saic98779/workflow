package com.metaverse.workflow.trainingandnontrainingtarget.dtos;

import lombok.Data;

@Data
public class NonTrainingTargetsAndAchievementsResponse {
    private String activityName;
    private String subActivityName;
    private String financialYear;
    private Long trainingTargetQ1; // Physical Training target for Q1
    private Long trainingTargetQ2;
    private Long trainingTargetQ3;
    private Long trainingTargetQ4;
    private Double financialTargetQ1;
    private Double financialTargetQ2;
    private Double financialTargetQ3;
    private Double financialTargetQ4;
    private String achievedQ1;     // Achieved for Q1
    private String achievedQ2;
    private String achievedQ3;
    private String achievedQ4;
    private Double financialAchievedQ1;
    private Double financialAchievedQ2;
    private Double financialAchievedQ3;
    private Double financialAchievedQ4;
    private Integer totalFinancialTarget;
    private Double totalFinancialAchieved;
    private Long totalTarget;
    private Long totalAchieved;
    private String physicalExpenditurePercentage;
    private Double financialExpenditurePercentage;
}
