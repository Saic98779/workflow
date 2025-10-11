package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "momsme_actual_quarterly_report")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoMSMEActualQuarterlyReport extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quarter_id")
    private Long quarterlyId;

    @Column(name = "financial_year")
    private String financialYear;

    @Column(name = "physical_achievement")
    private Double physicalAchievement;

    @Column(name = "financial_achievement")
    private Double financialAchievement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mo_msme_activity_id", nullable = false)
    private MoMSMEActualReport moMSMEActualReport;

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
}
