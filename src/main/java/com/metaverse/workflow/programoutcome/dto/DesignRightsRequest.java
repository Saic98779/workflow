package com.metaverse.workflow.programoutcome.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DesignRightsRequest {

    public String dateOfApplication;
    public String dateOfDesignRightsGranted;
    public String certificationNumber;
    public String typeOfDesignRegistered;
    public Double revenueFromDesignProducts;
    public Boolean isAwardedForDesignProtection;
    public String dateOfAwarded;
    public String nameOfAward;
    public String dateOfExport;
    public Double valueOfExport;
    public Double volumeOfExport;
    public String units;
    public Boolean isInfluenced;
    private Long agencyId;
    private Long participantId;
    private Long organizationId;
}
