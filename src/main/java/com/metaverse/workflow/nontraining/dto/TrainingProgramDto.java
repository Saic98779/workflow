package com.metaverse.workflow.nontraining.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TrainingProgramDto {

    private Long trainingBudgetAllocatedId;
    private String agency;
    private String activity;
    private String subActivityId;
    private Long trainingTarget;
    private Double budgetAllocated;
}
