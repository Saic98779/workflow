package com.metaverse.workflow.programoutcome.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeanRequest {
    public String zedCertificationType; // Bronze / Silver / Gold /dropdown
    public String certificationNumber;
    public String dateOfCertification;
    public String validUpto;
    public Double productionRate;
    public Double defectRate;
    public Double rawMaterialWastage;
    public Double energyConsumption;
    public String dateOfExport;
    public Double valueOfExport;
    public String units;
    public Double volumeOfExport;
    public Double annualTurnover;
    public Boolean isInfluenced;
    private Long agencyId;
    private Long participantId;
    private Long organizationId;
}
