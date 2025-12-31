package com.metaverse.workflow.aleap_handholding.request_dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditCounsellingRequest {

    private Long handholdingSupportId;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String handHoldingType;

    private String counselledBy;
    private String counsellingDate;
    private String subjectDelivered;
}
