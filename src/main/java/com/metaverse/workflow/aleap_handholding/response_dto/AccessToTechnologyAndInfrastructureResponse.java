package com.metaverse.workflow.aleap_handholding.response_dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccessToTechnologyAndInfrastructureResponse {
    private Long vendorConnectionId;
    private Long machineryIdentificationId;
    private Long cfsSupportId;

    private Long handholdingSupportId;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String nonTrainingActivityName;
    private String nonTrainingSubActivityName;

    private String vendorSuggested;
    private String quotationDate;
    private String details;
    private Double cost;

    private String requirement;
    private String existingMachinery;
    private String suggestedMachinery;
    private String manufacturer;
    private String groundingDate;
    private String placeOfInstallation;
    private Double costOfMachinery;

    private String technologyDetails;
    private String vendorName;
    private String vendorContactNo;
    private String vendorEmail;
    private Double approxCost;
}
