package com.metaverse.workflow.model.outcomes;

import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Table(name = "outcome_greening_of_msme")
public class GreeningOfMSME {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "greening_of_msme")
    private Long greeningOfMSME;

    @Column(name = "type_of_ intervention")
    private String typeOfIntervention;
    @Column(name = "type_of_prototype_proposed")
    private String typeOfPrototypeProposed;

    @Column(name = "type_of_trainings_received")
    private String typeOfTrainingsReceived;

    @Column(name = "training_completion_date")
    private Date trainingCompletionDate;

    @Column(name = "business_plan_submission_date")
    private Date businessPlanSubmissionDate;

    @Column(name = "amount_sanctioned_date")
    private Date amountSanctionedDate;

    @Column(name = "amount_released_date")
    private Date amountReleasedDate;

    @Column(name = "amount_released")
    private Double amountReleased; // in Lakhs

    @Column(name = "loan_provider_bank_name")
    private String nameOfBankProvidedLoan;

    @Column(name = "date_of_grounding")
    private Date dateOfGrounding;

    @Column(name = "purpose_of_loan_utilised")
    private String purposeOfLoanUtilised;

    @Column(name = "parameter_1")
    private String parameter1;

    @Column(name = "parameter_2")
    private String parameter2;

    @Column(name = "unit_of_measurement")
    private String unitForMeasurementOfProduction;

    @Column(name = "production_per_hour")
    private Double productionPerHour;
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
