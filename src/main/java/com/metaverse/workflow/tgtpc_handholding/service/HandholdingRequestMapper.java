package com.metaverse.workflow.tgtpc_handholding.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.InfluencedParticipant;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.tgtpc_handholding.*;
import com.metaverse.workflow.tgtpc_handholding.request_dto.*;

import java.util.List;

public class HandholdingRequestMapper {

    public static TGTPCHandholdingSupport mapToTGTPCHandholdingSupport(
            TGTPCHandholdingSupportRequest request,
            NonTrainingSubActivity subActivity,
            Organization organization,
            List<Participant> participants,
            List<InfluencedParticipant>influencedParticipants
    ) {
        if (request == null) {return null;}
        return TGTPCHandholdingSupport.builder()
                .nonTrainingSubActivity(subActivity)
                .handholdingSupportBy(request.getHandholdingSupportBy())
                .organization(organization)
                .handholdingDate(DateUtil.covertStringToDate(request.getHandholdingDate()))
                .handholdingTime(request.getHandholdingTime())
                .participants(participants)
                .influencedParticipants(influencedParticipants)
                .packagingStandardsSupportDetails(request.getPackagingStandardsSupportDetails())
                .brandingSupportDetails(request.getBrandingSupportDetails())
                .build();
    }
    public static void updateEntity(
            TGTPCHandholdingSupport existing,
            TGTPCHandholdingSupportRequest request,
            NonTrainingSubActivity subActivity,
            Organization organization,
            List<Participant> participants,
            List<InfluencedParticipant> influencedParticipants
    ) {
        existing.setNonTrainingSubActivity(subActivity != null ? subActivity : existing.getNonTrainingSubActivity());
        existing.setHandholdingSupportBy(request.getHandholdingSupportBy() != null ? request.getHandholdingSupportBy() : existing.getHandholdingSupportBy());
        existing.setOrganization(organization != null ? organization : existing.getOrganization());
        existing.setHandholdingDate(request.getHandholdingDate() != null ? DateUtil.covertStringToDate(request.getHandholdingDate()) : existing.getHandholdingDate());
        existing.setHandholdingTime(request.getHandholdingTime() != null ? request.getHandholdingTime() : existing.getHandholdingTime());
        existing.setParticipants((participants != null && !participants.isEmpty()) ? participants : existing.getParticipants());
        existing.setInfluencedParticipants((influencedParticipants != null && !influencedParticipants.isEmpty()) ? influencedParticipants : existing.getInfluencedParticipants());
        existing.setPackagingStandardsSupportDetails(request.getPackagingStandardsSupportDetails() != null ? request.getPackagingStandardsSupportDetails() : existing.getPackagingStandardsSupportDetails());
        existing.setBrandingSupportDetails(request.getBrandingSupportDetails() != null ? request.getBrandingSupportDetails() : existing.getBrandingSupportDetails());
    }


    public static TGTPCNTReports mapToEntity(TGTPCNTReportsRequest request, NonTrainingSubActivity subActivity) {
        return TGTPCNTReports.builder()
                .sectorName(request.getSectorName())
                .productName(request.getProductName())
                .reportSubmissionDate(DateUtil.covertStringToDate(request.getReportSubmissionDate()))
                .approvalDate(DateUtil.covertStringToDate(request.getApprovalDate()))
                .nonTrainingSubActivity(subActivity)
                .build();
    }

    public static void updateEntity(TGTPCNTReports entity, TGTPCNTReportsRequest request) {
        entity.setSectorName(request.getSectorName());
        entity.setProductName(request.getProductName());
        entity.setReportSubmissionDate(DateUtil.covertStringToDate(request.getReportSubmissionDate()));
        entity.setApprovalDate(DateUtil.covertStringToDate(request.getApprovalDate()));
    }

    public static TGTPC4NTHandholding mapToEntity(TGTPC4NTHandholdingRequest request, NonTrainingSubActivity subActivity) {

        return TGTPC4NTHandholding.builder()
                .nonTrainingSubActivity(subActivity)
                .msmeName(request.getMsmeName())
                .importedComponents(request.getImportedComponents())
                .designDeveloped(request.getDesignDeveloped())
                .adoptionDate(DateUtil.covertStringToDate(request.getAdoptionDate()))
                .domesticProductsTested(request.getDomesticProductsTested())
                .testingLab(request.getTestingLab())
                .testName(request.getTestName())
                .qualityCertifications(request.getQualityCertifications())
                .domesticBuyer(request.getDomesticBuyer())
                .productCatalogue(request.getProductCatalogue())
                .manufacturingLine(request.getManufacturingLine())
                .productionStartDate(DateUtil.covertStringToDate(request.getProductionStartDate()))
                .schemeName(request.getSchemeName())
                .investmentValue(request.getInvestmentValue())
                .schemeSubsidy(request.getSchemeSubsidy())
                .releaseDate(DateUtil.covertStringToDate(request.getReleaseDate()))
                .dprSubmissionDate(DateUtil.covertStringToDate(request.getDprSubmissionDate()))
                .sanctionDate(DateUtil.covertStringToDate(request.getSanctionDate()))
                .sanctionAmount(request.getSanctionAmount())
                .bankNbfc(request.getBankNbfc())
                .productLaunchDate(DateUtil.covertStringToDate(request.getProductLaunchDate()))
                .launchedProducts(request.getLaunchedProducts())
                .technologyDesign(request.getTechnologyDesign())
                .technicalInstitution(request.getTechnicalInstitution())
                .iprName(request.getIprName())
                .iprRegistrationDate(DateUtil.covertStringToDate(request.getIprRegistrationDate()))
                .build();
    }

