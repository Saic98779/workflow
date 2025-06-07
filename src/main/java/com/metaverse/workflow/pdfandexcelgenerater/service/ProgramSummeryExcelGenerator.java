package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.program.repository.ProgramRepository;
import com.metaverse.workflow.program.service.ProgramService;
import com.metaverse.workflow.program.service.ProgramSummary;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public class ProgramSummeryExcelGenerator {

    @Autowired
    ProgramService programService;
    @Autowired
    ProgramRepository programRepository;

    public void generateProgramsExcel(HttpServletResponse response, Long programId) throws IOException, DataException {
        if (!programRepository.existsById(programId)) {
            throw new DataException("No summary data found for program ID: " + programId, "PROGRAM-NOT-FOUND", 400);
        }
        WorkflowResponse summary = programService.getProgramSummaryByProgramId(programId);
        ProgramSummary programSummary = (ProgramSummary) summary.getData();

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Program Summery");

        HSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        HSSFRow row = sheet.createRow(0);
        String[] headers = {
                "ProgramName", "AgencyName", "ParticipantName", "StartDate", "EndDate",
                "SC", "ST", "OC", "OBC", "Minorities", "Male", "Female", "Transgender",
                "physicallyChallenged", "NoOfSHG", "NoOfMSME", "NoOfStartups", "NoOfAspirants"
        };
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        HSSFRow dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(programSummary.getProgramName());
        dataRow.createCell(1).setCellValue(programSummary.getAgencyName());
        dataRow.createCell(2).setCellValue(programSummary.getParticipant());
        dataRow.createCell(3).setCellValue(programSummary.getStartDate());
        dataRow.createCell(4).setCellValue(programSummary.getEndDate());
        dataRow.createCell(5).setCellValue(programSummary.getSc());
        dataRow.createCell(6).setCellValue(programSummary.getSt());
        dataRow.createCell(7).setCellValue(programSummary.getOc());
        dataRow.createCell(8).setCellValue(programSummary.getObc());
        dataRow.createCell(9).setCellValue(programSummary.getMinorities());
        dataRow.createCell(10).setCellValue(programSummary.getMale());
        dataRow.createCell(11).setCellValue(programSummary.getFemale());
        dataRow.createCell(12).setCellValue(programSummary.getTransgender());
        dataRow.createCell(13).setCellValue(programSummary.getPhysicallyChallenge());
        dataRow.createCell(14).setCellValue(programSummary.getNoOfSHGs());
        dataRow.createCell(15).setCellValue(programSummary.getNoOfMSMEs());
        dataRow.createCell(16).setCellValue(programSummary.getNoOfStartups());
        dataRow.createCell(17).setCellValue(programSummary.getNoOfAspirants());

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }
}
