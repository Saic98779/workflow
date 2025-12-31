package com.metaverse.workflow.aleap_handholding.request_dto;

import lombok.Data;

@Data
public class HandholdingSupportRequest {

    private Long handholdingSupportId;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String nonTrainingAction;
}

