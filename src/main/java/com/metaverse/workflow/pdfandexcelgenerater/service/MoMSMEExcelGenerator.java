package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.metaverse.workflow.MoMSMEReport.service.MoMSMERowDTO;
import com.metaverse.workflow.MoMSMEReport.service.TiHclMoMSMEReportService;
import com.metaverse.workflow.activity.repository.ActivityRepository;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.Activity;
import com.metaverse.workflow.model.NonTrainingActivity;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.SubActivity;
import com.metaverse.workflow.nontraining.repository.NonTrainingActivityRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MoMSMEExcelGenerator {
    private final TiHclMoMSMEReportService tiHclMoMSMEReportService;
    private final ActivityRepository activityRepository;
    private final NonTrainingActivityRepository nonTrainingActivityRepository;

    public void generateExcel(Long agencyId, HttpServletResponse response) throws IOException, DataException {
        List<MoMSMERowDTO> rows = new ArrayList<>();
        List<Activity> activities = activityRepository.findByAgencyAgencyId(agencyId);
        List<Long> subActivityIds = activities.stream()
                .flatMap(activity -> activity.getSubActivities().stream())
                .map(SubActivity::getSubActivityId)
                .toList();
        for (Long subActivityId : subActivityIds) {
            rows.add(tiHclMoMSMEReportService.getMoMSMERowData(subActivityId, "Training"));
        }
        List<NonTrainingActivity> nonTrainingActivities = nonTrainingActivityRepository.findByAgency_AgencyId(agencyId);
        List<Long> nonTrainingSubActivityIds = nonTrainingActivities.stream().flatMap(activity -> activity.getSubActivities().stream())
                .map(NonTrainingSubActivity::getSubActivityId)
                .toList();
        for (Long subActivityId : nonTrainingSubActivityIds) {
            rows.add(tiHclMoMSMEReportService.getMoMSMERowData(subActivityId, "NonTraining"));
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("MoMSME Report");


        CellStyle headerStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);


        String[] headers = {
                "SN", "Activity Name", "Sub Activity Name", "Total Target",
                "Achievement upto last month (upto Sept '25)", "Achivement during the month (During Oct'25)",
                "Cumulative achivement", "% Achievement ", "MSMEs Benefited", "Amount Approved",
                "Expenditure upto last month (upto Sept, 25)", "Expenditure during the month (During Oct'25)",
                "Cumulative Expenditure", "% of Expenditure"
        };

        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowCount = 1;
        int sn = 1;

        for (MoMSMERowDTO dto : rows) {
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(sn++);
            row.createCell(1).setCellValue(dto.getActivityName());
            row.createCell(2).setCellValue(dto.getSubActivityName());
            row.createCell(3).setCellValue(dto.getTotalTarget());
            row.createCell(4).setCellValue(getSafeInt(dto.getAchievementLastMonth()));
            row.createCell(5).setCellValue(getSafeInt(dto.getAchievementDuringMonth()));
            row.createCell(6).setCellValue(getSafeInt(dto.getCumulativeAchievement()));
            row.createCell(7).setCellValue(dto.getAchievementPercentage());
            row.createCell(8).setCellValue(getSafeInt(dto.getMsmesBenefited()));
            row.createCell(9).setCellValue(getSafeDouble(dto.getAmountApproved()));
            row.createCell(10).setCellValue(getSafeDouble(dto.getExpenditureLastMonth()));
            row.createCell(11).setCellValue(getSafeDouble(dto.getExpenditureDuringMonth()));
            row.createCell(12).setCellValue(getSafeDouble(dto.getCumulativeExpenditure()));
            row.createCell(13).setCellValue(dto.getExpenditurePercentage());
        }

        // Auto-size all columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // ===== WRITE TO RESPONSE =====
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=msme-report.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    private int getSafeInt(Integer value) {
        return value == null ? 0 : value;
    }

    private double getSafeDouble(Double value) {
        return value == null ? 0.0 : value;
    }
}
