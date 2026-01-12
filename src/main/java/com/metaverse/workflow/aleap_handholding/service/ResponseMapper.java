package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.response_dto.*;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.InfluencedParticipant;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.aleap_handholding.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class ResponseMapper {
        public static BusinessPlanResponse mapToBusinessPlanResponse(BusinessPlanDetails entity) {
            return BusinessPlanResponse.builder()
                    .businessPlanId(entity.getBusinessPlanDetailsId())
                    .handholdingSupportId(entity.getHandholdingSupport().getId())
                    .organizationId(entity.getOrganization().getOrganizationId())
                    .organizationName(entity.getOrganization().getOrganizationName())
                    .counselledBy(entity.getCounselledBy())
                    .counsellingDate(DateUtil.dateToString(entity.getCounsellingDate(),"dd-MM-yyyy"))
                    .counsellingTime(entity.getCounsellingTime())
                    .planFileUploadPath(entity.getPlanFileUploadPath())
                    .bankName(entity.getBankName())
                    .branchName(entity.getBranchName())
                    .bankRemarks(entity.getBankRemarks())
                    .nonTrainingActivityId(entity.getHandholdingSupport().getNonTrainingActivity().getActivityId())
                    .nonTrainingActivityName(entity.getHandholdingSupport().getNonTrainingActivity().getActivityName())
                    .nonTrainingSubActivityId(entity.getHandholdingSupport().getNonTrainingSubActivity().getSubActivityId())
                    .nonTrainingSubActivityName(entity.getHandholdingSupport().getNonTrainingSubActivity().getSubActivityName())
                    .handHoldingType(entity.getHandholdingSupport().getHandholdingSupportType())
                    .participantNames(
                            Stream.concat(
                                    entity.getParticipants() != null
                                            ? entity.getParticipants().stream()
                                            .map(Participant::getParticipantName)
                                            : Stream.empty(),
                                    entity.getInfluencedParticipants() != null
                                            ? entity.getInfluencedParticipants().stream()
                                            .map(InfluencedParticipant::getParticipantName)
                                            : Stream.empty()
                            ).toList()
                    )
                    .build();
        }

    public static CounsellingResponse mapToCounsellingResponse(Counselling entity) {
        HandholdingSupport support = entity.getHandholdingSupport();
        return CounsellingResponse.builder()
                .counselletingId(entity.getId())
                .handholdingSupportId(support != null ? support.getId() : null)
                .organizationId(
                        entity.getOrganization() != null
                                ? entity.getOrganization().getOrganizationId()
                                : null
                )
                .organizationName(
                        entity.getOrganization() != null
                                ? entity.getOrganization().getOrganizationName()
                                : null
                )
                .counselledBy(entity.getCounselledBy())
                .counsellingDate(DateUtil.dateToString(entity.getCounsellingDate(),"dd-MM-yyyy"))
                .counsellingTime(entity.getCounsellingTime())
                .subjectDelivered(entity.getSubjectDelivered())
                .originalIdea(entity.getOriginalIdea())
                .finalIdea(entity.getFinalIdea())
                .participantNames(
                        Stream.concat(
                                entity.getParticipants() != null
                                        ? entity.getParticipants().stream()
                                        .map(Participant::getParticipantName)
                                        : Stream.empty(),
                                entity.getInfluencedParticipants() != null
                                        ? entity.getInfluencedParticipants().stream()
                                        .map(InfluencedParticipant::getParticipantName)
                                        : Stream.empty()
                        ).toList()
                )
                .nonTrainingActivityId(
                        support != null && support.getNonTrainingActivity() != null
                                ? support.getNonTrainingActivity().getActivityId()
                                : null
                )
                .nonTrainingActivityName(
                        support != null && support.getNonTrainingActivity() != null
                                ? support.getNonTrainingActivity().getActivityName()
                                : null
                )
                .nonTrainingSubActivityId(
                        support != null && support.getNonTrainingSubActivity() != null
                                ? support.getNonTrainingSubActivity().getSubActivityId()
                                : null
                )
                .nonTrainingSubActivityName(
                        support != null && support.getNonTrainingSubActivity() != null
                                ? support.getNonTrainingSubActivity().getSubActivityName()
                                : null
                )
                .handHoldingType(
                        support != null ? support.getHandholdingSupportType() : null
                )
                .build();
    }
    public static SectorAdvisoryResponse mapToSectorAdvisoryResponse(SectorAdvisory entity) {
        return SectorAdvisoryResponse.builder()
                .adviseDetails(entity.getAdviseDetails())
                .sectorAdvisoryId(entity.getId())
                .handholdingSupportId(entity.getHandholdingSupport().getId())
                .organizationId(entity.getOrganization().getOrganizationId())
                .organizationName(entity.getOrganization().getOrganizationName())
                .counselledBy(entity.getCounselledBy())
                .counsellingDate(DateUtil.dateToString(entity.getCounsellingDate(),"dd-MM-yyyy"))
                .counsellingTime(entity.getCounsellingTime())
                .nonTrainingActivityId(entity.getHandholdingSupport().getNonTrainingActivity().getActivityId())
                .nonTrainingActivityName(entity.getHandholdingSupport().getNonTrainingActivity().getActivityName())
                .nonTrainingSubActivityId(entity.getHandholdingSupport().getNonTrainingSubActivity().getSubActivityId())
                .nonTrainingSubActivityName(entity.getHandholdingSupport().getNonTrainingSubActivity().getSubActivityName())
                .handHoldingType(entity.getHandholdingSupport().getHandholdingSupportType())
                .participantNames(
                        Stream.concat(
                                entity.getParticipants() != null
                                        ? entity.getParticipants().stream()
                                        .map(Participant::getParticipantName)
                                        : Stream.empty(),
                                entity.getInfluencedParticipants() != null
                                        ? entity.getInfluencedParticipants().stream()
                                        .map(InfluencedParticipant::getParticipantName)
                                        : Stream.empty()
                        ).toList()
                )
                .build();
    }
    public static FeasibilityInputResponse mapToFeasibilityInputResponse(FeasibilityInput entity) {
        return FeasibilityInputResponse.builder()
                .feasibilityInputId(entity.getId())
                .marketStudyId(entity.getMarketStudy().getId())
                .inputDetails(entity.getInputDetails())
                .source(entity.getSource())
                .sector(entity.getSector())
                .feasibilityActivity(entity.getFeasibilityActivity())
                .build();
    }

    public static MarketStudyResponse mapToMarketStudyResponse(MarketStudy entity) {
        return MarketStudyResponse.builder()
                .marketStudyId(entity.getId())
                .handholdingSupportId(entity.getHandholdingSupport().getId())
                .nonTrainingActivityId(
                        entity.getHandholdingSupport()
                                .getNonTrainingActivity()
                                .getActivityId()
                )
                .nonTrainingSubActivityId(
                        entity.getHandholdingSupport()
                                .getNonTrainingSubActivity()
                                .getSubActivityId()
                )
                .nonTrainingActivityName(
                        entity.getHandholdingSupport()
                                .getNonTrainingActivity()
                                .getActivityName()
                )
                .nonTrainingSubActivityName(
                        entity.getHandholdingSupport()
                                .getNonTrainingSubActivity()
                                .getSubActivityName()
                )
                .dateOfStudy(
                        DateUtil.dateToString(entity.getDateOfStudy(),"dd-MM-yyyy")
                )
                .organizationId(entity.getOrganization().getOrganizationId())
                .organizationName(entity.getOrganization().getOrganizationName())
                .counselledBy(entity.getCounselledBy())
                .counsellingDate(
                        DateUtil.dateToString(entity.getCounsellingDate(),"dd-MM-yyyy")
                )
                .counsellingTime(entity.getCounsellingTime())
                .participantNames(
                        Stream.concat(
                                entity.getParticipants() != null
                                        ? entity.getParticipants().stream()
                                        .map(Participant::getParticipantName)
                                        : Stream.empty(),
                                entity.getInfluencedParticipants() != null
                                        ? entity.getInfluencedParticipants().stream()
                                        .map(InfluencedParticipant::getParticipantName)
                                        : Stream.empty()
                        ).toList()
                )
                .feasibilityInputResponses(
                        entity.getFeasibilityInputs() != null
                                ? entity.getFeasibilityInputs().stream()
                                .map(ResponseMapper::mapToFeasibilityInputResponse)
                                .toList()
                                : List.of()
                )
                .handHoldingType(entity.getHandholdingSupport().getHandholdingSupportType())
                .build();
    }

    public static GovtSchemeApplicationResponse mapToGovtSchemeApplicationResponse(GovtSchemeApplication entity) {
        return GovtSchemeApplicationResponse.builder()
                .govtSchemeApplicationId(entity.getId())
                .nonTrainingActivityId(entity.getNonTrainingActivity().getActivityId())
                .nonTrainingSubActivityId(entity.getNonTrainingSubActivity().getSubActivityId())
                .organizationId(entity.getOrganization().getOrganizationId())
                .organizationName(entity.getOrganization().getOrganizationName())
                .applicationNo(entity.getApplicationNo())
                .status(entity.getStatus())
                .applicationDate(DateUtil.dateToString(entity.getApplicationDate(),"dd-MM-yyyy"))
                .time(entity.getTime())
                .sanctionDetails(entity.getSanctionDetails())
                .sanctionDate(DateUtil.dateToString(entity.getSanctionDate(),"dd-MM-yyyy"))
                .sanctionedAmount(entity.getSanctionedAmount())
                .details(entity.getDetails())
                .build();
    }
    public static AccessToFinanceResponse mapToAccessToFinanceResponse(AccessToFinance entity) {
        HandholdingSupport support = entity.getHandholdingSupport();
        Organization org = entity.getOrganization();
        return AccessToFinanceResponse.builder()
                .accessToFinanceId(entity.getId())
                .handholdingSupportId(support != null ? support.getId() : null)
                .nonTrainingActivityId(
                        support != null && support.getNonTrainingActivity() != null
                                ? support.getNonTrainingActivity().getActivityId()
                                : null
                )
                .nonTrainingActivityName(
                        support != null && support.getNonTrainingActivity() != null
                                ? support.getNonTrainingActivity().getActivityName()
                                : null
                )
                .nonTrainingSubActivityId(
                        support != null && support.getNonTrainingSubActivity() != null
                                ? support.getNonTrainingSubActivity().getSubActivityId()
                                : null
                )
                .nonTrainingSubActivityName(
                        support != null && support.getNonTrainingSubActivity() != null
                                ? support.getNonTrainingSubActivity().getSubActivityName()
                                : null
                )
                .organizationId(org != null ? org.getOrganizationId() : null)
                .organizationName(org != null ? org.getOrganizationName() : null)
                .accessToFinanceType(entity.getAccessToFinanceType())
                .schemeName(entity.getSchemeName())
                .govtApplicationStatus(
                        entity.getGovtApplicationStatus() != null
                                ? entity.getGovtApplicationStatus().name()
                                : null
                )
                .govtSanctionDate(DateUtil.dateToString(entity.getGovtSanctionDate(), "dd-MM-yyyy"))
                .govtSanctionedAmount(entity.getGovtSanctionedAmount())
                .govtDetails(entity.getGovtDetails())
                .institutionName(entity.getInstitutionName())
                .branchName(entity.getBranchName())
                .dprSubmissionDate(DateUtil.dateToString(entity.getDprSubmissionDate(), "dd-MM-yyyy"))
                .bankApplicationStatus(
                        entity.getBankApplicationStatus() != null
                                ? entity.getBankApplicationStatus().name()
                                : null
                )
                .bankSanctionDate(DateUtil.dateToString(entity.getBankSanctionDate(), "dd-MM-yyyy"))
                .bankSanctionedAmount(entity.getBankSanctionedAmount())
                .bankDetails(entity.getBankDetails())
                .counselledBy(entity.getCounselledBy())
                .counsellingDate(DateUtil.dateToString(entity.getCounsellingDate(), "dd-MM-yyyy"))
                .subjectDelivered(entity.getSubjectDelivered())
                .loanDocumentDetails(entity.getLoanDocumentDetails())
                .build();
    }
    public static AccessToTechnologyAndInfrastructureResponse mapToAccessToTechnologyAndInfrastructureResponse(AccessToTechnologyAndInfrastructure entity) {
        HandholdingSupport support = entity.getHandholdingSupport();
        Organization org = entity.getOrganization();
        return AccessToTechnologyAndInfrastructureResponse.builder()
                .accessToTechnologyId(entity.getId())
                .handholdingSupportId(support != null ? support.getId() : null)
                .nonTrainingActivityId(
                        support != null && support.getNonTrainingActivity() != null
                                ? support.getNonTrainingActivity().getActivityId()
                                : null
                )
                .nonTrainingActivityName(
                        support != null && support.getNonTrainingActivity() != null
                                ? support.getNonTrainingActivity().getActivityName()
                                : null
                )
                .nonTrainingSubActivityId(
                        support != null && support.getNonTrainingSubActivity() != null
                                ? support.getNonTrainingSubActivity().getSubActivityId()
                                : null
                )
                .nonTrainingSubActivityName(
                        support != null && support.getNonTrainingSubActivity() != null
                                ? support.getNonTrainingSubActivity().getSubActivityName()
                                : null
                )
                .organizationId(entity.getOrganization().getOrganizationId())
                .organizationName(entity.getOrganization().getOrganizationName())
                .vendorSuggested(entity.getVendorSuggested())
                .quotationDate(DateUtil.dateToString(entity.getQuotationDate(), "dd-MM-yyyy"))
                .details(entity.getDetails())
                .cost(entity.getCost())
                .requirement(entity.getRequirement())
                .existingMachinery(entity.getExistingMachinery())
                .suggestedMachinery(entity.getSuggestedMachinery())
                .manufacturer(entity.getManufacturer())
                .groundingDate(DateUtil.dateToString(entity.getGroundingDate(), "dd-MM-yyyy"))
                .placeOfInstallation(entity.getPlaceOfInstallation())
                .costOfMachinery(entity.getCostOfMachinery())
                .technologyDetails(entity.getTechnologyDetails())
                .vendorName(entity.getVendorName())
                .vendorContactNo(entity.getVendorContactNo())
                .vendorEmail(entity.getVendorEmail())
                .approxCost(entity.getApproxCost())
                .build();
    }
    public static AccessToPackagingLabellingAndBrandingResponse mapToAccessToPackagingLabellingAndBrandingResponse(AccessToPackagingLabellingAndBranding entity) {
        return AccessToPackagingLabellingAndBrandingResponse.builder()
                .accessToPackagingId(entity.getAccessToPackagingId())
                .handholdingSupportId(entity.getHandholdingSupport().getId())
                .nonTrainingActivityId(
                        entity.getHandholdingSupport()
                                .getNonTrainingActivity()
                                .getActivityId()
                )
                .nonTrainingSubActivityId(
                        entity.getHandholdingSupport()
                                .getNonTrainingSubActivity()
                                .getSubActivityId()
                )
                .nonTrainingActivityName(
                        entity.getHandholdingSupport()
                                .getNonTrainingActivity()
                                .getActivityName()
                )
                .nonTrainingSubActivityName(
                        entity.getHandholdingSupport()
                                .getNonTrainingSubActivity()
                                .getSubActivityName()
                )
                .accessToPackagingType(entity.getAccessToPackagingType())
                .organizationId(entity.getOrganization().getOrganizationId())
                .organizationName(entity.getOrganization().getOrganizationName())
                .studioAccessDate(DateUtil.dateToString(entity.getStudioAccessDate(),"dd-MM-yyyy"))
                .details(entity.getDetails())
                .aleapDesignStudioImage1(entity.getAleapDesignStudioImage1())
                .aleapDesignStudioImage2(entity.getAleapDesignStudioImage2())
                .aleapDesignStudioImage3(entity.getAleapDesignStudioImage3())
                .eventType(entity.getEventType() != null ? entity.getEventType().name() : null)
                .eventDate(DateUtil.dateToString(entity.getEventDate(), "dd-MM-yyyy"))
                .eventLocation(entity.getEventLocation())
                .organizedBy(entity.getOrganizedBy())
                .build();
    }
}