package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "other_training_budget")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtherTrainingBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long budgetId;
    private String budgetHead;
    private Double phyTarget;
    private String phyTargetAchievement;
    private Double finTarget;
    private Double finTargetAchievement;

    @ManyToOne
    @JoinColumn(name = "agency_id", nullable = false)
    private Agency agency;

    @OneToMany(mappedBy = "otherTrainingBudget", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OtherTrainingExpenditure> otherTrainingExpenditures = new ArrayList<>();

    @Column(name="created_on",insertable = true,updatable = false)
    @CreationTimestamp
    private Date createdOn;

    @Column(name="updated_on",insertable = false,updatable = true)
    @UpdateTimestamp
    private Date updatedOn;


    public void addExpenditure(OtherTrainingExpenditure expenditure) {
        expenditure.setOtherTrainingBudget(this); // 🔁 child points to parent
        this.otherTrainingExpenditures.add(expenditure);
    }
}

