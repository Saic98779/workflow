package com.metaverse.workflow.aleap_handholding.response_dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FormalisationComplianceResponse {

    private Long formalisationComplianceId;

    private Long nonTrainingSubActivityId;
    private String nonTrainingSubActivityName;

    private Long nonTrainingActivityId;
    private String nonTrainingActivityName;

    private Long organizationId;
    private String organizationName;

    private String documentPath;
    private String details;
}

