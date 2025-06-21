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
@Table(name = "outcome_tread_mark")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreadMark {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long treadMarkId;

    @Column(name = "name_of_trade_mark")
    private String nameOfTradMark;

    @Column(name = "trademark_class")
    private String trademarkClass;

    @Column(name = "trade_mark_registration_no")
    private String tradeMarkRegistrationNo;

    @Column(name = "date_of_registration")
    private Date dateOfRegistration;

    @Column(name = "jurisdiction")
    private String jurisdictionCovered;

    @Column(name = "annual_revenue_after_registration")
    private Double annualRevenueAfterRegistration;

    @Column(name = "date_of_export")
    private Date dateOfExport;

    @Column(name = "value_of_export")
    private Double valueOfExport;

    @Column(name = "country_of_export")
    private String countryOfExport;

    @Column(name = "retail_partnership")
    private String retailPartnership;

    @Column(name = "value_of_supply")
    private Double valueOfSupply;

    @Column(name = "date_of_supply")
    private Date dateOfSupply;

    @Column(name = "total_jobs_created")
    private Integer totalJobsCreated;

    @Column(name = "no_of_franchise_outlets_opened")
    private Integer noOfFranchiseOutletsOpened;

    @Column(name = "annual_royalty_earnings_from_franchise")
    private Double annualRoyaltyEarningsFromFranchise;

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
