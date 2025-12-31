package com.metaverse.workflow.aleap_handholding.request_dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CFCSupportRequest {

    private Long handholdingSupportId;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String handHoldingType;

    private String technologyDetails;
    private String vendorName;
    private String vendorContactNo;
    private String vendorEmail;
    private Double approxCost;
}
