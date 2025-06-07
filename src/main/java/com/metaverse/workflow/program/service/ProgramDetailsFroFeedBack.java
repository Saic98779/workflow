package com.metaverse.workflow.program.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ProgramDetailsFroFeedBack {
    private String state;
    private String district;
    private String dateOfMonitoring;
    private String agencyName;
    private String programType;
    private String programName;
    private String venueName;
    private String hostingAgencyName;
    private String spocName;
    private Long spocContact;
    private String inTime;
    private String outTime;
}
