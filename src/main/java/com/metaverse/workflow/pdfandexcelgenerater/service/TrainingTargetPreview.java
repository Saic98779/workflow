package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.metaverse.workflow.agency.repository.AgencyRepository;
import jakarta.servlet.ServletOutputStream;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingTargetPreview {

    private final ReportDataService reportDataService;
    private final AgencyRepository agencyRepository;

    public void generateExcel(
            ReportRequest request,
            String createdBy,
            ServletOutputStream outputStream) throws IOException {

        HSSFWorkbook workbook = new HSSFWorkbook();

        createSummarySheet(workbook, request, createdBy);
        createDataSheet(workbook, request);

        workbook.write(outputStream);
        workbook.close();
    }

    private void createSummarySheet(HSSFWorkbook workbook, ReportRequest request, String createdBy) {
        HSSFSheet sheet = workbook.createSheet("Summary");
        int rowNum = 0;
        row(sheet, rowNum++, "Created By", createdBy);
        row(sheet, rowNum++, "Generated On", LocalDateTime.now().toString());
        String agencies = String.join(", ",
                agencyRepository.findAgencyNamesByIds(request.getAgencyIds()));

        row(sheet, rowNum++, "Agencies", agencies);
        row(sheet, rowNum++, "Report Type", request.getReportType().name());
        row(sheet, rowNum++, "Training Type", request.getTrainingType().name());
        if (request.getDateType() == DateType.FINANCIAL_YEAR) {
            row(sheet, rowNum++, "Financial Year", request.getFinancialYear());
        } else {
            row(sheet, rowNum++, "From Date", request.getFromDate().toString());
            row(sheet, rowNum++, "To Date", request.getToDate().toString());
        }
    }

    private void row(HSSFSheet sheet, int rowNum, String key, String value) {
        HSSFRow row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(key);
        row.createCell(1).setCellValue(value);
    }

    private void createDataSheet(HSSFWorkbook workbook, ReportRequest request) {

        HSSFSheet sheet = workbook.createSheet("Report Data");
        int rowNum = 0;

        // Header
        createHeader(sheet, request.getReportType());

        rowNum++;

        // Loop agencies (MULTI-AGENCY SUPPORT)
        for (Long agencyId : request.getAgencyIds()) {

            List<ExcelReportRow> rows =
                    reportDataService.prepareExcelRows(
                            agencyId,
                            request
                    );

            for (ExcelReportRow data : rows) {
                HSSFRow row = sheet.createRow(rowNum++);
                writeRow(row, data, request.getReportType());
            }
        }

        autoSize(sheet);
    }

    private void createHeader(HSSFSheet sheet, ReportType reportType) {

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

    private void autoSize(HSSFSheet sheet) {
        if (sheet.getRow(0) == null) return;

        int lastCol = sheet.getRow(0).getLastCellNum();
        for (int i = 0; i < lastCol; i++) {
            sheet.autoSizeColumn(i);
        }
    }

}