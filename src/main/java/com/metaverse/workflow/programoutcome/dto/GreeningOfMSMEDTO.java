package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GreeningOfMSMEDTO {
    private Long id;
    private String typeOfIntervention;
    private String typeOfPrototypeProposed;
    private String typeOfTrainingsReceived;
    private Date trainingCompletionDate;
    private Date businessPlanSubmissionDate;
    private Date amountSanctionedDate;
    private Date amountReleasedDate;
    private Double amountReleased;
    private String nameOfBankProvidedLoan;
    private Date dateOfGrounding;
    private String purposeOfLoanUtilised;
    private String parameter1;
    private String parameter2;
    private Double parameter1Value;
    private String parameter1Units;
    private String parameter2Value;
    private String parameter2Units;
    private Double productionPerHour;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;
    private String influencedParticipantName;

    private Date createdOn;
    private Date updatedOn;
}
