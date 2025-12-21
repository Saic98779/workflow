package com.metaverse.workflow.pdfandexcelgenerater.controller;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.pdfandexcelgenerater.service.*;
import com.metaverse.workflow.program.repository.ProgramRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FileGeneratorController {
    private final MoMSMEExcelGenerator moMSMEExcelGenerator;
    private final TrainingProgramExcelGenerator excelGenerator;
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
    private final ProgramStatusPdfGenerator programStatusPdfGenerator;
    private final TrainingTargetPreview trainingTargetPreview;

    @GetMapping(value = "/program/pdf/{agencyId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> generatePdfReport(HttpServletResponse response, @PathVariable Long agencyId) throws IOException {
        ByteArrayInputStream bis = programPdfGenerator.generateProgramsPdf(response, agencyId);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Program_Details.pdf");
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
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Session_Details.pdf");
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
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Program_Attendance_Details.pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));

    }

    @GetMapping(value = "/program/participant/pdf/{programId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> generateParticipantPdfReport(@PathVariable Long programId) throws IOException {
        if (!programRepository.existsById(programId)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(null);
        }
        ByteArrayInputStream bis = participantsPDFGenerator.generateProgramParticipantPdf(programId);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Participant_Details.pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }


    @GetMapping("/program/excel")
    public void generateExcelReport(HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;fileName=Program_Details.xls");
            programExcelGenerator.generateProgramsExcel(response);
        } catch (Exception e) {
            response.reset();
            response.setContentType("text/plain");
            response.getWriter().write("{\"error\":\"Excel generation failed : "+ e.getMessage()+"\"}");
        }
    }

    @GetMapping("/organization/excel")
    public void generateOrganizationExcelReport(HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;fileName=Organization_Details.xls");
            organizationExcelGenerator.exportOrganizationsToExcel(response);
        } catch (Exception e) {
        response.reset();
        response.setContentType("text/plain");
        response.getWriter().write("{\"error\":\"Excel generation failed : "+ e.getMessage()+"\"}");
    }
    }

    @GetMapping("/location/excel/{agencyId}")
    public void generateLocationExcelReport(HttpServletResponse response, @PathVariable Long agencyId) throws IOException {
        try{
        response.setContentType("application/vnd.ms-excel");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=Location_Details.xls");
        locationExcelGenerator.locationsExportToExcel(response, agencyId);
        } catch (Exception e) {
            response.reset();
            response.setContentType("text/plain");
            response.getWriter().write("{\"error\":\"Excel generation failed : "+ e.getMessage()+"\"}");
        }
    }

    @GetMapping("/resource/excel/{agencyId}")
    public void generateResourceExcelReport(HttpServletResponse response, @PathVariable Long agencyId) throws IOException {
        try{
        response.setContentType("application/vnd.ms-excel");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=Resource_Details.xls");
        resourceExcelGenerator.exportAgencyResourcesToExcel(response, agencyId);
        } catch (Exception e) {
            response.reset();
            response.setContentType("text/plain");
            response.getWriter().write("{\"error\":\"Excel generation failed : "+ e.getMessage()+"\"}");
        }
    }

    @GetMapping(value = "/program/summary/pdf/{programId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> generateProgramSummeryPDF(@PathVariable Long programId, HttpServletResponse response) {
        try {

            ByteArrayInputStream bis;
            try {
                bis = programSummeryPdfGenerator.generateProgramsSummeryPdf(response, programId);
            } catch (DataException e) {
                return ResponseEntity.status(400).body(e.getMessage());
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Program_Summary_Details.pdf");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/program/summery/excel/{programId}")
    public void generateProgramSummeryExcel(@PathVariable Long programId, HttpServletResponse response) throws IOException, DataException {

        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;fileName=Program_Summary_Details.xls");
            programSummeryExcelGenerator.generateProgramsExcel(response, programId);

        } catch (Exception e) {
            response.reset();
            response.setContentType("text/plain");
            response.getWriter().write("{\"error\":\"Excel generation failed : "+ e.getMessage()+"\"}");
        }
    }

    @GetMapping("/export-program-expenditure")
    public void exportProgramExpenditure(HttpServletResponse response,
                                         @RequestParam Long programId, @RequestParam Long agencyId) throws IOException {
    try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + "program_expenditure" + programId + ".xls");
            expenditureExcelGenerator.generateProgramsExcel(response, programId, agencyId);
    }catch (Exception e) {
        response.reset();
        response.setContentType("text/plain");
        response.getWriter().write("{\"error\":\"Excel generation failed : "+ e.getMessage()+"\"}");
        }
    }

    @GetMapping("/programs-status/{agencyId}")
    public void exportProgramStatus(HttpServletResponse response, @PathVariable Long agencyId) throws IOException {
        try{
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + "Program_status.xls");
        programStatusGenerator.generateProgramsExcel(response, agencyId);

        } catch (Exception e) {
            response.reset();
            response.setContentType("text/plain");
            response.getWriter().write("{\"error\":\"Excel generation failed : "+ e.getMessage()+"\"}");
        }
    }

    @GetMapping("/programs-participant-status/{agencyId}")
    public void exportProgramParticipantStatus(HttpServletResponse response, @PathVariable Long agencyId) throws IOException {
        try{
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + "Program_Participant_status.xls");

        programParticipantDetails.generateProgramsParticipantExcel(response, agencyId);
        } catch (Exception e) {
            response.reset();
            response.setContentType("text/plain");
            response.getWriter().write("{\"error\":\"Excel generation failed : "+ e.getMessage()+"\"}");
        }
    }

    @GetMapping("/trg/calender/agency-wise/{agencyId}")
    public void exportProgramTrgCalender(HttpServletResponse response, @PathVariable Long agencyId) throws IOException {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + "Trg_calender_agency_wise.xls");

            trainingCalendarAgencywise.generateProgramsExcel(response, agencyId);
        } catch (Exception e) {
            response.reset();
            response.setContentType("text/plain");
            response.getWriter().write("{\"error\":\"Excel generation failed : "+ e.getMessage()+"\"}");
        }
    }

    @GetMapping("/trg/calender/district-wise")
    public void exportProgramTrgCalenderDistrict(HttpServletResponse response,
                                                 @RequestParam String district, @RequestParam Long agencyId) throws IOException {
        try{
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + "Trg_calender_district_wise.xls");

        trainingCalendarDistrictwise.generateProgramsExcel(response, agencyId, district);
        } catch (Exception e) {
            response.reset();
            response.setContentType("text/plain");
            response.getWriter().write("{\"error\":\"Excel generation failed : "+ e.getMessage()+"\"}");
        }
    }

    @GetMapping("/trg/calender/date-wise")
    public void exportProgramTrgCalenderDate(HttpServletResponse response, @RequestParam Long agencyId, @RequestParam String startDate, @RequestParam String endDate) throws IOException {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + "Trg_calender_date_wise.xls");

            trainingCalendarDatewise.generateProgramsExcel(response, agencyId, DateUtil.stringToDate(startDate, "dd-MM-yyyy"), DateUtil.stringToDate(endDate, "dd-MM-yyyy"));
        } catch (Exception e) {
            response.reset();
            response.setContentType("text/plain");
            response.getWriter().write("{\"error\":\"Excel generation failed : "+ e.getMessage()+"\"}");
        }
    }

    @GetMapping("/combined/expenditure/excel/{programId}")
    public void exportExpenditure(HttpServletResponse response, @PathVariable Long programId) throws IOException, DataException {
        try{
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + "Total_Expenditure.xls");
        combinedExpenditureExcel.generateCombinedExpenditureExcel(response,programId);
        } catch (Exception e) {
            response.reset();
            response.setContentType("text/plain");
            response.getWriter().write("{\"error\":\"Excel generation failed : "+ e.getMessage()+"\"}");
        }
    }
    @GetMapping("/program/excel/{agencyId}")
    public void exportProgramDetails(HttpServletResponse response, @PathVariable Long agencyId) throws IOException, DataException {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + "Program_Details.xls");
            generateProgramAllDataExcel.generateProgramsExcel(response, agencyId);
        } catch (Exception e) {
            response.reset();
            response.setContentType("text/plain");
            response.getWriter().write("{\"error\":\"Excel generation failed : "+ e.getMessage()+"\"}");
        }
    }

    @GetMapping("/program/excel/sheets")
    public void exportProgramDetailsExcelSheets(HttpServletResponse response) throws IOException, DataException {
        try{
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + "Program_Details.xls");
        programAllDataExcelSheets.generateProgramsExcel(response);
        } catch (Exception e) {
            response.reset();
            response.setContentType("text/plain");
            response.getWriter().write("{\"error\":\"Excel generation failed : "+ e.getMessage()+"\"}");
        }
    }

    @GetMapping("/ParticipantTemp/excel/sheets/{programId}")
    public void exportParticipantTempDetailsExcelSheets(HttpServletResponse response,@PathVariable Long programId) throws IOException, DataException {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=Participant_Details.xlsx");
            generateParticipantTempExcel.generateParticipantTempExcel(response, programId);
        } catch (Exception e) {
            response.reset();
            response.setContentType("text/plain");
            response.getWriter().write("{\"error\":\"Excel generation failed : "+ e.getMessage()+"\"}");
        }
    }

    @GetMapping("/program-details/excel/{agencyId}")
    public void generateProgramExcelReport(HttpServletResponse response, @PathVariable Long agencyId) throws IOException {
        try {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;fileName=Program_Details.xls");
        programDetailsExcel.generateProgramsExcel(response,agencyId);
        } catch (Exception e) {
            response.reset();
            response.setContentType("text/plain");
            response.getWriter().write("{\"error\":\"Excel generation failed : "+ e.getMessage()+"\"}");
        }
    }

    @GetMapping("/participant-details/excel/")
    public void generateParticipantExcelReport(HttpServletResponse response,
                                               @RequestParam(required = false) Long agencyId,
                                               @RequestParam(required = false) Long programId) throws IOException {
        try {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=Participant_Details.xls");
        participantDetailsExcel.generateParticipantDetailsExcel(response,programId,agencyId);
        } catch (Exception e) {
            response.reset();
            response.setContentType("text/plain");
            response.getWriter().write("{\"error\":\"Excel generation failed : "+ e.getMessage()+"\"}");
        }
    }

    @GetMapping("/programs-participant-status/pdf/{agencyId}")
    public void exportProgramsPdf(HttpServletResponse response,
                                  @PathVariable Long agencyId) throws IOException {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=programs_participants.pdf");

            programParticipantDetails.generateProgramsParticipantPdf(response, agencyId);
        } catch (Exception e) {
            response.reset();
            response.setContentType("text/plain");
            response.getWriter().write("{\"error\":\"Excel generation failed : "+ e.getMessage()+"\"}");
        }
    }

    @GetMapping("/program-monitoring-report/pdf/{monitoringId}")
    public void exportProgramMonitoringPdf(HttpServletResponse response,
                                  @PathVariable Long monitoringId) throws IOException {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=program_monitoring_report.pdf");

            programMonitoringPDF.generateProgramsMonitoringPdf(response, monitoringId);
        } catch (Exception e) {
            response.reset();
            response.setContentType("text/plain");
            response.getWriter().write("{\"error\":\"Excel generation failed : "+ e.getMessage()+"\"}");
        }
    }
    @GetMapping("/programs-status/pdf/{agencyId}")
    public void exportProgramStatusPdf(HttpServletResponse response,
                                           @PathVariable Long agencyId) throws IOException {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=program_overview_report.pdf");

            programStatusPdfGenerator.generateProgramsPdf(response, agencyId);
        } catch (Exception e) {
            response.reset();
            response.setContentType("text/plain");
            response.getWriter().write("{\"error\":\"Excel generation failed : "+ e.getMessage()+"\"}");
        }
    }
    @GetMapping("/export/training-programs/{agencyId}")
    public void exportTrainingProgramsExcel(HttpServletResponse response, @PathVariable Long agencyId) throws IOException {
        try{
        response.setContentType("application/vnd.ms-excel");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=Training_Programs.xls"
        );
        excelGenerator.generateTrainingProgramExcel(response, agencyId);
        } catch (Exception e) {
            response.reset();
            response.setContentType("text/plain");
            response.getWriter().write("{\"error\":\"Excel generation failed : "+ e.getMessage()+"\"}");
        }
    }

    @GetMapping("/export/momsme/{agencyId}")
    public void exportMoMSMEReport(@PathVariable Long agencyId, HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=Momsme_Report.xls"
            );
            moMSMEExcelGenerator.generateExcel(agencyId,response);
        } catch (Exception e) {
            response.reset();
            response.setContentType("text/plain");
            response.getWriter().write("{\"error\":\"Excel generation failed : "+ e.getMessage()+"\"}");
        }
    }

    @PostMapping("/export/progress/excel")
    public void exportExcelReport(
            @RequestBody ReportRequest request,
            HttpServletResponse response) throws IOException {

        log.info("Excel export request received");

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=Agency_Report.xls");

        ServletOutputStream outputStream = response.getOutputStream();

        trainingTargetPreview.generateExcel(request, request.getLoginName(), outputStream);

        outputStream.flush();
        response.flushBuffer();

        log.info("Excel export response sent successfully");
    }
}
