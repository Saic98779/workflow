package com.metaverse.workflow.model.outcomes;

import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name="outcome_zed_certification")
public class ZEDCertification {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="zed_certificate_registration_id")
	private Long zedCertificateRegistrationId;

    @Column(name = "machinery_type")
    private String machineryType;

    @Column(name = "dpr_submission_date")
    private Date dprSubmissionDate;

    @Column(name = "amount_released_date")
    private Date amountReleasedDate;

    @Column(name = "released_value")
    private Double releasedValue;

    @Column(name = "grounding_date")
    private Date groundingDate;

    @Column(name = "certification_date")
    private Date certificationDate;

    @Column(name = "zed_certification_type")
    private String zedCertificationType; // Bronze / Silver / Gold

    @Column(name = "turnover")
    private Double turnover;

    @Column(name = "energy_consumption_kwh_per_hr")
    private Double energyConsumptionKwhHr;

    @Column(name = "production_mt_per_hr")
    private Integer productionMtHr;

    @Column(name = "product_defect_rate_per_100_units")
    private Integer defectRatePer100Units;

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
	

}
