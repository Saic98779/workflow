package com.metaverse.workflow.pdfandexcelgenerater.service;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.program.repository.ProgramRepository;
import com.metaverse.workflow.program.service.ProgramService;
import com.metaverse.workflow.program.service.ProgramSummary;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class ProgramSummeryPdfGenerator {
    @Autowired
    ProgramService programService;
    @Autowired
    ProgramRepository programRepository;
    public ByteArrayInputStream generateProgramsSummeryPdf(HttpServletResponse response, Long programId) throws DataException, DataException {
        if (!programRepository.existsById(programId)) {
            throw new DataException("No summary data found for program ID: " + programId,"PROGRAM-NOT-FOUND",400);
        }
        WorkflowResponse summary = programService.getProgramSummaryByProgramId(programId);
        ProgramSummary programSummary = (ProgramSummary) summary.getData();
        Document document = new Document(PageSize.A4, 20, 20, 30, 30);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Color.DARK_GRAY);
            Paragraph title = new Paragraph("Program Summery", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(2); // Only 2 columns now (Header & Data)
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
            Font rowFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.BLACK);

            // Set table column widths
            float[] columnWidths = { 1f, 2f }; // First column for header, second for value
            table.setWidths(columnWidths);

            // Adding headers and corresponding data
            table.addCell(createCell("Program Name", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(programSummary.getProgramName()), rowFont, Color.WHITE));

            table.addCell(createCell("Agency Name", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(programSummary.getAgencyName()), rowFont, Color.WHITE));

            table.addCell(createCell("Participant Name", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(programSummary.getParticipant()), rowFont, Color.WHITE));

            table.addCell(createCell("Start Date", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(programSummary.getStartDate()), rowFont, Color.WHITE));

            table.addCell(createCell("End Date", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(programSummary.getEndDate()), rowFont, Color.WHITE));

            table.addCell(createCell("SC", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(programSummary.getSc()), rowFont, Color.WHITE));

            table.addCell(createCell("ST", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(programSummary.getSt()), rowFont, Color.WHITE));

            table.addCell(createCell("OC", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(programSummary.getOc()), rowFont, Color.WHITE));

            table.addCell(createCell("OBC", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(programSummary.getObc()), rowFont, Color.WHITE));

            table.addCell(createCell("Minorities", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(programSummary.getMinorities()), rowFont, Color.WHITE));

            table.addCell(createCell("Male", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(programSummary.getMale()), rowFont, Color.WHITE));

            table.addCell(createCell("Female", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(programSummary.getFemale()), rowFont, Color.WHITE));

            table.addCell(createCell("Transgender", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(programSummary.getTransgender()), rowFont, Color.WHITE));

            table.addCell(createCell("Physically Challenged", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(programSummary.getPhysicallyChallenge()), rowFont, Color.WHITE));

            table.addCell(createCell("No Of SHGs", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(programSummary.getNoOfSHGs()), rowFont, Color.WHITE));

            table.addCell(createCell("No Of MSMEs", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(programSummary.getNoOfMSMEs()), rowFont, Color.WHITE));

            table.addCell(createCell("No Of Startups", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(programSummary.getNoOfStartups()), rowFont, Color.WHITE));

            table.addCell(createCell("No Of Aspirants", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(programSummary.getNoOfAspirants()), rowFont, Color.WHITE));

            document.add(table);
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
        cell.setHorizontalAlignment(Element.ALIGN_LEFT); // Aligning header to left for better readability
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(bgColor);
        return cell;
    }
}
