package com.metaverse.workflow.nontraining.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainingProgramDto {
    private Long trainingBudgetAllocatedId;
    private String agency;
    private String activity;
    private String subActivityId;
    private Double budgetAllocated;
    private String trainingActivity;
    private String trainingSubActivity;
    private Long trainingTarget;
}
