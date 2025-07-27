package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "other_training_expenditure")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtherTrainingExpenditure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateOfExpenditure;
    private String details;
    private Double amount;
    private String billPath;
    private String billNo;
    private Date billDate;

    @ManyToOne
    @JoinColumn(name = "budget_id")
    private OtherTrainingBudget otherTrainingBudget;

    @Column(name="created_on",insertable = true,updatable = false)
    @CreationTimestamp
    private Date createdOn;

    @Column(name="updated_on",insertable = false,updatable = true)
    @UpdateTimestamp
    private Date updatedOn;
}
