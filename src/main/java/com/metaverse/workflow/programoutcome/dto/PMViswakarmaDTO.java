package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PMViswakarmaDTO {
    private Long id;
    private String artisanCategory;
    private Date dateOfTraining;
    private Date certificateIssueDate;
    private Date dateOfCreditAvailed;
    private Double amountOfCreditAvailed;
    private String purposeOfUtilisation;
    private Double monthlyIncomeAfterCredit;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;
    private String influencedParticipantName;

    private Date createdOn;
    private Date updatedOn;
}

