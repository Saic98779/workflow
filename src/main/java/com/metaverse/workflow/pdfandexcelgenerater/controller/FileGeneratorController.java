package com.metaverse.workflow.pdfandexcelgenerater.controller;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.pdfandexcelgenerater.service.*;
import com.metaverse.workflow.program.repository.ProgramRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FileGeneratorController {

    private final ExpenditureExcelGenerator expenditureExcelGenerator;
    private final ProgramPdfGenerator programPdfGenerator;
    private final ProgramExcelGenerator programExcelGenerator;
    private final ProgramSummeryPdfGenerator programSummeryPdfGenerator;
    private final ProgramSummeryExcelGenerator programSummeryExcelGenerator;
    private final AttendancePDFGenerator attendancePDFGenerator;
    private final SessionPDFGenerator sessionPDFGenerator;
    private final ProgramRepository programRepository;
    private final ParticipantsPDFGenerator participantsPDFGenerator;
    private final LocationExcelGenerator locationExcelGenerator;
    private final ResourceExcelGenerator resourceExcelGenerator;
    private final OrganizationExcelGenerator organizationExcelGenerator;
    private final ProgramStatusGenerator programStatusGenerator;
    private final ProgramParticipantDetails programParticipantDetails;
    private final TrainingCalendarAgencywise trainingCalendarAgencywise;
    private final TrainingCalendarDistrictwise trainingCalendarDistrictwise;
    private final TrainingCalendarDatewise trainingCalendarDatewise;
    private final GenerateCombinedExpenditureExcel combinedExpenditureExcel;
    private final GenerateProgramAllDataExcel generateProgramAllDataExcel;
    private final GenerateProgramAllDataExcelSheets programAllDataExcelSheets;
    private final ParticipantTempExcel generateParticipantTempExcel;
    private final ProgramDetailsExcel programDetailsExcel;
    private final  ParticipantDetailsExcel participantDetailsExcel;
    private final ProgramMonitoringPDF programMonitoringPDF;

    @GetMapping(value = "/program/pdf/{agencyId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> generatePdfReport(HttpServletResponse response, @PathVariable Long agencyId) throws IOException {
        ByteArrayInputStream bis = programPdfGenerator.generateProgramsPdf(response, agencyId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=Program_Details.pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));

    }

    @GetMapping(value = "/program/session/pdf/{programId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> generateSessionPdfReport(@PathVariable Long programId) throws IOException {
        if (!programRepository.existsById(programId)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Program with ID " + programId + " not found.");
        }
        ByteArrayInputStream bis = sessionPDFGenerator.generateProgramSessionsPdf(programId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=Session_Details.pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));

    }

    @GetMapping(value = "/program/attendance/pdf/{programId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> generateAttendancePdfReport(@PathVariable Long programId) throws IOException {
        if (!programRepository.existsById(programId)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Program with ID " + programId + " not found.");
        }
        ByteArrayInputStream bis = attendancePDFGenerator.programAttendancePDF(programId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=Program_Attendance_Details.pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));

    }

    @GetMapping(value = "/program/participant/pdf/{programId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> generateParticipantPdfReport(@PathVariable Long programId) throws IOException {
        if (!programRepository.existsById(programId)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Program with ID " + programId + " not found.");
        }
        ByteArrayInputStream bis = participantsPDFGenerator.generateProgramParticipantPdf(programId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=Participant_Details.pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));

    }


    @GetMapping("/program/excel")
    public void generateExcelReport(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=Program_Details.xls");
        programExcelGenerator.generateProgramsExcel(response);
    }

    @GetMapping("/organization/excel")
    public void generateOrganizationExcelReport(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=Organization_Details.xls");
        organizationExcelGenerator.exportOrganizationsToExcel(response);
    }

    @GetMapping("/location/excel/{agencyId}")
    public void generateLocationExcelReport(HttpServletResponse response, @PathVariable Long agencyId) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=Location_Details.xls");
        locationExcelGenerator.locationsExportToExcel(response, agencyId);
    }

    @GetMapping("/resource/excel/{agencyId}")
    public void generateResourceExcelReport(HttpServletResponse response, @PathVariable Long agencyId) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=Resource_Details.xls");
        resourceExcelGenerator.exportAgencyResourcesToExcel(response, agencyId);
    }

    @GetMapping(value = "/program/summary/pdf/{programId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> generateProgramSummeryPDF(@PathVariable Long programId, HttpServletResponse response) {

        ByteArrayInputStream bis;
        try {
            bis = programSummeryPdfGenerator.generateProgramsSummeryPdf(response, programId);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=Program_Summary_Details.pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));

    }

    @GetMapping("/program/summery/excel/{programId}")
    public void generateProgramSummeryExcel(@PathVariable Long programId, HttpServletResponse response) throws IOException, DataException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=Program_Summary_Details.xls");
        programSummeryExcelGenerator.generateProgramsExcel(response, programId);

    }

    @GetMapping("/export-program-expenditure")
    public void exportProgramExpenditure(HttpServletResponse response,
                                         @RequestParam Long programId, @RequestParam Long agencyId) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + "program_expenditure_" + programId + ".xls");

        expenditureExcelGenerator.generateProgramsExcel(response, programId, agencyId);
    }

    @GetMapping("/programs-status/{agencyId}")
    public void exportProgramStatus(HttpServletResponse response, @PathVariable Long agencyId) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + "Program_status.xls");

        programStatusGenerator.generateProgramsExcel(response, agencyId);
    }

    @GetMapping("/programs-participant-status/{agencyId}")
    public void exportProgramParticipantStatus(HttpServletResponse response, @PathVariable Long agencyId) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + "Program_Participant_status.xls");

        programParticipantDetails.generateProgramsParticipantExcel(response, agencyId);
    }

    @GetMapping("/trg/calender/agency-wise/{agencyId}")
    public void exportProgramTrgCalender(HttpServletResponse response, @PathVariable Long agencyId) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + "Trg_calender_agency_wise.xls");

        trainingCalendarAgencywise.generateProgramsExcel(response, agencyId);
    }

    @GetMapping("/trg/calender/district-wise")
    public void exportProgramTrgCalenderDistrict(HttpServletResponse response,
                                                 @RequestParam String district, @RequestParam Long agencyId) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + "Trg_calender_district_wise.xls");

        trainingCalendarDistrictwise.generateProgramsExcel(response, agencyId, district);
    }

    @GetMapping("/trg/calender/date-wise")
    public void exportProgramTrgCalenderDate(HttpServletResponse response,
                                             @RequestParam Long agencyId,
                                             @RequestParam String startDate,
                                             @RequestParam String endDate) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + "Trg_calender_date_wise.xls");

        trainingCalendarDatewise.generateProgramsExcel(response, agencyId, DateUtil.stringToDate(startDate, "dd-MM-yyyy"), DateUtil.stringToDate(endDate, "dd-MM-yyyy"));
    }

    @GetMapping("/combined/expenditure/excel/{programId}")
    public void exportExpenditure(HttpServletResponse response, @PathVariable Long programId) throws IOException, DataException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + "Total_Expenditure.xls");
        combinedExpenditureExcel.generateCombinedExpenditureExcel(response,programId);
    }
    @GetMapping("/program/excel/{agencyId}")
    public void exportProgramDetails(HttpServletResponse response, @PathVariable Long agencyId) throws IOException, DataException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + "Program_Details.xls");
        generateProgramAllDataExcel.generateProgramsExcel(response,agencyId);
    }

    @GetMapping("/program/excel/sheets")
    public void exportProgramDetailsExcelSheets(HttpServletResponse response) throws IOException, DataException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + "Program_Details.xls");
        programAllDataExcelSheets.generateProgramsExcel(response);
    }

    @GetMapping("/ParticipantTemp/excel/sheets/{programId}")
    public void exportParticipantTempDetailsExcelSheets(HttpServletResponse response,@PathVariable Long programId) throws IOException, DataException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Participant_Details.xlsx");
        generateParticipantTempExcel.generateParticipantTempExcel(response,programId);
    }

    @GetMapping("/program-details/excel/{agencyId}")
    public void generateProgramExcelReport(HttpServletResponse response, @PathVariable Long agencyId) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;fileName=Program_Details.xls");
        programDetailsExcel.generateProgramsExcel(response,agencyId);
    }

    @GetMapping("/participant-details/excel/")
    public void generateParticipantExcelReport(HttpServletResponse response,
                                               @RequestParam(required = false) Long agencyId,
                                               @RequestParam(required = false) Long programId) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=Participant_Details.xls");
        participantDetailsExcel.generateParticipantDetailsExcel(response,programId,agencyId);
    }

    @GetMapping("/programs-participant-status/pdf/{agencyId}")
    public void exportProgramsPdf(HttpServletResponse response,
                                  @PathVariable Long agencyId) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=programs_participants.pdf");

        programParticipantDetails.generateProgramsParticipantPdf(response, agencyId);
    }

    @GetMapping("/program-monitoring-report/pdf/{monitoringId}")
    public void exportProgramMonitoringPdf(HttpServletResponse response,
                                  @PathVariable Long monitoringId) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=program_monitoring_report.pdf");

        programMonitoringPDF.generateProgramsMonitoringPdf(response,monitoringId);
    }
}
