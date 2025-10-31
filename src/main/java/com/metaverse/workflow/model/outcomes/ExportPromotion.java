package com.metaverse.workflow.model.outcomes;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.InfluencedParticipant;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "outcome_export_promotion")
public class ExportPromotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sector_name")
    private String sectorName;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "export_import_licence_no")
    private String exportImportLicenceNo;

    @Column(name = "mapping_with_international_buyer")
    private Boolean mappingWithInternationalBuyer;

    @Column(name = "monthly_turnover_in_lakhs")
    private Double monthlyTurnoverInLakhs;

    @Column(name = "is_export", length = 10)
    private String isExport; // Yes/No

    @Column(name = "export_date")
    private Date exportDate;

    @Column(name = "export_value_in_lakhs")
    private Double exportValueInLakhs;

    @Column(name = "export_volume_in_mts")
    private Double exportVolumeInMts;

    @Column(name="Influenced")
    Boolean isInfluenced;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;
    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;
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