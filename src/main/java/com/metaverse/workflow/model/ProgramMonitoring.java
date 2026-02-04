package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "program_monitoring")
public class ProgramMonitoring extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "program_monitoring_id")
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
    @ElementCollection
    @CollectionTable(
            name = "no_ias_participated",
            joinColumns = @JoinColumn(name = "program_monitoring_id")
    )
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
    private Integer totalScore;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(insertable = true,updatable = false)
    @CreationTimestamp
    private Date moniteringDate;

    private Integer screen1Score;
    private Integer screen2Score;
    private Integer screen3Score;
    private Integer screen4Score;
    private Integer screen5Score;
    private Integer screen6Score;
    private Integer screen7Score;

    @ElementCollection
    @CollectionTable(
            name = "best_practices_identified",
            joinColumns = @JoinColumn(name = "program_monitoring_id")
    )
    @Column(name = "best_practices_identified")
    private List<String> bestPracticesIdentified;
}