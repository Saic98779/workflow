package com.metaverse.workflow.aleap_handholding.response_dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessToFinanceResponse {
    private Long accessToFinanceId;
    private Long handholdingSupportId;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String nonTrainingActivityName;
    private String nonTrainingSubActivityName;
    private Long organizationId;
    private String organizationName;
    private String accessToFinanceType;
    private String schemeName;
    private String govtApplicationStatus;
    private String govtSanctionDate;
    private Double govtSanctionedAmount;
    private String govtDetails;
    private String institutionName;
    private String branchName;
    private String dprSubmissionDate;
    private String bankApplicationStatus;
    private String bankSanctionDate;
    private Double bankSanctionedAmount;
    private String bankDetails;
    private String counselledBy;
    private String counsellingDate;
    private String subjectDelivered;
    private String loanDocumentDetails;
}

