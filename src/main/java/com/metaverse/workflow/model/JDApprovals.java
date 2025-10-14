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

    @Column(name = "program_id")
    private Long programId;

    @Column(name = "status")
    private String status;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
}
