package com.metaverse.workflow.nontraining.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainingProgramDto {
    private String agency;
    private String activity;
    private Double budgetAllocated;
    private Long trainingTarget;
    private Long trainingAchievement;
    private double trainingPercentage;
    private Double expenditure;
    private Double expenditurePercentage;
}
