package com.metaverse.workflow.aleap_handholding.response_dto;

import com.metaverse.workflow.model.aleap_handholding.GovtSchemeApplication.ApplicationStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GovtSchemeApplicationResponse {
    private Long govtSchemeApplicationId;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String nonTrainingActivityName;
    private String nonTrainingSubActivityName;
    private Long organizationId;
    private String organizationName;
    private String applicationNo;
    private ApplicationStatus status;
    private String applicationDate;
    private String time;
    private String sanctionDetails;
    private String sanctionDate;
    private Double sanctionedAmount;
    private String details;
}
