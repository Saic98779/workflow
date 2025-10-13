package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "momsme_report_submitted_quarterly")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoMSMEReportSubmittedQuarterly extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submitted_quarterly_id")
    private Long submittedQuarterlyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_id", nullable = false)
    private MoMSMEReportSubmitted moMSMEReportSubmitted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mo_msme_activity_id", nullable = false)
    private MoMSMEReport moMSMEReport;

    @Column(name = "quarter")
    private String quarter; // e.g. "Q1", "Q2", etc.

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

    @Column(name = "obc")
    private Integer obc;
}

