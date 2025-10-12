package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "momsme_report")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoMSMEReport extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mo_msme_activity_id")
    private Long moMSMEActivityId;

    @Column(name = "intervention")
    private String intervention;

    @Column(name = "component")
    private String component;
    
    @Column(name = "activity")
    private String activity;

    @OneToMany(mappedBy = "moMSMEReport", fetch = FetchType.LAZY)
    private List<MoMSMEQuarterlyReportTargets> moMSMEQuarterlyReportTargets;

    @OneToMany(mappedBy = "moMSMEReport", fetch = FetchType.LAZY)
    private List<MoMSMEMonthlyReportTargets> moMSMEMonthlyReportTargets;

    @OneToMany(mappedBy = "moMSMEReport", fetch = FetchType.LAZY)
    private List<MoMSMEReportSubmitted> moMSMEReportSubmitted;
}
