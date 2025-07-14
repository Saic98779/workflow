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
@Table(name = "program_monitoring")
public class ProgramMonitoring {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "program_monitoring_id")
    private Long programMonitoringId;
    private Long programId;
    private Integer stepNumber;

    //Basic information
    private String state;
    private String district;
    private Date startDate;
    private String agencyName;
    private String programType;
    private String programName;
    private String venueName; //locationName
    private String hostingAgencyName;
    private String spocName;
    private Long spocContact;
    private String inTime;
    private String outTime;

    //Audience profile
    private Integer maleParticipants;
    private Integer femaleParticipants;
    private Integer transGenderParticipants;
    private Integer totalParticipants;
    private Integer noOfSHG;
    private Integer noOfMSME;
    private Integer noOfStartup;
    private Integer noOfDIC;
    private Integer noOfIAs;

    //PreEventChecklist
    @OneToMany(mappedBy = "programMonitoring", cascade = CascadeType.ALL)
    private List<PreEventChecklistNew> preEventChecklists;
    private String participantKnowAboutProgram;

    //ProgramDeliveryDetails
    @OneToMany(mappedBy = "programMonitoring", cascade = CascadeType.ALL)
    private List<ProgramDeliveryDetailsNew> programDeliveryDetails;

    //programExecution
    private Boolean timingPunctuality ;
    private Boolean sessionContinuity;
    private Boolean participantInterestLevel;

    //LogisticsEvaluation
    @OneToMany(mappedBy = "programMonitoring", cascade = CascadeType.ALL)
    private List<LogisticsEvaluationNew> logisticsEvaluations;

    //Document checklist
    private Boolean attendanceSheet;
    private Boolean registrationForms;
    private Boolean participantFeedBack;
    private Boolean speakerFeedBack;

    //additionalRemarks/Recommendations
    private String overallObservation;
}