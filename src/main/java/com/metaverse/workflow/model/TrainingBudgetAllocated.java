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
@Table(name = "training_budget_allocated")
// The TrainingBudgetAllocated class represents a budget allocated for training activities.
public class TrainingBudgetAllocated {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "training_budget_allocated_id")
    private Long trainingBudgetAllocatedId;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activityId;

    @ManyToOne
    @JoinColumn(name = "sub_activity_id")
    private SubActivity subActivityId;

    @Column(name = "training_target")
    private Long trainingTarget;

    @Column(name="budget_allocated")
    private Double budgetAllocated;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private Date createdOn;

    @Column(name="updated_on",insertable = false,updatable = true)
    @UpdateTimestamp
    private Date updatedOn;
}
