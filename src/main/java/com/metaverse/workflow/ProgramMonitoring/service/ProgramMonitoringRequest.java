package com.metaverse.workflow.ProgramMonitoring.service;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.JoinColumn;
import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class
ProgramMonitoringRequest {

    private Long agencyId;
    private String district;
    private Long programId;
    private String userId;
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

    private Boolean venueQuality;
    private Boolean accessibility;
    private Boolean teaSnacks;
    private Boolean lunch;
    private Boolean cannedWater;
    private Boolean toiletHygiene;
    private Boolean avEquipment;
    private Boolean stationary;

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
    private List<String> bestPracticesIdentified;
}