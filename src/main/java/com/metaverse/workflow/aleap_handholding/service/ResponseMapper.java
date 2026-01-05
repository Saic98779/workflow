package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.response_dto.GovtSchemeFinanceResponse;
import com.metaverse.workflow.aleap_handholding.response_dto.*;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.InfluencedParticipant;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.aleap_handholding.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
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
                .dateOfStudy(
                        entity.getMarketStudy().getDateOfStudy() != null
                                ? entity.getMarketStudy().getDateOfStudy().toString()
                                : null
                )
                .inputDetails(entity.getInputDetails())
                .source(entity.getSource())
                .sector(entity.getSector())
                .feasibilityActivity(entity.getFeasibilityActivity())
                .build();
    }

    public static BankNbfcFinanceResponse mapToBankNbfcFinanceResponse(
            BankNbfcFinance entity) {

        return BankNbfcFinanceResponse.builder()
                .bankNbfcId(entity.getId())
                .institutionName(entity.getInstitutionName())
                .branchName(entity.getBranchName())
                .dprSubmissionDate(DateUtil.dateToString(entity.getDprSubmissionDate(),"dd-MM-yyyy"))
                .status(entity.getStatus())
                .sanctionDate(DateUtil.dateToString(entity.getSanctionDate(),"dd-MM-yyyy"))
                .sanctionedAmount(entity.getSanctionedAmount())
                .details(entity.getDetails())
                .handHoldingType(
                        entity.getHandholdingSupport().getHandholdingSupportType())
                .nonTrainingActivityId(
                        entity.getHandholdingSupport().getNonTrainingActivity().getActivityId())
                .nonTrainingSubActivityId(
                        entity.getHandholdingSupport().getNonTrainingSubActivity().getSubActivityId())
                .nonTrainingActivityName(
                        entity.getHandholdingSupport().getNonTrainingActivity().getActivityName())
                .nonTrainingSubActivityName(
                        entity.getHandholdingSupport().getNonTrainingSubActivity().getSubActivityName())
                .build();
    }

    public static GovtSchemeFinanceResponse mapToGovtSchemeFinanceResponse(GovtSchemeFinance entity) {

        return GovtSchemeFinanceResponse.builder()
                .govtSchemeFinanceId(entity.getId())
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
                .schemeName(entity.getSchemeName())
                .status(entity.getStatus())
                .sanctionDate(
                        DateUtil.dateToString(entity.getSanctionDate(),"dd-MM-yyyy")
                )
                .sanctionedAmount(entity.getSanctionedAmount())
                .details(entity.getDetails())
                .build();
    }

    public static CreditCounsellingResponse mapToCreditCounsellingResponse(
            CreditCounselling entity
    ) {

        return CreditCounsellingResponse.builder()
                .creditCounselingId(entity.getId())
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
                .counselledBy(entity.getCounselledBy())
                .counsellingDate(DateUtil.dateToString(entity.getCounsellingDate(),"dd-MM-yyyy"))
                .subjectDelivered(entity.getSubjectDelivered())
                .build();
    }
    public static LoanDocumentPreparationResponse
    mapToLoanDocumentPreparationResponse(LoanDocumentPreparation entity) {

        return LoanDocumentPreparationResponse.builder()
                .loanDocumentPreparationId(entity.getId())
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
                .details(entity.getDetails())
                .build();
    }
    public static MarketStudyResponse mapToMarketStudyResponse(
            MarketStudy entity
    ) {

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
                .handHoldingType(entity.getHandholdingSupport().getHandholdingSupportType())
                .build();
    }
    public static VendorConnectionResponse mapToVendorConnectionResponse(
            VendorConnection entity
    ) {

        return VendorConnectionResponse.builder()
                .vendorConnectionId(entity.getId())
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
                .vendorSuggested(entity.getVendorSuggested())
                .quotationDate(DateUtil.dateToString(entity.getQuotationDate(),"dd-MM-yyyy"))
                .details(entity.getDetails())
                .cost(entity.getCost())
                .build();
    }

    public static MachineryIdentificationResponse mapToMachineryIdentificationResponse(
            MachineryIdentification entity
    ) {

        return MachineryIdentificationResponse.builder()
                .machineryIdentificationId(entity.getId())
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
                .requirement(entity.getRequirement())
                .existingMachinery(entity.getExistingMachinery())
                .suggestedMachinery(entity.getSuggestedMachinery())
                .manufacturer(entity.getManufacturer())
                .groundingDate(
                        entity.getGroundingDate() != null
                                ? DateUtil.dateToString(entity.getGroundingDate(),"dd-MM-yyyy")
                                : null
                )
                .placeOfInstallation(entity.getPlaceOfInstallation())
                .costOfMachinery(entity.getCostOfMachinery())
                .build();
    }
    public static CFCSupportResponse mapToCFCSupportResponse(CFCSupport entity
    ) {

        return CFCSupportResponse.builder()
                .cfsSupportId(entity.getId())
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
                .technologyDetails(entity.getTechnologyDetails())
                .vendorName(entity.getVendorName())
                .vendorContactNo(entity.getVendorContactNo())
                .vendorEmail(entity.getVendorEmail())
                .approxCost(entity.getApproxCost())
                .build();
    }

    public static TradeFairParticipationResponse mapToTradeFairParticipationResponse(TradeFairParticipation entity) {

        return TradeFairParticipationResponse.builder()
                .tradeFairParticipationId(entity.getId())
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
                .eventType(entity.getEventType().name())
                .eventDate(DateUtil.dateToString(entity.getEventDate(), "dd-MM-yyyy"))
                .eventLocation(entity.getEventLocation())
                .organizedBy(entity.getOrganizedBy())
                .build();
    }
    public static AleapDesignStudioResponse mapToAleapDesignStudioResponse(AleapDesignStudio entity) {
        return AleapDesignStudioResponse.builder()
                .aleapDesignStudioId(entity.getAleapDesignStudioId())
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
                .studioAccessDate(DateUtil.dateToString(entity.getStudioAccessDate(),"dd-MM-yyyy"))
                .details(entity.getDetails())
                .aleapDesignStudioImage1(entity.getAleapDesignStudioImage1())
                .aleapDesignStudioImage2(entity.getAleapDesignStudioImage2())
                .aleapDesignStudioImage3(entity.getAleapDesignStudioImage3())
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
}