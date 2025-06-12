package com.metaverse.workflow.programoutcome.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.outcomes.*;
import com.metaverse.workflow.programoutcome.dto.*;

public class OutcomeRequestMapper {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static ONDCRegistration mapOndcRegistration(ONDCRegistrationRequest request, Agency agency, Participant participant, Organization organization) {
        return ONDCRegistration.builder()
                .ondcRegistrationNo(request.getOndcRegistrationNo())
                .ondcRegistrationDate(DateUtil.stringToDate(request.getOndcRegistrationDate(), DATE_FORMAT))
                .isInfluenced(request.getIsInfluenced())
                .agency(agency)
                .organization(organization)
                .participant(participant)
                .build();
    }

    public static ONDCTransaction mapOndcTransaction(ONDCTransactionRequest request, ONDCRegistration ondcRegistration) {
        return ONDCTransaction.builder()
                .ondcRegistration(ondcRegistration)
                .transactionDate(DateUtil.stringToDate(request.getTransactionDate(), DATE_FORMAT))
                .transactionType(request.getTransactionType())
                .transactionValue(request.getTransactionValue())
                .productName(request.getProductName())
                .productUnits(request.getProductUnits())
                .productQuantity(request.getProductQuantity())
                .build();
    }

    public static UdyamRegistration mapUdyamRegistration(UdyamRegistrationRequest request, Agency agency, Participant participant, Organization organization) {
        return UdyamRegistration.builder()
                .udyamRegistationDate(DateUtil.stringToDate(request.getUdyamRegistrationDate(), DATE_FORMAT))
                .udyamRegistrationNo(request.getUdyamRegistrationNo())
                .isInfluenced(request.getIsInfluenced())
                .agency(agency)
                .organization(organization)
                .participant(participant)
                .build();
    }

    public static CGTMSETransaction mapCGTMSETransaction(CGTMSETransactionRequest request, Agency agency, Participant participant, Organization organization) {
        return CGTMSETransaction.builder()
                .creditApplicationDate(DateUtil.stringToDate(request.getCreditApplicationDate(), DATE_FORMAT))
                .dprSubmissionDate(DateUtil.stringToDate(request.getDprSubmissionDate(), DATE_FORMAT))
                .approvalDate(DateUtil.stringToDate(request.getApprovalDate(), DATE_FORMAT))
                .amountReleaseDate(DateUtil.stringToDate(request.getAmountReleaseDate(), DATE_FORMAT))
                .valueReleased(request.getValueReleased())
                .creditGuaranteePercentage(request.getCreditGuaranteePercentage())
                .purpose(request.getPurpose())
                .groundingDate(DateUtil.stringToDate(request.getGroundingDate(), DATE_FORMAT))
                .productName(request.getProductName())
                .productionPerMonth(request.getProductionPerMonth())
                .marketType(request.getMarketType())
                .marketingDate(DateUtil.stringToDate(request.getMarketingDate(), DATE_FORMAT))
                .marketVolume(request.getMarketVolume())
                .marketValue(request.getMarketValue())
                .totalTurnover(request.getTotalTurnover())
                .employmentMale(request.getEmploymentMale())
                .employmentFemale(request.getEmploymentFemale())
                .proposeOfEnterpriseUpgradation(request.getProposeOfEnterpriseUpGradation())
                .isInfluenced(request.getIsInfluenced())
                .agency(agency)
                .organization(organization)
                .participant(participant)
                .build();
    }

    public static GeMRegistration mapGeMRegistration(GeMRegistrationRequest request, Agency agency, Participant participant, Organization organization) {
        return GeMRegistration.builder()
                .gemRegistrationDate(DateUtil.stringToDate(request.getGemRegistrationDate(), DATE_FORMAT))
                .gemRegistrationId(request.getGemRegistrationId())
                .isInfluenced(request.getIsInfluenced())
                .agency(agency)
                .organization(organization)
                .participant(participant)
                .build();
    }


