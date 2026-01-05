package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.request_dto.*;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.model.aleap_handholding.*;


import java.util.List;

public class RequestMapper {
    public static BusinessPlanDetails mapToBusinessPlan(
            BusinessPlanRequest request, HandholdingSupport support,
            Organization organization, List<Participant> participants
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
                .build();
    }


    public static Counselling mapToCounselling(CounsellingRequest request, HandholdingSupport support, Organization organization, List<Participant> participants,List<InfluencedParticipant> influencedParticipants) {
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

    public static SectorAdvisory mapToSectorAdvisory(SectorAdvisoryRequest request, HandholdingSupport support, Organization organization, List<Participant> participants) {
        return SectorAdvisory.builder()
                .adviseDetails(request.getAdviseDetails())
                .handholdingSupport(support)
                .organization(organization)
                .participants(participants)
                .counsellingDate(DateUtil.covertStringToDate(request.getCounsellingDate()))
                .counsellingTime(request.getCounsellingTime())
                .counselledBy(request.getCounselledBy())
                .adviseDetails(request.getAdviseDetails())
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

    public static BankNbfcFinance mapToBankNbfcFinance(
            BankNbfcFinanceRequest request,
            HandholdingSupport handholdingSupport) {

        return BankNbfcFinance.builder()
                .handholdingSupport(handholdingSupport)
                .institutionName(request.getInstitutionName())
                .branchName(request.getBranchName())
                .dprSubmissionDate(DateUtil.covertStringToDate(request.getDprSubmissionDate()))
                .status(request.getStatus())
                .sanctionDate(DateUtil.covertStringToDate(request.getSanctionDate()))
                .sanctionedAmount(request.getSanctionedAmount())
                .details(request.getDetails())
                .build();
    }

    public static void updateBankNbfcFinance(
            BankNbfcFinance entity,
            BankNbfcFinanceRequest request) {

        entity.setInstitutionName(request.getInstitutionName());
        entity.setBranchName(request.getBranchName());
        entity.setDprSubmissionDate(DateUtil.covertStringToDate(request.getDprSubmissionDate()));
        entity.setStatus(request.getStatus());
        entity.setSanctionDate(DateUtil.covertStringToDate(request.getSanctionDate()));
        entity.setSanctionedAmount(request.getSanctionedAmount());
        entity.setDetails(request.getDetails());
    }

    public static GovtSchemeFinance mapToGovtSchemeFinance(GovtSchemeFinanceRequest request, HandholdingSupport support) {
        GovtSchemeFinance entity = new GovtSchemeFinance();
        entity.setHandholdingSupport(support);
        entity.setSchemeName(request.getSchemeName());
        entity.setStatus(request.getStatus());
        entity.setSanctionedAmount(request.getSanctionedAmount());
        entity.setDetails(request.getDetails());

        if (request.getSanctionDate() != null) {
            entity.setSanctionDate(
                    DateUtil.covertStringToDate(request.getSanctionDate())
            );
        }
        return entity;
    }

    public static CreditCounselling mapToCreditCounselling(CreditCounsellingRequest request, HandholdingSupport support) {

        return CreditCounselling.builder()
                .handholdingSupport(support)
                .counselledBy(request.getCounselledBy())
                .subjectDelivered(request.getSubjectDelivered())
                .counsellingDate(DateUtil.covertStringToDate(request.getCounsellingDate()))
                .build();
    }

    public static LoanDocumentPreparation mapToLoanDocumentPreparation(
            LoanDocumentPreparationRequest request,
            HandholdingSupport support
    ) {

        return new LoanDocumentPreparation(
                support,
                request.getDetails()
        );
    }

    public static MarketStudy mapToMarketStudy(MarketStudyRequest request, HandholdingSupport support,
                                               Organization organization, List<Participant> participants
    ) {

        MarketStudy entity = new MarketStudy();
        entity.setHandholdingSupport(support);
        entity.setOrganization(organization);
        entity.setParticipants(participants);
        entity.setCounselledBy(request.getCounselledBy());
        entity.setCounsellingTime(request.getCounsellingTime());

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

    public static VendorConnection mapToVendorConnection(
            VendorConnectionRequest request,
            HandholdingSupport support
    ) {

        VendorConnection entity = new VendorConnection();
        entity.setHandholdingSupport(support);
        entity.setVendorSuggested(request.getVendorSuggested());
        entity.setDetails(request.getDetails());
        entity.setCost(request.getCost());

        if (request.getQuotationDate() != null) {
            entity.setQuotationDate(
                    DateUtil.covertStringToDate(request.getQuotationDate())
            );
        }

        return entity;
    }

    public static MachineryIdentification mapToMachineryIdentification(MachineryIdentificationRequest request, HandholdingSupport support) {
        MachineryIdentification entity = new MachineryIdentification();
        entity.setHandholdingSupport(support);
        entity.setRequirement(request.getRequirement());
        entity.setExistingMachinery(request.getExistingMachinery());
        entity.setSuggestedMachinery(request.getSuggestedMachinery());
        entity.setManufacturer(request.getManufacturer());
        entity.setPlaceOfInstallation(request.getPlaceOfInstallation());
        entity.setCostOfMachinery(request.getCostOfMachinery());

        if (request.getGroundingDate() != null) {
            entity.setGroundingDate(
                    DateUtil.covertStringToDate(request.getGroundingDate())
            );
        }

        return entity;
    }

    public static CFCSupport mapToCFCSupport(CFCSupportRequest request, HandholdingSupport support) {
        return CFCSupport.builder()
                .handholdingSupport(support)
                .technologyDetails(request.getTechnologyDetails())
                .vendorName(request.getVendorName())
                .vendorContactNo(request.getVendorContactNo())
                .vendorEmail(request.getVendorEmail())
                .approxCost(request.getApproxCost())
                .build();
    }

    public static TradeFairParticipation mapToTradeFairParticipation(TradeFairParticipationRequest request, HandholdingSupport support) {
        return TradeFairParticipation.builder()
                .handholdingSupport(support)
                .eventLocation(request.getEventLocation())
                .organizedBy(request.getOrganizedBy())
                .eventType(request.getEventType())
                .eventDate(DateUtil.covertStringToDate(request.getEventDate()))
                .build();
    }

    public static AleapDesignStudio mapToAleapDesignStudio(AleapDesignStudioRequest request, HandholdingSupport support) {
        return AleapDesignStudio.builder()
                .handholdingSupport(support)
                .details(request.getDetails())
                .studioAccessDate(DateUtil.covertStringToDate(request.getStudioAccessDate()))
                .build();
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
}


