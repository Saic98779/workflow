package com.metaverse.workflow.programoutcome.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TReDSTransactionRequest {

    public String tredsTransactionDate;
    public String invoiceNumber;
    public String buyerName;
    public String tredsPlatformUsed;
    public Double invoiceAmount;
    public String bidOpeningDate;
    public String winnerFinancier;
    public Double discountRateOffered;
    public Double discountingFeeFor60Days;
    public Double finalPayoutToMsme;
    public String paymentSettlementDate;
    public String buyerDueDateToPay;
    public String repaymentDate;
    private Long participantId;
    private Long organizationId;
    private Long agencyId;

}
