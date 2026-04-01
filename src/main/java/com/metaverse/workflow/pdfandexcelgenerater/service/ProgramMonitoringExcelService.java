package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.metaverse.workflow.ProgramMonitoring.repository.ProgramMonitoringRepo;
import com.metaverse.workflow.ProgramMonitoring.service.ProgramMonitoringMapper;
import com.metaverse.workflow.ProgramMonitoring.service.ProgramMonitoringResponse;
import com.metaverse.workflow.common.util.CommonUtil;
import com.metaverse.workflow.lookupHelper.EntityLookupHelper;
import com.metaverse.workflow.model.ProgramMonitoring;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramMonitoringExcelService {

    private final ProgramMonitoringRepo programMonitoringRepo;
    private final EntityLookupHelper entityLookupHelper;
    private final ProgramMonitoringMapper programMonitoringMapper;

    public void exportAllProgramMonitoringExcel(HttpServletResponse response) {

        List<ProgramMonitoring> monitoringList = programMonitoringRepo.findAll();

        /*List<ProgramMonitoringResponse> list = monitoringList.stream()
                .map(programMonitoringMapper::mapResponse)
                .toList();*/
        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Program Monitoring");

            // Header Style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // === HEADER ROW ===
            Row header = sheet.createRow(0);

            String[] columns = {
                    "Agency Name", "Program Name", "Program Rating", "District",
                    "Agenda Circulated", "Program As Per Schedule",
                    "Training Material", "Seating", "AV Projector",
                    "Male", "Female", "Transgender",
                    "Speaker1", "Speaker2",
                    "Venue Quality", "Accessibility",
                    "Tea/Snacks", "Lunch",
                    "Relevant", "Enthusiastic",
                    "Remarks"
            };

            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // === DATA ROWS ===
            int rowIdx = 1;

            /*for (ProgramMonitoringResponse monitoring : list) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(safe(CommonUtil.agencyMap.get(monitoring.getAgencyId())));

                row.createCell(1).setCellValue(safe(entityLookupHelper.getProgramById(monitoring.getProgramId()).getProgramTitle()));

                row.createCell(2).setCellValue(getStarRating(monitoring.getTotalScore()));

                row.createCell(3).setCellValue(safe(monitoring.getDistrict()));

                row.createCell(4).setCellValue(bool(monitoring.getProgramAsPerSchedule()));
                row.createCell(5).setCellValue(bool(monitoring.getProgramAsPerSchedule()));
                row.createCell(6).setCellValue(bool(monitoring.getTrainingMaterialSupplied()));
                row.createCell(7).setCellValue(bool(monitoring.getSeatingArrangementsMade()));
                row.createCell(8).setCellValue(bool(monitoring.getAvProjectorAvailable()));

                row.createCell(9).setCellValue(bool(monitoring.getParticipantsMale()));
                row.createCell(10).setCellValue(bool(monitoring.getParticipantsFemale()));
                row.createCell(11).setCellValue(bool(monitoring.getParticipantsTransgender()));

                row.createCell(12).setCellValue(safe(monitoring.getSpeaker1Name()));
                row.createCell(13).setCellValue(safe(monitoring.getSpeaker2Name()));

                row.createCell(14).setCellValue(bool(monitoring.getVenueQuality()));
                row.createCell(15).setCellValue(bool(monitoring.getAccessibility()));

                row.createCell(16).setCellValue(bool(monitoring.getTeaSnacks()));
                row.createCell(17).setCellValue(bool(monitoring.getLunch()));

                row.createCell(18).setCellValue(bool(monitoring.getRelevant()));
                row.createCell(19).setCellValue(bool(monitoring.getEnthusiast()));

                row.createCell(20).setCellValue(safe(monitoring.getOverallObservation()));
            }*/
            for (ProgramMonitoring monitoring : monitoringList) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(safe(CommonUtil.agencyMap.get(monitoring.getAgencyId())));

                row.createCell(1).setCellValue(safe(entityLookupHelper.getProgramById(monitoring.getProgramId()).getProgramTitle()));

                row.createCell(2).setCellValue(getStarRating(monitoring.getTotalScore()));

                row.createCell(3).setCellValue(safe(monitoring.getDistrict()));

                row.createCell(4).setCellValue(bool(monitoring.getProgramAsPerSchedule()));
                row.createCell(5).setCellValue(bool(monitoring.getProgramAsPerSchedule()));
                row.createCell(6).setCellValue(bool(monitoring.getTrainingMaterialSupplied()));
                row.createCell(7).setCellValue(bool(monitoring.getSeatingArrangementsMade()));
                row.createCell(8).setCellValue(bool(monitoring.getAvProjectorAvailable()));

                row.createCell(9).setCellValue(bool(monitoring.getParticipantsMale()));
                row.createCell(10).setCellValue(bool(monitoring.getParticipantsFemale()));
                row.createCell(11).setCellValue(bool(monitoring.getParticipantsTransgender()));

                row.createCell(12).setCellValue(safe(monitoring.getSpeaker1Name()));
                row.createCell(13).setCellValue(safe(monitoring.getSpeaker2Name()));

                row.createCell(14).setCellValue(bool(monitoring.getVenueQuality()));
                row.createCell(15).setCellValue(bool(monitoring.getAccessibility()));

                row.createCell(16).setCellValue(bool(monitoring.getTeaSnacks()));
                row.createCell(17).setCellValue(bool(monitoring.getLunch()));

                row.createCell(18).setCellValue(bool(monitoring.getRelevant()));
                row.createCell(19).setCellValue(bool(monitoring.getEnthusiast()));

                row.createCell(20).setCellValue(safe(monitoring.getOverallObservation()));
            }

            // Auto size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Response setup
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=program-monitoring.xlsx");

            workbook.write(response.getOutputStream());
            workbook.close();

        } catch (Exception e) {
            throw new RuntimeException("Error generating Excel", e);
        }
    }

    private String safe(Object obj) {
        return obj != null ? obj.toString() : "";
    }

    private String bool(Boolean b) {
        if(b == null) return " ";
        return b ? "Yes" : "No";
    }

    private String getStarRating(double percentage) {
        int fullStars = (int) (percentage / 20);
        boolean half = (percentage % 20) >= 10;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fullStars; i++) sb.append("★");
        if (half) sb.append("⯨");
        while (sb.length() < 5) sb.append("☆");

        return sb.toString();
    }
}
