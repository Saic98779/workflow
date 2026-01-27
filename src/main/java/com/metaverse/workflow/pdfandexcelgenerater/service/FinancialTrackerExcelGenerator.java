package com.metaverse.workflow.pdfandexcelgenerater.service;


import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.expenditure.repository.BulkExpenditureTransactionRepository;
import com.metaverse.workflow.expenditure.repository.ProgramExpenditureRepository;
import com.metaverse.workflow.model.BulkExpenditureTransaction;
import com.metaverse.workflow.model.ProgramExpenditure;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
@Service
@RequiredArgsConstructor
public class FinancialTrackerExcelGenerator {
    private final ProgramExpenditureRepository programExpenditureRepository;
    private final BulkExpenditureTransactionRepository transactionRepository;

    public void financialTrackerExportToExcel(HttpServletResponse response) throws IOException {
        List<FinancialTracker> financialTrackers = buildFinancialTrackerData();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Financial Tracker");

        HSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        HSSFRow headerRow = sheet.createRow(0);
        String[] headers = {"Agency Name","Total Bills Uploaded","Approved","Rejected","Need Clarification","Total Bills Verified"};

        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        int rowIndex = 1;
        for (FinancialTracker res : financialTrackers) {
            HSSFRow dataRow = sheet.createRow(rowIndex++);
            setCellValue(dataRow, 0, res.getAgencyName());
            setCellValue(dataRow, 1, res.getTotalBillsUploaded());
            setCellValue(dataRow, 2, res.getApproved());
            setCellValue(dataRow, 3, res.getRejected());
            setCellValue(dataRow, 4, res.getNeedClarification());
            setCellValue(dataRow, 5, res.getTotalBillsUploaded());
        }
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    // Helper method for null-safe value setting
    private void setCellValue(HSSFRow row, int columnIndex, Object value) {
        HSSFCell cell = row.createCell(columnIndex);
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Date) {
            String formattedDate = DateUtil.dateToString((Date) value, "dd-MM-yyyy");
            cell.setCellValue(formattedDate);
        } else {
            cell.setCellValue(value.toString());
        }
    }
    private List<FinancialTracker> buildFinancialTrackerData() {
        try {
            System.out.print("ndfjv cmndfvdfkjnbvds");
            List<ProgramExpenditure> programExpenditures = programExpenditureRepository.findAll();
            List<BulkExpenditureTransaction> bulkTransactions = transactionRepository.findAll();

            Map<Long, FinancialTracker> agencyMap = new HashMap<>();

            // -------- Program Expenditure --------
            for (ProgramExpenditure pe : programExpenditures) {
                if (pe.getAgency() == null) continue;

                Long agencyId = pe.getAgency().getAgencyId();
                FinancialTracker tracker = agencyMap.computeIfAbsent(agencyId, id -> {
                    FinancialTracker ft = new FinancialTracker();
                    ft.setAgencyName(pe.getAgency().getAgencyName());
                    ft.setTotalBillsUploaded(0.0);
                    ft.setApproved(0.0);
                    ft.setRejected(0.0);
                    ft.setNeedClarification(0.0);
                    ft.setTotalBillsVerified(0.0);
                    return ft;
                });

                Double amount = pe.getCost() != null ? pe.getCost() : 0.0;
                tracker.setTotalBillsUploaded(tracker.getTotalBillsUploaded() + amount);

                if (pe.getStatus() != null) {
                    switch (pe.getStatus()) {
                        case APPROVED ->
                                tracker.setApproved(tracker.getApproved() + amount);
                        case REJECTED ->
                                tracker.setRejected(tracker.getRejected() + amount);
                        case NEED_CLARIFICATION ->
                                tracker.setNeedClarification(tracker.getNeedClarification() + amount);
                    }
                }
            }

            // -------- Bulk Expenditure --------
            for (BulkExpenditureTransaction bt : bulkTransactions) {
                if (bt.getAgency() == null) continue;

                Long agencyId = bt.getAgency().getAgencyId();
                FinancialTracker tracker = agencyMap.computeIfAbsent(agencyId, id -> {
                    FinancialTracker ft = new FinancialTracker();
                    ft.setAgencyName(bt.getAgency().getAgencyName());
                    ft.setTotalBillsUploaded(0.0);
                    ft.setApproved(0.0);
                    ft.setRejected(0.0);
                    ft.setNeedClarification(0.0);
                    ft.setTotalBillsVerified(0.0);
                    return ft;
                });

                Double amount = bt.getAllocatedCost() != null ? bt.getAllocatedCost() : 0.0;
                tracker.setTotalBillsUploaded(tracker.getTotalBillsUploaded() + amount);

                if (bt.getStatus() != null) {
                    switch (bt.getStatus()) {
                        case APPROVED ->
                                tracker.setApproved(tracker.getApproved() + amount);
                        case REJECTED ->
                                tracker.setRejected(tracker.getRejected() + amount);
                        case NEED_CLARIFICATION ->
                                tracker.setNeedClarification(tracker.getNeedClarification() + amount);
                    }
                }
            }

            // -------- Final Calculation --------
            agencyMap.values().forEach(ft ->
                    ft.setTotalBillsVerified(
                            ft.getApproved() + ft.getRejected() + ft.getNeedClarification()
                    )
            );

            return new ArrayList<>(agencyMap.values());

        } catch (Exception e) {
            // log properly in real apps
            e.printStackTrace();
            throw new RuntimeException("Error while building Financial Tracker data", e);
        }
    }


}
