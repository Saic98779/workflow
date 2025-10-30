package com.metaverse.workflow.model.outcomes;

import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.InfluencedParticipant;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
@Entity
@Table(name = "outcome_e_commerce_transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ECommerceTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer year;
    private String month;
    private Integer numberOfTransactions;
    private Double totalBusinessAmount;

    @ManyToOne
    @JoinColumn(name = "registration_id")
    private ECommerceRegistration ecommerceRegistration;

    @Column(name = "is_influenced")
    private Boolean isInfluenced;
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
    @com.fasterxml.jackson.annotation.JsonIgnore
    @ManyToOne
    @JoinColumn(name = "influenced_id")
    private InfluencedParticipant influencedParticipant;
}
