package com.metaverse.workflow.MoMSMEReport.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiHclMoMSMEReportDTO {
    private Long noOfApplicationsReceivedFromMsmes;
    private Long noOfApplicationsShortlisted;
    private Long noOfUnitsVisited;
    private Long noOfMsmesExtendedDebtFinance;
    private Double disbursedAmount;
}
