package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "momsme_actual_report")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoMSMEActualReport extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "actual_id")
    private Long actualId;

    @Column(name = "financial_year")
    private String financialYear;

    @Column(name = "month")
    private String month;

    @Column(name = "physical_achievement")
    private Double physicalAchievement;

    @Column(name = "financial_achievement")
    private Double financialAchievement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mo_msme_activity_id", nullable = false)
    private MoMSMEReport moMSMEReport;

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
