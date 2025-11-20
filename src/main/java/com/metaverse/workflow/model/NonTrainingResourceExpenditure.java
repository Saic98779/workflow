package com.metaverse.workflow.model;

import com.metaverse.workflow.enums.BillRemarksStatus;
import com.metaverse.workflow.enums.RemarkBy;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "non_training_resource_expenditure")
public class NonTrainingResourceExpenditure {

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

    @Column(name = "created_on", updatable = false)
    @CreationTimestamp
    private Date createdOn;

    @Column(name = "updated_on", insertable = false, updatable = true)
    @UpdateTimestamp
    private Date updatedOn;

    @Column(name = "status")
    private BillRemarksStatus status;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "nonTrainingResourceExpenditure")
    private List<NonTrainingSpiuComments> spiuComments;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "nonTrainingResourceExpenditure")
    private List<NonTrainingAgencyComments> agencyComments;

}
