package com.metaverse.workflow.aleap_handholding.response_dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CFCSupportResponse {

    private Long cfsSupportId;

    private Long handholdingSupportId;
    private Long nonTrainingActivityId;
    private String nonTrainingActivityName;
    private Long nonTrainingSubActivityId;
    private String nonTrainingSubActivityName;

    private String technologyDetails;
    private String vendorName;
    private String vendorContactNo;
    private String vendorEmail;
    private Double approxCost;
}
