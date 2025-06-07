package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.expenditure.service.BulkTransactions;
import com.metaverse.workflow.expenditure.service.ExpenditureService;
import com.metaverse.workflow.expenditure.service.ProgramExpenditureResponse;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ExpenditureExcelGenerator {

    @Autowired
    ExpenditureService expenditureService;

    public void generateProgramsExcel(HttpServletResponse response, Long programId, Long agencyId) throws IOException {
        List<ProgramExpenditureResponse> ListOfProgramExpenditure = expenditureService.getAllProgramExpenditure(agencyId, programId);
        WorkflowResponse transaction = expenditureService.getAllBulkExpenditureTransactionByProgram(programId);
        List<BulkTransactions> bulkTransactionsList = (List<BulkTransactions>) transaction.getData();

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet preSheet = workbook.createSheet("Pre Expenditure");
        HSSFSheet postSheet = workbook.createSheet("Post Expenditure");
        HSSFSheet bulkSheet = workbook.createSheet("Bulk Transactions");

        HSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        String[] programHeaders = {
                "programExpenditureId", "activityId", "subActivityId", "programId", "agencyId",
                "activityName", "subActivityName", "programName", "agencyName", "expenditureType",
                "headOfExpense", "cost", "billNo", "billDate", "payeeName", "bankName", "ifscCode",
                "transactionId", "modeOfPayment", "purpose", "uploadBillUrl"
        };

        String[] bulkHeaders = {
                "bulkExpenditureTransactionId", "itemName", "purchasedQuantity", "unitCost", "bulkExpenditureId",
                "purchaseDate", "consumedQuantity", "availableQuantity", "expenditureType", "headOfExpense",
                "allocatedCost", "billNo", "billDate", "payeeName", "bankName", "ifscCode", "modeOfPayment",
                "remarks", "uploadBillUrl"
        };

        // Write headers for each sheet
        HSSFRow preHeaderRow = preSheet.createRow(0);
        HSSFRow postHeaderRow = postSheet.createRow(0);
        HSSFRow bulkHeaderRow = bulkSheet.createRow(0);

        for (int i = 0; i < programHeaders.length; i++) {
            HSSFCell preCell = preHeaderRow.createCell(i);
            preCell.setCellValue(programHeaders[i]);
            preCell.setCellStyle(headerStyle);

            HSSFCell postCell = postHeaderRow.createCell(i);
            postCell.setCellValue(programHeaders[i]);
            postCell.setCellStyle(headerStyle);
        }

        for (int i = 0; i < bulkHeaders.length; i++) {
            HSSFCell cell = bulkHeaderRow.createCell(i);
            cell.setCellValue(bulkHeaders[i]);
            cell.setCellStyle(headerStyle);
        }

        int preRowIndex = 1;
        int postRowIndex = 1;

        // Write ProgramExpenditure data to appropriate sheet
        for (ProgramExpenditureResponse res : ListOfProgramExpenditure) {
            HSSFRow dataRow;
            if ("pre".equalsIgnoreCase(res.getExpenditureType())) {
                dataRow = preSheet.createRow(preRowIndex++);
            } else if ("post".equalsIgnoreCase(res.getExpenditureType())) {
                dataRow = postSheet.createRow(postRowIndex++);
            } else {
                continue; // skip unknown types
            }

            dataRow.createCell(0).setCellValue(res.getProgramExpenditureId());
            dataRow.createCell(1).setCellValue(res.getActivityId());
            dataRow.createCell(2).setCellValue(res.getSubActivityId());
            dataRow.createCell(3).setCellValue(res.getProgramId());
            dataRow.createCell(4).setCellValue(res.getAgencyId());
            dataRow.createCell(5).setCellValue(res.getActivityName());
            dataRow.createCell(6).setCellValue(res.getSubActivityName());
            dataRow.createCell(7).setCellValue(res.getProgramName());
            dataRow.createCell(8).setCellValue(res.getAgencyName());
            dataRow.createCell(9).setCellValue(res.getExpenditureType());
            dataRow.createCell(10).setCellValue(res.getHeadOfExpense());
            dataRow.createCell(11).setCellValue(res.getCost());
            dataRow.createCell(12).setCellValue(res.getBillNo());
            dataRow.createCell(13).setCellValue(res.getBillDate());
            dataRow.createCell(14).setCellValue(res.getPayeeName());
            dataRow.createCell(15).setCellValue(res.getBankName());
            dataRow.createCell(16).setCellValue(res.getIfscCode());
            dataRow.createCell(17).setCellValue(res.getTransactionId());
            dataRow.createCell(18).setCellValue(res.getModeOfPayment());
            dataRow.createCell(19).setCellValue(res.getPurpose());
            dataRow.createCell(20).setCellValue(res.getUploadBillUrl());
        }

        // Write BulkTransactions to bulkSheet
        int bulkRowIndex = 1;
        for (BulkTransactions tx : bulkTransactionsList) {
            HSSFRow dataRow = bulkSheet.createRow(bulkRowIndex++);
            dataRow.createCell(0).setCellValue(tx.getBulkExpenditureTransactionId());
            dataRow.createCell(1).setCellValue(tx.getItemName());
            dataRow.createCell(2).setCellValue(tx.getPurchasedQuantity());
            dataRow.createCell(3).setCellValue(tx.getUnitCost());
            dataRow.createCell(4).setCellValue(tx.getBulkExpenditureId());
            dataRow.createCell(5).setCellValue(tx.getPurchaseDate());
            dataRow.createCell(6).setCellValue(tx.getConsumedQuantity());
            dataRow.createCell(7).setCellValue(tx.getAvailableQuantity());
            dataRow.createCell(8).setCellValue(tx.getExpenditureType());
            dataRow.createCell(9).setCellValue(tx.getHeadOfExpense());
            dataRow.createCell(10).setCellValue(tx.getAllocatedCost());
            dataRow.createCell(11).setCellValue(tx.getBillNo());
            dataRow.createCell(12).setCellValue(tx.getBillDate());
            dataRow.createCell(13).setCellValue(tx.getPayeeName());
            dataRow.createCell(14).setCellValue(tx.getBankName());
            dataRow.createCell(15).setCellValue(tx.getIfscCode());
            dataRow.createCell(16).setCellValue(tx.getModeOfPayment());
            dataRow.createCell(17).setCellValue(tx.getRemarks());
            dataRow.createCell(18).setCellValue(tx.getUploadBillUrl());
        }

        // Export Excel
        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }

}
