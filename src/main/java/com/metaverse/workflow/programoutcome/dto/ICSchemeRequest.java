package com.metaverse.workflow.programoutcome.dto;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ICSchemeRequest {

    public String industryName;
    public String location;
    public String typeOfMsme;
    public Double investment;
    public Double annualTurnover;
    public Double domesticSales;
    public Boolean isInfluenced;
    private Long agencyId;
    private Long participantId;
    private Long organizationId;
    private Long influencedId;
}
