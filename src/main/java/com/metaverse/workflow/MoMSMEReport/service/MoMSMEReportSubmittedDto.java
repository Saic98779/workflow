package com.metaverse.workflow.MoMSMEReport.service;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MoMSMEReportSubmittedDto {
    private Long submittedId;
    private Long moMSMEActivityId;
    private String financialYear;
    private String month;
    private Double physicalAchievement;
    private Double financialAchievement;
    private Integer total;
    private Integer women;
    private Integer sc;
    private Integer st;
    private Integer obc;
    private String intervention;
    private String component;
    private String activity;
    private Double physicalTarget;
    private Double financialTarget;
}
