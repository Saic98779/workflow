package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsortiaTenderDTO {
    private Long id;
    private String productOrServiceOffered;
    private String consortiaMemberType;
    private String consortiaName;
    private Date dateOfJoiningConsortia;
    private String tenderParticipatedName;
    private String departmentTenderIssued;
    private String tenderId;
    private Double tenderValue;
    private String tenderOutcome;
    private Date workOrderIssueDate;
    private Boolean isOrderExecuted;
    private String challengesFaced;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;
    private String influencedParticipantName;

    private Date createdOn;
    private Date updatedOn;
}