    public static GeMTransaction mapGeMTransaction(GeMTransactionRequest request, GeMRegistration geMRegistration) {
        return GeMTransaction.builder()
                .procurementDate(DateUtil.stringToDate(request.getProcurementDate(), DATE_FORMAT))
                .productName(request.getProductName())
                .unitOfMeasurement(request.getUnitOfMeasurement())
                .registeredAs(request.getRegisteredAs())
                .quantity(request.getQuantity())
                .productValue(request.getProductValue())
                .gemRegistration(geMRegistration)
                .build();
    }

    public static TReDSRegistration mapTredsRegistration(TReDSRegistrationRequest request, Agency agency, Participant participant, Organization organization) {
        return TReDSRegistration.builder()
                .tredsRegistrationNo(request.getTrdseRegistrationNo())
                .tredsRegistrationDate(DateUtil.stringToDate(request.getTredsRegistrationDate(), DATE_FORMAT))
                .isInfluenced(request.getIsInfluenced())
                .agency(agency)
                .organization(organization)
                .participant(participant)
                .build();
    }

    public static TReDSTransaction mapTredsTransaction(TReDSTransactionRequest request, TReDSRegistration tredsRegistration) {
        return TReDSTransaction.builder()
                .tredsRegistration(tredsRegistration)
                .tredsTransactionDate(DateUtil.stringToDate(request.getTredsTransactionDate(), DATE_FORMAT))
                .invoiceNumber(request.getInvoiceNumber())
                .buyerName(request.getBuyerName())
                .tredsPlatformUsed(request.getTredsPlatformUsed())
                .invoiceAmount(request.getInvoiceAmount())
                .bidOpeningDate(DateUtil.stringToDate(request.getBidOpeningDate(), DATE_FORMAT))
                .winnerFinancier(request.getWinnerFinancier())
                .discountRateOffered(request.getDiscountRateOffered())
                .discountingFeeFor60Days(request.getDiscountingFeeFor60Days())
                .finalPayoutToMsme(request.getFinalPayoutToMsme())
                .paymentSettlementDate(DateUtil.stringToDate(request.getPaymentSettlementDate(), DATE_FORMAT))
                .buyerDueDateToPay(DateUtil.stringToDate(request.getBuyerDueDateToPay(), DATE_FORMAT))
                .repaymentDate(DateUtil.stringToDate(request.getRepaymentDate(), DATE_FORMAT))
                .build();
    }

    public static PMEGP mapPmegp(PMEGPRequest request, Agency agency, Participant participant, Organization organization) {
        return PMEGP.builder()
                .loanSanctionedDate(DateUtil.stringToDate(request.getLoanSanctionedDate(), DATE_FORMAT))
                .loanAmountReleased(request.getLoanAmountReleased())
                .govtSubsidy(request.getGovtSubsidy())
                .beneficiaryContribution(request.getBeneficiaryContribution())
                .totalAmountReleased(request.getTotalAmountReleased())
                .groundingDate(DateUtil.stringToDate(request.getGroundingDate(), DATE_FORMAT))
                .businessTurnover(request.getBusinessTurnover())
                .dateOfMarketLinkage(DateUtil.stringToDate(request.getDateOfMarketLinkage(), DATE_FORMAT))
                .volumeOfMarket(request.getVolumeOfMarket())
                .units(request.getUnits())
                .valueOfMarket(request.getValueOfMarket())
                .nameOfProductMarket(request.getNameOfProductMarket())
                .numberOfPersonsEmployed(request.getNumberOfPersonsEmployed())
                .isInfluenced(request.getIsInfluenced())
                .agency(agency)
                .participant(participant)
                .organization(organization)
                .build();
    }

