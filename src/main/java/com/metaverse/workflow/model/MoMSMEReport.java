package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "momsme_report")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoMSMEReport extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mo_msme_activity_id")
    private Long moMSMEActivityId;

    @Column(name = "intervention")
    private String intervention;

    @Column(name = "component")
    private String component;//Activity

    @Column(name = "mo_msme_activity")
    private String moMSMEActivity;//subActivity

    @OneToOne
    @JoinColumn(name = "non_training_activity_id", referencedColumnName = "activityId")
    private NonTrainingActivity nonTrainingActivity;

    @OneToOne
    @JoinColumn(name = "non_training_sub_activity_id", referencedColumnName = "subActivityId")
    private NonTrainingSubActivity nonTrainingSubActivity;
}
