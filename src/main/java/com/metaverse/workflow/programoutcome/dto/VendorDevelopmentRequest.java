package com.metaverse.workflow.programoutcome.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorDevelopmentRequest {
    public String dateOfParticipation;
    public String vdpProgramName;
    public String productShowcased;
    public List<String> nameOfBuyersInterested;
    public String vendorRegisteredWith;
    public Boolean iseProcurementRegistered;
    public String portalName;
    public Boolean isDigitalCatalogCreated;
    public String dateOfSupply;
    public Double volumeOfSupply;
    public String units;
    public Double valueOfSupply;
    public Double monthlyTurnover;
    public Boolean isInfluenced;

    private Long participantId;
    private Long organizationId;
    private Long agencyId;

}
