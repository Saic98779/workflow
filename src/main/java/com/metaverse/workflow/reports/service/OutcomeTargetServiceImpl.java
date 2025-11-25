package com.metaverse.workflow.reports.service;

import com.metaverse.workflow.model.PhysicalTarget;
import com.metaverse.workflow.programoutcome.repository.*;
import com.metaverse.workflow.programoutcometargets.repository.PhysicalRepository;
import com.metaverse.workflow.reports.dto.OutcomeTargetDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OutcomeTargetServiceImpl implements OutcomeTargetService {

    private final PhysicalRepository physicalTargetRepository;

    private final ONDCRegistrationRepository ondcRegistrationRepository;
    private final ONDCTransactionRepository ondcTransactionRepository;
    private final UdyamRegistrationRepository udyamRegistrationRepository;
    private final TReDSRegistrationRepository tredsRegistrationRepository;
    private final TReDSTransactionRepository tReDSTransactionRepository;
    private final ZEDCertificationRepository zedCertificationRepository;
    private final BarcodeRepository barcodeRepository;
    private final GIProductRepository giProductRepository;
    private final ICSchemeRepository icSchemeRepository;
    private final PatentsRepository patentsRepository;
    private final TreadMarkRepository treadMarkRepository;
    private final GeMRegistrationRepository geMRegistrationRepository;
    private final GeMTransactionRepository geMTransactionRepository;
    private final CGTMSETransactionRepository cgtmseTransactionRepository;
    private final ConsortiaTenderRepository consortiaTenderRepository;
    private final CopyRightsRepository copyRightsRepository;
    private final DesignRightsRepository designRightsRepository;
    private final NSICRepository nsicRepository;
    private final OEMRepository oemRepository;
    private final PMEGPRepository pmegpRepository;
    private final PMFMESchemeRepository pmfmeSchemeRepository;
    private final PMMYRepository pmmyRepository;
    private final VendorDevelopmentRepository vendorDevelopmentRepository;
    private final PMSRepository pmsRepository;
    private final LeanRepository leanRepository;
    private final PMViswakarmaReposiroty pmViswakarmaReposiroty;
    private final SIDBIAspireRepository sidbiAspireRepository;
    private final ECommerceTransactionRepository eCommerceTransactionRepository;
    private final ECommerceRegistrationRepository eCommerceRegistrationRepository;
    private final ExportPromotionRepository exportPromotionRepository;
    private final SkillUpgradationRepository skillUpgradationRepository;
    private final ImportSubsititutionRepository importSubsititutionRepository;
    private final LoanRepository loanRepository;
    private final GreeningOfMSMERepository greeningOfMSMERepository;
    private final ScStHubRepository scStHubRepository;

    @Override
    public List<OutcomeTargetDTO> getTargetsByYear(String financialYear, Long agencyId) {

        if (financialYear.equals("-1")) {
            return getAllYearsGrouped(agencyId);
        }

        return getQuarterly(financialYear, agencyId);
    }

    // ===================================================================================
    // 1) YEAR = -1 → GROUP BY OUTCOME + FINANCIAL YEAR (NO DUPLICATES)
    // ===================================================================================
    private List<OutcomeTargetDTO> getAllYearsGrouped(Long agencyId) {

        List<PhysicalTarget> targets = physicalTargetRepository.findByAgency_AgencyId(agencyId);

        Map<String, OutcomeTargetDTO> map = new LinkedHashMap<>();

        for (PhysicalTarget t : targets) {

            String outcome = t.getProgramOutcomeTable().getOutcomeTableName();
            String year = t.getFinancialYear();

            String key = outcome + "_" + year;

            int targetSum = t.getQ1() + t.getQ2() + t.getQ3() + t.getQ4();

            // If exists, add to previous target
            if (map.containsKey(key)) {
                OutcomeTargetDTO existing = map.get(key);
                existing.setTotalTarget(existing.getTotalTarget() + targetSum);
            }
            else {
                // Create fresh dto
                OutcomeTargetDTO dto = OutcomeTargetDTO.builder()
                        .outcomeName(outcome)
                        .financialYear(year)
                        .totalTarget(targetSum)
                        .totalAchieved(0)
                        .physicalTargetQ1(0)
                        .physicalTargetQ2(0)
                        .physicalTargetQ3(0)
                        .physicalTargetQ4(0)
                        .achievedQ1(0)
                        .achievedQ2(0)
                        .achievedQ3(0)
                        .achievedQ4(0)
                        .build();

                map.put(key, dto);
            }
        }

        // Set achievements (currently overall achievement)
        for (OutcomeTargetDTO dto : map.values()) {
            long achieved = countTotalAchieved(dto.getOutcomeName(), agencyId);
            dto.setTotalAchieved((int) achieved);
        }

        return new ArrayList<>(map.values());
    }

    // Achievements count (all years combined)
    private long countTotalAchieved(String outcome, Long agencyId) {
        switch (outcome) {
            case "ONDCRegistration": return ondcRegistrationRepository.countByAgency_AgencyId(agencyId);
            case "ONDCTransaction": return ondcTransactionRepository.countByAgency_AgencyId(agencyId);
            case "UdyamRegistration": return udyamRegistrationRepository.countByAgency_AgencyId(agencyId);
            case "TReDSRegistration": return tredsRegistrationRepository.countByAgency_AgencyId(agencyId);
            case "TReDSTransaction": return tReDSTransactionRepository.countByAgency_AgencyId(agencyId);
            case "ZEDCertification": return zedCertificationRepository.countByAgency_AgencyId(agencyId);
            case "Barcode": return barcodeRepository.countByAgency_AgencyId(agencyId);
            case "GIProduct": return giProductRepository.countByAgency_AgencyId(agencyId);
            case "ICScheme": return icSchemeRepository.countByAgency_AgencyId(agencyId);
            case "Patents": return patentsRepository.countByAgency_AgencyId(agencyId);
            case "TreadMark": return treadMarkRepository.countByAgency_AgencyId(agencyId);
            case "GeMRegistration": return geMRegistrationRepository.countByAgency_AgencyId(agencyId);
            case "GeMTransaction": return geMTransactionRepository.countByAgency_AgencyId(agencyId);
            case "CGTMSETransaction": return cgtmseTransactionRepository.countByAgency_AgencyId(agencyId);
            case "ConsortiaTender": return consortiaTenderRepository.countByAgency_AgencyId(agencyId);
            case "CopyRights": return copyRightsRepository.countByAgency_AgencyId(agencyId);
            case "DesignRights": return designRightsRepository.countByAgency_AgencyId(agencyId);
            case "NSIC": return nsicRepository.countByAgency_AgencyId(agencyId);
            case "OEM": return oemRepository.countByAgency_AgencyId(agencyId);
            case "PMEGP": return pmegpRepository.countByAgency_AgencyId(agencyId);
            case "PMFMEScheme": return pmfmeSchemeRepository.countByAgency_AgencyId(agencyId);
            case "PMMY": return pmmyRepository.countByAgency_AgencyId(agencyId);
            case "VendorDevelopment": return vendorDevelopmentRepository.countByAgency_AgencyId(agencyId);
            case "PMS": return pmsRepository.countByAgency_AgencyId(agencyId);
            case "Lean": return leanRepository.countByAgency_AgencyId(agencyId);
            case "PMViswakarma": return pmViswakarmaReposiroty.countByAgency_AgencyId(agencyId);
            case "SIDBIAspire": return sidbiAspireRepository.countByAgency_AgencyId(agencyId);
            case "ScStHub": return scStHubRepository.countByAgency_AgencyId(agencyId);
            case "ECommerceRegistration": return eCommerceRegistrationRepository.countByAgency_AgencyId(agencyId);
            case "ECommerceTransaction": return eCommerceTransactionRepository.countByAgency_AgencyId(agencyId);
            case "ExportPromotion": return exportPromotionRepository.countByAgency_AgencyId(agencyId);
            case "SkillUpgradation": return skillUpgradationRepository.countByAgency_AgencyId(agencyId);
            case "ImportSubsititution": return importSubsititutionRepository.countByAgency_AgencyId(agencyId);
            case "Loan": return loanRepository.countByAgency_AgencyId(agencyId);
            case "GreeningOfMSME": return greeningOfMSMERepository.countByAgency_AgencyId(agencyId);

            default: return 0;
        }
    }

    // ===================================================================================
    // 2) QUARTERLY LOGIC (FINANCIAL YEAR SPECIFIC)
    // ===================================================================================
    private List<OutcomeTargetDTO> getQuarterly(String financialYear, Long agencyId) {

        int fyStart = Integer.parseInt(financialYear.split("-")[0]);

        Date q1s = toDate(LocalDate.of(fyStart, 4, 1));
        Date q1e = toDate(LocalDate.of(fyStart, 6, 30));
        Date q2s = toDate(LocalDate.of(fyStart, 7, 1));
        Date q2e = toDate(LocalDate.of(fyStart, 9, 30));
        Date q3s = toDate(LocalDate.of(fyStart, 10, 1));
        Date q3e = toDate(LocalDate.of(fyStart, 12, 31));
        Date q4s = toDate(LocalDate.of(fyStart + 1, 1, 1));
        Date q4e = toDate(LocalDate.of(fyStart + 1, 3, 31));

        List<OutcomeTargetDTO> dtoList = new ArrayList<>();
        List<PhysicalTarget> targets = physicalTargetRepository.findByFinancialYearAndAgency_AgencyId(financialYear, agencyId);

        for (PhysicalTarget t : targets) {

            String name = t.getProgramOutcomeTable().getOutcomeTableName();

            long q1 = countQuarter(name, agencyId, q1s, q1e);
            long q2 = countQuarter(name, agencyId, q2s, q2e);
            long q3 = countQuarter(name, agencyId, q3s, q3e);
            long q4 = countQuarter(name, agencyId, q4s, q4e);

            dtoList.add(createOutcomeDto(name, financialYear, t, q1, q2, q3, q4));
        }

        return dtoList;
    }

    private long countQuarter(String outcome, Long agencyId, Date s, Date e) {
        switch (outcome) {
            case "ONDCRegistration": return ondcRegistrationRepository.countONDCRegistration(agencyId, s, e);
            case "ONDCTransaction": return ondcTransactionRepository.countONDCTransaction(agencyId, s, e);
            case "UdyamRegistration": return udyamRegistrationRepository.countUdyamRegistration(agencyId, s, e);
            case "TReDSRegistration": return tredsRegistrationRepository.countTReDSRegistration(agencyId, s, e);
            case "TReDSTransaction": return tReDSTransactionRepository.countTReDSTransaction(agencyId, s, e);
            case "ZEDCertification": return zedCertificationRepository.countZedCertification(agencyId, null, s, e);
            case "Barcode": return barcodeRepository.countBarcode(agencyId, s, e);
            case "GIProduct": return giProductRepository.countGIProduct(agencyId, s, e);
            case "ICScheme": return icSchemeRepository.countICScheme(agencyId, s, e);
            case "Patents": return patentsRepository.countPatents(agencyId, s, e);
            case "TreadMark": return treadMarkRepository.countTreadMark(agencyId, s, e);
            case "GeMRegistration": return geMRegistrationRepository.countGeMRegistration(agencyId, s, e);
            case "GeMTransaction": return geMTransactionRepository.countGeMTransaction(agencyId, s, e);
            case "CGTMSETransaction": return cgtmseTransactionRepository.countCGTMSETransaction(agencyId, s, e);
            case "ConsortiaTender": return consortiaTenderRepository.countConsortiaTender(agencyId, s, e);
            case "CopyRights": return copyRightsRepository.countCopyRights(agencyId, s, e);
            case "DesignRights": return designRightsRepository.countDesignRights(agencyId, s, e);
            case "NSIC": return nsicRepository.countNSIC(agencyId, s, e);
            case "OEM": return oemRepository.countOEM(agencyId, s, e);
            case "PMEGP": return pmegpRepository.countPMEGP(agencyId, s, e);
            case "PMFMEScheme": return pmfmeSchemeRepository.countPMFMEScheme(agencyId, s, e);
            case "PMMY": return pmmyRepository.countPMMY(agencyId, s, e);
            case "VendorDevelopment": return vendorDevelopmentRepository.countVendorDevelopment(agencyId, s, e);
            case "PMS": return pmsRepository.countPMS(agencyId, s, e);
            case "Lean": return leanRepository.countLean(agencyId, s, e);
            case "PMViswakarma": return pmViswakarmaReposiroty.countPMViswakarma(agencyId, s, e);
            case "SIDBIAspire": return sidbiAspireRepository.countSIDBIAspire(agencyId, s, e);
            case "ScStHub": return scStHubRepository.countScStHub(agencyId, s, e);
            case "ECommerceRegistration": return eCommerceRegistrationRepository.countECommerceRegistration(agencyId, s, e);
            case "ECommerceTransaction": return eCommerceTransactionRepository.countECommerceTransaction(agencyId, s, e);
            case "ExportPromotion": return exportPromotionRepository.countExportPromotion(agencyId, s, e);
            case "SkillUpgradation": return skillUpgradationRepository.countSkillUpgradation(agencyId, s, e);
            case "ImportSubsititution": return importSubsititutionRepository.countImportSubsititution(agencyId, s, e);
            case "Loan": return loanRepository.countLoan(agencyId, s, e);
            case "GreeningOfMSME": return greeningOfMSMERepository.countGreeningOfMSME(agencyId, s, e);
            default: return 0;
        }
    }

    private Date toDate(LocalDate d) {
        return Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private OutcomeTargetDTO createOutcomeDto(String name, String year, PhysicalTarget target,
                                              long q1, long q2, long q3, long q4) {

        return OutcomeTargetDTO.builder()
                .outcomeName(name)
                .financialYear(year)
                .physicalTargetQ1(target.getQ1())
                .physicalTargetQ2(target.getQ2())
                .physicalTargetQ3(target.getQ3())
                .physicalTargetQ4(target.getQ4())
                .achievedQ1((int) q1)
                .achievedQ2((int) q2)
                .achievedQ3((int) q3)
                .achievedQ4((int) q4)
                .totalTarget(target.getQ1() + target.getQ2() + target.getQ3() + target.getQ4())
                .totalAchieved((int) (q1 + q2 + q3 + q4))
                .build();
    }
}

