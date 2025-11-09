package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PMEGPDTO {
    private Long id;
    private Double loanAmountReleased;
    private Double govtSubsidy;
    private Double beneficiaryContribution;
    private Double totalAmountReleased;
    private Double businessTurnover;
    private Integer numberOfPersonsEmployed;

    private String agencyName;
    private String participantName;
    private String organizationName;

    private Date createdOn;
    private Date updatedOn;
}

