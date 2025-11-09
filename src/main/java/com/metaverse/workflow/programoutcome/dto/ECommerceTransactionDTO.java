package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ECommerceTransactionDTO {
    private Long id;
    private Date fromDate;
    private Date toDate;
    private Integer numberOfTransactions;
    private Double totalBusinessAmount;
    private Boolean isInfluenced;
    private String registrationDetails;
    private String agencyName;
    private String participantName;
    private String organizationName;
    private String influencedParticipantName;

    private Date createdOn;
    private Date updatedOn;
}

