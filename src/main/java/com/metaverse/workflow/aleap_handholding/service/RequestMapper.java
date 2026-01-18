package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.request_dto.*;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.model.aleap_handholding.*;


import java.util.List;

public class RequestMapper {
    public static BusinessPlanDetails mapToBusinessPlan(
            BusinessPlanRequest request, HandholdingSupport support,
            Organization organization, List<Participant> participants,
            List<InfluencedParticipant> influencedParticipants
    ) {

        return BusinessPlanDetails.builder()
                .handholdingSupport(support)
                .organization(organization)
                .participants(participants)
                .bankName(request.getBankName())
                .branchName(request.getBranchName())
                .bankRemarks(request.getBankRemarks())
                .counsellingDate(DateUtil.covertStringToDate(request.getCounsellingDate()))
                .counsellingTime(request.getCounsellingTime())
                .counselledBy(request.getCounselledBy())
                .influencedParticipants(influencedParticipants)
                .build();
    }


    public static Counselling mapToCounselling(CounsellingRequest request, HandholdingSupport support, Organization organization, List<Participant> participants, List<InfluencedParticipant> influencedParticipants) {
        return Counselling.builder()
                .handholdingSupport(support)
                .organization(organization)
                .participants(participants)
                .subjectDelivered(request.getSubjectDelivered())
                .originalIdea(request.getOriginalIdea())
                .finalIdea(request.getFinalIdea())
                .counsellingDate(DateUtil.covertStringToDate(request.getCounsellingDate()))
                .counsellingTime(request.getCounsellingTime())
                .counselledBy(request.getCounselledBy())
                .influencedParticipants(influencedParticipants)
                .build();
    }

    public static SectorAdvisory mapToSectorAdvisory(SectorAdvisoryRequest request, HandholdingSupport support, Organization organization, List<Participant> participants,List<InfluencedParticipant> influencedParticipants) {
        return SectorAdvisory.builder()
                .adviseDetails(request.getAdviseDetails())
                .handholdingSupport(support)
                .organization(organization)
                .participants(participants)
                .counsellingDate(DateUtil.covertStringToDate(request.getCounsellingDate()))
                .counsellingTime(request.getCounsellingTime())
                .counselledBy(request.getCounselledBy())
                .adviseDetails(request.getAdviseDetails())
                .influencedParticipants(influencedParticipants)
                .build();
    }

    public static FeasibilityInput mapToFeasibilityInput(
            FeasibilityInputRequest request,
            MarketStudy marketStudy
    ) {

        FeasibilityInput input = new FeasibilityInput();
        input.setMarketStudy(marketStudy);
        input.setInputDetails(request.getInputDetails());
        input.setSource(request.getSource());
        input.setSector(request.getSector());
        input.setFeasibilityActivity(request.getFeasibilityActivity());

        return input;
    }


    public static MarketStudy mapToMarketStudy(MarketStudyRequest request, HandholdingSupport support,
                                               Organization organization, List<Participant> participants,
                                               List<InfluencedParticipant> influencedParticipants
    ) {

        MarketStudy entity = new MarketStudy();
        entity.setHandholdingSupport(support);
        entity.setOrganization(organization);
        entity.setParticipants(participants);
        entity.setCounselledBy(request.getCounselledBy());
        entity.setCounsellingTime(request.getCounsellingTime());
        entity.setInfluencedParticipants(influencedParticipants);
        if (request.getDateOfStudy() != null) {
            entity.setDateOfStudy(
                    DateUtil.covertStringToDate(request.getDateOfStudy())
            );
        }

        if (request.getCounsellingDate() != null) {
            entity.setCounsellingDate(
                    DateUtil.covertStringToDate(request.getCounsellingDate())
            );
        }

        return entity;
    }

