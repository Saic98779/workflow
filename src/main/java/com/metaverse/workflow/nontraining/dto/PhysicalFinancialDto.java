package com.metaverse.workflow.nontraining.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhysicalFinancialDto {

    private Long nonTrainingSubActivityId;
    private Long physicalTarget;
    private String physicalTargetAchievement;
    private Double financialTarget;
    private Double financialTargetAchievement;
}
