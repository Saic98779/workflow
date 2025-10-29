package com.metaverse.workflow.programoutcome.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class eCommerceRegistrationRequest {
    public String platformName;
    public String dateOfOnboarding;
    public String registrationDetails; // Reg No / Email / Mobile
    public Boolean isInfluenced;
    private Long agencyId;
    private Long participantId;
    private Long organizationId;
    private Long influencedId;
}