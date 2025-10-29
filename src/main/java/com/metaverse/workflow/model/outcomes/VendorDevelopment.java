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
import java.util.List;


@Data

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "outcome_vendor_development")
public class VendorDevelopment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long vendorDevelopmentId;

    @Column(name = "date_of_participation")
    private Date dateOfParticipation;

    @Column(name = "vdp_program_name")
    private String vdpProgramName;

    @Column(name = "product_showcased")
    private String productShowcased;

    @ElementCollection
    @CollectionTable(
            name = "skill_upgradation_buyers_interested",
            joinColumns = @JoinColumn(name = "skill_upgradation_id")
    )
    @Column(name = "name_of_buyer_interested")
    private List<String> nameOfBuyersInterested;

    @Column(name = "vendor_registered_with")
    private String vendorRegisteredWith;

    @Column(name = "is_e_procurement_registered")
    private Boolean iseProcurementRegistered;
    //if yes
    @Column(name = "portal_name")
    private String portalName;//doubt

    @Column(name = "is_digital_catalog_created")
    private Boolean isDigitalCatalogCreated;

    @Column(name = "date_of_supply")
    private Date dateOfSupply;

    @Column(name = "volume_of_supply")
    private Double volumeOfSupply;

    @Column(name="units")
    private String units;

    @Column(name = "value_of_supply")
    private Double valueOfSupply;

    @Column(name = "monthly_turnover")
    private Double monthlyTurnover;

    @Column(name = "Influenced")
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
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "influenced_id")
    private InfluencedParticipant influencedParticipant;
}
