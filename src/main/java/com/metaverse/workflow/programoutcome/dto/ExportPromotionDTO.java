package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportPromotionDTO {
    private Long id;
    private String sectorName;
    private String productName;
    private String exportImportLicenceNo;
    private Boolean mappingWithInternationalBuyer;
    private Double monthlyTurnoverInLakhs;
    private String isExport;
    private Date exportDate;
    private Double exportValueInLakhs;
    private Double exportVolumeInMts;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;
    private String influencedParticipantName;

    private Date createdOn;
    private Date updatedOn;
}