    public static PMMY mapPmmy(PMMYRequest request, Agency agency, Participant participant, Organization organization) {
        return PMMY.builder()
                .loanAmountReleased(request.getLoanAmountReleased())
                .loanSanctionedDate(DateUtil.stringToDate(request.getLoanSanctionedDate(), DATE_FORMAT))
                .groundingDate(DateUtil.stringToDate(request.getGroundingDate(), DATE_FORMAT))
                .businessTurnover(request.getBusinessTurnover())
                .marketLinkageDate(DateUtil.stringToDate(request.getMarketLinkageDate(), DATE_FORMAT))
                .marketVolume(request.getMarketVolume())
                .units(request.getUnits())
                .marketValue(request.getMarketValue())
                .productMarketedName(request.getProductMarketedName())
                .isInfluenced(request.getIsInfluenced())
                .agency(agency)
                .participant(participant)
                .organization(organization)
                .build();
    }

    public static PMS mapPms(PMSRequest request, Agency agency, Participant participant, Organization organization) {
        return PMS.builder()
                .businessTurnover(request.getBusinessTurnover())
                .loanNumber(request.getLoanNumber())
                .purposeOfLoan(request.getPurposeOfLoan())
                .amountOfLoanReleased(request.getAmountOfLoanReleased())
                .dateOfLoanReleased(DateUtil.stringToDate(request.dateOfLoanReleased, DATE_FORMAT))
                .employmentCreatedDirect(request.getEmploymentCreatedDirect())
                .employmentCreatedInDirect(request.getEmploymentCreatedInDirect())
                .repaymentAmount(request.getRepaymentAmount())
                .dateOfRepayment(DateUtil.stringToDate(request.getDateOfRepayment(), DATE_FORMAT))
                .isUpiOrQrAvailable(request.getIsUpiOrQrAvailable())
                .onlinePlatformUsed(request.getOnlinePlatformUsed())
                .isInfluenced(request.getIsInfluenced())
                .agency(agency)
                .participant(participant)
                .organization(organization)
                .build();
    }

    public static ICScheme mapIcScheme(ICSchemeRequest request, Agency agency, Participant participant, Organization organization) {
        return ICScheme.builder()
                .industryName(request.getIndustryName())
                .location(request.getLocation())
                .typeOfMsme(request.getTypeOfMsme())
                .annualTurnover(request.getAnnualTurnover())
                .domesticSales(request.getDomesticSales())
                .exportMarket(request.getExportMarket())
                .employmentDirect(request.getEmploymentDirect())
                .employmentIndirect(request.getEmploymentIndirect())
                .dateOfExport(request.getDateOfExport())
                .valueOfExport(request.getValueOfExport())
                .exportMarketDetails(request.getExportMarketDetails())
                .isInfluenced(request.getIsInfluenced())
                .agency(agency)
                .participant(participant)
                .organization(organization)
                .build();
    }

    public static NSIC mapNsic(NSICRequest request, Agency agency, Participant participant, Organization organization) {
        return NSIC.builder()
                .govtAgencyProcured(request.getGovtAgencyProcured())
                .dateOfProcurement(DateUtil.stringToDate(request.getDateOfProcurement(), DATE_FORMAT))
                .typeOfProductSupplied(request.getTypeOfProductSupplied())
                .valueOfProcurement(request.getValueOfProcurement())
                .costSavingsTender(request.getCostSavingsTender())
                .isInfluenced(request.getIsInfluenced())
                .agency(agency)
                .participant(participant)
                .organization(organization)
                .build();
    }

    public static Patents mapPatents(PatentsRequest request, Agency agency, Participant participant, Organization organization) {
        return Patents.builder()
                .nameOfPatent(request.getNameOfPatent())
                .typeOfPatent(request.getTypeOfPatent())
                .patentNumber(request.getPatentNumber())
                .patentIssueDate(DateUtil.stringToDate(request.getPatentIssueDate(), DATE_FORMAT))
                .patentCoverage(request.getPatentCoverage())
                .annualRevenue(request.getAnnualRevenue())
                .dateOfExport(DateUtil.stringToDate(request.getDateOfExport(), DATE_FORMAT))
                .valueOfExport(request.getValueOfExport())
                .countryOfExport(request.getCountryOfExport())
                .totalJobsCreated(request.getTotalJobsCreated())
                .nameOfAward(request.getNameOfAward())
                .dateOfAward(DateUtil.stringToDate(request.getDateOfAward(), DATE_FORMAT))
                .isInfluenced(request.getIsInfluenced())
                .agency(agency)
                .organization(organization)
                .participant(participant)
                .build();
    }

