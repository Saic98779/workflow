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
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "outcome_lean")
public class Lean {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long leanId;

    @Column(name = "certification_type")
    public String certificationType; //Basic Intermediate Advanced

    @Column(name = "date_of_certification")
    private Date dateOfCertification;

    @Column(name = "lean_consultant_appointed")
    private Boolean isLeanConsultantAppointed;

    @Column(name = "date_of_appointed")
    private Date dateOfAppointed;

    @Column(name = "raw_material_wastage")
    private Double rawMaterialWastage;

    @Column(name = "production_rate")
    private Double productionRate; // units/hour

    @Column(name = "defect_rate")
    private Double defectRate; //
    // in percentage
    @Column(name = "power_usage")
    private Double powerUsage; // in Kwh


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
