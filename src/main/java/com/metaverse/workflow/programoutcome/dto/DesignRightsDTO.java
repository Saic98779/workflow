package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DesignRightsDTO {
    private Long id;
    private Date dateOfApplication;
    private Date dateOfDesignRightsGranted;
    private String certificationNumber;
    private String typeOfDesignRegistered;
    private Double revenueFromDesignProducts;
    private Boolean isAwardedForDesignProtection;
    private Date dateOfAwarded;
    private String nameOfAward;
    private Date dateOfExport;
    private Double valueOfExport;
    private Double volumeOfExport;
    private String units;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;
    private String influencedParticipantName;

    private Date createdOn;
    private Date updatedOn;
}