    public static GovtSchemeApplication mapToGovtSchemeApplication(GovtSchemeApplicationRequest request, NonTrainingActivity activity,
                                                                   NonTrainingSubActivity subActivity, Organization organization) {
        return GovtSchemeApplication.builder()
                .nonTrainingActivity(activity)
                .nonTrainingSubActivity(subActivity)
                .organization(organization)
                .applicationNo(request.getApplicationNo())
                .status(request.getStatus())
                .applicationDate(request.getApplicationDate() != null
                        ? DateUtil.covertStringToDate(request.getApplicationDate())
                        : null)
                .time(request.getTime())
                .sanctionDetails(request.getSanctionDetails())
                .sanctionDate(request.getSanctionDate() != null
                        ? DateUtil.covertStringToDate(request.getSanctionDate())
                        : null)
                .sanctionedAmount(request.getSanctionedAmount())
                .details(request.getDetails())
                .build();
    }
    public static AccessToFinance mapToAccessToFinance(AccessToFinanceRequest request, HandholdingSupport support, Organization organization,List<Participant> participants,List<InfluencedParticipant> influencedParticipants) {
        return AccessToFinance.builder()
                .handholdingSupport(support)
                .organization(organization)
                .accessToFinanceType(request.getAccessToFinanceType())
                .schemeName(request.getSchemeName())
                .govtApplicationStatus(
                        request.getGovtApplicationStatus() != null
                                ? AccessToFinance.ApplicationStatus.valueOf(request.getGovtApplicationStatus())
                                : null
                )
                .govtSanctionDate(
                        DateUtil.covertStringToDate(request.getGovtSanctionDate())
                )
                .govtSanctionedAmount(request.getGovtSanctionedAmount())
                .govtDetails(request.getGovtDetails())
                .institutionName(request.getInstitutionName())
                .branchName(request.getBranchName())
                .dprSubmissionDate(DateUtil.covertStringToDate(request.getDprSubmissionDate()))
                .bankApplicationStatus(
                        request.getBankApplicationStatus() != null
                                ? AccessToFinance.ApplicationStatus.valueOf(request.getBankApplicationStatus())
                                : null
                )
                .bankSanctionDate(DateUtil.covertStringToDate(request.getBankSanctionDate()))
                .bankSanctionedAmount(request.getBankSanctionedAmount())
                .bankDetails(request.getBankDetails())
                .counselledBy(request.getCounselledBy())
                .counsellingDate(DateUtil.covertStringToDate(request.getCounsellingDate()))
                .subjectDelivered(request.getSubjectDelivered())
                .loanDocumentDetails(request.getLoanDocumentDetails())
                .participants(participants)
                .influencedParticipants(influencedParticipants)
                .build();
    }
    public static AccessToTechnologyAndInfrastructure mapToAccessToTechnologyAndInfrastructure(AccessToTechnologyAndInfrastructureRequest request, HandholdingSupport support, Organization organization) {
        return AccessToTechnologyAndInfrastructure.builder()
                .handholdingSupport(support)
                .organization(organization)
                .vendorSuggested(request.getVendorSuggested())
                .quotationDate(DateUtil.covertStringToDate(request.getQuotationDate()))
                .details(request.getDetails())
                .cost(request.getCost())
                .accessToTechnologyType(request.getAccessToTechnologyType())
                .requirement(request.getRequirement())
                .existingMachinery(request.getExistingMachinery())
                .suggestedMachinery(request.getSuggestedMachinery())
                .manufacturer(request.getManufacturer())
                .groundingDate(DateUtil.covertStringToDate(request.getGroundingDate()))
                .placeOfInstallation(request.getPlaceOfInstallation())
                .costOfMachinery(request.getCostOfMachinery())
                .technologyDetails(request.getTechnologyDetails())
                .vendorName(request.getVendorName())
                .vendorContactNo(request.getVendorContactNo())
                .vendorEmail(request.getVendorEmail())
                .approxCost(request.getApproxCost())

                .build();
    }
    public static AccessToPackagingLabellingAndBranding mapToAccessToPackagingLabellingAndBranding(AccessToPackagingLabellingAndBrandingRequest request,Organization organization, HandholdingSupport support) {
        return AccessToPackagingLabellingAndBranding.builder()
                .handholdingSupport(support)
                .accessToPackagingType(request.getAccessToPackagingType())
                .details(request.getDetails())
                .studioAccessDate(DateUtil.covertStringToDate(request.getStudioAccessDate()))
                .eventLocation(request.getEventLocation())
                .organizedBy(request.getOrganizedBy())
                .eventType(request.getEventType() != null ?request.getEventType():null)
                .eventDate(DateUtil.covertStringToDate(request.getEventDate()))
                .organization(organization)
                .build();
    }
}


