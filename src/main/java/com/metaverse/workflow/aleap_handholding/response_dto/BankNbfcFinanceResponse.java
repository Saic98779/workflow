package com.metaverse.workflow.aleap_handholding.response_dto;

import com.metaverse.workflow.model.aleap_handholding.BankNbfcFinance;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankNbfcFinanceResponse {

    private Long bankNbfcId;

    private String institutionName;
    private String branchName;
    private String dprSubmissionDate;
    private BankNbfcFinance.ApplicationStatus status;
    private String sanctionDate;
    private Double sanctionedAmount;
    private String details;

    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String handHoldingType;
    private String nonTrainingActivityName;
    private String nonTrainingSubActivityName;


}

