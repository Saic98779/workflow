package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "program_delivery_details_new")
public class ProgramDeliveryDetailsNew {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="program_delivery-details_id")
    private Long programDeliveryDetailsId;
    @Column(name="speaker-name")
    private String speakerName;
    @Column(name="topic_delivered")
    private String topicDelivered;
    @Column(name="time_taken")
    private Integer timeTaken;
    @Column(name="audio_visual_used")
    private Boolean audioVisualUsed;
    @Column(name="relevance")
    private String relevance;


    @ManyToOne
    @JoinColumn(name = "program_monitoring_id", nullable = false)
    private ProgramMonitoring programMonitoring;

}
