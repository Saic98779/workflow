package com.metaverse.workflow.nontraining.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NonTrainingProgramDto {
    private String nonTrainingActivity;
    private String nonTrainingSubActivity;
    private Integer physicalTarget;
    private String physicalAchievement;
    private Double financialTarget;
    private Double financialExpenditure;
    private Double percentage;
    private String physicalPercentage;

}
