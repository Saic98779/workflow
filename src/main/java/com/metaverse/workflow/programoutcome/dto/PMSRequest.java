package com.metaverse.workflow.programoutcome.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PMSRequest {
    public Double businessTurnover;
    public String loanNumber;
    public String purposeOfLoan;
    public Double amountOfLoanReleased;
    public String dateOfLoanReleased;
    public Integer employmentCreatedDirect;
    public Integer employmentCreatedInDirect;
    public Double repaymentAmount;
    public String dateOfRepayment;
    public Boolean upiOrQrCode;
    public String onlinePlatformUsed;
    public Boolean isInfluenced;
    private Long agencyId;
    private Long participantId;
    private Long organizationId;
}
