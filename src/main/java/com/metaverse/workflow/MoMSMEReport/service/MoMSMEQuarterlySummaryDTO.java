package com.metaverse.workflow.MoMSMEReport.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoMSMEQuarterlySummaryDTO {
    private String financialYear;
    private String quarter;
    private String intervention;
    private Double totalPhysicalAchievement;
    private Double totalFinancialAchievement;
}

