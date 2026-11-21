package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.metaverse.workflow.trainingandnontrainingtarget.dtos.TrainingTargetsAndAchievementsResponse;
import com.metaverse.workflow.trainingandnontrainingtarget.service.TrainingTargetsAndAchievementsService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class TrainingTargetsAndAchievementsExcelGenerator {

    private static final Logger log =
            LogManager.getLogger(TrainingTargetsAndAchievementsExcelGenerator.class);

    private static final String[] COLUMNS = {
            "Agency Name", "Activity Name", "Sub Activity Name", "Financial Year",
            "Target", "Achievement", "Budget Allocated", "Expenditure"
    };

    private static final int COL_AGENCY = 0;
    private static final int COL_ACTIVITY = 1;
    private static final int COL_SUB_ACTIVITY = 2;
    private static final int COL_FINANCIAL_YEAR = 3;
    private static final int COL_TARGET = 4;
    private static final int COL_ACHIEVEMENT = 5;
    private static final int COL_BUDGET = 6;
    private static final int COL_EXPENDITURE = 7;

    private final TrainingTargetsAndAchievementsService trainingTargetsAndAchievementsService;

    public byte[] generateExcel(String year, Long agencyId) throws IOException {

        log.info("Training Targets & Achievements Excel generation started | Agency: {} | FY: {}", agencyId, year);

        List<TrainingTargetsAndAchievementsResponse> rows =
                trainingTargetsAndAchievementsService.getTargetsAndAchievementsForExport(year, agencyId);

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Targets & Achievements");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle textStyle = createTextStyle(workbook);
            CellStyle countStyle = createNumberStyle(workbook, "#,##0", false);
            CellStyle amountStyle = createNumberStyle(workbook, "#,##0.00", false);
            CellStyle totalLabelStyle = createTotalLabelStyle(workbook);
            CellStyle totalCountStyle = createNumberStyle(workbook, "#,##0", true);
            CellStyle totalAmountStyle = createNumberStyle(workbook, "#,##0.00", true);

            // === HEADER ROW ===
            Row header = sheet.createRow(0);
            for (int i = 0; i < COLUMNS.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(COLUMNS[i]);
                cell.setCellStyle(headerStyle);
            }

            // === DATA ROWS ===
            long totalTarget = 0L;
            long totalAchievement = 0L;
            double totalBudget = 0.0;
            double totalExpenditure = 0.0;

            int rowIdx = 1;
            for (TrainingTargetsAndAchievementsResponse dto : rows) {
                Row row = sheet.createRow(rowIdx++);

                createTextCell(row, COL_AGENCY, dto.getAgencyName(), textStyle);
                createTextCell(row, COL_ACTIVITY, dto.getActivityName(), textStyle);
                createTextCell(row, COL_SUB_ACTIVITY, dto.getSubActivityName(), textStyle);
                createTextCell(row, COL_FINANCIAL_YEAR, dto.getFinancialYear(), textStyle);

                long target = nz(dto.getTotalTarget());
                long achievement = nz(dto.getTotalAchieved());
                double budget = nz(dto.getTotalFinancialTarget());
                double expenditure = nz(dto.getTotalFinancialAchieved());

                createNumericCell(row, COL_TARGET, target, countStyle);
                createNumericCell(row, COL_ACHIEVEMENT, achievement, countStyle);
                createNumericCell(row, COL_BUDGET, budget, amountStyle);
                createNumericCell(row, COL_EXPENDITURE, expenditure, amountStyle);

                totalTarget += target;
                totalAchievement += achievement;
                totalBudget += budget;
                totalExpenditure += expenditure;
            }

            // Show agency / activity / sub-activity once per group instead of repeating them
            int firstDataRow = 1;
            int lastDataRow = rowIdx - 1;
            mergeRepeatedValues(sheet, rows, firstDataRow, COL_AGENCY,
                    dto -> key(dto.getAgencyName()));
            mergeRepeatedValues(sheet, rows, firstDataRow, COL_ACTIVITY,
                    dto -> key(dto.getAgencyName(), dto.getActivityName()));
            mergeRepeatedValues(sheet, rows, firstDataRow, COL_SUB_ACTIVITY,
                    dto -> key(dto.getAgencyName(), dto.getActivityName(), dto.getSubActivityName()));

            // === GRAND TOTAL ROW ===
            Row totalRow = sheet.createRow(rowIdx);
            for (int i = COL_AGENCY; i <= COL_FINANCIAL_YEAR; i++) {
                totalRow.createCell(i).setCellStyle(totalLabelStyle);
            }
            totalRow.getCell(COL_AGENCY).setCellValue("Grand Total");
            sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, COL_AGENCY, COL_FINANCIAL_YEAR));

            createNumericCell(totalRow, COL_TARGET, totalTarget, totalCountStyle);
            createNumericCell(totalRow, COL_ACHIEVEMENT, totalAchievement, totalCountStyle);
            createNumericCell(totalRow, COL_BUDGET, totalBudget, totalAmountStyle);
            createNumericCell(totalRow, COL_EXPENDITURE, totalExpenditure, totalAmountStyle);

            log.info("Excel rows written: {} | Grand Total -> Target: {}, Achievement: {}, Budget: {}, Expenditure: {}",
                    lastDataRow, totalTarget, totalAchievement, totalBudget, totalExpenditure);

            // Freeze header and auto size columns
            sheet.createFreezePane(0, 1);
            for (int i = 0; i < COLUMNS.length; i++) {
                sheet.autoSizeColumn(i, true);
                sheet.setColumnWidth(i, Math.min(sheet.getColumnWidth(i) + 512, 255 * 256));
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    /**
     * Vertically merges consecutive rows in the given column that share the same group key.
     * Rows are expected to be sorted by agency, activity, sub activity and financial year.
     */
    private void mergeRepeatedValues(Sheet sheet,
                                     List<TrainingTargetsAndAchievementsResponse> rows,
                                     int firstDataRow,
                                     int column,
                                     Function<TrainingTargetsAndAchievementsResponse, String> groupKey) {
        int groupStart = 0;
        for (int i = 1; i <= rows.size(); i++) {
            boolean groupEnds = i == rows.size()
                    || !Objects.equals(groupKey.apply(rows.get(i)), groupKey.apply(rows.get(groupStart)));
            if (groupEnds) {
                if (i - groupStart > 1) {
                    sheet.addMergedRegion(new CellRangeAddress(
                            firstDataRow + groupStart, firstDataRow + i - 1, column, column));
                }
                groupStart = i;
            }
        }
    }

    private static String key(String... parts) {
        return String.join("\u0000", Arrays.stream(parts).map(p -> p == null ? "" : p).toList());
    }

    private void createTextCell(Row row, int column, String value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(safe(value));
        cell.setCellStyle(style);
    }

    private void createNumericCell(Row row, int column, double value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        applyBorders(style);
        return style;
    }

    private CellStyle createTextStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        applyBorders(style);
        return style;
    }

    private CellStyle createNumberStyle(Workbook workbook, String format, boolean grandTotal) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat(format));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        applyBorders(style);
        if (grandTotal) {
            applyGrandTotalHighlight(workbook, style);
        }
        return style;
    }

    private CellStyle createTotalLabelStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        applyBorders(style);
        applyGrandTotalHighlight(workbook, style);
        return style;
    }

    private void applyGrandTotalHighlight(Workbook workbook, CellStyle style) {
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.MEDIUM);
    }

    private void applyBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    private String safe(String value) {
        return value != null ? value : "";
    }

    private long nz(Long value) {
        return value != null ? value : 0L;
    }

    private long nz(Integer value) {
        return value != null ? value : 0L;
    }

    private double nz(Double value) {
        return value != null ? value : 0.0;
    }
}
