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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table
@Entity
public class TrainingTarget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainingTargetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id", nullable = false)
    private Agency agency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Column(name = "financial_year")
    private String financialYear;

    @Column(name = "q1")
    private Integer q1;

    @Column(name = "q2")
    private Integer q2;

    @Column(name = "q3")
    private Integer q3;

    @Column(name = "q4")
    private Integer q4;

    @Column(name="created_on",insertable = true,updatable = false)
    @CreationTimestamp
    private Date createdOn;

    @Column(name="updated_on",insertable = false,updatable = true)
    @UpdateTimestamp
    private Date updatedOn;
}
