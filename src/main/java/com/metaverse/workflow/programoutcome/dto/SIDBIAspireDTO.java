package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SIDBIAspireDTO {
    private Long id;
    private Date applicationSubmissionDate;
    private Date dateSanctionUnderAspire;
    private Boolean fundingSupportReceived;
    private String incubationPartnerName;
    private String fundingType;
    private Double supportAmount;
    private Date machinerySetupDate;
    private Date productionStartedDate;
    private String production;
    private String productionUnits;
    private Boolean marketLinkageEnabled;
    private Date marketLinkageDate;
    private String marketLinkageVolume;
    private Double marketLinkageValue;
    private Double turnover;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;
    private String influencedParticipantName;

    private Date createdOn;
    private Date updatedOn;
}
