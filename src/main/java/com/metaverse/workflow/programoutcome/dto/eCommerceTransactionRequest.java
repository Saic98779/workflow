package com.metaverse.workflow.programoutcome.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class eCommerceTransactionRequest {

    public Integer year;
    public String month;
    public Integer numberOfTransactions;
    public Double totalBusinessAmount;
    public Boolean isInfluenced;
    private Long agencyId;
    private Long participantId;
    private Long organizationId;
    private Long influencedId;
}
