package com.metaverse.workflow.ProgramMonitoring.dto;

import lombok.Data;

@Data
public class JDApprovalsDto {
    private Long jdApprovalsId;
    private Long programId;
    private String status;
    private String remarks;
}
