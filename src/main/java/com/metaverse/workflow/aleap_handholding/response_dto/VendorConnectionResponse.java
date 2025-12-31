package com.metaverse.workflow.aleap_handholding.response_dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VendorConnectionResponse {

    private Long id;

    private Long handholdingSupportId;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String nonTrainingActivityName;
    private String nonTrainingSubActivityName;

    private String vendorSuggested;
    private String quotationDate;
    private String details;
    private Double cost;
}
