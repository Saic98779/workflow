package com.metaverse.workflow.pdfandexcelgenerater.service;


import com.lowagie.text.Font;
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
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

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
                "Name of the IA",	"Budget Head",	"Name of the Program", "Start Date","End Date","Total Participants","Male",
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
            dataRow.createCell(3).setCellValue(program.getStartDate());
            dataRow.createCell(4).setCellValue(program.getEndDate());
            dataRow.createCell(5).setCellValue(program.getParticipants().size());
            dataRow.createCell(6).setCellValue(program.getParticipants().stream()
                    .filter(participant -> participant.getGender() != null && participant.getGender() == 'M')
                    .count());
            dataRow.createCell(7).setCellValue(program.getParticipants().stream()
                    .filter(participant -> participant.getGender() != null && participant.getGender() == 'F')
                    .count());
            dataRow.createCell(8).setCellValue(program.getParticipants().stream()
                    .filter(participant -> participant.getGender() != null && participant.getGender() == 'T')
                    .count());
            dataRow.createCell(9).setCellValue(program.getParticipants().stream()
                    .filter(participant -> "sc".equalsIgnoreCase(participant.getCategory()))
                    .count());

            dataRow.createCell(10).setCellValue(program.getParticipants().stream()
                    .filter(participant -> "st".equalsIgnoreCase(participant.getCategory()))
                    .count());
            dataRow.createCell(11).setCellValue(program.getParticipants().stream()
                    .filter(participant -> "bc".equalsIgnoreCase(participant.getCategory()))
                    .count());
            dataRow.createCell(12).setCellValue(program.getParticipants().stream()
                    .filter(participant -> "oc".equalsIgnoreCase(participant.getCategory()))
                    .count());
            dataRow.createCell(13).setCellValue(program.getParticipants().stream()
                    .filter(participant -> "minorities".equalsIgnoreCase(participant.getCategory()))
                    .count());
            dataRow.createCell(14).setCellValue(program.getParticipants().stream()
                    .filter(participant -> participant.getDisability() != null && participant.getDisability() == 'Y')
                    .count());
            dataRow.createCell(15).setCellValue(expenditureList.stream().mapToDouble(ProgramExpenditure::getCost).sum() +
                    transactionList.stream().mapToDouble(BulkExpenditureTransaction::getAllocatedCost).sum());
              dataRowIndex++;
        }
        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }

    public void generateProgramsParticipantPdf(HttpServletResponse response, Long agencyId) throws IOException {
        List<Program> programSet = (agencyId == -1)
                ? programRepository.findAll()
                : programRepository.findByAgencyAgencyId(agencyId);

        Set<Program> programList = new LinkedHashSet<>(programSet);

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Title
        Font titleFont = new Font(Font.HELVETICA, 14, Font.BOLD);
        Paragraph title = new Paragraph("Program Participant Details", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20f);
        document.add(title);

        // Headers
        String[] headers = {
                "Name of the IA", "Budget Head", "Name of the Program", "Total Participants", "Male",
                "Female", "Trans Gender", "SC", "ST", "BC", "OC", "Minorities",
                "Physically Challenged", "Total Expenditure"
        };

        PdfPTable table = new PdfPTable(headers.length);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        float[] columnWidths = {2f, 2f, 4f, 2f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 2f, 2f};
        table.setWidths(columnWidths);

        Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(Color.GREEN);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }

        Font dataFont = new Font(Font.HELVETICA, 9);

        // Data rows
        for (Program program : programList) {
            List<ProgramExpenditure> expenditureList =
                    expenditureRepository.findByAgency_AgencyIdAndProgram_ProgramId(
                            program.getAgency().getAgencyId(), program.getProgramId()
                    );

            List<BulkExpenditureTransaction> transactionList =
                    transactionRepository.findByProgram_ProgramId(program.getProgramId());

            table.addCell(new Phrase(program.getAgency().getAgencyName(), dataFont));
            table.addCell(new Phrase(program.getProgramType(), dataFont));
            table.addCell(new Phrase(program.getProgramTitle(), dataFont));

            // Participant calculations
            long total = program.getParticipants().size();
            long male = program.getParticipants().stream()
                    .filter(p -> "M".equalsIgnoreCase(String.valueOf(p.getGender()))).count();
            long female = program.getParticipants().stream()
                    .filter(p -> "F".equalsIgnoreCase(String.valueOf(p.getGender()))).count();
            long trans = program.getParticipants().stream()
                    .filter(p -> "T".equalsIgnoreCase(String.valueOf(p.getGender()))).count();

            long sc = program.getParticipants().stream()
                    .filter(p -> "sc".equalsIgnoreCase(p.getCategory())).count();
            long st = program.getParticipants().stream()
                    .filter(p -> "st".equalsIgnoreCase(p.getCategory())).count();
            long bc = program.getParticipants().stream()
                    .filter(p -> "bc".equalsIgnoreCase(p.getCategory())).count();
            long oc = program.getParticipants().stream()
                    .filter(p -> "oc".equalsIgnoreCase(p.getCategory())).count();
            long minorities = program.getParticipants().stream()
                    .filter(p -> "minorities".equalsIgnoreCase(p.getCategory())).count();
            long disabled = program.getParticipants().stream()
                    .filter(p -> "Y".equalsIgnoreCase(String.valueOf(p.getDisability()))).count();

            double totalExpenditure = expenditureList.stream().mapToDouble(ProgramExpenditure::getCost).sum()
                    + transactionList.stream().mapToDouble(BulkExpenditureTransaction::getAllocatedCost).sum();

            // Add data to table
            table.addCell(new Phrase(String.valueOf(total), dataFont));
            table.addCell(new Phrase(String.valueOf(male), dataFont));
            table.addCell(new Phrase(String.valueOf(female), dataFont));
            table.addCell(new Phrase(String.valueOf(trans), dataFont));
            table.addCell(new Phrase(String.valueOf(sc), dataFont));
            table.addCell(new Phrase(String.valueOf(st), dataFont));
            table.addCell(new Phrase(String.valueOf(bc), dataFont));
            table.addCell(new Phrase(String.valueOf(oc), dataFont));
            table.addCell(new Phrase(String.valueOf(minorities), dataFont));
            table.addCell(new Phrase(String.valueOf(disabled), dataFont));
            table.addCell(new Phrase(String.format("%.2f", totalExpenditure), dataFont));
        }

        document.add(table);
        document.close();
    }

}
