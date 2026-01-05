package com.metaverse.workflow.aleap_handholding.request_dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormalisationComplianceRequest {

    private Long nonTrainingSubActivityId;
    private Long nonTrainingActivityId;
    private Long organizationId;
    private String documentPath;
    private String details;
}
