package com.metaverse.workflow.nontrainingExpenditures.Dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NimsmeVdpResponse {

    private Long nimsmeVdpId;
    private Long subActivityId;
    private String subActivityName;
    private String sellerName;
    private Long sellerOrganizationId;
    private String sellerOrganizationName;
    private String productService;
    private String productCategory;
    private String specificationsFeatures;
    private String availableVolumeCapacity;
    private String priceQuotation;
    private String buyerName;
    private Long buyerOrganizationId;
    private String buyerOrganizationName;
    private String buyerComments;
    private String expressionOfInterest;
    private Boolean followUpRequired;
    private String responsibleOfficer;
    private String remarksNotes;
    private String uploadParticipantDetails;
}
