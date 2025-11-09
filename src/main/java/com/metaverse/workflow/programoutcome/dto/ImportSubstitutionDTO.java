package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportSubstitutionDTO {
    private Long id;
    private String sectorName;
    private String productName;
    private Boolean prototypeSelected;
    private Date businessPlanSubmissionDate;
    private Date amountSanctionedDate;
    private Date amountReleasedDate;
    private Double amountReleasedInLakhs;
    private String bankProvidedLoan;
    private Date groundingDate;
    private Double monthlyTurnoverInLakhs;
    private Boolean marketOfProduct;
    private Date marketDate;
    private Double marketValueInLakhs;
    private Double marketVolumeInMts;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;
    private String influencedParticipantName;

    private Date createdOn;
    private Date updatedOn;
}

