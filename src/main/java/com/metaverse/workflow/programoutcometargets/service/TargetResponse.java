package com.metaverse.workflow.programoutcometargets.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TargetResponse {
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
