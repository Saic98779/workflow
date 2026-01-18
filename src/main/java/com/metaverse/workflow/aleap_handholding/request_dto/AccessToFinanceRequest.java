package com.metaverse.workflow.aleap_handholding.request_dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessToFinanceRequest {

    private Long handholdingSupportId;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String handHoldingType;
    private Long organizationId;
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
    private List<Long> participantIds;
    private List<Long> influencedParticipantIds;
}
