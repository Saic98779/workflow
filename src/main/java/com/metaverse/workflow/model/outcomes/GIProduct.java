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
@Table(name = "outcome_gi_products")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GIProduct {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tread_mark_id")
    private Long giProductId;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "location")
    private String location;

    @Column(name = "industry")
    private String industry;

    @Column(name = "gi_product_name")
    private String giProductName;

    @Column(name = "gi_registration_number")
    private String giRegistrationNumber;

    @Column(name = "date_of_gi_registration")
    private Date dateOfGIRegistration;

    @Column(name = "jurisdiction_covered")
    private String jurisdictionCovered;

    @Column(name = "revenue_after_gi_certification")
    private Double revenueAfterGICertification;

    @Column(name = "date_of_export")
    private Date dateOfExport;

    @Column(name = "value_of_export")
    private Double valueOfExport;

    @Column(name = "country_exported")
    private String countryExported;

    @Column(name = "retail_partnership")
    private String retailPartnership;

    @Column(name = "value_of_supply")
    private Double valueOfSupply;

    @Column(name = "date_of_supply")
    private Date dateOfSupply;

    @Column(name = "jobs_created")
    private Integer totalJobsCreated;

    @Column(name = "franchise_outlets_opened")
    private Integer franchiseOutletsOpened;

    @Column(name = "annual_royalty_earnings")
    private Double annualRoyaltyEarnings;
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