    public static void updateEntity(TGTPC4NTHandholding entity, TGTPC4NTHandholdingRequest request) {

        entity.setMsmeName(request.getMsmeName());
        entity.setImportedComponents(request.getImportedComponents());
        entity.setDesignDeveloped(request.getDesignDeveloped());
        entity.setAdoptionDate(DateUtil.covertStringToDate(request.getAdoptionDate()));
        entity.setDomesticProductsTested(request.getDomesticProductsTested());
        entity.setTestingLab(request.getTestingLab());
        entity.setTestName(request.getTestName());
        entity.setQualityCertifications(request.getQualityCertifications());
        entity.setDomesticBuyer(request.getDomesticBuyer());
        entity.setProductCatalogue(request.getProductCatalogue());
        entity.setManufacturingLine(request.getManufacturingLine());
        entity.setProductionStartDate(DateUtil.covertStringToDate(request.getProductionStartDate()));
        entity.setSchemeName(request.getSchemeName());
        entity.setInvestmentValue(request.getInvestmentValue());
        entity.setSchemeSubsidy(request.getSchemeSubsidy());
        entity.setReleaseDate(DateUtil.covertStringToDate(request.getReleaseDate()));
        entity.setDprSubmissionDate(DateUtil.covertStringToDate(request.getDprSubmissionDate()));
        entity.setSanctionDate(DateUtil.covertStringToDate(request.getSanctionDate()));
        entity.setSanctionAmount(request.getSanctionAmount());
        entity.setBankNbfc(request.getBankNbfc());
        entity.setProductLaunchDate(DateUtil.covertStringToDate(request.getProductLaunchDate()));
        entity.setLaunchedProducts(request.getLaunchedProducts());
        entity.setTechnologyDesign(request.getTechnologyDesign());
        entity.setTechnicalInstitution(request.getTechnicalInstitution());
        entity.setIprName(request.getIprName());
        entity.setIprRegistrationDate(DateUtil.covertStringToDate(request.getIprRegistrationDate()));
    }


    public static IECRegistrationCertification mapToIECRegistrationCertification(IECRegistrationCertificationRequest request, TGTPCHandholdingSupport support
    ) {
        return IECRegistrationCertification.builder()
                .tgtpcHandholdingSupport(support)
                .supportType(request.getSupportType())
                .iecRegistrationNumber(request.getIecRegistrationNumber())
                .registrationDate(DateUtil.covertStringToDate(request.getRegistrationDate()))
                .certificationName(request.getCertificationName())
                .certificateNumber(request.getCertificateNumber())
                .certificateDate(DateUtil.covertStringToDate(request.getCertificateDate()))
                .build();
    }

    public static LocalTestingLabAttachment mapToLocalTestingLabAttachment(LocalTestingLabAttachmentRequest request, TGTPCHandholdingSupport support
    ) {
        return LocalTestingLabAttachment.builder()
                .tgtpcHandholdingSupport(support)
                .dateOfAttachment(DateUtil.covertStringToDate(request.getDateOfAttachment()))
                .labOrCfcName(request.getLabOrCfcName())
                .purposeOfAttachment(request.getPurposeOfAttachment())
                .build();
    }

    public static RawMaterialSupport mapToRawMaterialSupport(RawMaterialSupportRequest request, TGTPCHandholdingSupport support
    ) {
        return RawMaterialSupport.builder()
                .tgtpcHandholdingSupport(support)
                .rawMaterialDetails(request.getRawMaterialDetails())
                .firstDateOfSupply(DateUtil.covertStringToDate(request.getFirstDateOfSupply()))
                .cost(request.getCost())
                .build();
    }

    public static TestingQualityCertificationSupport mapToTestingQualityCertificationSupport(TestingQualityCertificationSupportRequest request, TGTPCHandholdingSupport support
    ) {
        return TestingQualityCertificationSupport.builder()
                .tgtpcHandholdingSupport(support)
                .testingAgencyName(request.getTestingAgencyName())
                .dateOfTest(DateUtil.covertStringToDate(request.getDateOfTest()))
                .productTested(request.getProductTested())
                .certificationOrTestFindings(request.getCertificationOrTestFindings())
                .build();
    }



}