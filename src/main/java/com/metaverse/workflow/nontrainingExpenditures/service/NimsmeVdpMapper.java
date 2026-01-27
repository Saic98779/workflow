package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.model.NimsmeVDP;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.nontrainingExpenditures.Dto.NimsmeVdpRequest;
import com.metaverse.workflow.nontrainingExpenditures.Dto.NimsmeVdpResponse;

public class NimsmeVdpMapper {

    public static NimsmeVDP mapToNimsmeVdp(NimsmeVdpRequest request, NonTrainingSubActivity subActivity, Organization sellerOrg, Organization buyerOrg) {
        return NimsmeVDP.builder()
                .nonTrainingSubActivity(subActivity)
                .sellerName(request.getSellerName())
                .sellerOrganization(sellerOrg)
                .productService(request.getProductService())
                .productCategory(request.getProductCategory())
                .specificationsFeatures(request.getSpecificationsFeatures())
                .availableVolumeCapacity(request.getAvailableVolumeCapacity())
                .priceQuotation(request.getPriceQuotation())
                .buyerName(request.getBuyerName())
                .buyerOrganization(buyerOrg)
                .buyerComments(request.getBuyerComments())
                .expressionOfInterest(request.getExpressionOfInterest())
                .followUpRequired(request.getFollowUpRequired())
                .responsibleOfficer(request.getResponsibleOfficer())
                .remarksNotes(request.getRemarksNotes())
                .build();
    }

    public static NimsmeVdpResponse mapToNimsmeResponse(NimsmeVDP entity) {
        return NimsmeVdpResponse.builder()
                .nimsmeVdpId(entity.getNimsmeVdpId())
                .subActivityId(entity.getNonTrainingSubActivity().getSubActivityId())
                .subActivityName(entity.getNonTrainingSubActivity().getSubActivityName())
                .sellerName(entity.getSellerName())
                .sellerOrganizationId(
                        entity.getSellerOrganization() != null ? entity.getSellerOrganization().getOrganizationId() : null)
                .sellerOrganizationName(
                        entity.getSellerOrganization() != null ? entity.getSellerOrganization().getOrganizationName() : null)
                .productService(entity.getProductService())
                .productCategory(entity.getProductCategory())
                .specificationsFeatures(entity.getSpecificationsFeatures())
                .availableVolumeCapacity(entity.getAvailableVolumeCapacity())
                .priceQuotation(entity.getPriceQuotation())
                .buyerName(entity.getBuyerName())
                .buyerOrganizationId(
                        entity.getBuyerOrganization() != null ? entity.getBuyerOrganization().getOrganizationId() : null)
                .buyerOrganizationName(
                        entity.getBuyerOrganization() != null ? entity.getBuyerOrganization().getOrganizationName() : null)
                .buyerComments(entity.getBuyerComments())
                .expressionOfInterest(entity.getExpressionOfInterest().name())
                .followUpRequired(entity.getFollowUpRequired())
                .responsibleOfficer(entity.getResponsibleOfficer())
                .remarksNotes(entity.getRemarksNotes())
                .uploadParticipantDetails(entity.getUploadParticipantDetails())
                .build();
    }
    public static void mapToUpdateNimsmeVdp(NimsmeVDP entity, NimsmeVdpRequest request, Organization sellerOrg, Organization buyerOrg) {
        entity.setSellerName(request.getSellerName());
        entity.setProductService(request.getProductService());
        entity.setProductCategory(request.getProductCategory());
        entity.setSpecificationsFeatures(request.getSpecificationsFeatures());
        entity.setAvailableVolumeCapacity(request.getAvailableVolumeCapacity());
        entity.setPriceQuotation(request.getPriceQuotation());
        entity.setBuyerName(request.getBuyerName());
        entity.setBuyerComments(request.getBuyerComments());
        entity.setExpressionOfInterest(request.getExpressionOfInterest());
        entity.setFollowUpRequired(request.getFollowUpRequired());
        entity.setResponsibleOfficer(request.getResponsibleOfficer());
        entity.setRemarksNotes(request.getRemarksNotes());
        if (sellerOrg != null) {entity.setSellerOrganization(sellerOrg);}
        if (buyerOrg != null) {entity.setBuyerOrganization(buyerOrg);}
    }

}

