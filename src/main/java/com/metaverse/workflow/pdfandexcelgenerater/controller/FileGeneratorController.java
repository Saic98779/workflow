package com.metaverse.workflow.pdfandexcelgenerater.controller;

import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.pdfandexcelgenerater.service.*;
import com.metaverse.workflow.program.repository.ProgramRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
public class FileGeneratorController {

    @Autowired
    ExpenditureExcelGenerator expenditureExcelGenerator;
    @Autowired
    ProgramPdfGenerator programPdfGenerator;
    @Autowired
    ProgramExcelGenerator programExcelGenerator;
    @Autowired
    ProgramSummeryPdfGenerator programSummeryPdfGenerator;
    @Autowired
    ProgramSummeryExcelGenerator programSummeryExcelGenerator;
    @Autowired
    AttendancePDFGenerator attendancePDFGenerator;
    @Autowired
    SessionPDFGenerator sessionPDFGenerator;
    @Autowired
    ProgramRepository programRepository;
    @Autowired
    ParticipantsPDFGenerator participantsPDFGenerator;
    @Autowired
    LocationExcelGenerator locationExcelGenerator;
    @Autowired
    ResourceExcelGenerator resourceExcelGenerator;
    @Autowired
    OrganizationExcelGenerator organizationExcelGenerator;
    
    @GetMapping(value = "/program/pdf/{agencyId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> generatePdfReport(HttpServletResponse response,@PathVariable Long agencyId) throws IOException {
        ByteArrayInputStream bis = programPdfGenerator.generateProgramsPdf(response,agencyId );
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=users.pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));

    }

    @GetMapping(value = "/program/session/pdf/{programId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> generateSessionPdfReport( @PathVariable Long programId) throws IOException {
        if (!programRepository.existsById(programId)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Program with ID " + programId + " not found.");
        }
        ByteArrayInputStream bis = sessionPDFGenerator.generateProgramSessionsPdf(programId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=users.pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));

    }
    @GetMapping(value = "/program/attendance/pdf/{programId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> generateAttendancePdfReport( @PathVariable Long programId) throws IOException {
        if (!programRepository.existsById(programId)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Program with ID " + programId + " not found.");
        }
        ByteArrayInputStream bis = attendancePDFGenerator.programAttendancePDF(programId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=users.pdf");
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
        headers.add("Content-Disposition", "inline; filename=users.pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));

    }


    @GetMapping("/program/excel")
    public void generateExcelReport(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=users.xls");
        programExcelGenerator.generateProgramsExcel(response);
    }
    @GetMapping("/organization/excel")
    public void generateOrganizationExcelReport(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=users.xls");
        organizationExcelGenerator.exportOrganizationsToExcel(response);
    }

    @GetMapping("/location/excel/{agencyId}")
    public void generateLocationExcelReport(HttpServletResponse response,@PathVariable Long agencyId) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=users.xls");
        locationExcelGenerator.locationsExportToExcel(response,agencyId);
    }
    @GetMapping("/resource/excel/{agencyId}")
    public void generateResourceExcelReport(HttpServletResponse response,@PathVariable Long agencyId) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=users.xls");
        resourceExcelGenerator.exportAgencyResourcesToExcel(response,agencyId);
    }

    @GetMapping(value = "/program/summary/pdf/{programId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> generateProgramSummeryPDF(@PathVariable Long programId, HttpServletResponse response) {

        ByteArrayInputStream bis ;
        try {
            bis = programSummeryPdfGenerator.generateProgramsSummeryPdf(response, programId);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=users.pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));

    }

    @GetMapping("/program/summery/excel/{programId}")
    public void generateProgramSummeryExcel(@PathVariable Long programId, HttpServletResponse response) throws IOException, DataException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=users.xls");
        programSummeryExcelGenerator.generateProgramsExcel(response, programId);

    }

    @GetMapping("/export-program-expenditure")
    public void exportProgramExpenditure(HttpServletResponse response,
                                         @RequestParam Long programId, @RequestParam Long agencyId) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + "program_expenditure_" + programId + ".xls");

        expenditureExcelGenerator.generateProgramsExcel(response, programId, agencyId);
    }



}
