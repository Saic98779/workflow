package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "bulk_expenditure")
public class BulkExpenditure {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="bulk_expenditure_id")
    private Long bulkExpenditureId;

    @OneToMany(mappedBy = "bulkExpenditure", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramSessionFile> bulkExpenditureFileList;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @ManyToOne
    @JoinColumn(name="expense_id")
    private HeadOfExpense headOfExpense;

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
    private String transactionId;//for upi

    @Column(name="remarks")
    private String remarks;

    @Column(name = "bill_url")
    private String uploadBillUrl;


    @Column(name="created_on", insertable = true, updatable = false)
    @CreationTimestamp
    private Date createdOn;

    @Column(name="updated_on", insertable = false, updatable = true)
    @UpdateTimestamp
    private Date updatedOn;
}
