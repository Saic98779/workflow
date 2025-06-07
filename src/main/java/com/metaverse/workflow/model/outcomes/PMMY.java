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
@Table(name = "outcome_pmmy")
public class PMMY{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pmmy_id")
    private Long pmmyId;

    @Column(name = "loan_amount_released")
    private Double loanAmountReleased;

    @Column(name = "loan_sanctioned_date")
    @Temporal(TemporalType.DATE)
    private Date loanSanctionedDate;

    @Column(name = "grounding_date")
    @Temporal(TemporalType.DATE)
    private Date groundingDate;

    @Column(name = "business_turnover")
    private Double businessTurnover;

    @Column(name = "market_linkage_date")
    @Temporal(TemporalType.DATE)
    private Date marketLinkageDate;

    @Column(name = "market_volume_mt")
    private Double marketVolume;
    @Column(name = "units")
    private String units;
    @Column(name = "market_value")
    private Double marketValue;

    @Column(name = "product_marketed_name")
    private String productMarketedName;
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
