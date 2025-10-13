package com.metaverse.workflow.MoMSMEReport.service;


import com.metaverse.workflow.MoMSMEReport.repository.MoMSMEReportRepo;
import com.metaverse.workflow.MoMSMEReport.repository.MoMSMEReportSubmittedRepository;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.MoMSMEReport;
import com.metaverse.workflow.model.MoMSMEReportSubmitted;
import com.metaverse.workflow.model.MoMSMEReportSubmittedMonthly;
import com.metaverse.workflow.model.MoMSMEReportSubmittedQuarterly;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MoMSMEReportSubmittedService {
    private final MoMSMEReportSubmittedRepository submittedRepository;
    private final MoMSMEReportRepo moMSMEReportRepo;
    private final com.metaverse.workflow.MoMSMEReport.repository.MoMSMEReportSubmittedMonthlyRepository monthlyRepo;
    private final com.metaverse.workflow.MoMSMEReport.repository.MoMSMEReportSubmittedQuarterlyRepository quarterlyRepo;

    @Transactional
    public WorkflowResponse saveReport(MoMSMEReportSubmittedDto dto) throws DataException {

        // Step 1: Validate MSME Report reference
        MoMSMEReport moMSMEReport = moMSMEReportRepo.findById(dto.getMoMSMEActivityId())
                .orElseThrow(() -> new DataException("MoMSME Report not found", "MOMSME-REPORT-DATA-NOT-FOUND", 400));

        // Step 2: Get or create parent record
        MoMSMEReportSubmitted parent = submittedRepository.findByFinancialYearAndMoMSMEReport_MoMSMEActivityId(
                        dto.getFinancialYear(), dto.getMoMSMEActivityId())
                .orElse(MoMSMEReportSubmitted.builder()
                        .financialYear(dto.getFinancialYear())
                        .moMSMEReport(moMSMEReport)
                        .build());

        MoMSMEReportSubmitted savedParent = submittedRepository.save(parent);

        // Step 3: Save or update Monthly record
        MoMSMEReportSubmittedMonthly monthly = monthlyRepo
                .findByMoMSMEReportSubmitted_SubmittedIdAndMonth(savedParent.getSubmittedId(), dto.getMonth())
                .orElse(MoMSMEReportSubmittedMonthly.builder()
                        .moMSMEReportSubmitted(savedParent)
                        .moMSMEReport(moMSMEReport)
                        .month(dto.getMonth())
                        .build());

        monthly.setPhysicalAchievement(dto.getPhysicalAchievement());
        monthly.setFinancialAchievement(dto.getFinancialAchievement());
        monthly.setTotal(dto.getTotal());
        monthly.setWomen(dto.getWomen());
        monthly.setSc(dto.getSc());
        monthly.setSt(dto.getSt());
        monthly.setObc(dto.getObc());

        monthlyRepo.save(monthly);

        String quarter = getQuarterForMonth(dto.getMonth());

        List<MoMSMEReportSubmittedMonthly> quarterlyMonths =
                monthlyRepo.findByMoMSMEReportSubmitted_SubmittedId(savedParent.getSubmittedId())
                        .stream()
                        .filter(m -> getQuarterForMonth(m.getMonth()).equals(quarter))
                        .toList();

        double totalPhysical = quarterlyMonths.stream()
                .mapToDouble(m -> m.getPhysicalAchievement() != null ? m.getPhysicalAchievement() : 0)
                .sum();

        double totalFinancial = quarterlyMonths.stream()
                .mapToDouble(m -> m.getFinancialAchievement() != null ? m.getFinancialAchievement() : 0)
                .sum();

        int totalBeneficiaries = quarterlyMonths.stream()
                .mapToInt(m -> m.getTotal() != null ? m.getTotal() : 0)
                .sum();

        int totalWomen = quarterlyMonths.stream()
                .mapToInt(m -> m.getWomen() != null ? m.getWomen() : 0)
                .sum();

        int totalSc = quarterlyMonths.stream()
                .mapToInt(m -> m.getSc() != null ? m.getSc() : 0)
                .sum();

        int totalSt = quarterlyMonths.stream()
                .mapToInt(m -> m.getSt() != null ? m.getSt() : 0)
                .sum();

        int totalObc = quarterlyMonths.stream()
                .mapToInt(m -> m.getObc() != null ? m.getObc() : 0)
                .sum();

        MoMSMEReportSubmittedQuarterly quarterly = quarterlyRepo
                .findByMoMSMEReportSubmitted_SubmittedIdAndQuarter(savedParent.getSubmittedId(), quarter)
                .orElse(MoMSMEReportSubmittedQuarterly.builder()
                        .moMSMEReportSubmitted(savedParent)
                        .moMSMEReport(moMSMEReport)
                        .quarter(quarter)
                        .build());

        quarterly.setPhysicalAchievement(totalPhysical);
        quarterly.setFinancialAchievement(totalFinancial);
        quarterly.setTotal(totalBeneficiaries);
        quarterly.setWomen(totalWomen);
        quarterly.setSc(totalSc);
        quarterly.setSt(totalSt);
        quarterly.setObc(totalObc);

        MoMSMEReportSubmittedQuarterly savedQuarterly = quarterlyRepo.save(quarterly);

        savedParent.setQuarterlyReport(savedQuarterly);
        submittedRepository.save(savedParent);

        return WorkflowResponse.builder()
                .data(MoMSMEReportSubmittedMapper.toDTO(savedParent))
                .message("Monthly and Quarterly data updated successfully")
                .status(200)
                .build();
    }

    public WorkflowResponse getById(Long id) throws DataException { MoMSMEReportSubmittedDto dto = submittedRepository.findById(id) .map(MoMSMEReportSubmittedMapper::toDTO) .orElseThrow(() -> new DataException("MoMSMEReport not found with id " + id,"MO-MSME_REPORT_NOT_FOUND ",400)); return WorkflowResponse.builder() .status(200) .message("Submission retrieved successfully.") .data(dto) .build(); }

    public WorkflowResponse getMonthlyReport(Long moMSMEActivityId, String financialYear, String month) throws DataException {

        // Step 1: Fetch monthly report entity
        Optional<MoMSMEReport> report = Optional.ofNullable(moMSMEReportRepo.findById(moMSMEActivityId)
                .orElseThrow(() -> new DataException(
                        "MoMSME Report not found for the given criteria",
                        "MO-MSME_REPORT_NOT_FOUND",
                        400)));


        if (report.isEmpty()) {
            throw new DataException(
                    "MoMSME Report not found for the given criteria",
                    "MO-MSME_REPORT_NOT_FOUND",
                    400);
        }

        // Step 2: Map entity to DTO (DTO already includes quarterly and monthly achievements)
        MoMSMEReportDto dto = MoMSMEReportSubmittedMapper.MoMSMEReportMapper.toDTOReport(report.get(), month, financialYear);

        return WorkflowResponse.builder()
                .status(200)
                .message("Monthly report retrieved successfully with quarterly achievements.")
                .data(dto)
                .build();
    }

    public WorkflowResponse getMonthlyReportByQuarter(Long moMSMEActivityId, String financialYear, String quarter) throws DataException {
        Optional<MoMSMEReport> entity = Optional.ofNullable(moMSMEReportRepo.findById(moMSMEActivityId)
                .orElseThrow(() -> new DataException(
                        "MoMSME Report not found for the given criteria",
                        "MO-MSME_REPORT_NOT_FOUND",
                        400)));

        MoMSMEReportDto dto = MoMSMEReportSubmittedMapper.MoMSMEReportMapper.toDTOReportByQuarter(entity.get(), quarter, financialYear);

        return WorkflowResponse.builder()
                .status(200)
                .message("Quarterly report retrieved successfully with quarterly achievements.")
                .data(dto)
                .build();
    }

    public WorkflowResponse getCumulativeReport(Long moMSMEActivityId, String financialYear) throws DataException {
        Optional<MoMSMEReport> entity = Optional.ofNullable(moMSMEReportRepo.findById(moMSMEActivityId)
                .orElseThrow(() -> new DataException(
                        "MoMSME Report not found for the given criteria",
                        "MO-MSME_REPORT_NOT_FOUND",
                        400)));

        // Map to DTO with cumulative data
        MoMSMEReportDto dto = MoMSMEReportSubmittedMapper.MoMSMEReportMapper.toDTOReportByCumulative(entity.get(), financialYear);

        return WorkflowResponse.builder()
                .status(200)
                .message("Cumulative report generated successfully.")
                .data(dto)
                .build();
    }

    public WorkflowResponse getAllIntervention() throws DataException {
        List<MoMSMEReport> moMSMEReports = moMSMEReportRepo.findAll();

        if (moMSMEReports.isEmpty()) {
            throw new DataException("Intervention not found", "INTERVENTION_NOT_FOUND", 400);
        }

        // Map only id and name (use HashMap to avoid type inference issues)
        List<Map<String, Object>> interventions = moMSMEReports.stream()
                .map(report -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("moMSMEActivityId", report.getMoMSMEActivityId());
                    map.put("intervention", report.getIntervention());
                    return map;
                })
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Interventions retrieved successfully.")
                .data(interventions)
                .build();
    }

    private String getQuarterForMonth(String month) {
        if (month == null) return null;
        return switch (month.toLowerCase()) {
            case "april", "may", "june" -> "Q1";
            case "july", "august", "september" -> "Q2";
            case "october", "november", "december" -> "Q3";
            case "january", "february", "march" -> "Q4";
            default -> throw new IllegalArgumentException("Invalid month: " + month);
        };
    }


}
