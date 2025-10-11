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
@Table(name = "mo_msme_report_submitted")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoMSMEReportSubmitted {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submitted_id")
    private Long submittedId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mo_msme_activity_id", nullable = false)
    private MoMSMEReport moMSMEReport;

    @Column(name = "financial_year")
    private String financialYear;

    @Column(name = "month")
    private String month;

    @Column(name = "physical_achievement")
    private Double physicalAchievement;

    @Column(name = "financial_achievement")
    private Double financialAchievement;

    @Column(name = "total")
    private Integer total;

    @Column(name = "women")
    private Integer women;

    @Column(name = "sc")
    private Integer sc;

    @Column(name = "st")
    private Integer st;

    @Column(name = "oc")
    private Integer oc;

    @Column(name = "bc")
    private Integer bc;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private Date createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on", insertable = false)
    private Date updatedOn;
}
