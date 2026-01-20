package com.metaverse.workflow.tgtpc_handholding.service;


import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.InfluencedParticipant;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.tgtpc_handholding.*;
import com.metaverse.workflow.tgtpc_handholding.response_dto.*;

import java.util.stream.Stream;

public class HandholdingResponseMapper {

    private static final String DATE_FORMAT = "dd-MM-yyyy";
    public static TGTPCHandholdingSupportResponse mapToTGTPCHandholdingSupportResponse(TGTPCHandholdingSupport support)
    {
        return TGTPCHandholdingSupportResponse.builder()
                .participantNames(
                        Stream.concat(
                                support.getParticipants() != null
                                        ? support.getParticipants().stream()
                                        .map(Participant::getParticipantName)
                                        : Stream.empty(),
                                support.getInfluencedParticipants() != null
                                        ? support.getInfluencedParticipants().stream()
                                        .map(InfluencedParticipant::getParticipantName)
                                        : Stream.empty()
                        ).toList()
                )
                .nonTrainingActivityId(support.getNonTrainingSubActivity().getNonTrainingActivity().getActivityId())
                .nonTrainingSubActivityId(support.getNonTrainingSubActivity().getSubActivityId())
                .nonTrainingActivityName(support.getNonTrainingSubActivity().getNonTrainingActivity().getActivityName())
                .nonTrainingSubActivityName(support.getNonTrainingSubActivity().getSubActivityName())
                .handholdingSupportBy(support.getHandholdingSupportBy())
                .organizationId(support.getOrganization().getOrganizationId())
                .organizationName(support.getOrganization().getOrganizationName())
                .handholdingDate(DateUtil.dateToString(support.getHandholdingDate(),DATE_FORMAT)                )
                .handholdingTime(support.getHandholdingTime())
                .brandingSupportDetails(support.getBrandingSupportDetails())
                .packagingStandardsSupportDetails(support.getPackagingStandardsSupportDetails())
                .build();
    }

    public static TGTPCNTReportsResponse mapToResponse(TGTPCNTReports entity) {
        return TGTPCNTReportsResponse.builder()
                .reportId(entity.getId())
                .sectorName(entity.getSectorName())
                .productName(entity.getProductName())
                .reportSubmissionDate(DateUtil.dateToString(entity.getReportSubmissionDate(),DATE_FORMAT))
                .approvalDate(DateUtil.dateToString(entity.getApprovalDate(),DATE_FORMAT))
                .nonTrainingActivityId(entity.getNonTrainingSubActivity().getNonTrainingActivity().getActivityId())
                .nonTrainingActivityName(entity.getNonTrainingSubActivity().getNonTrainingActivity().getActivityName())
                .nonTrainingSubActivityId(entity.getNonTrainingSubActivity().getSubActivityId())
                .nonTrainingSubActivityName(entity.getNonTrainingSubActivity().getSubActivityName())
                .build();
    }

    public static TGTPC4NTHandholdingResponse mapToResponse(TGTPC4NTHandholding entity) {

        return TGTPC4NTHandholdingResponse.builder()
                .id(entity.getId())
                .nameOfTheRawMaterial(entity.getNameOfTheRawMaterial())
                .nameOfTheDomesticSupplier(entity.getNameOfTheDomesticSupplier())
                .organizationId(entity.getOrganization().getOrganizationId())
                .organizationName(entity.getOrganization().getOrganizationName())
                .nonTrainingSubActivityId(entity.getNonTrainingSubActivity().getSubActivityId())
                .nameOfTheSector(entity.getNameOfTheSector())
                .importedComponents(entity.getImportedComponents())
                .designDeveloped(entity.getDesignDeveloped())
                .adoptionDate(DateUtil.dateToString(entity.getAdoptionDate(), DATE_FORMAT))
                .domesticProductsTested(entity.getDomesticProductsTested())
                .testingLab(entity.getTestingLab())
                .testName(entity.getTestName())
                .qualityCertifications(entity.getQualityCertifications())
                .domesticBuyer(entity.getDomesticBuyer())
                .productCatalogue(entity.getProductCatalogue())
                .manufacturingLine(entity.getManufacturingLine())
                .productionStartDate(DateUtil.dateToString(entity.getProductionStartDate(), DATE_FORMAT))
                .schemeName(entity.getSchemeName())
                .investmentValue(entity.getInvestmentValue())
                .schemeSubsidy(entity.getSchemeSubsidy())
                .releaseDate(DateUtil.dateToString(entity.getReleaseDate(), DATE_FORMAT))
                .dprSubmissionDate(DateUtil.dateToString(entity.getDprSubmissionDate(), DATE_FORMAT))
                .sanctionDate(DateUtil.dateToString(entity.getSanctionDate(), DATE_FORMAT))
                .sanctionAmount(entity.getSanctionAmount())
                .bankNbfc(entity.getBankNbfc())
                .productLaunchDate(DateUtil.dateToString(entity.getProductLaunchDate(), DATE_FORMAT))
                .launchedProducts(entity.getLaunchedProducts())
                .technologyDesign(entity.getTechnologyDesign())
                .technicalInstitution(entity.getTechnicalInstitution())
                .iprName(entity.getIprName())
                .iprRegistrationDate(DateUtil.dateToString(entity.getIprRegistrationDate(), DATE_FORMAT))
                .build();
    }

