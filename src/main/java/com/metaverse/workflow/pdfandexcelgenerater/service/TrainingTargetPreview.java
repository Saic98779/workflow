package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.program.controller.ProgramController;
import jakarta.servlet.ServletOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TrainingTargetPreview {

    private static final Logger log =
            LogManager.getLogger(TrainingTargetPreview.class);

    private final ReportDataService reportDataService;
    private final AgencyRepository agencyRepository;

    public void generateExcel(
            ReportRequest request,
            String createdBy,
            OutputStream outputStream) throws IOException {

        log.info("Excel generation started | createdBy={} | agencies={}",
                createdBy, request.getAgencyIds());

        HSSFWorkbook workbook = new HSSFWorkbook();

        try {
            createSummarySheet(workbook, request, createdBy);
            createDataSheet(workbook, request);

            workbook.write(outputStream);
            log.info("Excel written successfully to output stream");

        } catch (Exception ex) {
            log.error("Excel generation failed", ex);
            throw ex;
        } finally {
            workbook.close();
            log.debug("Workbook closed");
        }
    }

    private void createSummarySheet(HSSFWorkbook workbook, ReportRequest request, String createdBy) {

        log.debug("Creating Summary sheet");

        HSSFSheet sheet = workbook.createSheet("Summary");
        int rowNum = 0;

        row(sheet, rowNum++, "Created By", createdBy);
        row(sheet, rowNum++, "Generated On", LocalDateTime.now().toString());

        String agencies = "";
        List<Long> agencyIds = agencyIds(request);
        if (!agencyIds.isEmpty()) {
            try {
                agencies = String.join(", ", agencyRepository.findAgencyNamesByIds(agencyIds));
            } catch (Exception ex) {
                log.warn("Could not resolve agency names for {} : {}", agencyIds, ex.getMessage());
            }
        }

        log.debug("Summary agencies resolved: {}", agencies);

        row(sheet, rowNum++, "Agencies", agencies);
        row(sheet, rowNum++, "Report Type",
                request.getReportType() != null ? request.getReportType().name() : ReportType.TARGET.name());
        row(sheet, rowNum++, "Training Type",
                request.getTrainingType() != null ? request.getTrainingType().name()
                        : TrainingType.TRAINING_AND_NON_TRAINING.name());

        if (request.getDateType() == DateType.DATE_RANGE) {
            row(sheet, rowNum++, "From Date", request.getFromDate());
            row(sheet, rowNum++, "To Date", request.getToDate());
        } else {
            row(sheet, rowNum++, "Financial Year", request.getFinancialYear());
        }

        setColumnWidths(sheet, 2);
        log.debug("Summary sheet created");
    }

    private void row(HSSFSheet sheet, int rowNum, String key, String value) {
        HSSFRow row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(key);
        row.createCell(1).setCellValue(value != null ? value : "");
    }

    /** Distinct, non-null agency ids of the request. */
    private List<Long> agencyIds(ReportRequest request) {
        if (request.getAgencyIds() == null) {
            return List.of();
        }
        return request.getAgencyIds().stream().filter(Objects::nonNull).distinct().toList();
    }

    private void createDataSheet(HSSFWorkbook workbook, ReportRequest request) {

        log.debug("Creating Report Data sheet");

        HSSFSheet sheet = workbook.createSheet("Report Data");
        int rowNum = 0;

        ReportType reportType = request.getReportType() != null ? request.getReportType() : ReportType.TARGET;
        createHeader(sheet, reportType);
        rowNum++;

        // Highlight for rows whose data failed to load (values written as 0)
        HSSFCellStyle errorStyle = workbook.createCellStyle();
        errorStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
        errorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        for (Long agencyId : agencyIds(request)) {

            log.debug("Fetching report rows for agencyId={}", agencyId);

            List<ExcelReportRow> rows;
            try {
                rows = reportDataService.prepareExcelRows(agencyId, request);
            } catch (Exception ex) {
                log.error("Failed to prepare report rows for agencyId={}", agencyId, ex);
                rows = List.of(ExcelReportRow.builder()
                        .agency("Agency " + agencyId)
                        .activity("-")
                        .subActivity("-")
                        .physicalTarget(0L)
                        .budgetAllocated(0.0)
                        .trainingAchievement(0L)
                        .expenditure(0.0)
                        .error(true)
                        .remarks("Error while loading data - values shown as 0")
                        .build());
            }

            log.debug("Fetched {} rows for agencyId={}", rows.size(), agencyId);

            for (ExcelReportRow data : rows) {
                HSSFRow row = sheet.createRow(rowNum++);
                writeRow(row, data, reportType);
                if (data.isError()) {
                    for (int c = 0; c < row.getLastCellNum(); c++) {
                        row.getCell(c).setCellStyle(errorStyle);
                    }
                }
            }
        }

        setColumnWidths(sheet, sheet.getRow(0).getLastCellNum());

        log.debug("Report Data sheet created | totalRows={}", rowNum);
    }

    private void createHeader(HSSFSheet sheet, ReportType reportType) {

        log.debug("Creating header | reportType={}", reportType);

        HSSFRow header = sheet.createRow(0);
        int col = 0;

        header.createCell(col++).setCellValue("Agency");
        header.createCell(col++).setCellValue("Activity");
        header.createCell(col++).setCellValue("Sub Activity");
        header.createCell(col++).setCellValue("Report Type");

        if (reportType != ReportType.ACHIEVEMENT) {
            header.createCell(col++).setCellValue("Physical Target");
            header.createCell(col++).setCellValue("Budget Allocated");
        }

        if (reportType != ReportType.TARGET) {
            header.createCell(col++).setCellValue("Physical Achievement");
            header.createCell(col++).setCellValue("Expenditure");
        }

        header.createCell(col).setCellValue("Remarks");
    }

    private void writeRow(
            HSSFRow row,
            ExcelReportRow data,
            ReportType reportType) {

        int col = 0;

        row.createCell(col++).setCellValue(
                data.getAgency() != null ? data.getAgency() : "");

        row.createCell(col++).setCellValue(
                data.getActivity() != null ? data.getActivity() : "");

        row.createCell(col++).setCellValue(
                data.getSubActivity() != null ? data.getSubActivity() : "");

        row.createCell(col++).setCellValue(
                data.getReportType() != null ? data.getReportType() : "");

        if (reportType != ReportType.ACHIEVEMENT) {
            row.createCell(col++).setCellValue(
                    data.getPhysicalTarget() != null ? data.getPhysicalTarget() : 0);

            row.createCell(col++).setCellValue(
                    data.getBudgetAllocated() != null ? data.getBudgetAllocated() : 0L);
        }

        if (reportType != ReportType.TARGET) {
            row.createCell(col++).setCellValue(
                    data.getTrainingAchievement() != null ? data.getTrainingAchievement() : 0.0);

            row.createCell(col++).setCellValue(
                    data.getExpenditure() != null ? data.getExpenditure() : 0.0);
        }

        row.createCell(col).setCellValue(data.getRemarks() != null ? data.getRemarks() : "");
    }

    /**
     * ✅ Server-safe column sizing (NO AWT)
     */
    private void setColumnWidths(HSSFSheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.setColumnWidth(i, 6000);
        }
    }
}
