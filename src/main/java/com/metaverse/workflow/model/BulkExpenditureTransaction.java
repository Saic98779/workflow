package com.metaverse.workflow.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "bulk_expenditure_transaction")
public class BulkExpenditureTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="bulk_expenditure_transaction_id")
    private Long bulkExpenditureTransactionId;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "bulk_expenditure_id")
    private BulkExpenditure expenditure;

    @ManyToOne
    @JoinColumn(name = "sub_activity_id")
    private SubActivity subActivity;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @ManyToOne
    @JoinColumn(name="expense_id")
    private HeadOfExpense headOfExpense;


    @Column(name = "consumed_quantity")
    private Integer consumedQuantity;

    @Column(name = "allocated_cost")
    private Double allocatedCost;

    @Column(name="created_on", insertable = true, updatable = false)
    @CreationTimestamp
    private Date createdOn;

    @Column(name="updated_on", insertable = false, updatable = true)
    @UpdateTimestamp
    private Date updatedOn;
}
