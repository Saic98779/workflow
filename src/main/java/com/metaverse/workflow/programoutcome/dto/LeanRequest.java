package com.metaverse.workflow.programoutcome.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeanRequest {
    private String certificationType; // Basic, Intermediate, Advanced
    public String dateOfCertification;
    public Boolean isLeanConsultantAppointed;
    public String dateOfAppointed;
    public Double rawMaterialWastage;
    public Double productionRate;
    public Double defectRate;
    public Double powerUsage;
    public Boolean isInfluenced;
    private Long agencyId;
    private Long participantId;
    private Long organizationId;
}
