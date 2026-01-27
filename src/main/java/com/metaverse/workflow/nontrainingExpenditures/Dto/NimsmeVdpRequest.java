package com.metaverse.workflow.nontrainingExpenditures.Dto;


import com.metaverse.workflow.common.enums.ExpressionOfInterest;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NimsmeVdpRequest {

    private Long subActivityId;
    private String sellerName;
    private Long sellerOrganizationId;
    private String productService;
    private String productCategory;
    private String specificationsFeatures;
    private String unit;
    private Double priceQuotation;
    private Double availableVolume;
    private String buyerName;
    private Long buyerOrganizationId;
    private String buyerComments;
    private ExpressionOfInterest expressionOfInterest;
    private Boolean followUpRequired;
    private String responsibleOfficer;
    private String remarksNotes;
}

