package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "program_monitoring_feedback")
public class ProgramMonitoringFeedBack {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "monitoring_id")
    private Long monitoringFeedBackId;
    @Column(name = "program_id")
    private Long programId;
    @Column(name = "step_number")
    private Integer stepNumber;
    //Basic information
    @Column(name = "state")
    private String state;
    @Column(name = "district")
    private String district;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "agency_name")
    private String agencyName;
    @Column(name = "program_type")
    private String programType;
    @Column(name = "program_name")
    private String programName;
    @Column(name = "venue_name")
    private String venueName; //locationName
    @Column(name = "hosting_agency_name")
    private String hostingAgencyName;
    @Column(name = "spoc_name")
    private String spocName;
    @Column(name = "spoc_contact")
    private Long spocContact;
    @Column(name = "in_time")
    private String inTime;
    @Column(name = "out_time")
    private String outTime;

//Audience profile
    @Column(name = "male_participants")
    private Integer maleParticipants;
    @Column(name = "female_participants")
    private Integer femaleParticipants;
    @Column(name = "transe_gender_participants")
    private Integer transGenderParticipants;
    @Column(name = "total_participants")
    private Integer totalParticipants;
    @Column(name = "no_of_SHG")
    private Integer noOfSHG;
    @Column(name = "no_of_MSME")
    private Integer noOfMSME;
    @Column(name = "no_of_Startup")
    private Integer noOfStartup;
    @Column(name = "no_of_DIC")
    private Integer noOfDIC;
    @Column(name = "no_of_IAs")
    private Integer noOfIAs;
    @Column(name = "no_of_Others")
    private Integer noOfOthers;
    @Column(name = "no_of_SC")
    private Integer noOfSC;
    @Column(name = "no_of_ST")
    private Integer noOfST;
    @Column(name = "no_of_OC")
    private Integer noOfOC;
    @Column(name = "no_of_BC")
    private Integer noOfBC;
    @Column(name = "no_of_Minority")
    private Integer noOfMinority;


//PreEventChecklist
    @OneToMany(mappedBy = "programMonitoringFeedBack", cascade = CascadeType.ALL)
    private List<PreEventChecklist> preEventChecklists;

 //ProgramDeliveryDetails
    @OneToMany(mappedBy = "programMonitoringFeedBack", cascade = CascadeType.ALL)
    private List<ProgramDeliveryDetails> programDeliveryDetails;

    //programExecution
    @Column(name = "timing_punctuality",length = 1000)
    private String timingPunctuality ;
    @Column(name = "session_continuity")
    private String sessionContinuity;
    @Column(name = "participant_interest_level")
    private String participantInterestLevel;
    @Column(name = "overall_energy_engagement")
    private String overallEnergyEngagement;
    @Column(name = "participant_queries")
    private String participantQueries;
    @Column(name = "unforeseen_issues", length = 1000)
    private String unforeseenIssues;

    //LogisticsEvaluation
    @OneToMany(mappedBy = "programMonitoringFeedBack", cascade = CascadeType.ALL)
    private List<LogisticsEvaluation> logisticsEvaluations;

    //post-training Assessment
    @Column(name = "participants_feedback_collected")
    private Boolean participantsFeedbackCollected;
    @Column(name = "resource_feedback_collected")
    private Boolean resourceFeedbackCollected;
    @Column(name = "pre_post_tests_conducted")
    private Boolean prePostTestsConducted;
    @Column(name = "learning_highlights", length = 1000)
    private String learningHighlights;

    //Document checklist
    @Column(name = "attendance_sheet")
    private Boolean attendanceSheet;
    @Column(name = "registration_forms")
    private Boolean registrationForms;
    @Column(name = "participant_feedBack")
    private Boolean participantFeedBack;
    @Column(name = "speaker_feedBack")
    private Boolean speakerFeedBack;
    @Column(name = "photographs_attached")
    private Boolean photographsAttached;
    @Column(name = "summary_report_prepared")
    private Boolean summaryReportPrepared;

    //Observation,challenges,Best Practices
    @Column(name = "observed_practices", length = 1000)
    private String observedPractices;
    @Column(name = "challenges_faced", length = 1000)
    private String challengesFaced;
    @Column(name = "issues_and_corrections", length = 1000)
    private String issuesAndCorrections;

    //overall program rating
    @Column(name = "overall_program_organization")
    private Integer overallProgramOrganization;
    @Column(name = "quality_of_sessions")
    private Integer qualityOfSessions;
    @Column(name = "participants_satisfaction")
    private Integer participantsSatisfaction;
    @Column(name = "impact_potential")
    private Integer impactPotential;

    //additionalRemarks/Recommendations
    @Column(name = "additional_remarks")
    private String additionalRemarks;


}
