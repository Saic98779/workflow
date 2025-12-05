package com.metaverse.workflow.nontrainingExpenditures.service;

import lombok.Data;

import java.util.List;

@Data
public class VisitDetailsRequest {
    private Long organizationId;
    private Long subActivityId;
    private String dateOfVisit;
    private String timeOfVisit;
    private List<Long> nonTrainingResourceIds;  // resource ids list
}