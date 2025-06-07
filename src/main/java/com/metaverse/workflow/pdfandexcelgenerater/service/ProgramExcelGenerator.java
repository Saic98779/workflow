package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.program.service.ProgramResponse;
import com.metaverse.workflow.program.service.ProgramService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ProgramExcelGenerator {

    @Autowired
    ProgramService programService;

    public void generateProgramsExcel(HttpServletResponse response) throws IOException {
        WorkflowResponse programs = programService.getPrograms();
        List<ProgramResponse> programList = (List<ProgramResponse>) programs.getData();

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Program Details");


        HSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);


        HSSFRow row = sheet.createRow(0);
        String[] headers = {
                "ProgramTitle",  "ActivityName",  "subActivityName",
                 "AgencyName", "StartDate", "EndDate", "StartTime", "EndTime",
                "SpocName", "pocContactNo",  "ProgramLocationName", "Kpi"
        };


        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        int dataRowIndex = 1;
        for (ProgramResponse res : programList) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(res.getProgramTitle());
            dataRow.createCell(1).setCellValue(res.getActivityName());
            dataRow.createCell(2).setCellValue(res.getSubActivityName());
            dataRow.createCell(3).setCellValue(res.getAgencyName());
            dataRow.createCell(4).setCellValue(res.getStartDate());
            dataRow.createCell(5).setCellValue(res.getEndDate());
            dataRow.createCell(6).setCellValue(res.getStartTime());
            dataRow.createCell(7).setCellValue(res.getEndTime());
            dataRow.createCell(8).setCellValue(res.getSpocName());
            dataRow.createCell(9).setCellValue(res.getSpocContactNo());
            dataRow.createCell(10).setCellValue(res.getProgramLocationName());
            dataRow.createCell(11).setCellValue(res.getKpi());
            dataRowIndex++;
        }
        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }
}