    public static GIProduct mapGIProduct(GIProductRequest request, Agency agency, Participant participant, Organization organization) {
        return GIProduct.builder()
                .companyName(request.getCompanyName())
                .location(request.getLocation())
                .industry(request.getIndustry())
                .giProductName(request.getGiProductName())
                .giRegistrationNumber(request.getGiRegistrationNumber())
                .dateOfGIRegistration(DateUtil.stringToDate(request.getDateOfGIRegistration(), DATE_FORMAT))
                .jurisdictionCovered(request.getJurisdictionCovered())
                .revenueAfterGICertification(request.getRevenueAfterGICertification())
                .dateOfExport(DateUtil.stringToDate(request.getDateOfExport(), DATE_FORMAT))
                .valueOfExport(request.getValueOfExport())
                .countryExported(request.getCountryExported())
                .retailPartnership(request.getRetailPartnership())
                .valueOfSupply(request.getValueOfSupply())
                .dateOfSupply(DateUtil.stringToDate(request.getDateOfSupply(), DATE_FORMAT))
                .totalJobsCreated(request.getTotalJobsCreated())
                .franchiseOutletsOpened(request.getFranchiseOutletsOpened())
                .annualRoyaltyEarnings(request.getAnnualRoyaltyEarnings())
                .isInfluenced(request.getIsInfluenced())
                .agency(agency)
                .organization(organization)
                .participant(participant)
                .build();
    }

    public static Barcode mapBarcode(BarcodeRequest request, Agency agency, Participant participant, Organization organization) {
        return Barcode.builder()
                .industry(request.getIndustry())
                .location(request.getLocation())
                .barCodeType(request.getBarCodeType())
                .gs1RegistrationNumber(request.getGs1RegistrationNumber())
                .barCodeCoverage(request.getBarCodeCoverage())
                .revenueFromBarCodeIntegration(request.getRevenueFromBarCodeIntegration())
                .onlineMarketRegistered(request.getOnlineMarketRegistered())
                .dateOfRegistration(DateUtil.stringToDate(request.getDateOfRegistration(), DATE_FORMAT))
                .valueOfTransaction(request.getValueOfTransaction())
                .dateOfExport(DateUtil.stringToDate(request.getDateOfExport(), DATE_FORMAT))
                .valueOfExport(request.getValueOfExport())
                .countryExported(request.getCountryExported())
                .isInfluenced(request.getIsInfluenced())
                .agency(agency)
                .participant(participant)
                .organization(organization)
                .build();
    }

    public static TreadMark mapTreadMark(TreadMarkRequest request, Agency agency, Participant participant, Organization organization) {
        return TreadMark.builder()
                .nameOfTradMark(request.getNameOfTradMark())
                .trademarkClass(request.getTrademarkClass())
                .tradeMarkRegistrationNo(request.getTradeMarkRegistrationNo())
                .jurisdictionCovered(request.getJurisdictionCovered())
                .annualRevenueAfterRegistration(request.getAnnualRevenueAfterRegistration())
                .dateOfExport(DateUtil.stringToDate(request.getDateOfExport(), DATE_FORMAT))
                .valueOfExport(request.getValueOfExport())
                .countryOfExport(request.getCountryOfExport())
                .retailPartnership(request.getRetailPartnership())
                .valueOfSupply(request.getValueOfSupply())
                .dateOfSupply(DateUtil.stringToDate(request.getDateOfSupply(), DATE_FORMAT))
                .totalJobsCreated(request.getTotalJobsCreated())
                .noOfFranchiseOutletsOpened(request.getNoOfFranchiseOutletsOpened())
                .annualRoyaltyEarningsFromFranchise(request.getAnnualRoyaltyEarningsFromFranchise())
                .isInfluenced(request.getIsInfluenced())
                .agency(agency)
                .participant(participant)
                .organization(organization)
                .build();

    }

