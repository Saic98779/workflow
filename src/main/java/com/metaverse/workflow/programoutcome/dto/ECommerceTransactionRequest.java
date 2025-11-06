package com.metaverse.workflow.programoutcome.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ECommerceTransactionRequest {

    public String fromDate;
    public String toDate;
    public Integer numberOfTransactions;
    public Double totalBusinessAmount;
    private Long agencyId;
    private Long participantId;
    private Long organizationId;
    private Long influencedId;
}
