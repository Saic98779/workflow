package com.metaverse.workflow.programoutcometargets.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class FinancialTargetRequest {
    private Long agencyId;
    private Integer outcomeId;
    private String financialYear;
    private Double q1;
    private Double q2;
    private Double q3;
    private Double q4;

}
