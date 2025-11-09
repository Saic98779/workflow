package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorDevelopmentDTO {

    private Date dateOfParticipation;
    private String vdpProgramName;
    private String productShowcased;
    private List<String> nameOfBuyersInterested;
    private String vendorRegisteredWith;
    private Boolean iseProcurementRegistered;
    private String portalName;
    private Boolean isDigitalCatalogCreated;
    private Date dateOfSupply;
    private Double volumeOfSupply;
    private String units;
    private Double valueOfSupply;
    private Double monthlyTurnover;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;
    private String influencedParticipantName;

    private Date createdOn;
    private Date updatedOn;
}

