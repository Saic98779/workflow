package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PMSDTO {
    private Long id;
    private Double businessTurnover;
    private String loanNumber;
    private String purposeOfLoan;
    private Double amountOfLoanReleased;
    private Date dateOfLoanReleased;
    private Integer employmentCreatedDirect;
    private Integer employmentCreatedInDirect;
    private Double repaymentAmount;
    private Date dateOfRepayment;
    private Boolean isUpiOrQrAvailable;
    private String onlinePlatformUsed;
    private Date dateOfGrounding;
    private Double revenue;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;
    private String influencedParticipantName;

    private Date createdOn;
    private Date updatedOn;
}

