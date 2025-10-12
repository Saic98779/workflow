package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "momsme_report_submitted")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoMSMEReportSubmitted extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submitted_id")
    private Long submittedId;

    @ManyToOne
    @JoinColumn(name = "mo_msme_activity_id")
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

    @Column(name = "obc")
    private Integer obc;
}
