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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "outcome_sidbi_aspire")
public class SIDBIAspire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sidbiAspireId;

    @Column(name = "application_submission_date")
    private Date applicationSubmissionDate;

    @Column(name = "sanction_date")
    private Date sanctionDateUnderAspire;

    @Column(name = "funding_support_received")
    private Boolean fundingSupportReceived;

    @Column(name = "incubation_partner_name")
    private String incubationPartnerName;

    @Column(name = "funding_type")
    private String fundingType;

    @Column(name = "support_amount")
    private Double supportAmount;

    @Column(name = "machinery_setup_date")
    private Date machinerySetupDate;

    @Column(name = "production_started_date")
    private Date productionStartedDate;

    @Column(name = "monthly_production_units")
    private Integer monthlyProductionUnits;

    @Column(name = "market_linkage_enabled")
    private Boolean marketLinkageEnabled;

    @Column(name = "market_linkage_date")
    private Date marketLinkageDate;

    @Column(name = "market_linkage_volume")
    private String marketLinkageVolume;

    @Column(name = "market_linkage_value")
    private Double marketLinkageValue;

    @Column(name = "monthly_turnover")
    private Double monthlyTurnover;

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
