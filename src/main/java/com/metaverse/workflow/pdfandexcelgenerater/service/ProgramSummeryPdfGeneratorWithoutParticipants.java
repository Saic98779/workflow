package com.metaverse.workflow.pdfandexcelgenerater.service;


import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.metaverse.workflow.common.util.CommonUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.program.repository.ProgramRepository;
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

    public ByteArrayInputStream generateMultipleProgramSummaryPdf(List<Long> programIds) throws DataException {

        if (programIds == null || programIds.isEmpty()) {
            throw new DataException("Program list is empty", "EMPTY_LIST", 400);
        }

        Document document = new Document(PageSize.A4, 50, 20, 20, 0);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            writer.setPageEvent(new PageNumberEvent1());
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, Color.BLUE);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
            Font bodyFontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);

            Color headerBg = new Color(63, 81, 181);

            for (int index = 0; index < programIds.size(); index++) {

                Long programId = programIds.get(index);

                if (!programRepository.existsById(programId)) continue;

                ProgramSummary ps = (ProgramSummary) programService
                        .getProgramSummaryByProgramId(programId)
                        .getData();

                Program program = programRepository.findByProgramId(programId)
                        .orElseThrow(() -> new RuntimeException("Program not found"));

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
                headerTable.setSpacingAfter(10);
                headerTable.setWidths(new float[]{1, 1, 1});

                headerTable.addCell(kvCell("Program Rating", star, bodyFontBold));

                PdfPCell agencyCell = kvCell("Agency Name", ps.getAgencyName(), bodyFontBold);
                agencyCell.setColspan(2);
                headerTable.addCell(agencyCell);

                headerTable.addCell(kvCell("Start Date", ps.getStartDate(), bodyFontBold));

                PdfPCell endDateCell = kvCell("End Date", ps.getEndDate(), bodyFontBold);
                endDateCell.setColspan(2);
                headerTable.addCell(endDateCell);

                String locationRow = "Location : " + ps.getLocation()
                        + ", Mandal : " + ps.getMandal()
                        + ", District : " + ps.getDistrict();

                PdfPCell locationCell = new PdfPCell(new Phrase(locationRow, bodyFontBold));
                locationCell.setColspan(3);
                locationCell.setPadding(5);
                headerTable.addCell(locationCell);

                document.add(headerTable);

                // ===== SOCIAL CATEGORY =====
                document.add(sectionTitle("Social Category", sectionFont));
                document.add(threeColumnTable(
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

                // ===== GENDER =====
                document.add(sectionTitle("Gender", sectionFont));
                document.add(threeColumnTable(
                        headerFont, bodyFontBold, headerBg,
                        new String[]{"Gender", "Number", "%"},
                        new String[][]{
                                {"Male", val(ps.getMale()), percent(ps.getMale(), totalParticipants)},
                                {"Female", val(ps.getFemale()), percent(ps.getFemale(), totalParticipants)},
                                {"Transgender", val(ps.getTransgender()), percent(ps.getTransgender(), totalParticipants)}
                        }
                ));

                // ===== COLLAGE =====
                document.add(sectionTitle("Program Collage", sectionFont));
                document.add(getCollageTable(program, programId, sectionFont));

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
        table.setWidthPercentage(80);

        try {
            String programName = program.getProgramTitle().replace(" ", "%20");
            String imageUrl = "https://metaverseedu.in/workflowfiles/" +
                    programId + "/Collage/" + programName + ".png";

            Image img = loadImageFromUrl(imageUrl, 300, 200);

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
        cell.setPadding(5);

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
        p.setSpacingBefore(5);
        p.setSpacingAfter(5);
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

    private List<ParticipantDetailsDto> mapParticipantDetails(List<Participant> participants) {
        if (participants == null) return Collections.emptyList();

        List<ParticipantDetailsDto> list = new ArrayList<>();

        for (Participant p : participants) {
            if (p == null) continue;

            ParticipantDetailsDto dto = new ParticipantDetailsDto();
            dto.setParticipantName(p.getParticipantName());
            dto.setCategory(p.getCategory());
            dto.setMobileNumber(p.getMobileNo());

            if (p.getOrganization() != null) {
                dto.setOrganizationName(p.getOrganization().getOrganizationName());
                dto.setOrganizationType(p.getOrganization().getOrganizationType());

                String distId = p.getOrganization().getDistId();
                if (distId != null && distId.matches("\\d+")) {
                    dto.setDistrictName(CommonUtil.districtMap.getOrDefault(Integer.valueOf(distId), ""));
                }
            }
            list.add(dto);
        }
        return list;
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
class PageNumberEvent1 extends PdfPageEventHelper {

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
