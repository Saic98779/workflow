package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="activity_targets")
@Data
public class ActivityTargets {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name ="activity_target_id")
        private Long activityTargetId;

        @ManyToOne
        @JoinColumn(name = "agency_id", nullable = false)
        private Agency agency;

        @ManyToOne
        @JoinColumn(name = "activity_id", nullable = false)
        private NonTrainingActivity nonTrainingActivity;

        @Column(name = "financial_year")
        private String financialYear;

        @Column(name = "q1")
        private Double q1;

        @Column(name = "q2")
        private Double q2;

        @Column(name = "q3")
        private Double q3;

        @Column(name = "q4")
        private Double q4;
}
