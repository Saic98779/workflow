package com.metaverse.workflow.model.outcomes;

import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "outcome_gem_transaction")
public class GeMTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="gem_transaction_id")
    private Long gemTransactionId;

    @Column(name = "procurement_date")
    private Date procurementDate;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "unit_of_measurement")
    private String unitOfMeasurement;

    @Column(name = "registered_as")
    private String registeredAs; // Buyer or Seller

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "product_value")
    private Double productValue;


    @Column(name="Influenced")
    Boolean isInfluenced;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
    @Column(name = "created_on", insertable = true, updatable = false)
    @CreationTimestamp
    private Date createdOn;
    @Column(name = "updated_on", insertable = false, updatable = true)
    @UpdateTimestamp
    private Date updatedOn;
}
