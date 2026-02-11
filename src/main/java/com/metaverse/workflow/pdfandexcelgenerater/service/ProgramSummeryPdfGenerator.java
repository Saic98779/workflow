package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramSummeryPdfGenerator {

    private final ProgramService programService;
    private final ProgramRepository programRepository;

    public ByteArrayInputStream generateProgramSummaryPdf(Long programId) throws DataException {

        if (!programRepository.existsById(programId)) {
            throw new DataException("Program not found", "PROGRAM_NOT_FOUND", 400);
        }

        ProgramSummary ps = (ProgramSummary) programService.getProgramSummaryByProgramId(programId).getData();
        Program program = programRepository.findByProgramId(programId).orElseThrow(() -> new RuntimeException("Program not found"));
        List<ParticipantDetailsDto> participantDetails = mapParticipantDetails(program.getParticipants());

        Document document = new Document(PageSize.A4, 20, 20, 20, 20);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Font bodyFont1 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, Color.BLUE);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);


            Color headerBg = new Color(63, 81, 181);

            int totalParticipants = safeInt(ps.getParticipant());

            // ===== LOGOS =====
            document.add(logoHeader());

            // ===== TITLE =====
            Paragraph title = new Paragraph("Program Summary", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(8f);
            document.add(title);
            //document.add(Chunk.NEWLINE);//for large space

            double ratingPercentage = ps.getMonitoringRating() != null
                    ? ps.getMonitoringRating()
                    : 0.0;

            Paragraph star = new Paragraph(
                    getStarRating(ratingPercentage),
                    getStarFont(16)
            );
            star.setSpacingBefore(2f);
            star.setSpacingAfter(2f);


            // ===== HEADER INFO =====
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setSpacingAfter(10);
            headerTable.setWidths(new float[]{1, 1});

            PdfPCell programNameCell = kvCell("Program Name", ps.getProgramName(), bodyFont1);
            programNameCell.setColspan(2);
            headerTable.addCell(programNameCell);
            headerTable.addCell(kvCell("Program Rating", star,bodyFont1));
            headerTable.addCell(kvCell("Agency Name", ps.getAgencyName(), bodyFont1));
            headerTable.addCell(kvCell("Start Date", ps.getStartDate(), bodyFont1));
            headerTable.addCell(kvCell("End Date", ps.getEndDate(), bodyFont1));

            document.add(headerTable);


            // ===== SOCIAL CATEGORY =====
            document.add(sectionTitle("Social Category", sectionFont));
            document.add(threeColumnTable(
                    headerFont, bodyFont1, headerBg,
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
                    headerFont, bodyFont1, headerBg,
                    new String[]{"Gender", "Number", "%"},
                    new String[][]{
                            {"Male", val(ps.getMale()), percent(ps.getMale(), totalParticipants)},
                            {"Female", val(ps.getFemale()), percent(ps.getFemale(), totalParticipants)},
                            {"Transgender", val(ps.getTransgender()), percent(ps.getTransgender(), totalParticipants)}
                    }
            ));


            // ===== Collage =====
            document.add(sectionTitle("Program Collage", sectionFont));

            PdfPTable imageTable = new PdfPTable(2);
            imageTable.setWidthPercentage(50);
            imageTable.setSpacingBefore(1f);
            imageTable.setWidths(new float[]{1, 1});

            try {
                String programName= program.getProgramTitle().replace(" ","%20");
                String imageUrl = "https://metaverseedu.in/workflowfiles/"+programId+"/Collage/"+programName+".png";
                PdfPCell cell;

                try {
                    URL url = new URL(imageUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);

                    try (InputStream is = connection.getInputStream();
                         ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            baos.write(buffer, 0, bytesRead);
                        }

                        Image img = Image.getInstance(baos.toByteArray());
                        img.scaleToFit(300, 200);
                        img.setAlignment(Image.ALIGN_CENTER);

                        cell = new PdfPCell(img, true);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    cell = new PdfPCell(new Phrase("Error loading image", sectionFont));
                }

                // ---- Cell styling ----
                cell.setColspan(2);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setPadding(4);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                imageTable.addCell(cell);

            } catch (Exception e) {
                e.printStackTrace();

                PdfPCell errorCell = new PdfPCell(
                        new Phrase("Error loading collage image", sectionFont));
                errorCell.setColspan(1);
                errorCell.setBorder(Rectangle.NO_BORDER);
                errorCell.setPadding(1);
                errorCell.setHorizontalAlignment(Element.ALIGN_CENTER);

                imageTable.addCell(errorCell);
            }

            // ---- Add table to document ----
            document.add(imageTable);


            // ===== PARTICIPANT DETAILS =====
            document.add(sectionTitle("Participant Details", sectionFont));
            document.add(participantDetailsTable(participantDetails, headerFont, bodyFont, headerBg));

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private PdfPTable logoHeader() {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);

        try {
            table.setWidths(new float[]{2, 6, 2});
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

        Image telanganaLogo = loadImage("images/telengana-logo.jpeg", 80, 80);
        Image rampLogo = loadImage("images/ramp-logo.png", 80, 80);
        Image grantThorntonLogo = loadImage("images/grantThornton_logo.png", 140, 100);

        // LEFT – TELANGANA
        PdfPCell left = new PdfPCell(telanganaLogo);
        left.setBorder(Rectangle.NO_BORDER);
        left.setHorizontalAlignment(Element.ALIGN_LEFT);
        left.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // CENTER – RAMP
        PdfPCell center = new PdfPCell(rampLogo);
        center.setBorder(Rectangle.NO_BORDER);
        center.setHorizontalAlignment(Element.ALIGN_CENTER);
        center.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // RIGHT – GRANT THORNTON
        PdfPCell right = new PdfPCell(grantThorntonLogo);
        right.setBorder(Rectangle.NO_BORDER);
        right.setHorizontalAlignment(Element.ALIGN_RIGHT);
        right.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table.addCell(left);
        table.addCell(center);
        table.addCell(right);

        table.setSpacingAfter(10);
        return table;
    }


    private Image loadImage(String path, float w, float h) {
        try {
            Image img = Image.getInstance(getClass().getClassLoader().getResource(path));
            img.scaleToFit(w, h);
            return img;
        } catch (Exception e) {
            throw new RuntimeException("Image load failed: " + path, e);
        }
    }

    // ================= PARTICIPANT TABLE =================
    private PdfPTable participantDetailsTable(
            List<ParticipantDetailsDto> list,
            Font headerFont,
            Font bodyFont,
            Color headerBg) {

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 3, 3, 1, 2, 5, 2});

        String[] headers = {
                "SNo", "Participant Name", "District Name",
                "Cat.", "Mobile No", "Organization Name", "Org Type"
        };

        for (String h : headers) {
            table.addCell(headerCell(h, headerFont, headerBg));
        }

        int i = 1;
        for (ParticipantDetailsDto p : list) {
            table.addCell(bodyCell(String.valueOf(i++), bodyFont));
            table.addCell(bodyCell(val(p.getParticipantName()), bodyFont));
            table.addCell(bodyCell(val(p.getDistrictName()), bodyFont));
            table.addCell(bodyCell(val(p.getCategory()), bodyFont));
            table.addCell(bodyCell(val(p.getMobileNumber()), bodyFont));
            table.addCell(bodyCell(val(p.getOrganizationName()), bodyFont));
            table.addCell(bodyCell(val(p.getOrganizationType()), bodyFont));
        }
        return table;
    }

    // =====================================================
    // ================= HELPERS ===========================
    // =====================================================

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

    private PdfPCell headerCell(String text, Font font, Color bg) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bg);
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private PdfPCell bodyCell1(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);  // ✅ Center horizontally
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);    // ✅ Center vertically
        return cell;
    }

    private PdfPCell bodyCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(6);
        return cell;
    }

    private PdfPCell kvCell(String key, Object value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setPadding(6f);

        Paragraph p = new Paragraph(key + " :     ", font);

        if (value instanceof Paragraph) {
            p.add((Paragraph) value);
        } else {
            p.add(new Chunk(String.valueOf(value), font));
        }

        cell.addElement(p);
        return cell;
    }



    private Paragraph sectionTitle(String text, Font font) {
        Paragraph p = new Paragraph(text, font);
        p.setSpacingBefore(15);
        p.setSpacingAfter(5);
        return p;
    }

    private PdfPTable threeColumnTable(
            Font headerFont, Font bodyFont, Color headerBg,
            String[] headers, String[][] rows) {

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);

        for (String h : headers) {
            table.addCell(headerCell(h, headerFont, headerBg));
        }

        for (String[] r : rows) {
            table.addCell(bodyCell1(r[0], bodyFont));
            table.addCell(bodyCell1(r[1], bodyFont));
            table.addCell(bodyCell1(r[2], bodyFont));
        }
        return table;
    }

    // =====================================================
    // ================= PARTICIPANT MAPPER ================
    // =====================================================

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
                    dto.setDistrictName(
                            CommonUtil.districtMap.getOrDefault(Integer.valueOf(distId), "")
                    );
                }
            }
            list.add(dto);
        }
        return list;
    }

    // =====================================================
    // ================= STAR RATING =======================
    // =====================================================

    private String getStarRating(double percentage) {
        if (percentage <= 0) return "☆☆☆☆☆";

        percentage = Math.min(percentage, 100);
        int fullStars = (int) (percentage / 20);
        boolean halfStar = (percentage % 20) >= 10;

        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < fullStars; i++) stars.append("★");
        if (halfStar && fullStars < 5) stars.append("☆");
        while (stars.length() < 5) stars.append("☆");

        return stars.toString();
    }

    private Font getStarFont(float size) {
        try {
            URL fontUrl = getClass()
                    .getClassLoader()
                    .getResource("fonts/DejaVuSans.ttf");

            if (fontUrl == null) {
                throw new RuntimeException("DejaVuSans.ttf not found in classpath");
            }

            BaseFont baseFont = BaseFont.createFont(
                    fontUrl.toString(),
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED
            );

            return new Font(baseFont, size, Font.BOLD, Color.ORANGE);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load DejaVuSans font", e);
        }
    }



    private Image loadImageFromUrl(String path, float w, float h) {
        try {
            Image img;

            if (path.startsWith("http")) {
                // URL image
                img = Image.getInstance(path);
            } else {
                // Local file path
                img = Image.getInstance(path);
            }

            img.scaleAbsolute(w, h);
            img.setAlignment(Image.ALIGN_CENTER);
            return img;

        } catch (Exception e) {
            e.printStackTrace(); // 🔥 shows real error in logs
            throw new RuntimeException("Image load failed: " + path, e);
        }
    }


}
