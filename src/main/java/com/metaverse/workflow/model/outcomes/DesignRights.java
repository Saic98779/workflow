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
@Table(name = "outcome_design_rights")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DesignRights {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "design_rights_id")
    private Long designRightsId;
    @Column(name = "date_of_application")
    private Date dateOfApplication;
    @Column(name = "date_of_design_rights_granted")
    private Date dateOfDesignRightsGranted;
    @Column(name = "certification_number")
    private String certificationNumber;
    @Column(name = "type_of_design_registered")
    private String typeOfDesignRegistered;
    @Column(name = "revenue_from_design_products")
    private Double revenueFromDesignProducts;
    @Column(name = "is_awarded_For_design_protection")
    private Boolean  isAwardedForDesignProtection;
    @Column(name = "date_of_awarded")
    private Date dateOfAwarded;
    @Column(name = "name_of_award")
    private String nameOfAward;
    @Column(name = "date_of_export")
    private Date dateOfExport;
    @Column(name = "value_of_export")
    private Double valueOfExport;
    @Column(name = "volume_of_export")
    private Double volumeOfExport;
    @Column(name = "units")
    private String units;

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
