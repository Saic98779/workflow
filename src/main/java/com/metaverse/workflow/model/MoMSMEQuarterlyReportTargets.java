package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "momsme_quarterly_report_targets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoMSMEQuarterlyReportTargets extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quarter_target_id")
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
