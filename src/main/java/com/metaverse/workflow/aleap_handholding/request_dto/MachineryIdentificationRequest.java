package com.metaverse.workflow.aleap_handholding.request_dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MachineryIdentificationRequest {

    private Long handholdingSupportId;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String handHoldingType;

    private String requirement;
    private String existingMachinery;
    private String suggestedMachinery;
    private String manufacturer;
    private String groundingDate;   // String → Date
    private String placeOfInstallation;
    private Double costOfMachinery;
}
