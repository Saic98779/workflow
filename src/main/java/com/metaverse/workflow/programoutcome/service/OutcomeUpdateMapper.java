package com.metaverse.workflow.programoutcome.service;


import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.outcomes.*;
import com.metaverse.workflow.programoutcome.dto.*;

public class OutcomeUpdateMapper {
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static void updateOndcRegistrationFields(ONDCRegistration existingEntity, ONDCRegistrationRequest request) {
        existingEntity.setOndcRegistrationNo(request.getOndcRegistrationNo());
        existingEntity.setOndcRegistrationDate(DateUtil.stringToDate(request.getOndcRegistrationDate(), DATE_FORMAT));
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateOndcTransactionFields(ONDCTransaction existingEntity, ONDCTransactionRequest request) {
        existingEntity.setTransactionDate(DateUtil.stringToDate(request.getTransactionDate(), DATE_FORMAT));
        existingEntity.setTransactionType(request.getTransactionType());
        existingEntity.setTransactionValue(request.getTransactionValue());
        existingEntity.setProductName(request.getProductName());
        existingEntity.setProductUnits(request.getProductUnits());
        existingEntity.setProductQuantity(request.getProductQuantity());
        // Skip: ondcRegistration
    }

    public static void updateUdyamRegistrationFields(UdyamRegistration existingEntity, UdyamRegistrationRequest request) {
        existingEntity.setUdyamRegistrationNo(request.getUdyamRegistrationNo());
        existingEntity.setUdyamRegistationDate(DateUtil.stringToDate(request.getUdyamRegistrationDate(), DATE_FORMAT));
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateCgtmseTransactionFields(CGTMSETransaction existingEntity, CGTMSETransactionRequest request) {
        existingEntity.setCreditApplicationDate(DateUtil.stringToDate(request.getCreditApplicationDate(), DATE_FORMAT));
        existingEntity.setDprSubmissionDate(DateUtil.stringToDate(request.getDprSubmissionDate(), DATE_FORMAT));
        existingEntity.setApprovalDate(DateUtil.stringToDate(request.getApprovalDate(), DATE_FORMAT));
        existingEntity.setAmountReleaseDate(DateUtil.stringToDate(request.getAmountReleaseDate(), DATE_FORMAT));
        existingEntity.setValueReleased(request.getValueReleased());
        existingEntity.setCreditGuaranteePercentage(request.getCreditGuaranteePercentage());
        existingEntity.setPurpose(request.getPurpose());
        existingEntity.setGroundingDate(DateUtil.stringToDate(request.getGroundingDate(), DATE_FORMAT));
        existingEntity.setProductName(request.getProductName());
        existingEntity.setProductionPerMonth(request.getProductionPerMonth());
        existingEntity.setMarketType(request.getMarketType());
        existingEntity.setMarketingDate(DateUtil.stringToDate(request.getMarketingDate(), DATE_FORMAT));
        existingEntity.setMarketVolume(request.getMarketVolume());
        existingEntity.setMarketValue(request.getMarketValue());
        existingEntity.setTotalTurnover(request.getTotalTurnover());
        existingEntity.setEmploymentMale(request.getEmploymentMale());
        existingEntity.setEmploymentFemale(request.getEmploymentFemale());
        existingEntity.setProposeOfEnterpriseUpgradation(request.getProposeOfEnterpriseUpGradation());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateGeMRegistrationFields(GeMRegistration existingEntity, GeMRegistrationRequest request) {
        existingEntity.setGemRegistrationId(request.getGemRegistrationId());
        existingEntity.setGemRegistrationDate(DateUtil.stringToDate(request.getGemRegistrationDate(), DATE_FORMAT));
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateGeMTransactionFields(GeMTransaction existingEntity, GeMTransactionRequest request) {
        existingEntity.setProcurementDate(DateUtil.stringToDate(request.getProcurementDate(), DATE_FORMAT));
        existingEntity.setProductName(request.getProductName());
        existingEntity.setUnitOfMeasurement(request.getUnitOfMeasurement());
        existingEntity.setRegisteredAs(request.getRegisteredAs());
        existingEntity.setQuantity(request.getQuantity());
        existingEntity.setProductValue(request.getProductValue());
        // Skip: gemRegistration
    }

    public static void updateTReDSRegistrationFields(TReDSRegistration existingEntity, TReDSRegistrationRequest request) {
        existingEntity.setTredsRegistrationNo(request.getTrdseRegistrationNo());
        existingEntity.setTredsRegistrationDate(DateUtil.stringToDate(request.getTredsRegistrationDate(), DATE_FORMAT));
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateTReDSTransactionFields(TReDSTransaction existingEntity, TReDSTransactionRequest request) {
        existingEntity.setTredsTransactionDate(DateUtil.stringToDate(request.getTredsTransactionDate(), DATE_FORMAT));
        existingEntity.setInvoiceNumber(request.getInvoiceNumber());
        existingEntity.setBuyerName(request.getBuyerName());
        existingEntity.setTredsPlatformUsed(request.getTredsPlatformUsed());
        existingEntity.setInvoiceAmount(request.getInvoiceAmount());
        existingEntity.setBidOpeningDate(DateUtil.stringToDate(request.getBidOpeningDate(), DATE_FORMAT));
        existingEntity.setWinnerFinancier(request.getWinnerFinancier());
        existingEntity.setDiscountRateOffered(request.getDiscountRateOffered());
        existingEntity.setDiscountingFeeFor60Days(request.getDiscountingFeeFor60Days());
        existingEntity.setFinalPayoutToMsme(request.getFinalPayoutToMsme());
        existingEntity.setPaymentSettlementDate(DateUtil.stringToDate(request.getPaymentSettlementDate(), DATE_FORMAT));
        existingEntity.setBuyerDueDateToPay(DateUtil.stringToDate(request.getBuyerDueDateToPay(), DATE_FORMAT));
        existingEntity.setRepaymentDate(DateUtil.stringToDate(request.getRepaymentDate(), DATE_FORMAT));
        // Skip: tredsRegistration
    }

    public static void updatePmegpFields(PMEGP existingEntity, PMEGPRequest request) {
        existingEntity.setLoanSanctionedDate(DateUtil.stringToDate(request.getLoanSanctionedDate(), DATE_FORMAT));
        existingEntity.setLoanAmountReleased(request.getLoanAmountReleased());
        existingEntity.setGovtSubsidy(request.getGovtSubsidy());
        existingEntity.setBeneficiaryContribution(request.getBeneficiaryContribution());
        existingEntity.setTotalAmountReleased(request.getTotalAmountReleased());
        existingEntity.setGroundingDate(DateUtil.stringToDate(request.getGroundingDate(), DATE_FORMAT));
        existingEntity.setBusinessTurnover(request.getBusinessTurnover());
        existingEntity.setDateOfMarketLinkage(DateUtil.stringToDate(request.getDateOfMarketLinkage(), DATE_FORMAT));
        existingEntity.setVolumeOfMarket(request.getVolumeOfMarket());
        existingEntity.setUnits(request.getUnits());
        existingEntity.setValueOfMarket(request.getValueOfMarket());
        existingEntity.setNameOfProductMarket(request.getNameOfProductMarket());
        existingEntity.setNumberOfPersonsEmployed(request.getNumberOfPersonsEmployed());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updatePmmyFields(PMMY existingEntity, PMMYRequest request) {
        existingEntity.setLoanAmountReleased(request.getLoanAmountReleased());
        existingEntity.setLoanSanctionedDate(DateUtil.stringToDate(request.getLoanSanctionedDate(), DATE_FORMAT));
        existingEntity.setGroundingDate(DateUtil.stringToDate(request.getGroundingDate(), DATE_FORMAT));
        existingEntity.setBusinessTurnover(request.getBusinessTurnover());
        existingEntity.setMarketLinkageDate(DateUtil.stringToDate(request.getMarketLinkageDate(), DATE_FORMAT));
        existingEntity.setMarketVolume(request.getMarketVolume());
        existingEntity.setUnits(request.getUnits());
        existingEntity.setMarketValue(request.getMarketValue());
        existingEntity.setProductMarketedName(request.getProductMarketedName());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updatePmsFields(PMS existingEntity, PMSRequest request) {
        existingEntity.setBusinessTurnover(request.getBusinessTurnover());
        existingEntity.setLoanNumber(request.getLoanNumber());
        existingEntity.setPurposeOfLoan(request.getPurposeOfLoan());
        existingEntity.setAmountOfLoanReleased(request.getAmountOfLoanReleased());
        existingEntity.setDateOfLoanReleased(DateUtil.stringToDate(request.getDateOfLoanReleased(), DATE_FORMAT));
        existingEntity.setEmploymentCreatedDirect(request.getEmploymentCreatedDirect());
        existingEntity.setEmploymentCreatedInDirect(request.getEmploymentCreatedInDirect());
        existingEntity.setRepaymentAmount(request.getRepaymentAmount());
        existingEntity.setDateOfRepayment(DateUtil.stringToDate(request.getDateOfRepayment(), DATE_FORMAT));
        existingEntity.setIsUpiOrQrAvailable(request.getIsUpiOrQrAvailable());
        existingEntity.setOnlinePlatformUsed(request.getOnlinePlatformUsed());
        existingEntity.setDateOfGrounding(DateUtil.covertStringToDate(request.getDateOfGrounding()));
        existingEntity.setRevenue(request.getRevenue());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateIcSchemeFields(ICScheme existingEntity, ICSchemeRequest request) {
        existingEntity.setIndustryName(request.getIndustryName());
        existingEntity.setLocation(request.getLocation());
        existingEntity.setTypeOfMsme(request.getTypeOfMsme());
        existingEntity.setAnnualTurnover(request.getAnnualTurnover());
        existingEntity.setDomesticSales(request.getDomesticSales());
        existingEntity.setInvestment(request.getInvestment());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateNsicFields(NSIC existingEntity, NSICRequest request) {
        existingEntity.setGovtAgencyProcured(request.getGovtAgencyProcured());
        existingEntity.setDateOfProcurement(DateUtil.stringToDate(request.getDateOfProcurement(), DATE_FORMAT));
        existingEntity.setTypeOfProductSupplied(request.getTypeOfProductSupplied());
        existingEntity.setValueOfProcurement(request.getValueOfProcurement());
        existingEntity.setCostSavingsTender(request.getCostSavingsTender());
        existingEntity.setTypeOfProcurement(request.getTypeOfProcurement());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updatePatentsFields(Patents existingEntity, PatentsRequest request) {
        existingEntity.setNameOfPatent(request.getNameOfPatent());
        existingEntity.setTypeOfPatent(request.getTypeOfPatent());
        existingEntity.setPatentNumber(request.getPatentNumber());
        existingEntity.setPatentIssueDate(DateUtil.stringToDate(request.getPatentIssueDate(), DATE_FORMAT));
        existingEntity.setPatentCoverage(request.getPatentCoverage());
        existingEntity.setAnnualRevenue(request.getAnnualRevenue());
        existingEntity.setDateOfExport(DateUtil.stringToDate(request.getDateOfExport(), DATE_FORMAT));
        existingEntity.setValueOfExport(request.getValueOfExport());
        existingEntity.setCountryOfExport(request.getCountryOfExport());
        existingEntity.setTotalJobsCreated(request.getTotalJobsCreated());
        existingEntity.setNameOfAward(request.getNameOfAward());
        existingEntity.setDateOfAward(DateUtil.stringToDate(request.getDateOfAward(), DATE_FORMAT));
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateGiProductFields(GIProduct existingEntity, GIProductRequest request) {
        existingEntity.setCompanyName(request.getCompanyName());
        existingEntity.setLocation(request.getLocation());
        existingEntity.setIndustry(request.getIndustry());
        existingEntity.setGiProductName(request.getGiProductName());
        existingEntity.setGiRegistrationNumber(request.getGiRegistrationNumber());
        existingEntity.setDateOfGIRegistration(DateUtil.stringToDate(request.getDateOfGIRegistration(), DATE_FORMAT));
        existingEntity.setJurisdictionCovered(request.getJurisdictionCovered());
        existingEntity.setRevenueAfterGICertification(request.getRevenueAfterGICertification());
        existingEntity.setDateOfExport(DateUtil.stringToDate(request.getDateOfExport(), DATE_FORMAT));
        existingEntity.setValueOfExport(request.getValueOfExport());
        existingEntity.setCountryExported(request.getCountryExported());
        existingEntity.setRetailPartnership(request.getRetailPartnership());
        existingEntity.setValueOfSupply(request.getValueOfSupply());
        existingEntity.setDateOfSupply(DateUtil.stringToDate(request.getDateOfSupply(), DATE_FORMAT));
        existingEntity.setTotalJobsCreated(request.getTotalJobsCreated());
        existingEntity.setFranchiseOutletsOpened(request.getFranchiseOutletsOpened());
        existingEntity.setAnnualRoyaltyEarnings(request.getAnnualRoyaltyEarnings());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateBarcodeFields(Barcode existingEntity, BarcodeRequest request) {
        existingEntity.setTypeOfMarket(request.getTypeOfMarket());
        existingEntity.setBarCodeType(request.getBarCodeType());
        existingEntity.setGs1RegistrationNumber(request.getGs1RegistrationNumber());
        existingEntity.setBarCodeCoverage(request.getBarCodeCoverage());
        existingEntity.setRevenueFromBarCodeIntegration(request.getRevenueFromBarCodeIntegration());
        existingEntity.setOnlineMarketRegistered(request.getOnlineMarketRegistered());
        existingEntity.setDateOfRegistration(DateUtil.stringToDate(request.getDateOfRegistration(), DATE_FORMAT));
        existingEntity.setValueOfTransaction(request.getValueOfTransaction());
        existingEntity.setDateOfExport(DateUtil.stringToDate(request.getDateOfMarket(), DATE_FORMAT));
        existingEntity.setValueOfExport(request.getValueOfMarket());
        existingEntity.setCountryExported(request.getCountryMarket());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateTreadMarkFields(TreadMark existingEntity, TreadMarkRequest request) {
        existingEntity.setNameOfTradMark(request.getNameOfTradMark());
        existingEntity.setTrademarkClass(request.getTrademarkClass());
        existingEntity.setTradeMarkRegistrationNo(request.getTradeMarkRegistrationNo());
        existingEntity.setJurisdictionCovered(request.getJurisdictionCovered());
        existingEntity.setAnnualRevenueAfterRegistration(request.getAnnualRevenueAfterRegistration());
        existingEntity.setDateOfExport(DateUtil.stringToDate(request.getDateOfExport(), DATE_FORMAT));
        existingEntity.setValueOfExport(request.getValueOfExport());
        existingEntity.setCountryOfExport(request.getCountryOfExport());
        existingEntity.setRetailPartnership(request.getRetailPartnership());
        existingEntity.setValueOfSupply(request.getValueOfSupply());
        existingEntity.setDateOfSupply(DateUtil.stringToDate(request.getDateOfSupply(), DATE_FORMAT));
        existingEntity.setTotalJobsCreated(request.getTotalJobsCreated());
        existingEntity.setNoOfFranchiseOutletsOpened(request.getNoOfFranchiseOutletsOpened());
        existingEntity.setAnnualRoyaltyEarningsFromFranchise(request.getAnnualRoyaltyEarningsFromFranchise());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateLeanFields(Lean existingEntity, LeanRequest request) {
        existingEntity.setCertificationType(request.getCertificationType());
        existingEntity.setDateOfCertification(DateUtil.stringToDate(request.getDateOfCertification(), DATE_FORMAT));
        existingEntity.setIsLeanConsultantAppointed(request.getIsLeanConsultantAppointed());
        existingEntity.setDateOfAppointed(DateUtil.stringToDate(request.getDateOfAppointed(), DATE_FORMAT));
        existingEntity.setRawMaterialWastage(request.getRawMaterialWastage());
        existingEntity.setRawMaterialWastageUnits(request.getRawMaterialWastageUnits());
        existingEntity.setProductionOutput(request.getProductionOutput());
        existingEntity.setProductionOutputUnits(request.getProductionOutputUnits());
        existingEntity.setPowerUsage(request.getPowerUsage());
        existingEntity.setPowerUsageUnits(request.getPowerUsageUnits());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateZedCertificationFields(ZEDCertification existingEntity, ZEDCertificationRequest request) {
        existingEntity.setOwnerName(request.getOwnerName());
        existingEntity.setNicCode(request.getNicCode());
        existingEntity.setZedCertificationId(request.getZedCertificationId());
        existingEntity.setUnitAddress(request.getUnitAddress());
        existingEntity.setCertificationDate(DateUtil.stringToDate(request.getCertificationDate(), DATE_FORMAT));
        existingEntity.setZedCertificationType(request.getZedCertificationType());
        existingEntity.setTurnover(request.getTurnover());
        existingEntity.setEnergyConsumptionKwhHr(request.getEnergyConsumptionKwhHr());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateConsortiaTenderFields(ConsortiaTender existingEntity, ConsortiaTenderRequest request) {
        existingEntity.setProductOrServiceOffered(request.getProductOrServiceOffered());
        existingEntity.setConsortiaMemberType(request.getConsortiaMemberType());
        existingEntity.setDateOfJoiningConsortia(DateUtil.stringToDate(request.getDateOfJoiningConsortia(), DATE_FORMAT));
        existingEntity.setTenderParticipatedName(request.getTenderParticipatedName());
        existingEntity.setDepartmentTenderIssued(request.getDepartmentTenderIssued());
        existingEntity.setTenderId(request.getTenderId());
        existingEntity.setTenderValue(request.getTenderValue());
        existingEntity.setTenderOutcome(request.getTenderOutcome());
        existingEntity.setWorkOrderIssueDate(DateUtil.stringToDate(request.getWorkOrderIssueDate(), DATE_FORMAT));
        existingEntity.setIsOrderExecuted(request.getIsOrderExecuted());
        existingEntity.setChallengesFaced(request.getChallengesFaced());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateOemFields(OEM existingEntity, OEMRequest request) {
        existingEntity.setOemRegistrationDate(DateUtil.stringToDate(request.getOemRegistrationDate(), DATE_FORMAT));
        existingEntity.setOemRegistrationNumber(request.getOemRegistrationNumber());
        existingEntity.setOemTargeted(request.getOemTargeted());
        existingEntity.setOemVendorCode(request.getOemVendorCode());
        existingEntity.setProductsSupplied(request.getProductsSupplied());
        existingEntity.setVendorRegistrationDate(DateUtil.stringToDate(request.getVendorRegistrationDate(), DATE_FORMAT));
        existingEntity.setFirstPurchaseOrderDate(DateUtil.stringToDate(request.getFirstPurchaseOrderDate(), DATE_FORMAT));
        existingEntity.setFirstPOValue(request.getFirstPOValue());
        existingEntity.setCurrentMonthlySupplyValue(request.getCurrentMonthlySupplyValue());
        existingEntity.setIsCertificationStatus(request.getIsCertificationStatus());
        existingEntity.setMachineryUpGradation(request.getMachineryUpGradation());
        existingEntity.setOemAuditScore(request.getOemAuditScore());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updatePmfmeSchemeFields(PMFMEScheme existingEntity, PMFMESchemeRequest request) {
        existingEntity.setDateOfApplicationSubmission(DateUtil.stringToDate(request.getDateOfApplicationSubmission(), DATE_FORMAT));
        existingEntity.setLoanSanctioned(request.getLoanSanctioned());
        existingEntity.setGrantReceived(request.getGrantReceived());
        existingEntity.setWorkingCapitalAvailed(request.getWorkingCapitalAvailed());
        existingEntity.setDateOfApprovalUnderPMFME(DateUtil.stringToDate(request.getDateOfApprovalUnderPMFME(), DATE_FORMAT));
        existingEntity.setIsCommonFacilityCentreUsed(request.getIsCommonFacilityCentreUsed());
        existingEntity.setIsBrandingMarketingSupportAvailed(request.getIsBrandingMarketingSupportAvailed());
        existingEntity.setSupportDetails(request.getSupportDetails());
        existingEntity.setProductionCapacity(request.getProductionCapacityPerHour());
        existingEntity.setIsCertificationSupportAvailed(request.getIsCertificationSupportAvailed());
        existingEntity.setDateOfMarketLinkage(DateUtil.stringToDate(request.getDateOfMarketLinkage(), DATE_FORMAT));
        existingEntity.setVolumeOfMarketLinkage(request.getVolumeOfMarketLinkage());
        existingEntity.setUnits(request.getUnits());
        existingEntity.setValueOfMarketLinkage(request.getValueOfMarketLinkage());
        existingEntity.setMonthlyTurnover(request.getMonthlyTurnover());
        existingEntity.setTurnoverChange(request.getTurnoverChange());
        existingEntity.setProductionCapacityChange(request.getProductionCapacityChange());
        existingEntity.setBrandingOrMarketingSupportChange(request.getBrandingOrMarketingSupportChange());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updatePmViswakarmaFields(PMViswakarma existingEntity, PMViswakarmaRequest request) {
        existingEntity.setArtisanCategory(request.getArtisanCategory());
        existingEntity.setDateOfTraining(DateUtil.stringToDate(request.getDateOfTraining(), DATE_FORMAT));
        existingEntity.setCertificateIssueDate(DateUtil.stringToDate(request.getCertificateIssueDate(), DATE_FORMAT));
        existingEntity.setDateOfCreditAvailed(DateUtil.stringToDate(request.getDateOfCreditAvailed(), DATE_FORMAT));
        existingEntity.setAmountOfCreditAvailed(request.getAmountOfCreditAvailed());
        existingEntity.setPurposeOfUtilisation(request.getPurposeOfUtilisation());
        existingEntity.setMonthlyIncomeAfterCredit(request.getMonthlyIncomeAfterCredit());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateImportSubsititutionFields(ImportSubsititution existingEntity, ImportSubsititutionRequest request) {
        existingEntity.setSectorName(request.getSectorName());
        existingEntity.setProductName(request.getProductName());
        existingEntity.setPrototypeSelected(request.getIsPrototypeSelected());
        existingEntity.setBusinessPlanSubmissionDate(DateUtil.stringToDate(request.getBusinessPlanSubmissionDate(), DATE_FORMAT));
        existingEntity.setAmountSanctionedDate(DateUtil.stringToDate(request.getAmountSanctionedDate(), DATE_FORMAT));
        existingEntity.setAmountReleasedDate(DateUtil.stringToDate(request.getAmountReleasedDate(), DATE_FORMAT));
        existingEntity.setAmountReleasedInLakhs(request.getAmountReleasedInLakhs());
        existingEntity.setBankProvidedLoan(request.getBankProvidedLoan());
        existingEntity.setGroundingDate(DateUtil.stringToDate(request.getGroundingDate(), DATE_FORMAT));
        existingEntity.setMonthlyTurnoverInLakhs(request.getMonthlyTurnoverInLakhs());
        existingEntity.setMarketOfProduct(request.getIsMarketOfProduct());
        existingEntity.setMarketDate(DateUtil.stringToDate(request.getMarketDate(), DATE_FORMAT));
        existingEntity.setMarketValueInLakhs(request.getMarketValueInLakhs());
        existingEntity.setMarketVolumeInMts(request.getMarketVolumeInMts());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateSkillUpgradationFields(SkillUpgradation existingEntity, SkillUpgradationRequest request) {
        existingEntity.setTypeOfTrainingReceived(request.getTypeOfTrainingReceived());
        existingEntity.setTrainingCompletionDate(DateUtil.stringToDate(request.getTrainingCompletionDate(), DATE_FORMAT));
        existingEntity.setBusinessPlanSubmissionDate(DateUtil.stringToDate(request.getBusinessPlanSubmissionDate(), DATE_FORMAT));
        existingEntity.setAmountSanctionedDate(DateUtil.stringToDate(request.getAmountSanctionedDate(), DATE_FORMAT));
        existingEntity.setAmountReleasedDate(DateUtil.stringToDate(request.getAmountReleasedDate(), DATE_FORMAT));
        existingEntity.setAmountReleasedInLakhs(request.getAmountReleasedInLakhs());
        existingEntity.setBankProvidedLoan(request.getBankProvidedLoan());
        existingEntity.setLoanType(request.getLoanType());
        existingEntity.setLoanPurpose(request.getLoanPurpose());
        existingEntity.setGroundingDate(DateUtil.stringToDate(request.getGroundingDate(), DATE_FORMAT));
        existingEntity.setSectorType(request.getSectorType());
        existingEntity.setMonthlyTurnoverInLakhs(request.getMonthlyTurnoverInLakhs());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateExportPromotionFields(ExportPromotion existingEntity, ExportPromotionRequest request) {
        existingEntity.setSectorName(request.getSectorName());
        existingEntity.setProductName(request.getProductName());
        existingEntity.setExportImportLicenceNo(request.getExportImportLicenceNo());
        existingEntity.setMappingWithInternationalBuyer(request.getIsMappingWithInternationalBuyer());
        existingEntity.setMonthlyTurnoverInLakhs(request.getMonthlyTurnoverInLakhs());
        existingEntity.setIsExport(request.getIsExport());
        existingEntity.setExportDate(DateUtil.stringToDate(request.getExportDate(), DATE_FORMAT));
        existingEntity.setExportValueInLakhs(request.getExportValueInLakhs());
        existingEntity.setExportVolumeInMts(request.getExportVolumeInMts());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateVendorDevelopmentFields(VendorDevelopment existingEntity, VendorDevelopmentRequest request) {
        existingEntity.setDateOfParticipation(DateUtil.stringToDate(request.getDateOfParticipation(), DATE_FORMAT));
        existingEntity.setVdpProgramName(request.getVdpProgramName());
        existingEntity.setProductShowcased(request.getProductShowcased());
        existingEntity.setNameOfBuyersInterested(request.getNameOfBuyersInterested());
        existingEntity.setVendorRegisteredWith(request.getVendorRegisteredWith());
        existingEntity.setIseProcurementRegistered(request.getIseProcurementRegistered());
        existingEntity.setPortalName(request.getPortalName());
        existingEntity.setIsDigitalCatalogCreated(request.getIsDigitalCatalogCreated());
        existingEntity.setDateOfSupply(DateUtil.stringToDate(request.getDateOfSupply(), DATE_FORMAT));
        existingEntity.setVolumeOfSupply(request.getVolumeOfSupply());
        existingEntity.setUnits(request.getUnits());
        existingEntity.setValueOfSupply(request.getValueOfSupply());
        existingEntity.setMonthlyTurnover(request.getMonthlyTurnover());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateScStHubFields(ScStHub existingEntity, ScStHubRequest request) {
        existingEntity.setSupportAvailedUnderNSSH(request.getSupportAvailedUnderNSSH());
        existingEntity.setTrainingName(request.getTrainingName());
        existingEntity.setTrainingCompletedDate(DateUtil.stringToDate(request.getTrainingCompletedDate(), DATE_FORMAT));
        existingEntity.setCertificationName(request.getCertificationName());
        existingEntity.setCertificationReceivedDate(DateUtil.stringToDate(request.getCertificationReceivedDate(), DATE_FORMAT));
        existingEntity.setMarketLinkageCompanyName(request.getMarketLinkageCompanyName());
        existingEntity.setMarketLinkageDate(DateUtil.stringToDate(request.getMarketLinkageDate(), DATE_FORMAT));
        existingEntity.setMarketLinkageValue(request.getMarketLinkageValue());
        existingEntity.setMarketLinkageVolume(request.getMarketLinkageVolume());
        existingEntity.setVendorRegistrationWithPSUOrOEM(request.getVendorRegistrationWithPSUOrOEM());
        existingEntity.setTenderParticipatedName(request.getTenderParticipatedName());
        existingEntity.setHandholdingAgency(request.getHandholdingAgency());
        existingEntity.setCreditLinkageDate(DateUtil.stringToDate(request.getCreditLinkageDate(), DATE_FORMAT));
        existingEntity.setCreditLinkageAmount(request.getCreditLinkageAmount());
        existingEntity.setMonthlyRevenue(request.getMonthlyRevenue());
        existingEntity.setKeyChallengesFaced(request.getKeyChallengesFaced());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateSidbiAspireFields(SIDBIAspire existingEntity, SIDBIAspireRequest request) {
        existingEntity.setApplicationSubmissionDate(DateUtil.stringToDate(request.getApplicationSubmissionDate(), DATE_FORMAT));
        existingEntity.setDateSanctionUnderAspire(DateUtil.stringToDate(request.getDateSanctionUnderAspire(), DATE_FORMAT));
        existingEntity.setFundingSupportReceived(request.getIsFundingSupportReceived());
        existingEntity.setIncubationPartnerName(request.getIncubationPartnerName());
        existingEntity.setFundingType(request.getFundingType());
        existingEntity.setSupportAmount(request.getSupportAmount());
        existingEntity.setMachinerySetupDate(DateUtil.stringToDate(request.getMachinerySetupDate(), DATE_FORMAT));
        existingEntity.setProductionStartedDate(DateUtil.stringToDate(request.getProductionStartedDate(), DATE_FORMAT));
        existingEntity.setProductionUnits(request.getProductionUnits());
        existingEntity.setProduction(request.getProduction());
        existingEntity.setMarketLinkageEnabled(request.getIsMarketLinkageEnabled());
        existingEntity.setMarketLinkageDate(DateUtil.stringToDate(request.getMarketLinkageDate(), DATE_FORMAT));
        existingEntity.setMarketLinkageVolume(request.getMarketLinkageVolume());
        existingEntity.setMarketLinkageValue(request.getMarketLinkageValue());
        existingEntity.setTurnover(request.getTurnover());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateDesignRightsFields(DesignRights existingEntity, DesignRightsRequest request) {
        existingEntity.setDateOfApplication(DateUtil.stringToDate(request.getDateOfApplication(), DATE_FORMAT));
        existingEntity.setDateOfDesignRightsGranted(DateUtil.stringToDate(request.getDateOfDesignRightsGranted(), DATE_FORMAT));
        existingEntity.setCertificationNumber(request.getCertificationNumber());
        existingEntity.setTypeOfDesignRegistered(request.getTypeOfDesignRegistered());
        existingEntity.setRevenueFromDesignProducts(request.getRevenueFromDesignProducts());
        existingEntity.setIsAwardedForDesignProtection(request.getIsAwardedForDesignProtection());
        existingEntity.setDateOfAwarded(DateUtil.stringToDate(request.getDateOfAwarded(), DATE_FORMAT));
        existingEntity.setNameOfAward(request.getNameOfAward());
        existingEntity.setDateOfExport(DateUtil.stringToDate(request.getDateOfExport(), DATE_FORMAT));
        existingEntity.setValueOfExport(request.getValueOfExport());
        existingEntity.setVolumeOfExport(request.getVolumeOfExport());
        existingEntity.setUnits(request.getUnits());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateCopyRightsFields(CopyRights existingEntity, CopyRightsRequest request) {
        existingEntity.setDateOfApplicationFiled(DateUtil.stringToDate(request.getDateOfApplicationFiled(), DATE_FORMAT));
        existingEntity.setTypeOfIntellectualWorkRegistered(request.getTypeOfIntellectualWorkRegistered());
        existingEntity.setRegistrationCertificateReceivedDate(DateUtil.stringToDate(request.getRegistrationCertificateReceivedDate(), DATE_FORMAT));
        existingEntity.setRegistrationCertificateNumber(request.getRegistrationCertificateNumber());
        existingEntity.setNumberOfProductsProtected(request.getNumberOfProductsProtected());
        existingEntity.setNameOfProductProtected(request.getNameOfProductProtected());
        existingEntity.setRevenueFromCopyrightedMaterial(request.getRevenueFromCopyrightedMaterial());
        existingEntity.setMarketValueAfterCopyright(request.getMarketValueAfterCopyright());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateGreeningOfMSMEFields(GreeningOfMSME existingEntity, GreeningOfMSMERequest request) {
        existingEntity.setTypeOfIntervention(request.getTypeOfIntervention());
        existingEntity.setTypeOfPrototypeProposed(request.getTypeOfPrototypeProposed());
        existingEntity.setTypeOfTrainingsReceived(String.join(",", request.getTypeOfTrainingReceived()));
        existingEntity.setTrainingCompletionDate(DateUtil.stringToDate(request.getTrainingCompletionDate(), DATE_FORMAT));
        existingEntity.setBusinessPlanSubmissionDate(DateUtil.stringToDate(request.getBusinessPlanSubmissionDate(), DATE_FORMAT));
        existingEntity.setAmountSanctionedDate(DateUtil.stringToDate(request.getAmountSanctionedDate(), DATE_FORMAT));
        existingEntity.setAmountReleasedDate(DateUtil.stringToDate(request.getAmountReleasedDate(), DATE_FORMAT));
        existingEntity.setAmountReleased(request.getAmountReleased());
        existingEntity.setNameOfBankProvidedLoan(request.getNameOfBankProvidedLoan());
        existingEntity.setDateOfGrounding(DateUtil.stringToDate(request.getDateOfGrounding(), DATE_FORMAT));
        existingEntity.setPurposeOfLoanUtilised(request.getPurposeOfLoanUtilised());
        existingEntity.setParameter1(request.getParameter1());
        existingEntity.setParameter2(request.getParameter2());
        existingEntity.setParameter1Value(request.getParameter1Value());
        existingEntity.setParameter1Units(request.getParameter1Units());
        existingEntity.setParameter2Value(request.getParameter2Value());
        existingEntity.setParameter2Units(request.getParameter2Units());
        existingEntity.setProductionPerHour(request.getProductionPerHour());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateECommerceRegistrationFields(ECommerceRegistration existingEntity, ECommerceRegistrationRequest request) {
        existingEntity.setPlatformName(request.getPlatformName());
        existingEntity.setDateOfOnboarding(DateUtil.covertStringToDate(request.getDateOfOnboarding()));
        existingEntity.setRegistrationDetails(request.getRegistrationDetails());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }

    public static void updateECommerceTransactionFields(ECommerceTransaction existingEntity, ECommerceTransactionRequest request) {
        existingEntity.setFromDate(DateUtil.covertStringToDate(request.getFromDate()));
        existingEntity.setToDate(DateUtil.covertStringToDate(request.getToDate()));
        existingEntity.setNumberOfTransactions(request.getNumberOfTransactions());
        existingEntity.setTotalBusinessAmount(request.getTotalBusinessAmount());
        // Skip: ecommerceRegistration
    }

    public static void updateLoanFields(Loan existingEntity, LoanRequest request) {
        existingEntity.setBankName(request.getBankName());
        existingEntity.setLoanAmount(request.getLoanAmount());
        existingEntity.setDateOfFirstDisbursement(DateUtil.covertStringToDate(request.getDateOfFirstDisbursement()));
        existingEntity.setDisbursementAmount(request.getDisbursementAmount());
        // Skip: isInfluenced, agency, participant, organization, influencedParticipant
    }
}