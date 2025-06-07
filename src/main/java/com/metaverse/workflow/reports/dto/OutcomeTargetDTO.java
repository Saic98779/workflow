package com.metaverse.workflow.reports.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OutcomeTargetDTO {
    private String outcomeName;
    private String financialYear;
    private Integer physicalTargetQ1;
    private Integer physicalTargetQ2;
    private Integer physicalTargetQ3;
    private Integer physicalTargetQ4;
    private Integer achievedQ1;
    private Integer achievedQ2;
    private Integer achievedQ3;
    private Integer achievedQ4;
    private Integer totalTarget;
    private Integer totalAchieved;
}
