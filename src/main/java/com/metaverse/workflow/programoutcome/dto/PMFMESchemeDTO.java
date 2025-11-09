package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PMFMESchemeDTO {
    private Long id;
    private Date dateOfApplicationSubmission;
    private Double loanSanctioned;
    private Double grantReceived;
    private Double workingCapitalAvailed;
    private Date dateOfApprovalUnderPMFME;
    private Boolean isCommonFacilityCentreUsed;
    private Boolean isBrandingMarketingSupportAvailed;
    private String supportDetails;
    private Double productionCapacity;
    private Boolean isCertificationSupportAvailed;
    private Date dateOfMarketLinkage;
    private Double volumeOfMarketLinkage;
    private String units;
    private Double valueOfMarketLinkage;
    private Double monthlyTurnover;
    private String turnoverChange;
    private String productionCapacityChange;
    private String brandingOrMarketingSupportChange;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;
    private String influencedParticipantName;

    private Date createdOn;
    private Date updatedOn;
}

