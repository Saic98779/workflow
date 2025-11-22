package com.metaverse.workflow.model;

import com.metaverse.workflow.enums.BillRemarksStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "non_training_consumables_bulk")
public class NonTrainingConsumablesBulk extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "purchase_date")
    private Date purchaseDate;

    @Column(name = "purchased_quantity")
    private Integer purchasedQuantity;

    @Column(name = "unit_cost")
    private Double unitCost;

    @Column(name = "consumed_quantity")
    private Integer consumedQuantity;

    @Column(name = "available_quantity")
    private Integer availableQuantity;

    @Column(name = "total_cost")
    private Double totalCost;

    @Column(name = "bill_no")
    private String billNo;

    @Column(name = "bill_date")
    private Date billDate;

    @Column(name = "mode_of_payment")
    private String modeOfPayment;

    @Column(name = "payee_name")
    private String payeeName;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "ifsc_code")
    private String ifscCode;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "check_no")
    private String checkNo;

    @Column(name = "check_date")
    private Date checkDate;

    @Column(name = "bill_url")
    private String uploadBillUrl;

    @OneToMany(mappedBy = "nonTrainingConsumablesBulk")
    private List<NonTrainingConsumablesTransactions> transactions ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_activity_id", nullable = false)
    private NonTrainingSubActivity nonTrainingSubActivity;

    @Column(name = "bill_status")
    private BillRemarksStatus billStatus;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "nonTrainingConsumablesBulk")
    private List<NonTrainingSpiuComments> spiuComments;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "nonTrainingConsumablesBulk")
    private List<NonTrainingAgencyComments> agencyComments;
}
