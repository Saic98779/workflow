package com.metaverse.workflow.MoMSMEReport.dtos;

import com.metaverse.workflow.MoMSMEReport.service.CurrentMonthMoMSMEBenefitedDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MoMSMEReportDto {
    private Long moMSMEActivityId;
    private String financialYear;
    private String month;
    private String intervention;
    private String component;
    private String activity;
    private Double physicalTarget;
    private Double financialTarget;
    private CurrentMonthMoMSMEBenefitedDto currentMonthMSMEBenefitedDto;
    private CurrentQuarterMoMSMEBenefitedDto currentQuarterMSMEBenefitedDto;
    private CumulativeQuarterData cumulativeMSMEBenefitedDto;
}
