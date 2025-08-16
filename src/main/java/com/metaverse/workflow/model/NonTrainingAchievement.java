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

    @Column(name = "physical_target")
    private Double physicalTarget;

    @Column(name = "physical_target_achievement")
    private String physicalTargetAchievement;

    @Column(name = "financial_target")
    private Double financialTarget;

    @Column(name = "financial_target_achievement")
    private Double financialTargetAchievement;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private Date createdOn;

    @Column(name="updated_on", insertable = false, updatable = true)
    @UpdateTimestamp
    private Date updatedOn;
}
