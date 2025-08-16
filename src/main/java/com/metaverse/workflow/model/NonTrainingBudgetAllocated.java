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
@Table(name = "non_training_budget_allocated")
public class NonTrainingBudgetAllocated {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "non_training_budget_allocated_id")
    private Long nonTrainingActivityId;

    private Double budgetAllocated;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private NonTrainingActivity nonTrainingActivity;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private Date createdOn;

    @Column(name="updated_on",insertable = false,updatable = true)
    @UpdateTimestamp
    private Date updatedOn;
}
