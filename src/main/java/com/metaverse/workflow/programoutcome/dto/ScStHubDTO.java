package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScStHubDTO {
    private Long id;
    private String supportAvailedUnderNSSH;
    private String trainingName;
    private Date trainingCompletedDate;
    private String certificationName;
    private Date certificationReceivedDate;
    private String marketLinkageCompanyName;
    private Date marketLinkageDate;
    private Double marketLinkageValue;
    private String marketLinkageVolume;
    private String vendorRegistrationWithPSUOrOEM;
    private String tenderParticipatedName;
    private String handholdingAgency;
    private Date creditLinkageDate;
    private Double creditLinkageAmount;
    private Double monthlyRevenue;
    private String keyChallengesFaced;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;
    private String influencedParticipantName;

    private Date createdOn;
    private Date updatedOn;
}

