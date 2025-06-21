package com.metaverse.workflow.programoutcome.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GeMRegistrationRequest {
    public String gemRegistrationId;
    public String gemRegistrationDate;
    public Boolean isInfluenced;
    private Long participantId;
    private Long organizationId;
    private Long agencyId;
}
