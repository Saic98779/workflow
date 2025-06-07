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

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "outcome_cgtmse_transaction")
public class CGTMSETransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="cgtmse_transactio-id")
    private Long cgtmseTransactionId;

    @Column(name = "credit_application_date")
    private Date creditApplicationDate;


    @Column(name = "dpr_submission_date")
    private Date dprSubmissionDate;


    @Column(name = "approval_date")
    private Date approvalDate;


    @Column(name = "amount_release_date")
    private Date amountReleaseDate;

    @Column(name = "value_released_lakhs")
    private Double valueReleased;

    @Column(name = "credit_guarantee_percentage")
    private Integer creditGuaranteePercentage;

    @Column(name = "purpose")
    private String purpose;


    @Column(name = "grounding_date")
    private Date groundingDate;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "production_per_month_mts")
    private Integer productionPerMonth;

    @Column(name = "market_type")
    private String marketType;


    @Column(name = "marketing_date")
    private Date marketingDate;

    @Column(name = "market_volume_mts")
    private Integer marketVolume;

    @Column(name = "market_value_lakhs")
    private Double marketValue;

    @Column(name = "total_turnover")
    private Double totalTurnover;

    @Column(name = "employment_male")
    private Integer employmentMale;

    @Column(name = "employment_female")
    private Integer employmentFemale;

    @Column(name="purpose_of_upgradation")
    String proposeOfEnterpriseUpgradation;

    @Column(name="date_of_grounding")
    Date dateOfGrounding;

    @Column(name="Influenced")
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
