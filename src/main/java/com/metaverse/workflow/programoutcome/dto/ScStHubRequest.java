package com.metaverse.workflow.programoutcome.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScStHubRequest {
    public String supportAvailedUnderNSSH; // Training / Certification / Vendor Dev / Credit Facilitation
    public String trainingName;
    public String trainingCompletedDate;
    public String certificationName;
    public String certificationReceivedDate;
    public String marketLinkageCompanyName;
    public String marketLinkageDate;
    public Double marketLinkageValue;
    public String marketLinkageVolume;
    public String vendorRegistrationWithPSUOrOEM;
    public String tenderParticipatedName;
    public String handholdingAgency;
    public String creditLinkageDate;
    public Double creditLinkageAmount;
    public Double monthlyRevenue;
    public String keyChallengesFaced;
    public Boolean isInfluenced;

    private Long participantId;
    private Long organizationId;
    private Long agencyId;
}
