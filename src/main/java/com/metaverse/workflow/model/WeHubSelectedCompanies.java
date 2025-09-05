package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="wehub_selected_companies")
public class WeHubSelectedCompanies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_id")
    private Long candidateId;

    @Column(name = "udhyam_dpiit_registration_no")
    private String udhyamDpiitRegistrationNo;

    @Column(name = "application_received_date", nullable = false)
    private Date applicationReceivedDate;

    @Column(name = "application_source")
    private String applicationSource;

    @Column(name = "shortlisting_date")
    private Date shortlistingDate;

    @Column(name = "need_assessment_date")
    private Date needAssessmentDate;

    @Column(name = "candidate_finalised")
    private Boolean candidateFinalised;

    @Column(name = "cohort_name")
    private String cohortName;

    @Column(name = "baseline_assessment_date")
    private Date baselineAssessmentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_activity_id")
    private NonTrainingSubActivity nonTrainingSubActivity;

    @Column(name = "created_on", updatable = false)
    private Date createdOn;

    @Column(name = "updated_on")
    private Date updatedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;
}
