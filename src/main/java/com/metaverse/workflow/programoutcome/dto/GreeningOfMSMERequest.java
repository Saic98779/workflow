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
    public List<String> typeOfTrainingReceived;
    public String trainingCompletionDate;
    public String businessPlanSubmissionDate;
    public String amountSanctionedDate;
    public String amountReleasedDate;
    public Double amountReleased;
    public String nameOfBankProvidedLoan;
    public String dateOfGrounding;
    public String purposeOfLoanUtilised;
    public String parameter1;
    public String parameter2;
    public String unitForMeasurementOfProduction;
    public Double productionPerHour;


    private Long agencyId;
    private Long participantId;
    private Long organizationId;

    // Getters and Setters
}
