package com.metaverse.workflow.programoutcome.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConsortiaTenderRequest {

    public String productOrServiceOffered;
    private String consortiaMemberType; // Member / Lead member
    public String consortiaName;
    public String dateOfJoiningConsortia;
    public String tenderParticipatedName;
    public String departmentTenderIssued;
    public String tenderId;
    public Double tenderValue;
    private String tenderOutcome; // Awarded / Not Awarded
    public String workOrderIssueDate;
    public Boolean isOrderExecuted;
    public String challengesFaced;
    public Boolean isInfluenced;

    private Long agencyId;
    private Long participantId;
    private Long organizationId;
}
