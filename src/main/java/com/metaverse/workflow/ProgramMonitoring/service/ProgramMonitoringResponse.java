package com.metaverse.workflow.ProgramMonitoring.service;

import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProgramMonitoringResponse {
    private Long monitorId;
    private Long programId;
    private Integer stepNumber;
    private String state;
    private String district;
    private String startDate;
    private String agencyName;
    private String programType;
    private String programName;
    private String venueName;
    private String hostingAgencyName;
    private String spocName;
    private Long spocContact;
    private String inTime;
    private String outTime;

    private Integer maleParticipants;
    private Integer femaleParticipants;
    private Integer transGenderParticipants;
    private Integer totalParticipants;
    private Boolean noOfSHG;
    private Boolean noOfMSME;
    private Boolean noOfStartup;
    private Boolean noOfDIC;
    private Boolean noOfIAs;

    private Boolean timingPunctuality ;
    private Boolean sessionContinuity;
    private Boolean participantInterestLevel;

    private Boolean attendanceSheet;
    private Boolean registrationForms;
    private Boolean participantFeedBack;
    private Boolean speakerFeedBack;

    private String overallObservation;
    private List<PreEventChecklist> preEventChecklists;
    private String participantKnowAboutProgram;
    private List<ProgramDeliveryDetails> programDeliveryDetails;
    private List<LogisticsEvaluation> logisticsEvaluations;

    @Builder
    @AllArgsConstructor
    @Getter
    @Setter
    public static class PreEventChecklist {
        private String item;
        private Boolean status;
        private String remarks;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    @Setter
    public static class ProgramDeliveryDetails {
        private Long programDeliveryDetailsId;
        private String speakerName;
        private String topicDelivered;
        private Integer timeTaken;
        private Boolean audioVisualUsed;
        private String relevance;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    @Setter
    public static class LogisticsEvaluation {
        private String parameter;
        private Integer rating;
        private String remarks;
    }
}
