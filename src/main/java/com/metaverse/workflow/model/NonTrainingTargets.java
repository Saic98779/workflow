package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "non_training_targets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NonTrainingTargets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activity_id", nullable = false)
    private NonTrainingActivity nonTrainingActivity;

    @Column(name = "financial_year")
    private String financialYear;

    @Column(name = "q1_target")
    private Integer q1Target;

    @Column(name = "q1_budget")
    private Double q1Budget;

    @Column(name = "q2_target")
    private Integer q2Target;

    @Column(name = "q2_budget")
    private Double q2Budget;

    @Column(name = "q3_target")
    private Integer q3Target;

    @Column(name = "q3_budget")
    private Double q3Budget;

    @Column(name = "q4_target")
    private Integer q4Target;

    @Column(name = "q4_budget")
    private Double q4Budget;

    @OneToMany(mappedBy = "nonTrainingTarget", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NonTrainingAchievement> achievements;

    @Column(name="created_on",insertable = true,updatable = false)
    @CreationTimestamp
    private Date createdOn;

    @Column(name="updated_on",insertable = false,updatable = true)
    @UpdateTimestamp
    private Date updatedOn;
}
