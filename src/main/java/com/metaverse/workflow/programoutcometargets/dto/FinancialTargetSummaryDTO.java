package com.metaverse.workflow.programoutcometargets.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FinancialTargetSummaryDTO {
    private Long financialTargetId;
    private String agencyName;
    private String activityName;
    private String financialYear;
    private Double q1;
    private Double q2;
    private Double q3;
    private Double q4;
    private Double total;
}

