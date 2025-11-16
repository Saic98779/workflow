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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingProgramExcelGenerator {

    private final ProgressMonitoringService trainingProgramService;

    public void generateTrainingProgramExcel(HttpServletResponse response, Long agencyId) throws IOException {

        List<TrainingProgramDto> data = trainingProgramService.getAllTrainingProgress(agencyId);

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Training Program Progress");

        // Header Style
        HSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        // Headers
        String[] headers = {
                "S.No",
                "Activity",
                "Budget Allocated",
                "Training Target",
                "Expenditure",
                "Training Achievement"
        };

        // Header Row
        HSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowIndex = 1;
        int serialNo = 1;

        double grandBudget = 0;
        double grandTarget = 0;
        double grandExpenditure = 0;
        double grandAchievement = 0;

        if (agencyId != -1) {
            // ✅ Case 1: Single agency report
            double totalBudget = 0, totalTarget = 0, totalExpenditure = 0, totalAchievement = 0;

            for (TrainingProgramDto dto : data) {
                HSSFRow row = sheet.createRow(rowIndex++);

                double budget = dto.getBudgetAllocated() != null ? dto.getBudgetAllocated() : 0;
                double target = dto.getTrainingTarget() != null ? dto.getTrainingTarget() : 0;
                double expenditure = dto.getExpenditure() != null ? dto.getExpenditure() : 0;
                double achievement = dto.getTrainingAchievement() != null ? dto.getTrainingAchievement() : 0;

                row.createCell(0).setCellValue(serialNo++);
                row.createCell(1).setCellValue(dto.getActivity());
                row.createCell(2).setCellValue(budget);
                row.createCell(3).setCellValue(target);
                row.createCell(4).setCellValue(expenditure);
                row.createCell(5).setCellValue(achievement);

                totalBudget += budget;
                totalTarget += target;
                totalExpenditure += expenditure;
                totalAchievement += achievement;
            }

            // Total row for this agency
            HSSFRow totalRow = sheet.createRow(rowIndex + 1);
            totalRow.createCell(2).setCellValue("Total");
            totalRow.getCell(2).setCellStyle(headerStyle);
            totalRow.createCell(3).setCellValue(totalBudget);
            totalRow.createCell(4).setCellValue(totalTarget);
            totalRow.createCell(5).setCellValue(totalExpenditure);
            totalRow.createCell(6).setCellValue(totalAchievement);

        } else {
            // ✅ Case 2: All agencies report with per-agency and grand totals
            Map<String, List<TrainingProgramDto>> groupedByAgency = data.stream()
                    .collect(Collectors.groupingBy(TrainingProgramDto::getAgency));

            for (Map.Entry<String, List<TrainingProgramDto>> entry : groupedByAgency.entrySet()) {
                String agency = entry.getKey();
                List<TrainingProgramDto> list = entry.getValue();

                // Agency header row
                HSSFRow agencyRow = sheet.createRow(rowIndex++);
                HSSFCell agencyCell = agencyRow.createCell(0);
                agencyCell.setCellValue("Agency: " + agency);
                agencyCell.setCellStyle(headerStyle);

                double totalBudget = 0, totalTarget = 0, totalExpenditure = 0, totalAchievement = 0;

                for (TrainingProgramDto dto : list) {
                    HSSFRow row = sheet.createRow(rowIndex++);

                    double budget = dto.getBudgetAllocated() != null ? dto.getBudgetAllocated() : 0;
                    double target = dto.getTrainingTarget() != null ? dto.getTrainingTarget() : 0;
                    double expenditure = dto.getExpenditure() != null ? dto.getExpenditure() : 0;
                    double achievement = dto.getTrainingAchievement() != null ? dto.getTrainingAchievement() : 0;

                    row.createCell(0).setCellValue(serialNo++);
                    row.createCell(1).setCellValue(agency);
                    row.createCell(2).setCellValue(dto.getActivity());
                    row.createCell(3).setCellValue(budget);
                    row.createCell(4).setCellValue(target);
                    row.createCell(5).setCellValue(expenditure);
                    row.createCell(6).setCellValue(achievement);

                    totalBudget += budget;
                    totalTarget += target;
                    totalExpenditure += expenditure;
                    totalAchievement += achievement;

                    grandBudget += budget;
                    grandTarget += target;
                    grandExpenditure += expenditure;
                    grandAchievement += achievement;
                }

                // Per-agency total
                HSSFRow totalRow = sheet.createRow(rowIndex++);
                totalRow.createCell(2).setCellValue("Total (" + agency + ")");
                totalRow.getCell(2).setCellStyle(headerStyle);
                totalRow.createCell(3).setCellValue(totalBudget);
                totalRow.createCell(4).setCellValue(totalTarget);
                totalRow.createCell(5).setCellValue(totalExpenditure);
                totalRow.createCell(6).setCellValue(totalAchievement);

                rowIndex++; // Empty line before next agency
            }

            // Grand total at bottom
            HSSFRow grandRow = sheet.createRow(rowIndex + 1);
            grandRow.createCell(2).setCellValue("Grand Total");
            grandRow.getCell(2).setCellStyle(headerStyle);
            grandRow.createCell(3).setCellValue(grandBudget);
            grandRow.createCell(4).setCellValue(grandTarget);
            grandRow.createCell(5).setCellValue(grandExpenditure);
            grandRow.createCell(6).setCellValue(grandAchievement);
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
