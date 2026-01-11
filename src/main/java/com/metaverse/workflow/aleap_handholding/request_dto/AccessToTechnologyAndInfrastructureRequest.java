package com.metaverse.workflow.aleap_handholding.request_dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessToTechnologyAndInfrastructureRequest {

    private Long handholdingSupportId;
    private Long organizationId;

    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String handHoldingType;
    private String accessToTechnologyType;

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
