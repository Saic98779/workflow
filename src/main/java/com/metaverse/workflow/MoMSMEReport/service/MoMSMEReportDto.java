package com.metaverse.workflow.MoMSMEReport.service;

import com.metaverse.workflow.MoMSMEReport.dtos.CumulativeQuarterData;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MoMSMEReportDto {
    private Long moMSMEActivityId;
    private String financialYear;
    private String month;
    private Double physicalTarget;
    private Double financialTarget;
    private Double currentPhysicalAchievement;
    private Double currentFinancialAchievement;
    private Double physicalAchievement;
    private Double financialAchievement;
    private CumulativeQuarterData cumulativeMoMSMEBenefitedDto;
    private CurrentMonthMoMSMEBenefitedDto currentMonthMoMSMEBenefitedDto;
    private CurrentQuarterMoMSMEBenefitedDto currentQuarterMoMSMEBenefitedDto;
    private String intervention;
    private String component;
    private String activity;
}

