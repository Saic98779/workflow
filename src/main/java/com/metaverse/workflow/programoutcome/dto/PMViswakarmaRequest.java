package com.metaverse.workflow.programoutcome.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PMViswakarmaRequest {

    public String artisanCategory;

    public String dateOfTraining;

    public String certificateIssueDate;

    public String dateOfCreditAvailed;

    public Double amountOfCreditAvailed;

    public String purposeOfUtilisation; // Working Capital / Renovation / Equipment

    public Double monthlyIncomeAfterCredit;
    public Boolean isInfluenced;
    private Long participantId;
    private Long organizationId;
    private Long agencyId;
}