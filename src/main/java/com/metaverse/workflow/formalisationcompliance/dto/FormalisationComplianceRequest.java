package com.metaverse.workflow.formalisationcompliance.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormalisationComplianceRequest {

    private Long subActivityId;
    private Long activityId;
    private Long organizationId;
    private String documentPath;
    private String details;
}
