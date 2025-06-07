package com.metaverse.workflow.programoutcometargets.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class FinancialTargetResponse {
    private Long financialTargetId;
    private String agencyName;
    private String outcomeTableName;
    private String financialYear;
    private Double q1;
    private Double q2;
    private Double q3;
    private Double q4;
}
