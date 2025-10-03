package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.program.repository.ProgramRepository;
import com.metaverse.workflow.program.service.ProgramResponse;
import com.metaverse.workflow.program.service.ProgramResponseMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@Service
public class ProgramDetailsExcel {
    @Autowired
    ProgramRepository programRepository;

    public void generateProgramsExcel(HttpServletResponse response, Long agencyId) throws IOException {
        List<Program> programList;
        if (agencyId != -1) {
            programList = programRepository.findByAgencyAgencyId(agencyId);
        }else {
            programList = programRepository.findAll();
        }
        List<ProgramResponse> programResponseList = programList.stream().map(ProgramResponseMapper::map).toList();

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Program Details");


        HSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);


        HSSFRow row = sheet.createRow(0);
        String[] headers = {
                "SNo", "Start Date", "End Date", "InTime", "Out Time",
                "Program Location", "District", "Budget Head", "Agency Name",
                "Title Of Program", "Status", "Activity", "Sub Activity",
                "SPOC Name", "SPOC ContactNo"
        };



        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        int dataRowIndex = 1;
        for (ProgramResponse res : programResponseList) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);

            dataRow.createCell(0).setCellValue(dataRowIndex); // SNo
            dataRow.createCell(1).setCellValue(res.getStartDate()); // StartDate
            dataRow.createCell(2).setCellValue(res.getEndDate());   // EndDate
            dataRow.createCell(3).setCellValue(res.getStartTime()); // InTime
            dataRow.createCell(4).setCellValue(res.getEndTime());   // OutTime
            dataRow.createCell(5).setCellValue(res.getProgramLocationName()); // ProgramLocation
            dataRow.createCell(6).setCellValue(res.getDistrict()); // District
            dataRow.createCell(7).setCellValue(res.getProgramType()); // BudgetHead
            dataRow.createCell(8).setCellValue(res.getAgencyName()); // AgencyName
            dataRow.createCell(9).setCellValue(res.getProgramTitle()); // TitleOfProgram
            dataRow.createCell(10).setCellValue(res.getStatus()); // Status
            dataRow.createCell(11).setCellValue(res.getActivityName()); // TypeOfActivity
            dataRow.createCell(12).setCellValue(res.getSubActivityName()); // SubActivity
            dataRow.createCell(13).setCellValue(res.getSpocName()); // SPOCName
            dataRow.createCell(14).setCellValue(res.getSpocContactNo()); // SPOCContactNo

            dataRowIndex++;
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }
}
