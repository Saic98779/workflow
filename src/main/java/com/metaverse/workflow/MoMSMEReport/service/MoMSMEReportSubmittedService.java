package com.metaverse.workflow.MoMSMEReport.service;


import com.metaverse.workflow.MoMSMEReport.repository.MoMSMEReportRepo;
import com.metaverse.workflow.MoMSMEReport.repository.MoMSMEReportSubmittedRepo;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.MoMSMEReport;
import com.metaverse.workflow.model.MoMSMEReportSubmitted;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MoMSMEReportSubmittedService {
    private final MoMSMEReportSubmittedRepo submittedRepository;
    private final MoMSMEReportRepo moMSMEReportRepo;

    public WorkflowResponse saveReport(MoMSMEReportSubmittedDto dto) throws DataException {

        MoMSMEReport moMSMEReport=moMSMEReportRepo.findById(dto.getMoMSMEActivityId())
                .orElseThrow(() -> new DataException("MoMSME Report not found", "MOMSME-REPORT-DATA-NOT-FOUND", 400));
        MoMSMEReportSubmitted entity = MoMSMEReportSubmittedMapper.toEntity(dto,moMSMEReport);
        MoMSMEReportSubmitted saved = submittedRepository.save(entity);
        return WorkflowResponse.builder().data(MoMSMEReportSubmittedMapper.toDTO(saved))
                .message("Success").status(200)
                .build();
    }

    public WorkflowResponse getById(Long id) throws DataException {
        MoMSMEReportSubmittedDto dto = submittedRepository.findById(id)
                .map(MoMSMEReportSubmittedMapper::toDTO)
                .orElseThrow(() -> new DataException("MoMSMEReport not found with id " + id,"MO-MSME_REPORT_NOT_FOUND ",400));

        return WorkflowResponse.builder()
                .status(200)
                .message("Submission retrieved successfully.")
                .data(dto)
                .build();
    }

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

}
