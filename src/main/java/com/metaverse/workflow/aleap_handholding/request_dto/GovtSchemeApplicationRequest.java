package com.metaverse.workflow.aleap_handholding.request_dto;

import com.metaverse.workflow.model.aleap_handholding.GovtSchemeApplication.ApplicationStatus;
import lombok.Data;


@Data
public class GovtSchemeApplicationRequest {

    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private Long organizationId;
    private String applicationNo;
    private ApplicationStatus status;
    private String applicationDate;
    private String time;
    private String sanctionDetails;
    private String sanctionDate;
    private Double sanctionedAmount;
    private String details;
}
