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
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name="outcome_skill_upgradation")
public class SkillUpgradation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(
            name = "skill_upgradation_training_types",
            joinColumns = @JoinColumn(name = "skill_upgradation_id")
    )
    @Column(name = "type_of_training_received")
    private List<String> typeOfTrainingReceived;

    @Column(name = "training_completion_date")
    private Date trainingCompletionDate;

    @Column(name = "business_plan_submission_date")
    private Date businessPlanSubmissionDate;

    @Column(name = "amount_sanctioned_date")
    private Date amountSanctionedDate;

    @Column(name = "amount_released_date")
    private Date amountReleasedDate;

    @Column(name = "amount_released_in_lakhs")
    private Double amountReleasedInLakhs;

    @Column(name = "bank_provided_loan")
    private String bankProvidedLoan;

    @Column(name = "loan_type")
    private String loanType;

    @Column(name = "loan_purpose")
    private String loanPurpose;

    @Column(name = "grounding_date")
    private Date groundingDate;

    @Column(name = "sector_type")
    private String sectorType;

    @Column(name = "monthly_turnover_in_lakhs")
    private Double monthlyTurnoverInLakhs;

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
