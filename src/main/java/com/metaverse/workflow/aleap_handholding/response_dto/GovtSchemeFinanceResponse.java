package com.metaverse.workflow.aleap_handholding.response_dto;

import com.metaverse.workflow.model.aleap_handholding.GovtSchemeFinance.ApplicationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GovtSchemeFinanceResponse {

    private Long id;

    private Long handholdingSupportId;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;

    private String schemeName;
    private ApplicationStatus status;
    private String sanctionDate;
    private Double sanctionedAmount;
    private String details;
}

