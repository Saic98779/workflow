package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont; // <-- IMPORTANT: Added import
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
import java.io.IOException;

@Service
public class ProgramSummeryPdfGenerator {

    @Autowired
    ProgramService programService;

    @Autowired
    ProgramRepository programRepository;

    public ByteArrayInputStream generateProgramsSummeryPdf(HttpServletResponse response, Long programId) throws DataException {
        if (!programRepository.existsById(programId)) {
            throw new DataException("No summary data found for program ID: " + programId, "PROGRAM-NOT-FOUND", 400);
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
            Paragraph title = new Paragraph("Program Summary", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // === FONT SETUP (UPDATED) ===
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);

            // Define path to a Unicode-compatible font in your resources
            String FONT_PATH = "/font/DejaVuSans.ttf";

            // Create the row font by embedding the TTF file and using Unicode encoding
            Font rowFont = FontFactory.getFont(
                    FONT_PATH,
                    BaseFont.IDENTITY_H, // Use Unicode encoding for proper character support
                    BaseFont.EMBEDDED,   // Embed the font file into the PDF
                    11,                  // Font size
                    Font.NORMAL,
                    Color.BLACK
            );
            // === END OF UPDATE ===

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            float[] columnWidths = {1f, 2f};
            table.setWidths(columnWidths);

            // Program Name
            table.addCell(createCell("Program Name", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(programSummary.getProgramName()), rowFont, Color.WHITE));

            // Program Rating with Stars (This will now render correctly)
            table.addCell(createCell("Program Rating", headerFont, new Color(63, 81, 181)));
            double ratingPercentage = 0;
            if (programSummary.getMonitoringRating() != null) {
                try {
                    ratingPercentage = programSummary.getMonitoringRating();
                } catch (NumberFormatException e) {
                    ratingPercentage = 0; // Default to 0 if parsing fails
                }
            }
            String stars = getStarRating(ratingPercentage);
            table.addCell(createCell(stars, rowFont, Color.WHITE));

            // Remaining fields
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
            // It's good practice to catch IOException for font loading
            // and re-throw as a runtime exception or handle appropriately.
            throw new RuntimeException("Error generating PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private String safe(Object obj) {
        return obj != null ? obj.toString() : "";
    }

    private PdfPCell createCell(String content, Font font, Color bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(bgColor);
        cell.setBorderColor(Color.LIGHT_GRAY);
        return cell;
    }

    // Converts percentage to star rating string
    private String getStarRating(double percentage) {
        int fullStars = (int) (percentage / 20);
        boolean hasHalfStar = (percentage % 20) >= 10;
        StringBuilder stars = new StringBuilder();

        for (int i = 0; i < fullStars; i++) {
            stars.append("★");
        }
        if (hasHalfStar) {
            stars.append("⯨"); // Unicode half star alternative
        }
        while (stars.length() < 5) {
            stars.append("☆");
        }
        return stars.toString();
    }
}