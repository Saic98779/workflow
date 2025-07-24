package com.metaverse.workflow.ProgramMonitoring.service;

import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProgramMonitoringResponse {
    private Long programMonitoringId;
    private Long agencyId;
    private String district;
    private Long programId;
    private Integer stepNumber;

    private Boolean programAgendaCirculated;
    private Boolean programAsPerSchedule;
    private Boolean trainingMaterialSupplied;
    private Boolean seatingArrangementsMade;
    private Boolean avProjectorAvailable;
    private String howDidYouKnowAboutProgram;

    private Boolean participantsMale;
    private Boolean participantsFemale;
    private Boolean participantsTransgender;

    private Boolean dicRegistrationParticipated;
    private Boolean shgRegistrationParticipated;
    private Boolean msmeRegistrationParticipated;
    private Boolean startupsRegistrationParticipated;
    private List<String> noIAsParticipated;

    private String speaker1Name;
    private Boolean topicAsPerSessionPlan1;
    private Integer timeTaken1;
    private Boolean audioVisualAidUsed1;
    private String relevance1;
    private Boolean sessionContinuity1;
    private Boolean participantInteraction1;

    private String speaker2Name;
    private Boolean topicAsPerSessionPlan2;
    private Integer timeTaken2;
    private Boolean audioVisualAidUsed2;
    private String relevance2;
    private Boolean sessionContinuity2;
    private Boolean participantInteraction2;

    private String venueQuality;
    private String accessibility;
    private String teaSnacks;
    private String lunch;
    private String cannedWater;
    private String toiletHygiene;
    private String avEquipment;
    private String stationary;

    private Boolean relevant;
    private Boolean enthusiast;
    private Boolean feltUseful;
    private Boolean futureWillingToEngage;

    private Boolean qualified;
    private Boolean experienced;
    private Boolean certified;
    private Boolean deliveryMethodologyGood;
    private Boolean relevantExperience;

    private String overallObservation;
    private String submittedBy;
    private Integer totalScore;

}