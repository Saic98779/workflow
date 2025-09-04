package com.metaverse.workflow.nontrainingExpenditures.Dto;

import lombok.Data;

import java.time.LocalDate;


@Data
public class WeHubSelectedCompaniesRequest {

    private String udhyamDpiitRegistrationNo;
    private String applicationReceivedDate;
    private String applicationSource;
    private String shortlistingDate;
    private String needAssessmentDate;
    private Boolean candidateFinalised;
    private String cohortName;
    private String baselineAssessmentDate;
    private Long subActivityId;
}

