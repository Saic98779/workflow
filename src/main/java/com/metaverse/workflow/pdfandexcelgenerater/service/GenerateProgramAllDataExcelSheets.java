package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.metaverse.workflow.common.enums.ExpenditureType;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.expenditure.repository.BulkExpenditureTransactionRepository;
import com.metaverse.workflow.expenditure.repository.ProgramExpenditureRepository;
import com.metaverse.workflow.model.BulkExpenditureTransaction;
import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.model.ProgramExpenditure;
import com.metaverse.workflow.program.repository.ProgramRepository;
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
public class GenerateProgramAllDataExcelSheets {
    private final ProgramRepository repository;
    private final ProgramExpenditureRepository expenditureRepository;
    private final BulkExpenditureTransactionRepository transactionRepository;

    public void generateProgramsExcel(HttpServletResponse response) throws IOException, DataException {
        List<Program> programList = repository.findAllByOrderByStartDateAsc();

        // Group programs by agency
        Map<String, List<Program>> programsByAgency = programList.stream()
                .collect(Collectors.groupingBy(p -> p.getAgency().getAgencyName()));

        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        String[] headers = {
               "Sl. No", "Program Title", "Head Of Expense", "Start Date", "End Date", "Current Status",
                "No.of Sessions", "No.of Participants", "Total Expenditure","Start Time", "End Time",
                  "Spoc Name", "Spoc Contact No", "District", "Program Location Name"
        };

        // Create one sheet per agency
        for (Map.Entry<String, List<Program>> entry : programsByAgency.entrySet()) {
            String agencyName = entry.getKey();
            List<Program> agencyPrograms = entry.getValue();

            // Excel sheet name can't exceed 31 characters or contain special characters
            //String sheetName = agencyName.length() > 31 ? agencyName.substring(0, 31) : agencyName.replaceAll("[\\\\/?*\\[\\]]", "_");
            HSSFSheet sheet = workbook.createSheet(agencyName);

            // Create header row
            HSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Fill data rows
            int rowIndex = 1;
            int slNo = 1;
            for (Program res : agencyPrograms) {
                HSSFRow dataRow = sheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(slNo++);
                dataRow.createCell(1).setCellValue(res.getProgramTitle());
                dataRow.createCell(2).setCellValue(res.getProgramType());
                dataRow.createCell(3).setCellValue(res.getStartDate().toString().substring(0, 10));
                dataRow.createCell(4).setCellValue(res.getEndDate().toString().substring(0, 10));
                dataRow.createCell(5).setCellValue(res.getStatus());
                dataRow.createCell(6).setCellValue(res.getProgramSessionList().size());
                dataRow.createCell(7).setCellValue(res.getParticipants().size());
                dataRow.createCell(8).setCellValue(getTotalExpenditure(res.getProgramId()));
                dataRow.createCell(9).setCellValue(res.getStartTime());
                dataRow.createCell(10).setCellValue(res.getEndTime());
                dataRow.createCell(11).setCellValue(res.getSpocName());
                dataRow.createCell(12).setCellValue(res.getSpocContactNo());
                dataRow.createCell(13).setCellValue(res.getLocation().getDistrict());
                dataRow.createCell(14).setCellValue(res.getLocation().getLocationName());
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
        }

        // Final write
        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }
    public Double getTotalExpenditure(Long programId) {
        List<ProgramExpenditure> post = expenditureRepository.findByExpenditureTypeAndProgram_ProgramId(ExpenditureType.POST, programId);
        List<ProgramExpenditure> pre = expenditureRepository.findByExpenditureTypeAndProgram_ProgramId(ExpenditureType.PRE, programId);
        List<BulkExpenditureTransaction> bulk = transactionRepository.findByProgram_ProgramId(programId);
        Double postCost = post.stream().mapToDouble(ProgramExpenditure::getCost).sum();
        Double preCost = pre.stream().mapToDouble(ProgramExpenditure::getCost).sum();
        Double bulkCost = bulk.stream().mapToDouble(BulkExpenditureTransaction::getAllocatedCost).sum();

        return postCost + preCost + bulkCost;
    }
}
