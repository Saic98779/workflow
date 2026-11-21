package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.metaverse.workflow.trainingandnontrainingtarget.service.NonTrainingTargetsAndAchievementsService;
import com.metaverse.workflow.trainingandnontrainingtarget.service.TargetResponse;
import com.metaverse.workflow.trainingandnontrainingtarget.service.TrainingTargetsAndAchievementsService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

/**
 * Builds a single workbook with two worksheets:
 * <ol>
 *     <li>Training Targets</li>
 *     <li>Non-Training Targets</li>
 * </ol>
 * Each sheet has one row per (agency, activity, sub-activity) and, for every financial year present,
 * a Physical Target and a Financial Target column (the year's total target),
 * followed by a "Grand Total" block (sum across years) and a Grand Total row at the bottom.
 */
@Service
@RequiredArgsConstructor
public class TrainingAndNonTrainingTargetsExcelGenerator {

    private static final Logger log = LogManager.getLogger(TrainingAndNonTrainingTargetsExcelGenerator.class);

    private static final int FIXED_COLS = 3;   // agency, activity, sub-activity
    private static final int YEAR_COLS = 2;    // physical + financial
    private static final int HEADER_ROWS = 2;

    private final TrainingTargetsAndAchievementsService trainingTargetsAndAchievementsService;
    private final NonTrainingTargetsAndAchievementsService nonTrainingTargetsAndAchievementsService;

    /** One target record, normalised from either training or non-training targets. */
    private record TargetRow(String agency, String activity, String subActivity, String financialYear,
                             long physical, double financial) {
    }

    /** All years of one (agency, activity, sub-activity) combination. */
    private record GroupedRow(String agency, String activity, String subActivity,
                              Map<String, Long> physicalByYear, Map<String, Double> financialByYear) {
    }

    private record Styles(CellStyle header, CellStyle text, CellStyle count, CellStyle amount,
                          CellStyle totalLabel, CellStyle totalCount, CellStyle totalAmount) {
    }

