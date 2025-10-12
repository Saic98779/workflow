package com.metaverse.workflow.MoMSMEReport.controller;

import com.metaverse.workflow.MoMSMEReport.service.MoMSMEReportDto;
import com.metaverse.workflow.MoMSMEReport.service.MoMSMEReportSubmittedDto;
import com.metaverse.workflow.MoMSMEReport.service.MoMSMEReportSubmittedService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/momsme")
public class MoMSMEReportSubmittedController {
    private final MoMSMEReportSubmittedService service;

    @PostMapping("/submitted-save")
    public ResponseEntity<?> save(@RequestBody MoMSMEReportSubmittedDto dto) {
        try {
            WorkflowResponse response = service.saveReport(dto);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/submitted-get/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            WorkflowResponse response = service.getById(id);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/interventions")
    public ResponseEntity<?> getAllIntervention() {
        try {
            return ResponseEntity.ok(service.getAllIntervention());
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }

    }

    @Tag(name = "MoMSME Reports", description = "Endpoints for MoMSME Monthly Reports")
    @GetMapping("/report/by-month")
    @Operation(
            summary = "Get Monthly MoMSME Report",
            description = "Retrieves the monthly MoMSME report for a given activity, financial year, and month.",
            parameters = {
                    @Parameter(name = "moMSMEActivityId", description = "ID of the MoMSME activity", required = true),
                    @Parameter(name = "financialYear", description = "Financial year in format YYYY-YYYY", required = true),
                    @Parameter(name = "month", description = "Month name (e.g., january, february, march)", required = true)
            }
    )    public ResponseEntity<?> getMonthlyReport(@RequestParam Long moMSMEActivityId,
                                     @RequestParam String financialYear,
                                     @RequestParam String month) throws DataException {
        WorkflowResponse response = service.getMonthlyReport(moMSMEActivityId, financialYear, month);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/report/by-quarter")
    @Tag(name = "MoMSME Reports", description = "Endpoints for MoMSME Monthly Reports")
    @Operation(
            summary = "Get Monthly MoMSME Report by Quarter",
            description = "Retrieves the monthly and quarterly MoMSME report for a given activity, financial year, and quarter.",
            parameters = {
                    @Parameter(name = "moMSMEActivityId", description = "ID of the MoMSME activity", required = true),
                    @Parameter(name = "financialYear", description = "Financial year in format YYYY-YYYY", required = true),
                    @Parameter(name = "quarter", description = "Quarter (e.g., Q1, Q2, Q3, Q4)", required = true)
            }
    )
    public ResponseEntity<WorkflowResponse> getMonthlyReportByQuarter(
            @RequestParam Long moMSMEActivityId,
            @RequestParam String financialYear,
            @RequestParam String quarter
    ) throws DataException {

        WorkflowResponse response = service.getMonthlyReportByQuarter(moMSMEActivityId, financialYear, quarter);

        return ResponseEntity.ok(response);
    }

    @Tag(name = "MoMSME Reports", description = "Endpoints for MoMSME cumulative and quarterly reports")
    @GetMapping("/report/by-cumulative")
    @Operation(

            summary = "Get Cumulative MoMSME Report",
            description = "Retrieves the cumulative MoMSME report for the specified financial year and activity ID. "
                    + "This report aggregates all quarterly data (targets and achievements) across the full financial year.",
            parameters = {
                    @Parameter(
                            name = "moMSMEActivityId",
                            description = "Unique ID of the MoMSME activity",
                            required = true,
                            example = "1"
                    ),
                    @Parameter(
                            name = "financialYear",
                            description = "Financial year for which the cumulative report is generated (format: YYYY-YYYY)",
                            required = true,
                            example = "2024-2025"
                    )
            }
    )    public ResponseEntity<WorkflowResponse> getMonthlyReportByQuarter(
            @RequestParam Long moMSMEActivityId,
            @RequestParam String financialYear) throws DataException {

        WorkflowResponse dto = service.getCumulativeReport(moMSMEActivityId, financialYear);

        WorkflowResponse response = WorkflowResponse.builder()
                .status(200)
                .message("cumulative report retrieved successfully for financial year " + financialYear)
                .data(dto)
                .build();

        return ResponseEntity.ok(response);
    }
}