package com.metaverse.workflow.nontrainingExpenditures.Dto;

import lombok.Data;

@Data
public class WeHubSDGResponse {
    private Long eeHubSDGId;
    private Long organizationId;
    private String organizationName;
    private Long nonTrainingSubActivityId;
    private String nonTrainingSubActivityName;
    private Boolean adoptionStatus;
    private String technologyAdopted;
    private Boolean envCompCert;
    private String dateOfCert;

}
