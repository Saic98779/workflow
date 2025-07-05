package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.expenditure.service.CombinedExpenditure;
import com.metaverse.workflow.expenditure.service.ExpenditureService;
import com.metaverse.workflow.expenditure.service.ExpenditureSummaryResponse;
import com.metaverse.workflow.expenditure.service.HeadWiseExpenditureSummary;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.stereotype.Service;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class GenerateCombinedExpenditureExcel {

    private final ExpenditureService expenditureService;

    public void generateCombinedExpenditureExcel(HttpServletResponse response, Long programId) throws IOException, DataException {
        ExpenditureSummaryResponse data = expenditureService.getExpenditureHeadOfExpenseWise(programId);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Expenditure Summary");

        HSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        String[] headers = {"Head of Expense", "Date", "Type", "Amount", "Bill No", "Bill Date", "Payee Name", "Bank", "IFSC Code", "Mode of Payment", "Total"};
        HSSFRow headerRow = sheet.createRow(0);

        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowIndex = 1;

        for (HeadWiseExpenditureSummary summary : data.getSummaries()) {
            // Section header with headOfExpense
            HSSFRow headRow = sheet.createRow(rowIndex++);
            headRow.createCell(0).setCellValue(summary.getHeadOfExpense());

            for (CombinedExpenditure exp : summary.getExpenditures()) {
                HSSFRow row = sheet.createRow(rowIndex++);
                row.createCell(1).setCellValue(exp.getBillDate() != null ? exp.getBillDate() : "");
                row.createCell(2).setCellValue(exp.getExpenditureType().toString());
                row.createCell(3).setCellValue(exp.getCost());
                row.createCell(4).setCellValue(exp.getBillNo());
                row.createCell(5).setCellValue(exp.getBillDate());
                row.createCell(6).setCellValue(exp.getPayeeName());
                row.createCell(7).setCellValue(exp.getBankName() != null ? exp.getBankName() : "");
                row.createCell(8).setCellValue(exp.getIfscCode() != null ? exp.getIfscCode() : "");
                row.createCell(9).setCellValue(exp.getModeOfPayment());
            }

            // Total row for each head
            HSSFRow totalRow = sheet.createRow(rowIndex++);
            totalRow.createCell(2).setCellValue("Total");
            totalRow.createCell(10).setCellValue(summary.getTotalCost());
        }

        // Empty row before grand total
        rowIndex++;

        // Grand Total row
        HSSFRow grandTotalRow = sheet.createRow(rowIndex);
        grandTotalRow.createCell(2).setCellValue("Grand Total");
        grandTotalRow.createCell(10).setCellValue(data.getGrandTotal());

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Output to browser
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=combined_expenditure.xls");

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }

}
