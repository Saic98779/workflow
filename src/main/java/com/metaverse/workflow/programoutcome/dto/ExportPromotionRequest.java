package com.metaverse.workflow.programoutcome.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExportPromotionRequest {

    public String sectorName;
    public String productName;
    public String exportImportLicenceNo;
    public Boolean isMappingWithInternationalBuyer;
    public Double monthlyTurnoverInLakhs;
    public String isExport; // Yes/No
    public String exportDate;
    public Double exportValueInLakhs;
    public Double exportVolumeInMts;
    public Boolean isInfluenced;
    private Long agencyId;
    private Long participantId;
    private Long organizationId;
    private Long influencedId;
}