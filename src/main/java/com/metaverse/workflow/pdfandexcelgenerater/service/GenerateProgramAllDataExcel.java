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

@Service
@RequiredArgsConstructor
public class GenerateProgramAllDataExcel {

    private final ProgramRepository repository;
    private final ProgramExpenditureRepository expenditureRepository;
    private final BulkExpenditureTransactionRepository transactionRepository;

    public void generateProgramsExcel(HttpServletResponse response, Long agencyId) throws IOException, DataException {

        List<Program> programList = repository.getAllPrograms(agencyId);

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Program Details");

        HSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        HSSFRow row = sheet.createRow(0);
        String[] headers = {
                "Start Date", "End Date", "Current Status", "No.of Sessions", "No.of Participant", "Total Expenditure",
                "Program Title", "Head Of Expense", "Agency Name", "Start Time", "End Time", "Spoc Name", "Spoc Contact No",
                "District", "Program Location Name"
        };


        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        int dataRowIndex = 1;
        for (Program res : programList) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
             dataRow.createCell(0).setCellValue(res.getStartDate().toString().substring(0,10));
            dataRow.createCell(1).setCellValue(res.getEndDate().toString().substring(0,10));
            dataRow.createCell(2).setCellValue(res.getStatus());
            dataRow.createCell(3).setCellValue(res.getProgramSessionList().size());
            dataRow.createCell(4).setCellValue(res.getParticipants().size());
            dataRow.createCell(5).setCellValue(getTotalExpenditure(res.getProgramId()));
            dataRow.createCell(6).setCellValue(res.getProgramTitle());
            dataRow.createCell(7).setCellValue(res.getProgramType());
            dataRow.createCell(8).setCellValue(res.getAgency().getAgencyName());
            dataRow.createCell(9).setCellValue(res.getStartTime());
            dataRow.createCell(10).setCellValue(res.getEndTime());
            dataRow.createCell(11).setCellValue(res.getSpocName());
            dataRow.createCell(12).setCellValue(res.getSpocContactNo());
            dataRow.createCell(13).setCellValue(res.getLocation().getLocationName());
            dataRow.createCell(14).setCellValue(res.getLocation().getDistrict());
            dataRowIndex++;
        }
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
