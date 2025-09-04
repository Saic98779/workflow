package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "non_training_achievement")
public class NonTrainingAchievement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "non_training_achievement_id")
    private Long nonTrainingAchievementId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activity_id", nullable = false)
    private NonTrainingActivity nonTrainingActivity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sub_activity_id", nullable = false)
    private NonTrainingSubActivity nonTrainingSubActivity;


    @Column(name = "physical_target_achievement")
    private String physicalTargetAchievement;

    @Column(name = "financial_year")
    private String financialYear;

    @Column(name = "q1_achievement")
    private String q1Achievement;

    @Column(name = "q2_achievement")
    private String q2Achievement;

    @Column(name = "q3_achievement")
    private String q3Achievement;

    @Column(name = "q4_achievement")
    private String q4Achievement;

    @Column(name = "financial_target_achievement")
    private Double financialTargetAchievement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "non_training_target_id", nullable = false)
    private NonTrainingTargets nonTrainingTarget;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private Date createdOn;

    @Column(name="updated_on", insertable = false, updatable = true)
    @UpdateTimestamp
    private Date updatedOn;
}