    public static IECRegistrationCertificationResponse mapToIECRegistrationCertificationRes(IECRegistrationCertification entity) {
        TGTPCHandholdingSupport support = entity.getTgtpcHandholdingSupport();
        return IECRegistrationCertificationResponse.builder()
                .id(entity.getId())
                .supportType(entity.getSupportType().name())
                .iecRegistrationNumber(entity.getIecRegistrationNumber())
                .registrationDate(DateUtil.dateToString(entity.getRegistrationDate(),DATE_FORMAT) )
                .certificationName(entity.getCertificationName())
                .certificateNumber(entity.getCertificateNumber())
                .certificateDate(DateUtil.dateToString(entity.getCertificateDate(),DATE_FORMAT))
                .participantNames(
                        Stream.concat(
                                support.getParticipants() != null
                                        ? support.getParticipants().stream()
                                        .map(Participant::getParticipantName)
                                        : Stream.empty(),
                                support.getInfluencedParticipants() != null
                                        ? support.getInfluencedParticipants().stream()
                                        .map(InfluencedParticipant::getParticipantName)
                                        : Stream.empty()
                        ).toList()
                )
                .nonTrainingActivityId(support.getNonTrainingSubActivity().getNonTrainingActivity().getActivityId())
                .nonTrainingSubActivityId(support.getNonTrainingSubActivity().getSubActivityId())
                .nonTrainingActivityName(support.getNonTrainingSubActivity().getNonTrainingActivity().getActivityName())
                .nonTrainingSubActivityName(support.getNonTrainingSubActivity().getSubActivityName())
                .handholdingSupportBy(support.getHandholdingSupportBy())
                .organizationId(support.getOrganization().getOrganizationId())
                .organizationName(support.getOrganization().getOrganizationName())
                .handholdingDate(DateUtil.dateToString(support.getHandholdingDate(),DATE_FORMAT)                )
                .handholdingTime(support.getHandholdingTime())
                .build();
    }

