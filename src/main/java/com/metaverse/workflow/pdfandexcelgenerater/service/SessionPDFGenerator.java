package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.program.service.ProgramResponse;
import com.metaverse.workflow.program.service.ProgramService;
import com.metaverse.workflow.program.service.ProgramSessionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;


@Service
public class SessionPDFGenerator {
    @Autowired
    ProgramService programService;


    public ByteArrayInputStream generateProgramSessionsPdf( Long programId) {
        WorkflowResponse program = programService.getProgramById(programId);
        ProgramResponse programData = (ProgramResponse) program.getData();
        List<ProgramSessionResponse> programSessionList = programData.getProgramSessionList();

        Document document = new Document(PageSize.A4, 20, 20, 30, 30);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Color.DARK_GRAY);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.BLACK);
            Color headerBgColor = new Color(63, 81, 181);
            Color altRowColor = new Color(224, 224, 224);

            // Program Details Title
            Paragraph progDetailsTitle = new Paragraph("Program Details", titleFont);
            progDetailsTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(progDetailsTitle);
            document.add(new Paragraph(" "));

            PdfPTable programTable = new PdfPTable(4);
            programTable.setWidthPercentage(100);
            programTable.setSpacingBefore(5f);
            programTable.setSpacingAfter(10f);
            programTable.setWidths(new float[]{2, 4, 2, 4}); // Label:Value, Label:Value

// Add label-value pairs in 4-column format (label -> value)
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

            // Session Details Title
            Paragraph sessionDetailsTitle = new Paragraph("Session Details", titleFont);
            sessionDetailsTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(sessionDetailsTitle);
            document.add(new Paragraph(" "));

            // Session Table (Column-wise)
            PdfPTable sessionTable = new PdfPTable(6);
            sessionTable.setWidthPercentage(100);
            sessionTable.setSpacingBefore(5f);
            sessionTable.setSpacingAfter(10f);

            Stream.of("Session Date", "Start Time", "End Time", "Session Type", "Methodology", "Resource Person")
                    .forEach(header -> {
                        PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                        cell.setBackgroundColor(headerBgColor);
                        cell.setPadding(6);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        sessionTable.addCell(cell);
                    });

            boolean alternate = false;
            for (ProgramSessionResponse res : programSessionList) {
                Color bgColor = alternate ? altRowColor : Color.WHITE;
                sessionTable.addCell(createCell(safe(res.getSessionDate()), dataFont, bgColor));
                sessionTable.addCell(createCell(safe(res.getStartTime()), dataFont, bgColor));
                sessionTable.addCell(createCell(safe(res.getEndTime()), dataFont, bgColor));
                sessionTable.addCell(createCell(safe(res.getSessionTypeName()), dataFont, bgColor));
                sessionTable.addCell(createCell(safe(res.getSessionTypeMethodology()), dataFont, bgColor));
                sessionTable.addCell(createCell(safe(res.getResourceName()), dataFont, bgColor));
                alternate = !alternate;
            }

            document.add(sessionTable);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private String safe(Object obj) {
        return obj != null ? obj.toString() : "";
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
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(safe(value), valueFont));
        valueCell.setPadding(5);
        table.addCell(valueCell);
    }



}
