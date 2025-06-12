package com.metaverse.workflow.programoutcome.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PMFMESchemeRequest {
    public String dateOfApplicationSubmission;
    public Double loanSanctioned;
    public Double grantReceived;
    public Double workingCapitalAvailed;
    public String dateOfApprovalUnderPMFME;
    public Boolean isCommonFacilityCentreUsed;
    public Boolean isBrandingMarketingSupportAvailed;
    public String supportDetails; // if branding/marketing support was availed
    public Double productionCapacity; // in MTs/month
    public Boolean isCertificationSupportAvailed;
    public String dateOfMarketLinkage;
    public Double volumeOfMarketLinkage;
    public String units;
    public Double valueOfMarketLinkage;
    public Double monthlyTurnover;
    public String turnoverChange;
    public String productionCapacityChange;
    public String brandingOrMarketingSupportChange;
    public Boolean isInfluenced;
    private Long participantId;
    private Long organizationId;
    private Long agencyId;
}
