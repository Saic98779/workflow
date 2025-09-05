package com.metaverse.workflow.nontrainingExpenditures.Dto;

import lombok.Data;

@Data
public class WeHubSDGRequest {
    private Long organizationId;
    private Long nonTrainingSubActivityId;
    private Boolean adoptionStatus;
    private String technologyAdopted;
    private Boolean envCompCert;
    private String dateOfCert;
}
