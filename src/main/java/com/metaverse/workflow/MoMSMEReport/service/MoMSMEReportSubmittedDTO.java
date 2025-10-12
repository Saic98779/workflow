package com.metaverse.workflow.MoMSMEReport.service;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MoMSMEReportSubmittedDTO {
    private Long submittedId;
    private String financialYear;
    private String month;
    private Double physicalAchievement;
    private Double financialAchievement;
    private Integer total;
    private Integer women;
    private Integer sc;
    private Integer st;
    private Integer obc;
}