    /**
     * @param year     optional financial year (e.g. "2026-2027"); all years when blank
     * @param agencyId optional agency filter; all agencies when null
     */
    public byte[] generateExcel(String year, Long agencyId) throws IOException {
        log.info("Training & Non-Training Targets Excel generation started | Agency: {} | FY: {}", agencyId, year);

        List<TargetRow> trainingRows = toRows(
                trainingTargetsAndAchievementsService.getTrainingTargetsForExport(year, agencyId));
        List<TargetRow> nonTrainingRows = toRows(
                nonTrainingTargetsAndAchievementsService.getNonTrainingTargetsForExport(year, agencyId));

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Styles styles = createStyles(workbook);
            writeSheet(workbook.createSheet("Training Targets"), trainingRows,
                    "Activity Name", styles);
            writeSheet(workbook.createSheet("Non-Training Targets"), nonTrainingRows,
                    "Activity Name / Budget Head", styles);

            workbook.write(out);
            log.info("Targets Excel generated | Training rows: {} | Non-Training rows: {}",
                    trainingRows.size(), nonTrainingRows.size());
            return out.toByteArray();
        }
    }

    // ------------------------------------------------------------------ data

    private List<TargetRow> toRows(List<TargetResponse> targets) {
        return targets.stream().map(t -> new TargetRow(
                safe(t.getAgencyName()),
                safe(t.getActivityName()),
                safe(t.getSubActivityName()),
                safe(t.getFinancialYear()),
                nz(t.getPhysicalTargetQ1()) + nz(t.getPhysicalTargetQ2())
                        + nz(t.getPhysicalTargetQ3()) + nz(t.getPhysicalTargetQ4()),
                nz(t.getFinancialTargetQ1()) + nz(t.getFinancialTargetQ2())
                        + nz(t.getFinancialTargetQ3()) + nz(t.getFinancialTargetQ4())
        )).toList();
    }

    /** Pivots target records so each (agency, activity, sub-activity) becomes one row with per-year values. */
    private List<GroupedRow> group(List<TargetRow> rows) {
        Map<List<String>, GroupedRow> grouped = new LinkedHashMap<>();
        rows.stream()
                .sorted(Comparator.comparing(TargetRow::agency, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(TargetRow::activity, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(TargetRow::subActivity, String.CASE_INSENSITIVE_ORDER))
                .forEach(r -> {
                    GroupedRow g = grouped.computeIfAbsent(List.of(r.agency(), r.activity(), r.subActivity()),
                            k -> new GroupedRow(r.agency(), r.activity(), r.subActivity(),
                                    new HashMap<>(), new HashMap<>()));
                    g.physicalByYear().merge(r.financialYear(), r.physical(), Long::sum);
                    g.financialByYear().merge(r.financialYear(), r.financial(), Double::sum);
                });
        return new ArrayList<>(grouped.values());
    }

    // ------------------------------------------------------------------ sheet

    private void writeSheet(Sheet sheet, List<TargetRow> targetRows, String activityHeader, Styles s) {
        List<String> years = targetRows.stream()
                .map(TargetRow::financialYear)
                .distinct()
                .sorted()
                .toList();
        List<GroupedRow> rows = group(targetRows);

        // Year blocks followed by the Grand Total block
        List<String> blockTitles = new ArrayList<>(years);
        blockTitles.add("Grand Total");
        int lastCol = FIXED_COLS + blockTitles.size() * YEAR_COLS - 1;

        writeHeader(sheet, activityHeader, blockTitles, lastCol, s);

        // === DATA ROWS ===
        long[] grandPhysical = new long[blockTitles.size()];
        double[] grandFinancial = new double[blockTitles.size()];

        int rowIdx = HEADER_ROWS;
        for (GroupedRow g : rows) {
            Row row = sheet.createRow(rowIdx++);
            textCell(row, 0, g.agency(), s.text());
            textCell(row, 1, g.activity(), s.text());
            textCell(row, 2, g.subActivity(), s.text());

            long rowPhysTotal = 0;
            double rowFinTotal = 0;
            for (int b = 0; b < blockTitles.size(); b++) {
                boolean isGrandBlock = b == years.size();
                long phys;
                double fin;
                if (!isGrandBlock) {
                    phys = g.physicalByYear().getOrDefault(years.get(b), 0L);
                    fin = g.financialByYear().getOrDefault(years.get(b), 0.0);
                    rowPhysTotal += phys;
                    rowFinTotal += fin;
                } else {
                    phys = rowPhysTotal;
                    fin = rowFinTotal;
                }
                int physCol = FIXED_COLS + b * YEAR_COLS;
                numberCell(row, physCol, phys, isGrandBlock ? s.totalCount() : s.count());
                numberCell(row, physCol + 1, fin, isGrandBlock ? s.totalAmount() : s.amount());
                grandPhysical[b] += phys;
                grandFinancial[b] += fin;
            }
        }

        // Show agency / activity once per group instead of repeating them
        mergeRepeatedValues(sheet, rows, 0, GroupedRow::agency);
        mergeRepeatedValues(sheet, rows, 1, g -> g.agency() + "\u0000" + g.activity());

        // === GRAND TOTAL ROW ===
        Row totalRow = sheet.createRow(rowIdx);
        for (int c = 0; c < FIXED_COLS; c++) {
            totalRow.createCell(c).setCellStyle(s.totalLabel());
        }
        totalRow.getCell(0).setCellValue("Grand Total");
        sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, 0, FIXED_COLS - 1));
        for (int b = 0; b < blockTitles.size(); b++) {
            int physCol = FIXED_COLS + b * YEAR_COLS;
            numberCell(totalRow, physCol, grandPhysical[b], s.totalCount());
            numberCell(totalRow, physCol + 1, grandFinancial[b], s.totalAmount());
        }

        if (rows.isEmpty()) {
            Row empty = sheet.createRow(rowIdx + 2);
            textCell(empty, 0, "No targets found for the selected filters", s.text());
        }

        // === LAYOUT ===
        sheet.createFreezePane(FIXED_COLS, HEADER_ROWS);
        sheet.setColumnWidth(0, 30 * 256);
        sheet.setColumnWidth(1, 40 * 256);
        sheet.setColumnWidth(2, 45 * 256);
        for (int c = FIXED_COLS; c <= lastCol; c++) {
            sheet.setColumnWidth(c, 18 * 256);
        }
    }

    private void writeHeader(Sheet sheet, String activityHeader, List<String> blockTitles, int lastCol, Styles s) {
        Row r0 = sheet.createRow(0);
        Row r1 = sheet.createRow(1);
        r0.setHeightInPoints(22);
        r1.setHeightInPoints(20);
        for (Row r : List.of(r0, r1)) {
            for (int c = 0; c <= lastCol; c++) {
                r.createCell(c).setCellStyle(s.header());
            }
        }

        String[] fixed = {"Agency Name", activityHeader, "Sub-Activity Name"};
        for (int c = 0; c < FIXED_COLS; c++) {
            r0.getCell(c).setCellValue(fixed[c]);
            mergeWithBorder(sheet, new CellRangeAddress(0, HEADER_ROWS - 1, c, c));
        }

        for (int b = 0; b < blockTitles.size(); b++) {
            int start = FIXED_COLS + b * YEAR_COLS;
            String title = b == blockTitles.size() - 1 ? blockTitles.get(b) : "FY " + blockTitles.get(b);
            r0.getCell(start).setCellValue(title);
            mergeWithBorder(sheet, new CellRangeAddress(0, 0, start, start + YEAR_COLS - 1));

            r1.getCell(start).setCellValue("Physical Target");
            r1.getCell(start + 1).setCellValue("Financial Target (₹)");
        }
    }

    /** Vertically merges consecutive rows in the given column that share the same group key. */
    private void mergeRepeatedValues(Sheet sheet, List<GroupedRow> rows, int column,
                                     Function<GroupedRow, String> groupKey) {
        int groupStart = 0;
        for (int i = 1; i <= rows.size(); i++) {
            boolean groupEnds = i == rows.size()
                    || !Objects.equals(groupKey.apply(rows.get(i)), groupKey.apply(rows.get(groupStart)));
            if (groupEnds) {
                if (i - groupStart > 1) {
                    mergeWithBorder(sheet, new CellRangeAddress(
                            HEADER_ROWS + groupStart, HEADER_ROWS + i - 1, column, column));
                }
                groupStart = i;
            }
        }
    }

    private void mergeWithBorder(Sheet sheet, CellRangeAddress region) {
        sheet.addMergedRegion(region);
        RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
    }

    // ------------------------------------------------------------------ cells & styles

    private void textCell(Row row, int column, String value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(safe(value));
        cell.setCellStyle(style);
    }

    private void numberCell(Row row, int column, double value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private Styles createStyles(Workbook wb) {
        CellStyle header = wb.createCellStyle();
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        header.setFont(headerFont);
        header.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        header.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        header.setAlignment(HorizontalAlignment.CENTER);
        header.setVerticalAlignment(VerticalAlignment.CENTER);
        header.setWrapText(true);
        borders(header);

        CellStyle text = wb.createCellStyle();
        text.setVerticalAlignment(VerticalAlignment.CENTER);
        text.setWrapText(true);
        borders(text);

        CellStyle totalLabel = wb.createCellStyle();
        totalLabel.setAlignment(HorizontalAlignment.CENTER);
        totalLabel.setVerticalAlignment(VerticalAlignment.CENTER);
        borders(totalLabel);
        highlight(wb, totalLabel);

        return new Styles(header, text,
                numberStyle(wb, "#,##0", false),
                numberStyle(wb, "#,##0.00", false),
                totalLabel,
                numberStyle(wb, "#,##0", true),
                numberStyle(wb, "#,##0.00", true));
    }

    private CellStyle numberStyle(Workbook wb, String format, boolean total) {
        CellStyle style = wb.createCellStyle();
        style.setDataFormat(wb.createDataFormat().getFormat(format));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        borders(style);
        if (total) {
            highlight(wb, style);
        }
        return style;
    }

    private void highlight(Workbook wb, CellStyle style) {
        Font font = wb.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    private void borders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    // ------------------------------------------------------------------ helpers

    private static String safe(String value) {
        return value != null ? value : "";
    }

    private static long nz(Long value) {
        return value != null ? value : 0L;
    }

    private static double nz(Double value) {
        return value != null ? value : 0.0;
    }
}
