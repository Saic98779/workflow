package com.metaverse.workflow.program.service;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
public class ProgramSummary {

    private String programName;
    private String agencyName;
    private Integer participant;
    private String startDate;
    private String endDate;
    private  Long sc;
    private  Long st;
    private  Long bc;
    private  Long obc;
    private  Long oc;
    private  Long minorities;
    private  Long male;
    private  Long female;
    private  Long transgender;
    private  Long physicallyChallenge;
    private  Long noOfSHGs;
    private  Long noOfMSMEs;
    private  Long noOfStartups;
    private  Long noOfDepartment;
    private  Long noOfAspirants;

}
