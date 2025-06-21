package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.program.repository.ProgramRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
@RequiredArgsConstructor
@Service
public class TrainingCalendarDatewise {

    private final ProgramRepository programRepository;

    public void generateProgramsExcel(HttpServletResponse response,Long agencyId, Date startDate, Date endDate) throws IOException {
        List<Program> programList;
        if (agencyId == -1) {
            programList = programRepository.findByStartDateBetween(startDate,endDate);
        } else {
            programList = programRepository.findByAgency_AgencyIdAndStartDateBetween(agencyId,startDate,endDate);
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Program Details");

        HSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        HSSFRow row = sheet.createRow(0);
        String[] headers = {"Start date", "End date","Name of the IA", "District", "Mandal", "Budget Head", "Name Of The Program", "Venue"

        };


        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        int dataRowIndex = 1;
        for (Program program : programList) {

            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(program.getStartDate().toString().substring(0, 10));
            dataRow.createCell(1).setCellValue(program.getEndDate().toString().substring(0, 10));
            dataRow.createCell(2).setCellValue(program.getAgency().getAgencyName());
            dataRow.createCell(3).setCellValue(program.getLocation().getDistrict());
            dataRow.createCell(4).setCellValue(program.getLocation().getMandal());
            dataRow.createCell(5).setCellValue(program.getProgramType());
            dataRow.createCell(6).setCellValue(program.getProgramTitle());
            dataRow.createCell(7).setCellValue(program.getLocation().getTypeOfVenue());
            dataRowIndex++;
        }
        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }
}
