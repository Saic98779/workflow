package com.metaverse.workflow.aleap_handholding.request_dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AleapDesignStudioRequest {

    private Long handholdingSupportId;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String handHoldingType;

    private String studioAccessDate; // String → Date
    private String details;
}
