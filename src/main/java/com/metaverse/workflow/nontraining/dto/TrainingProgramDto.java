package com.metaverse.workflow.nontraining.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainingProgramDto {
    private String agency;
    private String activity;
    private String subActivity;
    private Long subActivityId;
    private Double budgetAllocated;
    private Double trainingTarget;
    private Long trainingAchievement;
    private Double trainingPercentage;
    private Double expenditure;
    private Double expenditurePercentage;
}
