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
@Service
@RequiredArgsConstructor
public class ProgramParticipantDetails {
    private final ProgramRepository programRepository;
    private final ProgramExpenditureRepository expenditureRepository;
    private final BulkExpenditureTransactionRepository transactionRepository;

    public void generateProgramsParticipantExcel(HttpServletResponse response, Long agencyId) throws IOException {
        List<Program> programList ;
        if(agencyId == -1)
        {
            programList = programRepository.findAll();
        }else{
            programList = programRepository.findByAgencyAgencyId(agencyId);
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Program Participant Details");

        HSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        HSSFRow row = sheet.createRow(0);
        String[] headers = {
                "Name of the IA",	"Budget Head",	"Name of the Program","Total Participants","Male",
                "Female","Trans Gender"	,"SC",	"ST",	"BC","OC","Minorities","Physically Challenged","Total Expenditure"
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
            dataRow.createCell(3).setCellValue(program.getParticipants().size());
            dataRow.createCell(4).setCellValue(program.getParticipants().stream()
                    .filter(participant -> participant.getGender() != null && participant.getGender() == 'M')
                    .count());
            dataRow.createCell(5).setCellValue(program.getParticipants().stream()
                    .filter(participant -> participant.getGender() != null && participant.getGender() == 'F')
                    .count());
            dataRow.createCell(6).setCellValue(program.getParticipants().stream()
                    .filter(participant -> participant.getGender() != null && participant.getGender() == 'T')
                    .count());
            dataRow.createCell(7).setCellValue(program.getParticipants().stream()
                    .filter(participant -> "sc".equalsIgnoreCase(participant.getCategory()))
                    .count());

            dataRow.createCell(8).setCellValue(program.getParticipants().stream()
                    .filter(participant -> "st".equalsIgnoreCase(participant.getCategory()))
                    .count());
            dataRow.createCell(9).setCellValue(program.getParticipants().stream()
                    .filter(participant -> "bc".equalsIgnoreCase(participant.getCategory()))
                    .count());
            dataRow.createCell(10).setCellValue(program.getParticipants().stream()
                    .filter(participant -> "oc".equalsIgnoreCase(participant.getCategory()))
                    .count());
            dataRow.createCell(11).setCellValue(program.getParticipants().stream()
                    .filter(participant -> "minorities".equalsIgnoreCase(participant.getCategory()))
                    .count());
            dataRow.createCell(12).setCellValue(program.getParticipants().stream()
                    .filter(participant -> participant.getDisability() != null && participant.getDisability() == 'Y')
                    .count());
            dataRow.createCell(13).setCellValue(expenditureList.stream().mapToDouble(ProgramExpenditure::getCost).sum() +
                    transactionList.stream().mapToDouble(BulkExpenditureTransaction::getAllocatedCost).sum());
              dataRowIndex++;
        }
        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }
}
