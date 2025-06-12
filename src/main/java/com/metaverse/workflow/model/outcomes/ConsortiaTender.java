package com.metaverse.workflow.model.outcomes;

import com.metaverse.workflow.model.Agency;
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
//Consortia & Tender Marketing
@Entity
@Table(name = "outcome_consortia_tender")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsortiaTender {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="consortia_tender_id")
    private Long consortiaTenderId;

    @Column(name = "product_or_service_offered", nullable = false)
    private String productOrServiceOffered;

    @Column(name = "consortia_member_type", nullable = false)
    private String consortiaMemberType; // Member / Lead member

    @Column(name = "consortia_name")
    private String consortiaName;

    @Column(name = "joining_date")
    private Date dateOfJoiningConsortia;

    @Column(name = "tender_name")
    private String tenderParticipatedName;

    @Column(name = "department_name")
    private String departmentTenderIssued;

    @Column(name = "tender_id")
    private String tenderId;

    @Column(name = "tender_value")
    private Double tenderValue;

    @Column(name = "tender_outcome")
    private String tenderOutcome; // Awarded / Not Awarded

    @Column(name = "work_order_issue_date")
    private Date workOrderIssueDate;

    @Column(name = "order_executed")
    private Boolean isOrderExecuted;

    @Column(name = "challenges_faced", columnDefinition = "TEXT")
    private String challengesFaced;

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
}










