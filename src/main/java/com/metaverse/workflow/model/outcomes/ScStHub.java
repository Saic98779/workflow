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
@Entity
@Table(name = "outcome_sc_st_hub")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ScStHub {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sc_st_hub_id")
    private Long scStHubId;

    @Column(name = "support_availed_under_nssh")
    private String supportAvailedUnderNSSH; // Training / Certification / Vendor Dev / Credit Facilitation

    @Column(name = "training_name")
    private String trainingName;

    @Column(name = "training_completed_date")
    private Date trainingCompletedDate;

    @Column(name = "certification_name")
    private String certificationName;

    @Column(name = "certification_received_date")
    private Date certificationReceivedDate;

    @Column(name = "market_linkage_company_name")
    private String marketLinkageCompanyName;

    @Column(name = "market_linkage_date")
    private Date marketLinkageDate;

    @Column(name = "market_linkage_value")
    private Double marketLinkageValue;

    @Column(name = "market_linkage_volume")
    private String marketLinkageVolume;

    @Column(name = "vendor_registration_with_psu_oem")
    private String vendorRegistrationWithPSUOrOEM;

    @Column(name = "tender_participated_name")
    private String tenderParticipatedName;

    @Column(name = "handholding_agency")
    private String handholdingAgency;

    @Column(name = "credit_linkage_date")
    private Date creditLinkageDate;

    @Column(name = "credit_linkage_amount")
    private Double creditLinkageAmount;

    @Column(name = "monthly_revenue")
    private Double monthlyRevenue;

    @Column(name = "key_challenges_faced")
    private String keyChallengesFaced;

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
