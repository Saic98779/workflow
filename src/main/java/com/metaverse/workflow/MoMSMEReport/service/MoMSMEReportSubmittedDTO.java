package com.metaverse.workflow.MoMSMEReport.service;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MoMSMEReportSubmittedDTO {
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
}
