package com.metaverse.workflow.programoutcome.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SIDBIAspireRequest {

    public String applicationSubmissionDate;
    public String dateSanctionUnderAspire;
    public Boolean isFundingSupportReceived;
    public String incubationPartnerName;
    public String fundingType;
    public Double supportAmount;
    public String machinerySetupDate;
    public String productionStartedDate;
    public String production;
    public String productionUnits;
    public Boolean isMarketLinkageEnabled;
    public String marketLinkageDate;
    public String marketLinkageVolume;
    public Double marketLinkageValue;
    public Double turnover;
    public Boolean isInfluenced;

    private Long agencyId;
    private Long participantId;
    private Long organizationId;
    private Long influencedId;
}
