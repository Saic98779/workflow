package com.metaverse.workflow.model.outcomes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "outcome_treds_transaction")
public class TReDSTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "treds_transaction_id")
    private Long tredsTransactionId;
    @ManyToOne
    @JoinColumn(name = "treds_registration_id")
    private TReDSRegistration tredsRegistration;

    @Column(name = "treds_transaction_date")
    private Date tredsTransactionDate;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "buyer_name")
    private String buyerName;

    @Column(name = "treds_platform_used")
    private String tredsPlatformUsed;

    @Column(name = "invoice_amount")
    private Double invoiceAmount;

    @Column(name = "bid_opening_date")
    private Date bidOpeningDate;

    @Column(name = "winner_financier")
    private String winnerFinancier;

    @Column(name = "discount_rate_offered")
    private Double discountRateOffered;

    @Column(name = "discounting_fee_for_60_days")
    private Double discountingFeeFor60Days;

    @Column(name = "final_payout_to_msme")
    private Double finalPayoutToMsme;

    @Column(name = "payment_settlement_date")
    private Date paymentSettlementDate;

    @Column(name = "buyer_due_date_to_pay")
    private Date buyerDueDateToPay;

    @Column(name = "repayment_date")
    private Date repaymentDate;
}
