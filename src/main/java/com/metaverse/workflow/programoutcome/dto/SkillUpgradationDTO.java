package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillUpgradationDTO {
    private Long id;
    private List<String> typeOfTrainingReceived;
    private Date trainingCompletionDate;
    private Date businessPlanSubmissionDate;
    private Date amountSanctionedDate;
    private Date amountReleasedDate;
    private Double amountReleasedInLakhs;
    private String bankProvidedLoan;
    private String loanType;
    private String loanPurpose;
    private Date groundingDate;
    private String sectorType;
    private Double monthlyTurnoverInLakhs;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;
    private String influencedParticipantName;

    private Date createdOn;
    private Date updatedOn;
}

