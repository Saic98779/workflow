package com.metaverse.workflow.aleap_handholding.response_dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MachineryIdentificationResponse {

    private Long machineryIdentificationId;

    private Long handholdingSupportId;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String nonTrainingActivityName;
    private String nonTrainingSubActivityName;

    private String requirement;
    private String existingMachinery;
    private String suggestedMachinery;
    private String manufacturer;
    private String groundingDate;
    private String placeOfInstallation;
    private Double costOfMachinery;
}
