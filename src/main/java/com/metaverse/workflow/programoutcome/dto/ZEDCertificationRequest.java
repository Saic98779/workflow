package com.metaverse.workflow.programoutcome.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZEDCertificationRequest {
    public String machineryType;
    public String dprSubmissionDate;
    public String amountReleasedDate;
    public Double releasedValue;
    public String groundingDate;
    public String certificationDate;
    public String zedCertificationType; // Bronze / Silver / Gold
    public Double turnover;
    public Double energyConsumptionKwhHr;
    public Integer productionMtHr;
    public Integer defectRatePer100Units;
    public Boolean isInfluenced;
    private Long participantId;
    private Long organizationId;
    private Long agencyId;

}
