package com.metaverse.workflow.pdfandexcelgenerater.service;


import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.program.service.ProgramResponse;
import com.metaverse.workflow.program.service.ProgramService;
import com.metaverse.workflow.programattendance.service.ProgramAttendanceResponse;
import com.metaverse.workflow.programattendance.service.ProgramAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Service
public class AttendancePDFGenerator {

    @Autowired
    ProgramService programService;
    @Autowired
    ProgramAttendanceService programAttendanceService;

    public ByteArrayInputStream programAttendancePDF(Long programId) {
        WorkflowResponse program = programService.getProgramById(programId);
        ProgramResponse programData = (ProgramResponse) program.getData();

        WorkflowResponse response = programAttendanceService.attendanceByProgramId(programId, 0, 0);
        ProgramAttendanceResponse attendance = (ProgramAttendanceResponse) response.getData();
        List<ProgramAttendanceResponse.ParticipantAttendance> list = attendance.getParticipantAttendanceList();

        Document document = new Document(PageSize.A4.rotate(), 20, 20, 30, 30);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Fonts and Colors
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Color.DARK_GRAY);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.BLACK);
            Color headerBgColor = new Color(63, 81, 181);
            Color altRowColor = new Color(224, 224, 224);

            // Program Details Title
            Paragraph progDetailsTitle = new Paragraph("Program Details", titleFont);
            progDetailsTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(progDetailsTitle);
            document.add(new Paragraph(" ")); // Empty line

            // Program Details Table
            PdfPTable programTable = new PdfPTable(4);
            programTable.setWidthPercentage(100);
            programTable.setSpacingBefore(5f);
            programTable.setSpacingAfter(10f);
            programTable.setWidths(new float[]{2, 4, 2, 4});

            addLabelValue(programTable, "Agency", programData.getAgencyName(), headerFont, dataFont, headerBgColor);
            addLabelValue(programTable, "Program Title", programData.getProgramTitle(), headerFont, dataFont, headerBgColor);
            addLabelValue(programTable, "Activity", programData.getActivityName(), headerFont, dataFont, headerBgColor);
            addLabelValue(programTable, "Sub Activity", programData.getSubActivityName(), headerFont, dataFont, headerBgColor);
            addLabelValue(programTable, "Start Date", programData.getStartDate(), headerFont, dataFont, headerBgColor);
            addLabelValue(programTable, "End Date", programData.getEndDate(), headerFont, dataFont, headerBgColor);
            addLabelValue(programTable, "Program Type", programData.getProgramType(), headerFont, dataFont, headerBgColor);
            addLabelValue(programTable, "Location", programData.getProgramLocationName(), headerFont, dataFont, headerBgColor);
            addLabelValue(programTable, "District", programData.getDistrict(), headerFont, dataFont, headerBgColor);
            addLabelValue(programTable, "Mandal", programData.getMandal(), headerFont, dataFont, headerBgColor);

            document.add(programTable);

            // Attendance Title
            Paragraph participantTitle = new Paragraph("Attendance Details", titleFont);
            participantTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(participantTitle);
            document.add(new Paragraph(" "));

            // Attendance Table Header
            int totalColumns = 7 + (list.get(0).getAttendanceData() != null ? list.get(0).getAttendanceData().length : 0);
            PdfPTable table = new PdfPTable(totalColumns);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);

            Stream.of("Name", "Member ID", "SHG Name", "Mobile No", "Email", "Aadhar No", "Designation")
                    .forEach(header -> {
                        PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                        cell.setBackgroundColor(headerBgColor);
                        cell.setPadding(5);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);
                    });

            if (!list.isEmpty() && list.get(0).getAttendanceData() != null) {
                for (int i = 0; i < list.get(0).getAttendanceData().length; i++) {
                    PdfPCell cell = new PdfPCell(new Phrase("Day " + (i + 1), headerFont));
                    cell.setBackgroundColor(headerBgColor);
                    cell.setPadding(5);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
            }

            // Attendance Table Data Rows
            boolean alternate = false;
            for (ProgramAttendanceResponse.ParticipantAttendance p : list) {
                Color bg = alternate ? altRowColor : Color.WHITE;

                table.addCell(createCell(safe(p.getParticipantName()), dataFont, bg));
                table.addCell(createCell(safe(p.getMemberId()), dataFont, bg));
                table.addCell(createCell(safe(p.getSHGName()), dataFont, bg));
                table.addCell(createCell(safe(String.valueOf(p.getMobileNo())), dataFont, bg));
                table.addCell(createCell(safe(p.getEmail()), dataFont, bg));
                table.addCell(createCell(safe(String.valueOf(p.getAadharNo())), dataFont, bg));
                table.addCell(createCell(safe(p.getDesignation()), dataFont, bg));

                if (p.getAttendanceData() != null) {
                    for (Character c : p.getAttendanceData()) {
                        table.addCell(createCell(String.valueOf(c), dataFont, bg));
                    }
                }

                alternate = !alternate;
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private String safe(String input) {
        return input != null ? input : "";
    }

    private PdfPCell createCell(String content, Font font, Color bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(bgColor);
        return cell;
    }

    private void addLabelValue(PdfPTable table, String label, String value, Font labelFont, Font valueFont, Color bgColor) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBackgroundColor(bgColor);
        labelCell.setPadding(5);
        labelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(safe(value), valueFont));
        valueCell.setPadding(5);
        valueCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(valueCell);
    }
}