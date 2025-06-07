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
@Table(name = "outcome_patents")
public class Patents {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "patent_id")
    private Long patentId;

    @Column(name = "patent_name")
    private String nameOfPatent;

    @Column(name = "patent_type")
    private String typeOfPatent;

    @Column(name = "patent_number")
    private String patentNumber;

    @Column(name = "date_of_grant")
    private Date dateOfGrant;

    @Column(name = "patent_coverage")
    private String patentCoverage;

    @Column(name = "annual_revenue")
    private Double annualRevenue;

    @Column(name = "date_of_export")
    private Date dateOfExport;

    @Column(name = "value_of_export")
    private Double valueOfExport;

    @Column(name = "country_of_export")
    private String countryOfExport;

    @Column(name = "jobs_created")
    private Integer totalJobsCreated;

    @Column(name = "award_name")
    private String nameOfAward;

    @Column(name = "date_of_award")
    private Date dateOfAward;

    @Column(name = "Influenced")
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
