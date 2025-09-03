package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.util.Date;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "non_training_resource_expenditure")
public class
NonTrainingResourceExpenditure {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "non_training_resource_expenditure_id")
    private Long nonTrainingResourceExpenditureId;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", nullable = false)
    private NonTrainingResource nonTrainingResource;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "payment_for_month", nullable = false)
    private String paymentForMonth;

    @Column(name = "date_of_payment", nullable = false)
    private Date dateOfPayment;

    private String uploadBillUrl;
}
