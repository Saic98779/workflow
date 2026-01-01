package com.metaverse.workflow.aleap_handholding.request_dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendorConnectionRequest {

    private Long handholdingSupportId;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String handHoldingType;

    private String vendorSuggested;
    private String quotationDate;
    private String details;
    private Double cost;
}
