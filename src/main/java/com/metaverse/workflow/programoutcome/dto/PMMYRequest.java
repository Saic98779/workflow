package com.metaverse.workflow.programoutcome.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PMMYRequest {
    public Double loanAmountReleased;
    public String loanSanctionedDate;
    public String groundingDate;
    public Double businessTurnover;
    public String marketLinkageDate; 
    public Double marketVolume;
    public String units;
    public Double marketValue; 
    public String productMarketedName;
    private String category;
    public Boolean isInfluenced;
    private Long agencyId;
    private Long participantId;
    private Long organizationId;
    private Long influencedId;
}
