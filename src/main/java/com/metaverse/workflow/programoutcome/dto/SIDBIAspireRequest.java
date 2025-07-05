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
    public Integer monthlyProductionUnits;
    public Boolean isMarketLinkageEnabled;
    public String marketLinkageDate;
    public String marketLinkageVolume;
    public Double marketLinkageValue;
    public Double monthlyTurnover;
    public Boolean isInfluenced;

    private Long agencyId;
    private Long participantId;
    private Long organizationId;
}
