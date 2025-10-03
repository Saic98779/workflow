package com.metaverse.workflow.ProgramMonitoring.service;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProgramMonitoringDetails {
    private Long programMonitoringId;
    private String programName;
    private String userId;
    private String userName;
    private String monitoringDate;
}
