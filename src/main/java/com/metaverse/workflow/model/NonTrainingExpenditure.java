package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "non_training_expenditure")
// The NonTrainingExpenditure class represents expenditures related to non-training activities.
public class NonTrainingExpenditure {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activityId;

    @Column(name = "payment_date")
    private Date paymentDate;

    @Column(name = "expenditure_amount")
    private Double expenditureAmount;

    @Column(name = "bill_no")
    private String billNo;

    @Column(name = "bill_date")
    private Date billDate;

    @Column(name = "payee_name")
    private String payeeName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "ifsc_code")
    private String ifscCode;

    @Column(name = "mode_of_payment")
    private String modeOfPayment;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "bill_url")
    private String uploadBillUrl;

    @Column(name = "created_on", updatable = false)
    private String createdOn;

    @Column(name = "updated_on", insertable = false, updatable = true)
    private String updatedOn;

}