    public static Lean mapLean(LeanRequest request, Agency agency, Participant participant, Organization organization) {
        return Lean.builder()
                .certificationType(request.getCertificationType())
                .dateOfCertification(DateUtil.stringToDate(request.getDateOfCertification(), DATE_FORMAT))
                .isLeanConsultantAppointed(request.getIsLeanConsultantAppointed())
                .dateOfAppointed(DateUtil.stringToDate(request.getDateOfAppointed(), DATE_FORMAT))
                .rawMaterialWastage(request.getRawMaterialWastage())
                .productionRate(request.getProductionRate())
                .defectRate(request.getDefectRate())
                .powerUsage(request.getPowerUsage())
                .isInfluenced(request.getIsInfluenced())
                .agency(agency)
                .participant(participant)
                .organization(organization)
                .build();
    }

    public static ZEDCertification mapZEDCertification(ZEDCertificationRequest request, Agency agency, Participant participant, Organization organization) {
        return ZEDCertification.builder()
                .machineryType(request.getMachineryType())
                .dprSubmissionDate(DateUtil.stringToDate(request.getDprSubmissionDate(), DATE_FORMAT))
                .amountReleasedDate(DateUtil.stringToDate(request.getAmountReleasedDate(), DATE_FORMAT))
                .releasedValue(request.getReleasedValue())
                .groundingDate(DateUtil.stringToDate(request.getGroundingDate(), DATE_FORMAT))
                .certificationDate(DateUtil.stringToDate(request.getCertificationDate(), DATE_FORMAT))
                .zedCertificationType(request.getZedCertificationType())
                .turnover(request.getTurnover())
                .energyConsumptionKwhHr(request.getEnergyConsumptionKwhHr())
                .productionMtHr(request.getProductionMtHr())
                .defectRatePer100Units(request.getDefectRatePer100Units())
                .isInfluenced(request.getIsInfluenced())
                .agency(agency)
                .participant(participant)
                .organization(organization)
                .build();

    }

    public static ConsortiaTender mapConsortiaTender(ConsortiaTenderRequest request, Agency agency, Participant participant, Organization organization) {
        return ConsortiaTender.builder()
                .productOrServiceOffered(request.getProductOrServiceOffered())
                .consortiaMemberType(request.getConsortiaMemberType())
                .dateOfJoiningConsortia(DateUtil.stringToDate(request.getDateOfJoiningConsortia(), DATE_FORMAT))
                .tenderParticipatedName(request.getTenderParticipatedName())
                .departmentTenderIssued(request.getDepartmentTenderIssued())
                .tenderId(request.getTenderId())
                .tenderValue(request.getTenderValue())
                .tenderOutcome(request.getTenderOutcome())
                .workOrderIssueDate(DateUtil.stringToDate(request.getWorkOrderIssueDate(), DATE_FORMAT))
                .isOrderExecuted(request.getIsOrderExecuted())
                .challengesFaced(request.getChallengesFaced())
                .isInfluenced(request.getIsInfluenced())
                .agency(agency)
                .participant(participant)
                .organization(organization)
                .build();
    }

