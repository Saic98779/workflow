package com.metaverse.workflow.aleap_handholding.request_dto;

import com.metaverse.workflow.model.aleap_handholding.BankNbfcFinance;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankNbfcFinanceRequest {
    private String institutionName;
    private String branchName;
    private String dprSubmissionDate;
    private BankNbfcFinance.ApplicationStatus status;
    private String sanctionDate;
    private Double sanctionedAmount;
    private String details;
    private Long handholdingSupportId;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String handHoldingType;
}
