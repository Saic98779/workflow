package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.model.Sector;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantDetailsExcel {
    private final  ParticipantRepository participantRepository;
    private final ParticipantExcelJdbcRepository repository;

    public List<ParticipantExcelDto> getExcelData() {
        return repository.fetchParticipantExcelData();
    }

    public void generateParticipantDetailsExcel(HttpServletResponse response, Long programId, Long agencyId) throws IOException {

        List<Participant> participantList= new ArrayList<>();
        if (programId != null) {
            participantList = participantRepository.findByProgramId(programId);
        } else if (agencyId != -1) {
            participantList = participantRepository.findByPrograms_Agency_AgencyId(agencyId);
        }

        if (programId == null && agencyId == -1) {
            generateExcelUsingJdbc(response);
            return;
        }

        System.out.println("Hello " + LocalDateTime.now());
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Participant Details");

        // Header style
        HSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        String[] headers = {
                "SNo", "Agency Name", "Program Name","Start Date","End Date", "District", "IsAspirant",
                "Organization Name","Organization Type", "Participant Name", "Gender", "Category",
                "Disability", "Aadhar No", "Participant MobileNo", "Participant Email", "Participant Designation",
                "Udyam Registration No","Sector's","Organization MobileNo","Organization Email","Owner Name","Owner MobileNo",
                "Owner Email"
        };

        HSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowIndex = 1;
        int serialNo = 1;

        for (Participant participant : participantList) {

            // Fetch programs relevant to this context
            List<Program> filteredPrograms = participant.getPrograms();

            // If programId is passed, only use that one program
            if (programId != null) {
                filteredPrograms = participant.getPrograms().stream()
                        .filter(p -> p.getProgramId().equals(programId))
                        .toList();
            }

            // If agencyId is passed, only use that agency’s programs
            else if (agencyId != -1) {
                filteredPrograms = participant.getPrograms().stream()
                        .filter(p -> p.getAgency() != null && p.getAgency().getAgencyId().equals(agencyId))
                        .toList();
            }

            // If no programs after filtering, skip
            if (filteredPrograms.isEmpty()) continue;

            // For each relevant program, write a row
            for (Program program : filteredPrograms) {
                HSSFRow dataRow = sheet.createRow(rowIndex++);

                dataRow.createCell(0).setCellValue(serialNo++); // SNo
                dataRow.createCell(1).setCellValue(program.getAgency() != null ? program.getAgency().getAgencyName() : "");
                dataRow.createCell(2).setCellValue(program.getProgramTitle());
                dataRow.createCell(3).setCellValue(program.getStartDate().toString());
                dataRow.createCell(4).setCellValue(program.getEndDate().toString());
                dataRow.createCell(5).setCellValue(program.getLocation() != null ? program.getLocation().getDistrict() : "");
                dataRow.createCell(6).setCellValue(participant.getOrganization() == null ? "YES" : "NO");
                dataRow.createCell(7).setCellValue(participant.getOrganization() != null ? participant.getOrganization().getOrganizationName() : "");
                dataRow.createCell(8).setCellValue(participant.getOrganization() != null ? participant.getOrganization().getOrganizationType() : "");
                dataRow.createCell(9).setCellValue(participant.getParticipantName());
                dataRow.createCell(10).setCellValue(participant.getGender() != null ? participant.getGender().toString() : "");
                dataRow.createCell(11).setCellValue(participant.getCategory() != null ? participant.getCategory() : "");
                dataRow.createCell(12).setCellValue(participant.getDisability() != null ? participant.getDisability().toString() : "");
                dataRow.createCell(13).setCellValue(participant.getAadharNo() != null ? participant.getAadharNo().toString() : "");
                dataRow.createCell(14).setCellValue(participant.getMobileNo() != null ? participant.getMobileNo().toString() : "");
                dataRow.createCell(15).setCellValue(participant.getEmail() != null ? participant.getEmail() : "");
                dataRow.createCell(16).setCellValue(participant.getDesignation() != null ? participant.getDesignation() : "");
                dataRow.createCell(17).setCellValue(participant.getOrganization() != null ? participant.getOrganization().getUdyamregistrationNo() : "");
                dataRow.createCell(18).setCellValue(participant.getOrganization() != null ? participant.getOrganization().getSectors().stream().map(Sector::getSectorName).collect(Collectors.joining(", ")) : "");
                dataRow.createCell(19).setCellValue(String.valueOf(participant.getOrganization() != null ? participant.getOrganization().getContactNo() : ""));
                dataRow.createCell(20).setCellValue(participant.getOrganization() != null ? participant.getOrganization().getEmail() : "");
                dataRow.createCell(21).setCellValue(participant.getOrganization() != null ? participant.getOrganization().getOwnerName() : "");
                dataRow.createCell(22).setCellValue(String.valueOf(participant.getOrganization() != null ? participant.getOrganization().getOwnerContactNo() : ""));
                dataRow.createCell(23).setCellValue(String.valueOf(participant.getOrganization() != null ? participant.getOrganization().getOwnerEmail() : ""));

            }
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=participant_details.xls");
        try (ServletOutputStream out = response.getOutputStream()) {
            workbook.write(out);
        }

        workbook.close();
    }

    private void generateExcelUsingJdbc(HttpServletResponse response) throws IOException {
        List<ParticipantExcelDto> excelData = getExcelData();
        writeExcelFromDto(response, excelData);
    }

    private void writeExcelFromDto(HttpServletResponse response,
                                   List<ParticipantExcelDto> excelData) throws IOException {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Participant Details");

        HSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        String[] headers = {
                "SNo", "Agency Name", "Program Name", "Start Date", "End Date",
                "District", "IsAspirant", "Organization Name", "Organization Type",
                "Participant Name", "Gender", "Category", "Disability", "Aadhar No",
                "Participant MobileNo", "Participant Email", "Participant Designation",
                "Udyam Registration No", "Sector's", "Organization MobileNo",
                "Organization Email", "Owner Name", "Owner MobileNo", "Owner Email"
        };

        HSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowIndex = 1;

        for (ParticipantExcelDto dto : excelData) {

            HSSFRow row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(dto.getSno());
            row.createCell(1).setCellValue(nullSafe(dto.getAgencyName()));
            row.createCell(2).setCellValue(nullSafe(dto.getProgramName()));
            row.createCell(3).setCellValue(dto.getStartDate() != null ? dto.getStartDate().toString() : "");
            row.createCell(4).setCellValue(dto.getEndDate() != null ? dto.getEndDate().toString() : "");
            row.createCell(5).setCellValue(nullSafe(dto.getDistrict()));
            row.createCell(6).setCellValue(nullSafe(dto.getIsAspirant()));

            row.createCell(7).setCellValue(nullSafe(dto.getOrganizationName()));
            row.createCell(8).setCellValue(nullSafe(dto.getOrganizationType()));

            row.createCell(9).setCellValue(nullSafe(dto.getParticipantName()));
            row.createCell(10).setCellValue(nullSafe(dto.getGender()));
            row.createCell(11).setCellValue(nullSafe(dto.getCategory()));
            row.createCell(12).setCellValue(nullSafe(dto.getDisability()));
            row.createCell(13).setCellValue(nullSafe(dto.getAadharNo()));
            row.createCell(14).setCellValue(nullSafe(dto.getParticipantMobileNo()));
            row.createCell(15).setCellValue(nullSafe(dto.getParticipantEmail()));
            row.createCell(16).setCellValue(nullSafe(dto.getParticipantDesignation()));

            row.createCell(17).setCellValue(nullSafe(dto.getUdyamRegistrationNo()));
            row.createCell(18).setCellValue(nullSafe(dto.getSectors()));

            row.createCell(19).setCellValue(nullSafe(dto.getOrganizationMobileNo()));
            row.createCell(20).setCellValue(nullSafe(dto.getOrganizationEmail()));
            row.createCell(21).setCellValue(nullSafe(dto.getOwnerName()));
            row.createCell(22).setCellValue(nullSafe(dto.getOwnerMobileNo()));
            row.createCell(23).setCellValue(nullSafe(dto.getOwnerEmail()));
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition",
                "attachment; filename=participant_details.xls");

        try (ServletOutputStream out = response.getOutputStream()) {
            workbook.write(out);
        }

        workbook.close();
    }

    private String nullSafe(String value) {
        return value == null ? "" : value;
    }
}