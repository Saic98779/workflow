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
                .proposeOfEnterpriseUpgradation(request.getProposeOfEnterpriseUpgradation())
                .dateOfGrounding(DateUtil.stringToDate(request.getDateOfGrounding(), DATE_FORMAT))
                .isInfluenced(request.getIsInfluenced())
                .agency(agency)
                .organization(organization)
                .participant(participant)
                .build();
    }


    public static GeMTransaction mapGeMTransaction(GeMTransactionRequest request, Agency agency, Participant participant, Organization organization) {
        return GeMTransaction.builder()
                .procurementDate(DateUtil.stringToDate(request.getProcurementDate(), DATE_FORMAT))
                .productName(request.getProductName())
                .unitOfMeasurement(request.getUnitOfMeasurement())
                .registeredAs(request.getRegisteredAs())
                .quantity(request.getQuantity())
                .productValue(request.getProductValue())
                .isInfluenced(request.getIsInfluenced())
                .agency(agency)
                .organization(organization)
                .participant(participant)
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
                .upiOrQrCode(request.getUpiOrQrCode())
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
                .dateOfGrant(DateUtil.stringToDate(request.getDateOfGrant(), DATE_FORMAT))
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
                .nameOfTradeMark(request.getNameOfTradeMark())
                .trademarkClass(request.getTrademarkClass())
                .tradeMarkRegistrationNo(request.getTradeMarkRegistrationNo())
                .jurisdictionCovered(request.getJurisdictionCovered())
                .annualRevenueAfterRegistration(request.getAnnualRevenueAfterRegistration())
                .dateOfExport(DateUtil.stringToDate(request.getDateOfExport(),DATE_FORMAT))
                .valueOfExport(request.getValueOfExport())
                .countryOfExport(request.getCountryOfExport())
                .retailPartnership(request.getRetailPartnership())
                .valueOfSupply(request.getValueOfSupply())
                .dateOfSupply(DateUtil.stringToDate(request.getDateOfSupply(),DATE_FORMAT))
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
                .zedCertificationType(request.getZedCertificationType())
                .certificationNumber(request.getCertificationNumber())
                .dateOfCertification(DateUtil.stringToDate(request.getDateOfCertification(),DATE_FORMAT))
                .validUpto(DateUtil.stringToDate(request.getValidUpto(),DATE_FORMAT))
                .productionRate(request.getProductionRate())
                .defectRate(request.getDefectRate())
                .rawMaterialWastage(request.getRawMaterialWastage())
                .energyConsumption(request.getEnergyConsumption())
                .dateOfExport(DateUtil.stringToDate(request.getDateOfExport(),DATE_FORMAT))
                .valueOfExport(request.getValueOfExport())
                .volumeOfExport(request.getValueOfExport())
                .units(request.getUnits())
                .annualTurnover(request.getAnnualTurnover())
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


    }
