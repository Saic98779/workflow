package com.metaverse.workflow.programoutcome.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ImportSubsititutionRequest {

    public String sectorName;
    public String productName;
    public Boolean isPrototypeSelected;
    public String businessPlanSubmissionDate;
    public String amountSanctionedDate;
    public String amountReleasedDate;
    public Double amountReleasedInLakhs;
    public String bankProvidedLoan;
    public String groundingDate;
    public Double monthlyTurnoverInLakhs;
    public Boolean isMarketOfProduct;
    public String marketDate;
    public Double marketValueInLakhs;
    public Double marketVolumeInMts;
    public Boolean isInfluenced;
    private Long agencyId;
    private Long participantId;
    private Long organizationId;
    private Long influencedId;
}
