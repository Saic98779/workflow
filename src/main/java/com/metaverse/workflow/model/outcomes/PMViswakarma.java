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


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "outcome_pm_viswakarma")
public class PMViswakarma {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pm_viswakarma_id")
    private Long pmViswakarmaId;

    @Column(name = "artisan_category")
    private String artisanCategory;

    @Column(name = "date_of_training")
    private Date dateOfTraining;

    @Column(name = "certificate_issue_date")
    private Date certificateIssueDate;

    @Column(name = "date_of_credit_availed")
    private Date dateOfCreditAvailed;

    @Column(name = "amount_of_credit_availed")
    private Double amountOfCreditAvailed;

    @Column(name = "purpose_of_utilisation")
    private String purposeOfUtilisation; //dp- Working Capital / Renovation / Equipment

    @Column(name = "monthly_income_after_credit")
    private Double monthlyIncomeAfterCredit;

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
