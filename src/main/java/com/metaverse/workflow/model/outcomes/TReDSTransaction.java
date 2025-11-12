package com.metaverse.workflow.model.outcomes;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
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

    @CreatedDate
    @Column(name = "created_timestamp", updatable = false)
    protected LocalDateTime createdTimestamp;

    @LastModifiedDate
    @Column(name = "last_modified")
    protected LocalDateTime lastModified;
}
