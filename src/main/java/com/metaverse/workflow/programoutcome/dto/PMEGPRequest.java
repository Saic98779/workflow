package com.metaverse.workflow.programoutcome.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PMEGPRequest {
    public String loanSanctionedDate;
    public Double loanAmountReleased;
    public Double govtSubsidy;
    public Double beneficiaryContribution;
    public Double totalAmountReleased;
    public String groundingDate;
    public Double businessTurnover;
    public String dateOfMarketLinkage;
    public Double volumeOfMarket;
    public String units;
    public Double valueOfMarket;
    public String nameOfProductMarket;
    public Integer numberOfPersonsEmployed;
    public Boolean isInfluenced;
    private Long agencyId;
    private Long participantId;
    private Long organizationId;
}
