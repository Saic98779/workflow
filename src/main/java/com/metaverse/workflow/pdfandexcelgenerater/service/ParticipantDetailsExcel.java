package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.model.Sector;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import com.metaverse.workflow.program.repository.ProgramRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantDetailsExcel {
    private final  ParticipantRepository participantRepository;

    public void generateParticipantDetailsExcel(HttpServletResponse response, Long programId, Long agencyId) throws IOException {

        List<Participant> participantList;
        System.out.println("Hi " + LocalDateTime.now());
        if (programId != null) {
            participantList = participantRepository.findByProgramId(programId);
        } else if (agencyId != -1) {
            participantList = participantRepository.findByPrograms_Agency_AgencyId(agencyId);
        } else {
            participantList = participantRepository.findAll();
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
}
