package com.metaverse.workflow.TGTPCNT.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.TGTPC4NTHandholding;

public class TGTPC4NTHandholdingMapper {

    private static final String DATE_FORMAT = "dd-MM-yyyy";

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

    public static TGTPC4NTHandholdingResponse mapToResponse(TGTPC4NTHandholding entity) {

        return TGTPC4NTHandholdingResponse.builder()
                .id(entity.getId())
                .nonTrainingSubActivityId(entity.getNonTrainingSubActivity().getSubActivityId())
                .msmeName(entity.getMsmeName())
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
}