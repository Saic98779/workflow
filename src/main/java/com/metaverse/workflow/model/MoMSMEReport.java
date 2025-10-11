package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne
    @JoinColumn(name = "non_training_activity_id", referencedColumnName = "activity_id")
    private NonTrainingActivity nonTrainingActivity;

    @OneToOne
    @JoinColumn(name = "non_training_sub_activity_id", referencedColumnName = "sub_activity_id")
    private NonTrainingSubActivity nonTrainingSubActivity;

}
