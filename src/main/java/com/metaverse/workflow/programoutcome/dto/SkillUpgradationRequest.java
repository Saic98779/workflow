package com.metaverse.workflow.programoutcome.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SkillUpgradationRequest {

    private List<String> typeOfTrainingReceived;
    public String trainingCompletionDate;
    public String businessPlanSubmissionDate;
    public String amountSanctionedDate;
    public String amountReleasedDate;
    public Double amountReleasedInLakhs;
    public String bankProvidedLoan;
    private String loanType;
    private String loanPurpose;
    public String groundingDate;
    public String sectorType;
    public Double monthlyTurnoverInLakhs;
    public Boolean isInfluenced;
    private Long agencyId;
    private Long participantId;
    private Long organizationId;
    private Long influencedId;
}
