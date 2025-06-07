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

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "outcome_pmegp")
public class PMEGP {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pmegp_id")
    private Long pmegpId;

    @Column(name = "loan_sanctioned_date")
    private Date loanSanctionedDate;

    @Column(name = "loan_amount_released_lakhs")
    private Double loanAmountReleased;

    @Column(name = "govt_subsidy_lakhs")
    private Double govtSubsidy;

    @Column(name = "beneficiary_contribution_lakhs")
    private Double beneficiaryContribution;

    @Column(name = "total_amount_released_lakhs")
    private Double totalAmountReleased;

    @Column(name = "grounding_date")
    private Date groundingDate;

    @Column(name = "business_turnover_lakhs")
    private Double businessTurnover;

    @Column(name = "market_linkage_date")
    private Date dateOfMarketLinkage;

    @Column(name = "market_volume")
    private Double volumeOfMarket;

    @Column(name = "units")
    private String units;

    @Column(name = "market_value")
    private Double valueOfMarket;

    @Column(name = "product_market_name")
    private String nameOfProductMarket;

    @Column(name = "persons_employed")
    private Integer numberOfPersonsEmployed;

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