    public static OEM mapOem(OEMRequest request, Agency agency, Participant participant, Organization organization) {

        return OEM.builder()
                .oemRegistrationDate(DateUtil.stringToDate(request.getOemRegistrationDate(), DATE_FORMAT))
                .oemRegistrationNumber(request.getOemRegistrationNumber())
                .oemTargeted(request.getOemTargeted())
                .oemVendorCode(request.getOemVendorCode())
                .productsSupplied(request.getProductsSupplied())
                .vendorRegistrationDate(DateUtil.stringToDate(request.getVendorRegistrationDate(), DATE_FORMAT))
                .firstPurchaseOrderDate(DateUtil.stringToDate(request.getFirstPurchaseOrderDate(), DATE_FORMAT))
                .firstPOValue(request.getFirstPOValue())
                .currentMonthlySupplyValue(request.getCurrentMonthlySupplyValue())
                .isCertificationStatus(request.getIsCertificationStatus())
                .machineryUpGradation(request.getMachineryUpGradation())
                .oemAuditScore(request.getOemAuditScore())
                .isInfluenced(request.getIsInfluenced())
                .participant(participant)
                .organization(organization)
                .agency(agency)
                .build();
    }

    public static PMFMEScheme mapPmfmseScheme(PMFMESchemeRequest request, Agency agency, Participant participant, Organization organization) {
        return PMFMEScheme.builder()
                .dateOfApplicationSubmission(DateUtil.stringToDate(request.getDateOfApplicationSubmission(), DATE_FORMAT))
                .loanSanctioned(request.getLoanSanctioned())
                .grantReceived(request.getGrantReceived())
                .workingCapitalAvailed(request.getWorkingCapitalAvailed())
                .dateOfApprovalUnderPMFME(DateUtil.stringToDate(request.getDateOfApprovalUnderPMFME(), DATE_FORMAT))
                .isCommonFacilityCentreUsed(request.getIsCommonFacilityCentreUsed())
                .isBrandingMarketingSupportAvailed(request.getIsBrandingMarketingSupportAvailed())
                .supportDetails(request.getSupportDetails())
                .productionCapacity(request.getProductionCapacity())
                .isCertificationSupportAvailed(request.getIsCertificationSupportAvailed())
                .dateOfMarketLinkage(DateUtil.stringToDate(request.getDateOfMarketLinkage(), DATE_FORMAT))
                .volumeOfMarketLinkage(request.getVolumeOfMarketLinkage())
                .units(request.getUnits())
                .valueOfMarketLinkage(request.getValueOfMarketLinkage())
                .monthlyTurnover(request.getMonthlyTurnover())
                .turnoverChange(request.getTurnoverChange())
                .productionCapacityChange(request.getProductionCapacityChange())
                .brandingOrMarketingSupportChange(request.getBrandingOrMarketingSupportChange())
                .isInfluenced(request.getIsInfluenced())
                .participant(participant)
                .organization(organization)
                .agency(agency)
                .build();
    }

    public static PMViswakarma mapPMViswakarma(PMViswakarmaRequest request, Agency agency, Participant participant, Organization organization) {
        return PMViswakarma.builder()
                .artisanCategory(request.getArtisanCategory())
                .dateOfTraining(DateUtil.stringToDate(request.getDateOfTraining(), DATE_FORMAT))
                .certificateIssueDate(DateUtil.stringToDate(request.getCertificateIssueDate(), DATE_FORMAT))
                .dateOfCreditAvailed(DateUtil.stringToDate(request.getDateOfCreditAvailed(), DATE_FORMAT))
                .amountOfCreditAvailed(request.getAmountOfCreditAvailed())
                .purposeOfUtilisation(request.getPurposeOfUtilisation())
                .monthlyIncomeAfterCredit(request.getMonthlyIncomeAfterCredit())
                .isInfluenced(request.getIsInfluenced())
                .participant(participant)
                .organization(organization)
                .agency(agency)
                .build();
    }