    public static LocalTestingLabAttachmentResponse mapToLocalTestingLabAttachmentRes(LocalTestingLabAttachment entity) {
        TGTPCHandholdingSupport support = entity.getTgtpcHandholdingSupport();
        return LocalTestingLabAttachmentResponse.builder()
                .labOrCfcName(entity.getLabOrCfcName())
                .purposeOfAttachment(entity.getPurposeOfAttachment())
                .dateOfAttachment(DateUtil.dateToString(entity.getDateOfAttachment(), DATE_FORMAT))
                .participantNames(
                        Stream.concat(
                                support.getParticipants() != null
                                        ? support.getParticipants().stream().map(Participant::getParticipantName)
                                        : Stream.empty(),
                                support.getInfluencedParticipants() != null
                                        ? support.getInfluencedParticipants().stream().map(InfluencedParticipant::getParticipantName)
                                        : Stream.empty()
                        ).toList()
                )
                .nonTrainingActivityId(support.getNonTrainingSubActivity().getNonTrainingActivity().getActivityId())
                .nonTrainingSubActivityId(support.getNonTrainingSubActivity().getSubActivityId())
                .nonTrainingActivityName(support.getNonTrainingSubActivity().getNonTrainingActivity().getActivityName())
                .nonTrainingSubActivityName(support.getNonTrainingSubActivity().getSubActivityName())
                .handholdingSupportBy(support.getHandholdingSupportBy())
                .organizationId(support.getOrganization().getOrganizationId())
                .organizationName(support.getOrganization().getOrganizationName())
                .handholdingDate(DateUtil.dateToString(support.getHandholdingDate(), DATE_FORMAT))
                .handholdingTime(support.getHandholdingTime())
                .build();
    }
    public static RawMaterialSupportResponse mapToRawMaterialSupportRes(RawMaterialSupport entity) {
        TGTPCHandholdingSupport support = entity.getTgtpcHandholdingSupport();
        return RawMaterialSupportResponse.builder()
                .rawMaterialDetails(entity.getRawMaterialDetails())
                .firstDateOfSupply(DateUtil.dateToString(entity.getFirstDateOfSupply(), DATE_FORMAT))
                .cost(entity.getCost())
                .participantNames(
                        Stream.concat(
                                support.getParticipants() != null
                                        ? support.getParticipants().stream().map(Participant::getParticipantName)
                                        : Stream.empty(),
                                support.getInfluencedParticipants() != null
                                        ? support.getInfluencedParticipants().stream().map(InfluencedParticipant::getParticipantName)
                                        : Stream.empty()
                        ).toList()
                )
                .nonTrainingActivityId(support.getNonTrainingSubActivity().getNonTrainingActivity().getActivityId())
                .nonTrainingSubActivityId(support.getNonTrainingSubActivity().getSubActivityId())
                .nonTrainingActivityName(support.getNonTrainingSubActivity().getNonTrainingActivity().getActivityName())
                .nonTrainingSubActivityName(support.getNonTrainingSubActivity().getSubActivityName())
                .handholdingSupportBy(support.getHandholdingSupportBy())
                .organizationId(support.getOrganization().getOrganizationId())
                .organizationName(support.getOrganization().getOrganizationName())
                .handholdingDate(DateUtil.dateToString(support.getHandholdingDate(), DATE_FORMAT))
                .handholdingTime(support.getHandholdingTime())
                .build();
    }
    public static TestingQualityCertificationSupportResponse mapToTestingQualityCertificationSupportRes(
            TestingQualityCertificationSupport entity
    ) {

        TGTPCHandholdingSupport support = entity.getTgtpcHandholdingSupport();

        return TestingQualityCertificationSupportResponse.builder()
                .testingAgencyName(entity.getTestingAgencyName())
                .dateOfTest(DateUtil.dateToString(entity.getDateOfTest(), DATE_FORMAT))
                .productTested(entity.getProductTested())
                .testResultsDate(DateUtil.dateToString(entity.getTestResultsDate(), DATE_FORMAT))
                .certificationOrTestFindings(entity.getCertificationOrTestFindings())
                .testResultFilePath(entity.getTestResultFilePath())
                .participantNames(
                        Stream.concat(
                                support.getParticipants() != null
                                        ? support.getParticipants().stream().map(Participant::getParticipantName)
                                        : Stream.empty(),
                                support.getInfluencedParticipants() != null
                                        ? support.getInfluencedParticipants().stream().map(InfluencedParticipant::getParticipantName)
                                        : Stream.empty()
                        ).toList()
                )
                .nonTrainingActivityId(support.getNonTrainingSubActivity().getNonTrainingActivity().getActivityId())
                .nonTrainingSubActivityId(support.getNonTrainingSubActivity().getSubActivityId())
                .nonTrainingActivityName(support.getNonTrainingSubActivity().getNonTrainingActivity().getActivityName())
                .nonTrainingSubActivityName(support.getNonTrainingSubActivity().getSubActivityName())
                .handholdingSupportBy(support.getHandholdingSupportBy())
                .organizationId(support.getOrganization().getOrganizationId())
                .organizationName(support.getOrganization().getOrganizationName())
                .handholdingDate(DateUtil.dateToString(support.getHandholdingDate(), DATE_FORMAT))
                .handholdingTime(support.getHandholdingTime())
                .build();
    }



}