package com.metaverse.workflow.model.outcomes;

import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "outcome_ondc_transaction")
public class ONDCTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ondc_transaction_id")
    private Long ondcTransactionId;
    @ManyToOne
    @JoinColumn(name = "ondc_registration_id")
    private ONDCRegistration ondcRegistration;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "transaction_date")
    private Date transactionDate;
    @Column(name = "product_quantity")
    private Integer productQuantity;
    @Column(name = "product_units")
    private String productUnits;
    @Column(name = "transaction_type")
    private String transactionType;
    @Column(name = "transaction_value")
    private Double transactionValue;
    @Column(name = "created_on", insertable = true, updatable = false)
    @CreationTimestamp
    private Date createdOn;
    @Column(name = "updated_on", insertable = false, updatable = true)
    @UpdateTimestamp
    private Date updatedOn;

}
