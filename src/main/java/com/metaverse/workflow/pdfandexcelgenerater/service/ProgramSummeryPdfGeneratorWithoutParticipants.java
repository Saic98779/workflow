package com.metaverse.workflow.pdfandexcelgenerater.service;


import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.model.ProgramSummaryDetails;
import com.metaverse.workflow.program.repository.ProgramRepository;
import com.metaverse.workflow.program.repository.ProgramSummaryDetailsRepo;
import com.metaverse.workflow.program.service.ProgramService;
import com.metaverse.workflow.program.service.ProgramSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramSummeryPdfGeneratorWithoutParticipants {

    private final ProgramService programService;
    private final ProgramRepository programRepository;
    private final ProgramSummaryDetailsRepo summaryDetailsRepo;

    public ByteArrayInputStream generateMultipleProgramSummaryPdf(List<Long> programIds) throws DataException {

        if (programIds == null || programIds.isEmpty()) {
            throw new DataException("Program list is empty", "EMPTY_LIST", 400);
        }

        Document document = new Document(PageSize.A4, 50, 20, 20, 0);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            writer.setPageEvent(new PageNumberEvent());
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, Color.BLUE);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
            Font bodyFontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

            Color headerBg = new Color(63, 81, 181);

            for (int index = 0; index < programIds.size(); index++) {

                Long programId = programIds.get(index);

                if (!programRepository.existsById(programId)) continue;

                ProgramSummary ps = (ProgramSummary) programService
                        .getProgramSummaryByProgramId(programId)
                        .getData();

                Program program = programRepository.findByProgramId(programId)
                        .orElseThrow(() -> new RuntimeException("Program not found"));

                Optional<ProgramSummaryDetails> optionalDetails =
                        summaryDetailsRepo.findByProgramProgramId(programId);

                ProgramSummaryDetails programSummaryDetails = optionalDetails.orElse(new ProgramSummaryDetails());

                int totalParticipants = ps.getParticipant() != null ? ps.getParticipant() : 0;

                document.add(logoHeader());

                Paragraph title = new Paragraph(program.getProgramTitle(), titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingAfter(8f);
                document.add(title);

                Paragraph star = new Paragraph(
                        getStarRating(ps.getMonitoringRating() != null ? ps.getMonitoringRating() : 0.0),
                        getStarFont(14)
                );

                PdfPTable headerTable = new PdfPTable(3);
                headerTable.setWidthPercentage(100);
                headerTable.setSpacingAfter(1);
                headerTable.setWidths(new float[]{1, 1, 1});

                PdfPCell ratingCell = kvCell(" Program Rating", star, bodyFontBold);
                headerTable.addCell(ratingCell);

                PdfPCell agencyCell = kvCell(" Agency Name", ps.getAgencyName(), bodyFontBold);
                agencyCell.setColspan(2);
                headerTable.addCell(agencyCell);

                PdfPCell startCell = kvCell(" Start Date", ps.getStartDate(), bodyFontBold);
                headerTable.addCell(startCell);

                PdfPCell endDateCell = kvCell(" End Date", ps.getEndDate(), bodyFontBold);
                endDateCell.setColspan(2);
                headerTable.addCell(endDateCell);

                String locationRow = "Location : " + ps.getLocation()
                        + ", Mandal : " + ps.getMandal()
                        + ", District : " + ps.getDistrict();

                PdfPCell locationCell = new PdfPCell(new Phrase(locationRow, bodyFontBold));
                locationCell.setColspan(3);
                locationCell.setPadding(5);

                locationCell.setMinimumHeight(3f);
                locationCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                headerTable.addCell(locationCell);

                document.add(headerTable);

                // Section title (optional combined)
                document.add(sectionTitle("Social Category & Gender", sectionFont));

                // Parent table with 2 columns
                PdfPTable parentTable = new PdfPTable(2);
                parentTable.setWidthPercentage(100);
                parentTable.setSpacingBefore(0.5f);
                parentTable.setSpacingAfter(0.5f);
                parentTable.setWidths(new float[]{1, 1}); // equal width

                // ===== SOCIAL CATEGORY TABLE =====
                PdfPTable socialTable = new PdfPTable(1);
                socialTable.setWidthPercentage(100);
                socialTable.addCell(threeColumnTable(
                        headerFont, bodyFontBold, headerBg,
                        new String[]{"Category", "Number", "%"},
                        new String[][]{
                                {"SC", val(ps.getSc()), percent(ps.getSc(), totalParticipants)},
                                {"ST", val(ps.getSt()), percent(ps.getSt(), totalParticipants)},
                                {"BC", val(ps.getBc()), percent(ps.getBc(), totalParticipants)},
                                {"OC", val(ps.getOc()), percent(ps.getOc(), totalParticipants)},
                                {"Minorities", val(ps.getMinorities()), percent(ps.getMinorities(), totalParticipants)}
                        }
                ));

                // ===== GENDER TABLE =====
                PdfPTable genderTable = new PdfPTable(1);
                genderTable.setWidthPercentage(100);

                genderTable.addCell(threeColumnTable(
                        headerFont, bodyFontBold, headerBg,
                        new String[]{"Gender", "Number", "%"},
                        new String[][]{
                                {"Male", val(ps.getMale()), percent(ps.getMale(), totalParticipants)},
                                {"Female", val(ps.getFemale()), percent(ps.getFemale(), totalParticipants)},
                                {"Transgender", val(ps.getTransgender()), percent(ps.getTransgender(), totalParticipants)},
                                {"        ", "", ""},
                                {"        ", "", ""}
                        }
                ));

                // Add both to parent table
                parentTable.addCell(new PdfPCell(socialTable) {{
                    setBorder(Rectangle.NO_BORDER);
                    setPadding(2);
                }});

                parentTable.addCell(new PdfPCell(genderTable) {{
                    setBorder(Rectangle.NO_BORDER);
                    setPadding(2);
                }});

                // Add to document
                document.add(parentTable);

                //====== Executive Summary ========
                document.add(sectionTitle("Program Summary", sectionFont));

                PdfPTable summaryTable = new PdfPTable(1);
                summaryTable.setWidthPercentage(100);
                summaryTable.setSpacingBefore(1f);
                summaryTable.setSpacingAfter(1f);

                String summaryText = programSummaryDetails.getExecutiveSummary() != null
                        ? programSummaryDetails.getExecutiveSummary()
                        : "N/A";

                Paragraph summaryParagraph = new Paragraph(summaryText, bodyFont);
                summaryParagraph.setLeading(0f, 1.5f);

                PdfPCell summaryCell = new PdfPCell(summaryParagraph);
                summaryCell.setPadding(5);
                summaryCell.setBorder(Rectangle.NO_BORDER);
                summaryCell.setNoWrap(false);

                summaryTable.addCell(summaryCell);

                document.add(summaryTable);
                // ===== COLLAGE =====
                document.add(sectionTitle("Program Collage", sectionFont));
                document.add(getCollageTable(program, programId, sectionFont));

                //====== College Details ========
                document.add(sectionTitle("College Details", sectionFont));

                PdfPTable collageDetails = new PdfPTable(1);
                collageDetails.setWidthPercentage(100);

                String detailsText = programSummaryDetails.getCollegeDetails() != null
                        ? programSummaryDetails.getCollegeDetails()
                        : "N/A";

                Paragraph detailsParagraph = new Paragraph(detailsText, bodyFont);
                detailsParagraph.setLeading(0f, 1.5f);

                PdfPCell detailsCell = new PdfPCell(detailsParagraph);
                detailsCell.setPadding(10);
                detailsCell.setBorder(Rectangle.NO_BORDER);
                detailsCell.setNoWrap(false);
                collageDetails.addCell(detailsCell);
                document.add(collageDetails);


                if (index != programIds.size() - 1) {
                    document.newPage();
                }

            }

            document.close();

        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private PdfPTable logoHeader() throws Exception {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 6, 2});

        table.addCell(getLogoCell("images/Government-of-Telangana.png", Element.ALIGN_LEFT, 50, 50));
        table.addCell(getLogoCell("images/ramp-logo.png", Element.ALIGN_CENTER, 50, 50));

        // Increase size for third logo
        table.addCell(getLogoCell("images/GT_Logo.png", Element.ALIGN_RIGHT, 130, 130));

        return table;
    }

    private PdfPCell getLogoCell(String path, int align, float width, float height) throws Exception {
        Image img = Image.getInstance(Objects.requireNonNull(getClass().getClassLoader().getResource(path)));
        img.scaleToFit(width, height);

        PdfPCell cell = new PdfPCell(img);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(align);
        return cell;
    }

    private PdfPTable getCollageTable(Program program, Long programId, Font font) {

        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(60);

        try {
            String programName = program.getProgramTitle().replace(" ", "%20");
            String imageUrl = "https://metaverseedu.in/workflowfiles/" +
                    programId + "/Collage/" + programName + ".png";

            Image img = loadImageFromUrl(imageUrl, 220, 140);

            PdfPCell cell = new PdfPCell(img, true);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(cell);

        } catch (Exception e) {
            table.addCell(new PdfPCell(new Phrase("Image not available", font)));
        }

        return table;
    }


    private PdfPCell headerCell(String text, Font font, Color bg) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bg);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        return cell;
    }

    private PdfPCell bodyCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        return cell;
    }

    private PdfPCell kvCell(String key, Object value, Font font) {

        PdfPCell cell = new PdfPCell();
        cell.setPadding(1);

        Font keyFont = font;

        Font valueFont = new Font(font.getFamily(), font.getSize() + 2, Font.BOLD);

        Paragraph p = new Paragraph();
        p.add(new Chunk(key + " : ", keyFont));

        if (value instanceof Paragraph) {
            Paragraph valPara = (Paragraph) value;
            valPara.setFont(valueFont);
            valPara.setAlignment(Element.ALIGN_CENTER);
            p.add(valPara);
        } else {
            p.add(new Chunk(String.valueOf(value), valueFont));
        }

        cell.addElement(p);
        return cell;
    }

    private Paragraph sectionTitle(String text, Font font) {
        Paragraph p = new Paragraph(text, font);
        p.setSpacingBefore(1);
        p.setSpacingAfter(1);
        return p;
    }

    private PdfPTable threeColumnTable(Font headerFont, Font bodyFont, Color headerBg,
                                       String[] headers, String[][] rows) {

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);

        for (String h : headers) {
            table.addCell(headerCell(h, headerFont, headerBg));
        }

        for (String[] r : rows) {
            table.addCell(bodyCell(r[0], bodyFont));
            table.addCell(bodyCellCenter(r[1], bodyFont));
            table.addCell(bodyCellCenter(r[2], bodyFont));
        }

        return table;
    }


    private int safeInt(Object val) {
        if (val == null) return 0;
        if (val instanceof Number) return ((Number) val).intValue();
        return Integer.parseInt(val.toString());
    }

    private String val(Object val) {
        return val == null ? "" : val.toString();
    }

    private String percent(Object count, int total) {
        if (count == null || total == 0) return "0.00";
        return String.format("%.2f", (safeInt(count) * 100.0) / total);
    }

    private String getStarRating(double percentage) {
        if (percentage <= 0) return "☆☆☆☆☆";

        int fullStars = (int) (percentage / 20);
        boolean halfStar = (percentage % 20) >= 10;

        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < fullStars; i++) stars.append("★");
        if (halfStar && fullStars < 5) stars.append("☆");
        while (stars.length() < 5) stars.append("☆");

        return stars.toString();
    }

    private Font getStarFont(float size) throws Exception {
        BaseFont baseFont = BaseFont.createFont(
                getClass().getClassLoader().getResource("fonts/DejaVuSans.ttf").toString(),
                BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED
        );
        return new Font(baseFont, size, Font.BOLD, Color.ORANGE);
    }

    private Image loadImageFromUrl(String url, float w, float h) throws Exception {
        Image img = Image.getInstance(url);
        img.scaleToFit(w, h);
        return img;
    }

    private PdfPCell bodyCellCenter(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

}

// ===== PAGE NUMBER EVENT =====
class PageNumberEvent extends PdfPageEventHelper {

    Font font = FontFactory.getFont(FontFactory.HELVETICA, 9);

    @Override
    public void onEndPage(PdfWriter writer, Document document) {

        PdfContentByte cb = writer.getDirectContent();

        String text = "Page " + writer.getPageNumber();

        float x = document.right() - 20;   // right side
        float y = document.bottom() + 10;  // slightly above bottom

        ColumnText.showTextAligned(
                cb,
                Element.ALIGN_RIGHT,
                new Phrase(text, font),
                x,
                y,
                0
        );
    }

}
