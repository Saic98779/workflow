package com.metaverse.workflow.pdfandexcelgenerater.service;


import com.metaverse.workflow.nontraining.dto.TrainingProgramDto;
import com.metaverse.workflow.nontraining.service.ProgressMonitoringService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingProgramExcelGenerator {

    private final ProgressMonitoringService trainingProgramService;

    public void generateTrainingProgramExcel(HttpServletResponse response, Long agencyId) throws IOException {

        // Fetch your data
        List<TrainingProgramDto> data = trainingProgramService.getAllTrainingProgress(agencyId);

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Training Program Progress");

        // Header style
        HSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        // Headers
        String[] headers = {
                "S.No",
                "Agency",
                "Activity",
                "Budget Allocated",
                "Training Target",
                "Expenditure",
                "Training Achievement"
        };

        // Create header row
        HSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowIndex = 1;
        int serialNo = 1;

        for (TrainingProgramDto dto : data) {
            HSSFRow row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(serialNo++);
            row.createCell(1).setCellValue(dto.getAgency() != null ? dto.getAgency() : "");
            row.createCell(2).setCellValue(dto.getActivity() != null ? dto.getActivity() : "");
            row.createCell(3).setCellValue(dto.getBudgetAllocated() != null ? dto.getBudgetAllocated() : 0);
            row.createCell(4).setCellValue(dto.getTrainingTarget() != null ? dto.getTrainingTarget() : 0);
            row.createCell(5).setCellValue(dto.getExpenditure() != null ? dto.getExpenditure() : 0);
            row.createCell(6).setCellValue(dto.getTrainingAchievement() != null ? dto.getTrainingAchievement() : 0);

        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Response setup
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=training_program_progress.xls");

        try (ServletOutputStream out = response.getOutputStream()) {
            workbook.write(out);
        }

        workbook.close();
    }
}
