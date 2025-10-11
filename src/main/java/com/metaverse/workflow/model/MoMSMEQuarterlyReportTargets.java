package com.metaverse.workflow.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

public class MoMSMEQuarterlyReportTargets extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quarterly_target_id")
    private Long quarterlyTargetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mo_msme_activity_id", nullable = false)
    private MoMSMEReport moMSMEReport;

    @Column(name = "financial_year")
    private String financialYear;

    @Column(name = "quarter")
    private String quarter;//q1

    @Column(name = "physical_target")
    private Double physicalTarget;

    @Column(name = "financial_target")
    private Double financialTarget;
}
