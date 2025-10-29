package com.metaverse.workflow.programoutcome.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GreeningOfMSMERequest {

    public String typeOfIntervention;
    public String typeOfPrototypeProposed;
    private List<String> typeOfTrainingReceived;
    public String trainingCompletionDate;
    public String businessPlanSubmissionDate;
    public String amountSanctionedDate;
    public String amountReleasedDate;
    public Double amountReleased;
    public String nameOfBankProvidedLoan;
    public String dateOfGrounding;
    public String purposeOfLoanUtilised;
    public Double productionPerHour;
    private String parameter1;
    public Double parameter1Value;
    public String parameter1Units;
    private String parameter2;
    public String parameter2Value;
    public String parameter2Units;
    private Long agencyId;
    private Long influencedId;
    private Long participantId;
    private Long organizationId;
    public Boolean isInfluenced;
}
