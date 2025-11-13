package com.metaverse.workflow.model;

import com.metaverse.workflow.enums.BillRemarksStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "non_training_expenditure")
public class NonTrainingExpenditure {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_activity_id", nullable = false)
    private NonTrainingSubActivity nonTrainingSubActivity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private NonTrainingActivity nonTrainingActivity;

    private String dateOfPurchase;

    private String category;

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
    @CreationTimestamp
    private Date createdOn;

    @Column(name = "updated_on", insertable = false, updatable = true)
    @UpdateTimestamp
    private Date updatedOn;

    @Column(name = "check_no")
    private String checkNo;

    @Column(name = "check_date")
    private Date checkDate;

    @Column(name = "status")
    private BillRemarksStatus status;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "nonTrainingExpenditure")
    private List<NonTrainingSpiuComments> spiuComments;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "nonTrainingExpenditure")
    private List<NonTrainingAgencyComments> agencyComments;

}