    public static VendorDevelopment mapVendorDevelopment(VendorDevelopmentRequest request, Agency agency, Participant participant, Organization organization) {
        return VendorDevelopment.builder()
                .dateOfParticipation(DateUtil.stringToDate(request.getDateOfParticipation(), DATE_FORMAT))
                .vdpProgramName(request.getVdpProgramName())
                .productShowcased(request.getProductShowcased())
                .nameOfBuyersInterested(request.getNameOfBuyersInterested())
                .vendorRegisteredWith(request.getVendorRegisteredWith())
                .iseProcurementRegistered(request.getIseProcurementRegistered())
                .portalName(request.getPortalName())
                .isDigitalCatalogCreated(request.getIsDigitalCatalogCreated())
                .dateOfSupply(DateUtil.stringToDate(request.getDateOfSupply(), DATE_FORMAT))
                .volumeOfSupply(request.getVolumeOfSupply())
                .units(request.getUnits())
                .valueOfSupply(request.getValueOfSupply())
                .monthlyTurnover(request.getMonthlyTurnover())
                .isInfluenced(request.getIsInfluenced())
                .participant(participant)
                .organization(organization)
                .agency(agency)
                .build();
    }

    public static ScStHub mapScStHub(ScStHubRequest request, Agency agency, Participant participant, Organization organization) {
        return ScStHub.builder()
                .supportAvailedUnderNSSH(request.getSupportAvailedUnderNSSH())
                .trainingName(request.getTrainingName())
                .trainingCompletedDate(DateUtil.stringToDate(request.getTrainingCompletedDate(), DATE_FORMAT))
                .certificationName(request.getCertificationName())
                .certificationReceivedDate(DateUtil.stringToDate(request.getCertificationReceivedDate(), DATE_FORMAT))
                .marketLinkageCompanyName(request.getMarketLinkageCompanyName())
                .marketLinkageDate(DateUtil.stringToDate(request.getMarketLinkageDate(), DATE_FORMAT))
                .marketLinkageValue(request.getMarketLinkageValue())
                .marketLinkageVolume(request.getMarketLinkageVolume())
                .vendorRegistrationWithPSUOrOEM(request.getVendorRegistrationWithPSUOrOEM())
                .tenderParticipatedName(request.getTenderParticipatedName())
                .handholdingAgency(request.getHandholdingAgency())
                .creditLinkageDate(DateUtil.stringToDate(request.getCreditLinkageDate(), DATE_FORMAT))
                .creditLinkageAmount(request.getCreditLinkageAmount())
                .monthlyRevenue(request.getMonthlyRevenue())
                .keyChallengesFaced(request.getKeyChallengesFaced())
                .isInfluenced(request.getIsInfluenced())
                .agency(agency)
                .participant(participant)
                .organization(organization)
                .build();
    }

    public static SIDBIAspire mapSidbiAspire(SIDBIAspireRequest dto, Agency agency, Participant participant, Organization organization) {
        return SIDBIAspire.builder()
                .applicationSubmissionDate(DateUtil.stringToDate(dto.getApplicationSubmissionDate(), DATE_FORMAT))
                .sanctionDateUnderAspire(DateUtil.stringToDate(dto.getSanctionDateUnderAspire(), DATE_FORMAT))
                .fundingSupportReceived(dto.getFundingSupportReceived())
                .incubationPartnerName(dto.getIncubationPartnerName())
                .fundingType(dto.getFundingType())
                .supportAmount(dto.getSupportAmount())
                .machinerySetupDate(DateUtil.stringToDate(dto.getMachinerySetupDate(), DATE_FORMAT))
                .productionStartedDate(DateUtil.stringToDate(dto.getProductionStartedDate(), DATE_FORMAT))
                .monthlyProductionUnits(dto.getMonthlyProductionUnits())
                .marketLinkageEnabled(dto.getMarketLinkageEnabled())
                .marketLinkageDate(DateUtil.stringToDate(dto.getMarketLinkageDate(), DATE_FORMAT))
                .marketLinkageVolume(dto.getMarketLinkageVolume())
                .marketLinkageValue(dto.getMarketLinkageValue())
                .monthlyTurnover(dto.getMonthlyTurnover())
                .isInfluenced(dto.getIsInfluenced())
                .agency(agency)
                .participant(participant)
                .organization(organization)
                .build();
    }

