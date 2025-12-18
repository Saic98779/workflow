package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.metaverse.workflow.agency.repository.AgencyRepository;
import jakarta.servlet.ServletOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingTargetPreview {

    private final ReportDataService reportDataService;
    private final AgencyRepository agencyRepository;

    public void generateExcel(
            ReportRequest request,
            String createdBy,
            ServletOutputStream outputStream) throws IOException {

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

        String agencies = String.join(", ",
                agencyRepository.findAgencyNamesByIds(request.getAgencyIds()));

        log.debug("Summary agencies resolved: {}", agencies);

        row(sheet, rowNum++, "Agencies", agencies);
        row(sheet, rowNum++, "Report Type", request.getReportType().name());
        row(sheet, rowNum++, "Training Type", request.getTrainingType().name());

        if (request.getDateType() == DateType.FINANCIAL_YEAR) {
            row(sheet, rowNum++, "Financial Year", request.getFinancialYear());
        } else {
            row(sheet, rowNum++, "From Date", request.getFromDate().toString());
            row(sheet, rowNum++, "To Date", request.getToDate().toString());
        }

        setColumnWidths(sheet, 2);
        log.debug("Summary sheet created");
    }

    private void row(HSSFSheet sheet, int rowNum, String key, String value) {
        HSSFRow row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(key);
        row.createCell(1).setCellValue(value);
    }

    private void createDataSheet(HSSFWorkbook workbook, ReportRequest request) {

        log.debug("Creating Report Data sheet");

        HSSFSheet sheet = workbook.createSheet("Report Data");
        int rowNum = 0;

        createHeader(sheet, request.getReportType());
        rowNum++;

        for (Long agencyId : request.getAgencyIds()) {

            log.debug("Fetching report rows for agencyId={}", agencyId);

            List<ExcelReportRow> rows =
                    reportDataService.prepareExcelRows(agencyId, request);

            log.debug("Fetched {} rows for agencyId={}", rows.size(), agencyId);

            for (ExcelReportRow data : rows) {
                HSSFRow row = sheet.createRow(rowNum++);
                writeRow(row, data, request.getReportType());
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

        row.createCell(col++).setCellValue(data.getReportType());

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
