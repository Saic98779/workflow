package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "momsme_monthly_report_targets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoMSMEMonthlyReportTargets extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "month_id")
    private Long monthlyTargetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mo_msme_activity_id", nullable = false)
    private MoMSMEReport moMSMEReport;

    @Column(name = "financial_year")
    private String financialYear;

    @Column(name = "month")
    private String month;//apr/may

    @Column(name = "physical_target")
    private Double physicalTarget;

    @Column(name = "financial_target")
    private Double financialTarget;
}
