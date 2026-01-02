package com.metaverse.workflow.aleap_handholding.request_dto;

import com.metaverse.workflow.model.aleap_handholding.GovtSchemeFinance.ApplicationStatus;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GovtSchemeFinanceRequest {

    private Long handholdingSupportId;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String handHoldingType;

    private String schemeName;
    private ApplicationStatus status;
    private String sanctionDate;     // dd-MM-yyyy or yyyy-MM-dd
    private Double sanctionedAmount;
    private String details;
}

