package com.metaverse.workflow.MoMSMEReport.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CumulativeQuarterData {
    private String intervention;
    private String component;
    private String activity;
    private String financialYear;
    private String month;
    private Double physicalTarget;
    private Double financialTarget;
    private Double physicalAchievement;
    private Double financialAchievement;
    private Integer total;
    private Integer women;
    private Integer sc;
    private Integer st;
    private Integer obc;
}
