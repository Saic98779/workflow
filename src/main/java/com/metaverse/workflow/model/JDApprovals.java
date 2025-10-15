package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "jd_approvals")
@Data
public class JDApprovals extends AuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jd_approval_id")
    private Long JdApprovalsId;

    @OneToOne
    @JoinColumn(name = "program")
    private Program program;

    @OneToOne
    @JoinColumn(name = "program_monitoring")
    private ProgramMonitoring programMonitoring;

    @Column(name = "status")
    private String status;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
}
