package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "participant_temp")
public class ParticipantTemp {
    @Id
    @Column(name = "participant_temp_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long participantTempId;
    @Column(name = "participant_name")
    private String participantName;
    @Column(name = "gender")
    private Character gender;
    @Column(name = "category")
    private String category;
    @Column(name = "disability")
    private Character disability;
    @Column(name = "aadhar_no")
    private Long aadharNo;
    @Column(name = "mobile_no")
    private Long mobileNo;
    @Column(name = "email")
    private String email;
    @Column(name = "designation")
    private String designation;
    @Column(name = "is_participated_before")
    private Character isParticipatedBefore;
    @Column(name = "previous_participation_details")
    private String previousParticipationDetails;
    @Column(name = "pre_training_ssessment_conducted")
    private Character preTrainingAssessmentConducted;
    @Column(name = "post_training_ssessment_conducted")
    private Character postTrainingAssessmentConducted;
    @Column(name = "is_certificate_issued")
    private Character isCertificateIssued;
    @Column(name = "certificate_issue_date")
    private Date certificateIssueDate;
    @Column(name = "need_assessment_methodology")
    private String needAssessmentMethodology;
    @ManyToOne(cascade = CascadeType.ALL,targetEntity = OrganizationTemp.class)
    @JoinColumn(name = "organization_temp_id",referencedColumnName = "organization_temp_id")
    private OrganizationTemp organizationTemp;
    @ManyToMany
    @JoinTable(
            name = "program_participant_temp",
            joinColumns = @JoinColumn(name = "participant_temp_id"),
            inverseJoinColumns = @JoinColumn(name = "program_id")
    )
    private List<Program> programs = new ArrayList<>();
    @Column(name = "created_on",insertable = true,updatable = false)
    @CreationTimestamp
    private Date createdOn;
    @Column(name = "updated_on",insertable = false,updatable = true)
    @UpdateTimestamp
    private Date updatedOn;
}
