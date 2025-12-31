package com.metaverse.workflow.aleap_handholding.response_dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreditCounsellingResponse {

    private Long id;

    private Long handholdingSupportId;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String nonTrainingActivityName;
    private String nonTrainingSubActivityName;

    private String counselledBy;
    private String counsellingDate;
    private String subjectDelivered;
}
