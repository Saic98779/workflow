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
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="outcome_lean")
public class Lean {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long leanId;

    @Column(name = "zed_certification_type")
    public String zedCertificationType; // Bronze / Silver / Gold /dropdown

    @Column(name = "certification_number")
    private String certificationNumber;

    @Column(name = "date_of_certification")
    private Date dateOfCertification;

    @Column(name = "valid_upto")
    private Date validUpto;

    @Column(name = "production_rate")
    private Double productionRate; // units/hour

    @Column(name = "defect_rate")
    private Double defectRate; // in percentage

    @Column(name = "raw_material_wastage")
    private Double rawMaterialWastage; // MTs/hour

    @Column(name = "energy_consumption")
    private Double energyConsumption; // in Kwh


    @Column(name = "date_of_export")
    private Date dateOfExport;

    @Column(name = "value_of_export")
    private Double valueOfExport; // in Lakhs

    @Column(name = "volume_of_export")
    private Double volumeOfExport; // in MTs

    @Column(name = "units")
    private String units;

    @Column(name = "annual_turnover")
    private Double annualTurnover; // in Lakhs

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
}
