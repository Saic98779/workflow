package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TReDSTransactionDTO {
    private Long id;
    private String tredsRegistrationNo;
    private Date tredsTransactionDate;
    private String invoiceNumber;
    private String buyerName;
    private String tredsPlatformUsed;
    private Double invoiceAmount;
    private Date bidOpeningDate;
    private String winnerFinancier;
    private Double discountRateOffered;
    private Double discountingFeeFor60Days;
    private Double finalPayoutToMsme;
    private Date paymentSettlementDate;
    private Date buyerDueDateToPay;
    private Date repaymentDate;

    private String agencyName;
    private String participantName;
    private String organizationName;

    private Date createdOn;  // optional if added later
    private Date updatedOn;
}

