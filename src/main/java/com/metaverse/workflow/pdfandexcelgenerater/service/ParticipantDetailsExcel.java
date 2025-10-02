package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@Service
public class ParticipantDetailsExcel {
    @Autowired
    ParticipantRepository participantRepository;

    public void generateParticipantDetailsExcel(HttpServletResponse response, Long programId, Long agencyId) throws IOException {
        List<Participant> participantList;

        if (agencyId == null) {
            participantList = participantRepository.findByPrograms_ProgramId(programId);
        } else if (agencyId != -1) {
            participantList = participantRepository.findByPrograms_Agency_AgencyId(agencyId);
        }else {
            participantList = participantRepository.findAll();
        }


        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Participant Details");


        HSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);


        HSSFRow row = sheet.createRow(0);

        String[] headers = {
                "SNo", "Agency Name", "Program Name", "District", "IsAspirant",
                "OrganizationName", "ParticipantName", "Gender", "Category",
                "Disability", "AadharNo", "MobileNo", "Email", "Designation"
        };

// Create header row
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int dataRowIndex = 1;

        for (Participant res : participantList) {
            for (Program prog : res.getPrograms()) {
                HSSFRow dataRow = sheet.createRow(dataRowIndex);
                if (agencyId != -1 && !prog.getAgency().getAgencyId().equals(agencyId)) {
                    continue;
                }
                dataRow.createCell(0).setCellValue(dataRowIndex); // SNo
                dataRow.createCell(1).setCellValue(prog.getAgency() != null ? prog.getAgency().getAgencyName() : "");
                dataRow.createCell(2).setCellValue(prog.getProgramTitle());
                dataRow.createCell(3).setCellValue(prog.getLocation() != null ? prog.getLocation().getDistrict() : "");
                dataRow.createCell(4).setCellValue(res.getOrganization() == null ? "YES" : "NO"); // IsAspirant
                dataRow.createCell(5).setCellValue(res.getOrganization() != null ? res.getOrganization().getOrganizationName() : "");
                dataRow.createCell(6).setCellValue(res.getParticipantName());
                dataRow.createCell(7).setCellValue(res.getGender() != null ? res.getGender().toString() : "");
                dataRow.createCell(8).setCellValue(res.getCategory());
                dataRow.createCell(9).setCellValue(res.getDisability() != null ? res.getDisability().toString() : "");
                dataRow.createCell(10).setCellValue(res.getAadharNo() != null ? res.getAadharNo().toString() : "");
                dataRow.createCell(11).setCellValue(res.getMobileNo() != null ? res.getMobileNo().toString() : "");
                dataRow.createCell(12).setCellValue(res.getEmail());
                dataRow.createCell(13).setCellValue(res.getDesignation());

                dataRowIndex++;
            }
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }
}
