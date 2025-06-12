package com.metaverse.workflow.model.outcomes;

import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "outcome_pmfme_scheme")
public class PMFMEScheme {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pmfme_id")
    private Long pmfmeId;

    @Column(name = "application_submission_date")
    private Date dateOfApplicationSubmission;

    @Column(name = "loan_sanctioned")
    private Double loanSanctioned;

    @Column(name = "grant_received")
    private Double grantReceived;

    @Column(name = "working_capital_availed")
    private Double workingCapitalAvailed;

    @Column(name = "approval_date")
    private Date dateOfApprovalUnderPMFME;

    @Column(name = "common_facility_centre_used")
    private Boolean isCommonFacilityCentreUsed;

    @Column(name = "branding_marketing_support_availed")
    private Boolean isBrandingMarketingSupportAvailed;

    @Column(name = "support_details")
    private String supportDetails;

    @Column(name = "production_capacity_mts")
    private Double productionCapacity;

    @Column(name = "certification_support_availed")
    private Boolean isCertificationSupportAvailed;

    @Column(name = "market_linkage_date")
    private Date dateOfMarketLinkage;

    @Column(name = "market_linkage_volume")
    private Double volumeOfMarketLinkage;

    @Column(name="units")
    private String units;

    @Column(name = "market_linkage_value")
    private Double valueOfMarketLinkage;

    @Column(name = "monthly_turnover")
    private Double monthlyTurnover;

    @Column(name = "turnover_change")
    private String turnoverChange;

    @Column(name = "production_capacity_change")
    private String productionCapacityChange;

    @Column(name = "branding_marketing_support_change")
    private String brandingOrMarketingSupportChange;

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

