package com.metaverse.workflow.formalisationcompliance.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FormalisationComplianceResponse {

    private Long formalisationComplianceId;

    private Long subActivityId;
    private String subActivityName;

    private Long activityId;
    private String activityName;

    private Long organizationId;
    private String organizationName;

    private String documentPath;
    private String details;
}

