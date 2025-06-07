package com.metaverse.workflow.programoutcometargets.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PhysicalTargetResponse {
    private Long physicalTargetId;
    private Integer outcomeId;
    private String agencyName;
    private String outcomeTableName;
    private String financialYear;
    private Integer q1;
    private Integer q2;
    private Integer q3;
    private Integer q4;
    private Integer total;
}