    public static DesignRights mapDesignRights(DesignRightsRequest dto, Agency agency, Participant participant, Organization organization) {
        return DesignRights.builder()
                .dateOfApplication(DateUtil.stringToDate(dto.getDateOfApplication(), DATE_FORMAT))
                .dateOfDesignRightsGranted(DateUtil.stringToDate(dto.getDateOfDesignRightsGranted(), DATE_FORMAT))
                .certificationNumber(dto.getCertificationNumber())
                .typeOfDesignRegistered(dto.getTypeOfDesignRegistered())
                .revenueFromDesignProducts(dto.getRevenueFromDesignProducts())
                .isAwardedForDesignProtection(dto.getIsAwardedForDesignProtection())
                .dateOfAwarded(DateUtil.stringToDate(dto.getDateOfAwarded(), DATE_FORMAT))
                .nameOfAward(dto.getNameOfAward())
                .dateOfExport(DateUtil.stringToDate(dto.getDateOfExport(), DATE_FORMAT))
                .valueOfExport(dto.getValueOfExport())
                .volumeOfExport(dto.getVolumeOfExport())
                .units(dto.getUnits())
                .isInfluenced(dto.getIsInfluenced())
                .agency(agency)
                .participant(participant)
                .organization(organization)
                .build();
    }

    public static CopyRights mapCopyRights(CopyRightsRequest request, Agency agency, Participant participant, Organization organization) {
        return CopyRights.builder()
                .dateOfApplicationFiled(DateUtil.stringToDate(request.getDateOfApplicationFiled(), DATE_FORMAT))
                .typeOfIntellectualWorkRegistered(request.getTypeOfIntellectualWorkRegistered())
                .registrationCertificateReceivedDate(DateUtil.stringToDate(request.getRegistrationCertificateReceivedDate(), DATE_FORMAT))
                .registrationCertificateNumber(request.getRegistrationCertificateNumber())
                .numberOfProductsProtected(request.getNumberOfProductsProtected())
                .nameOfProductProtected(request.getNameOfProductProtected())
                .revenueFromCopyrightedMaterial(request.getRevenueFromCopyrightedMaterial())
                .marketValueAfterCopyright(request.getMarketValueAfterCopyright())
                .isInfluenced(request.getIsInfluenced())
                .agency(agency)
                .participant(participant)
                .organization(organization)
                .build();
    }

    public static GreeningOfMSME mapGreeningOfMSME(GreeningOfMSMERequest request, Agency agency, Participant participant, Organization organization) {
        return GreeningOfMSME.builder()
                .typeOfIntervention(request.getTypeOfIntervention())
                .typeOfPrototypeProposed(request.getTypeOfPrototypeProposed())
                .typeOfTrainingsReceived(String.join(",", request.getTypeOfTrainingReceived()))
                .trainingCompletionDate(DateUtil.stringToDate(request.getTrainingCompletionDate(), DATE_FORMAT))
                .businessPlanSubmissionDate(DateUtil.stringToDate(request.getBusinessPlanSubmissionDate(), DATE_FORMAT))
                .amountSanctionedDate(DateUtil.stringToDate(request.getAmountSanctionedDate(), DATE_FORMAT))
                .amountReleasedDate(DateUtil.stringToDate(request.getAmountReleasedDate(), DATE_FORMAT))
                .amountReleased(request.getAmountReleased())
                .nameOfBankProvidedLoan(request.getNameOfBankProvidedLoan())
                .dateOfGrounding(DateUtil.stringToDate(request.getDateOfGrounding(), DATE_FORMAT))
                .purposeOfLoanUtilised(request.getPurposeOfLoanUtilised())
                .parameter1(request.getParameter1())
                .parameter2(request.getParameter2())
                .unitForMeasurementOfProduction(request.getUnitForMeasurementOfProduction())
                .productionPerHour(request.getProductionPerHour())
                .build();
    }

}
