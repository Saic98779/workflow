package com.metaverse.workflow.pdfandexcelgenerater.service;

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
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ProgramStatusGenerator {


    private final ProgramRepository programRepository;
    private final ProgramExpenditureRepository expenditureRepository;
    private final BulkExpenditureTransactionRepository transactionRepository;

    public void generateProgramsExcel(HttpServletResponse response, Long agencyId) throws IOException {
        List<Program> programList;
        if (agencyId == -1) {
            programList = programRepository.findAll();
        } else {
            programList = programRepository.findByAgencyAgencyId(agencyId);
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Program Details");

        HSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        HSSFRow row = sheet.createRow(0);
        String[] headers = {
                "Name of the IA", "Budget Head", "Name Of The Program", "No of sessions added",
                "No of resource persons participated", "No of participants added", "Attendance updated for No participants",
                "No of images uploaded", "No of Media images uploaded", "Total Expenditure updated ", "Final submission status "
        };


        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        int dataRowIndex = 1;
        for (Program program : programList) {
            List<ProgramExpenditure> expenditureList = expenditureRepository.findByAgency_AgencyIdAndProgram_ProgramId(program.getAgency().getAgencyId(), program.getProgramId());
            List<BulkExpenditureTransaction> transactionList = transactionRepository.findByProgram_ProgramId(program.getProgramId());

            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(program.getAgency().getAgencyName());
            dataRow.createCell(1).setCellValue(program.getProgramType());
            dataRow.createCell(2).setCellValue(program.getProgramTitle());
            dataRow.createCell(3).setCellValue(program.getProgramSessionList().size());
            dataRow.createCell(4).setCellValue(program.getProgramSessionList().stream()
                    .map(session -> session.getResource().getResourceId())
                    .filter(Objects::nonNull)
                    .distinct()
                    .count());
            dataRow.createCell(5).setCellValue(program.getParticipants().size());
            dataRow.createCell(6).setCellValue(program.getParticipants().size());
            dataRow.createCell(7).setCellValue(program.getProgramSessionList().stream()
                    .mapToLong(session ->
                            (session.getImage1() != null ? 1 : 0) +
                                    (session.getImage2() != null ? 1 : 0) +
                                    (session.getImage3() != null ? 1 : 0) +
                                    (session.getImage4() != null ? 1 : 0) +
                                    (session.getImage5() != null ? 1 : 0)
                    ).sum());
            dataRow.createCell(8).setCellValue(program.getMediaCoverageList().size());
            dataRow.createCell(9).setCellValue(expenditureList.stream().mapToDouble(ProgramExpenditure::getCost).sum() +
                    transactionList.stream().mapToDouble(BulkExpenditureTransaction::getAllocatedCost).sum());
            dataRow.createCell(10).setCellValue(program.getStatus().equals("Program Expenditure Updated") ? "YES" : "NO");
            dataRowIndex++;
        }
        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }
}
