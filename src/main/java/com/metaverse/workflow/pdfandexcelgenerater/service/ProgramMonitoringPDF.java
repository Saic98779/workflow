package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.metaverse.workflow.ProgramMonitoring.service.ProgramMonitoringResponse;
import com.metaverse.workflow.ProgramMonitoring.service.ProgramMonitoringService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.CommonUtil;
import com.metaverse.workflow.lookupHelper.EntityLookupHelper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramMonitoringPDF {

    private final ProgramMonitoringService programMonitoringService;

    private final EntityLookupHelper entityLookupHelper;


    public void generateProgramsMonitoringPdf(HttpServletResponse response, Long monitoringId) {
        WorkflowResponse res = programMonitoringService.getFeedBackById(monitoringId);
        ProgramMonitoringResponse monitoring = (ProgramMonitoringResponse) res.getData();

        Document document = new Document(PageSize.A4, 20, 20, 30, 30);

        try {
            // Write PDF directly to servlet response
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Color.DARK_GRAY);
            Paragraph title = new Paragraph("Program Monitoring Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Header font
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);

            // Load Unicode-compatible font from resources
            InputStream fontStream = getClass().getResourceAsStream("/fonts/DejaVuSans.ttf");
            if (fontStream == null) {
                throw new RuntimeException("Font file not found in resources/font/DejaVuSans.ttf");
            }
            BaseFont baseFont = BaseFont.createFont(
                    "DejaVuSans.ttf",
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED,
                    true,
                    fontStream.readAllBytes(),
                    null
            );
            Font rowFont = new Font(baseFont, 11, Font.NORMAL, Color.BLACK);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            float[] columnWidths = {1f, 2f};
            table.setWidths(columnWidths);

            // === Program Details ===
            table.addCell(createCell("Agency Name", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(CommonUtil.agencyMap.get(monitoring.getAgencyId())), rowFont, Color.WHITE));

            table.addCell(createCell("Program Name", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(entityLookupHelper.getProgramById(monitoring.getProgramId()).getProgramTitle()), rowFont, Color.WHITE));

            table.addCell(createCell("Program Rating", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(getStarRating(monitoring.getTotalScore()), rowFont, Color.WHITE));

            table.addCell(createCell("District Name", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getDistrict()), rowFont, Color.WHITE));

            table.addCell(createCell("Program Agenda Circulated", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getProgramAsPerSchedule().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Program As Per Schedule", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getProgramAsPerSchedule().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Training material supplied", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getTrainingMaterialSupplied().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Seating arrangements made", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getSeatingArrangementsMade().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("AV Projector available", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getAvProjectorAvailable().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("How did the participants know about the program", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getHowDidYouKnowAboutProgram()), rowFont, Color.WHITE));
            //Audience Profile
            table.addCell(createCell("Male representation", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getParticipantsMale().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Female representation", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getParticipantsFemale().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Transgender representation", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getParticipantsTransgender().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("DIC representatives participated", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getDicRegistrationParticipated().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("SHG representatives participated", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getShgRegistrationParticipated().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("MSME representative participated", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getMsmeRegistrationParticipated().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Startups representative participated", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getStartupsRegistrationParticipated().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("No Of IAs participated", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getNoIAsParticipated().size()), rowFont, Color.WHITE));

            //Program Delivery Details
            table.addCell(createCell("Speaker1 Name ", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getSpeaker1Name()), rowFont, Color.WHITE));

            table.addCell(createCell("Topics as per session plan", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getTopicAsPerSessionPlan1().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Time taken (In min) ", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getTimeTaken1()), rowFont, Color.WHITE));

            table.addCell(createCell("Audio visual ald used", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getAudioVisualAidUsed1().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Relevance ", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getRelevance1()), rowFont, Color.WHITE));

            table.addCell(createCell("Session continuity", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getSessionContinuity1().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Participant interaction ", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getParticipantInteraction1().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));
            //Audience Profile
            table.addCell(createCell("Speaker2 Name ", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getSpeaker2Name()), rowFont, Color.WHITE));

            table.addCell(createCell("Topics as per session plan", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getTopicAsPerSessionPlan2().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Time taken (In min) ", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getTimeTaken2()), rowFont, Color.WHITE));

            table.addCell(createCell("Audio visual ald used", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getAudioVisualAidUsed2().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Relevance ", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getRelevance2()), rowFont, Color.WHITE));

            table.addCell(createCell("Session continuity", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getSessionContinuity2().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Participant interaction", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getParticipantInteraction2().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));
            //Logistics/Facilities
            table.addCell(createCell("Venue quality", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getVenueQuality().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Accessibility to disabilities", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getAccessibility().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Tea/Snacks provided", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getTeaSnacks().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Lunch provided ", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getLunch().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Canned water available.", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getCannedWater().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Toilet Hygiene", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getToiletHygiene().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("AV Equipment", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getAvEquipment().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Stationary", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getStationary().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));
            //Participant Feedback
            table.addCell(createCell("Relevant ", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getRelevant().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Enthusiastic", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getEnthusiast().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Felt useful ", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getFeltUseful().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell(" Further willing to engage", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getFutureWillingToEngage().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));
            //Feedback on Speaker
            table.addCell(createCell("Qualified ", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getQualified().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Experienced ", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getExperienced().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Certified ", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getCertified().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Delivery methodology good", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getDeliveryMethodologyGood().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));

            table.addCell(createCell("Comes with relevant experience", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getRelevantExperience().equals(Boolean.TRUE)? "Yes" :"No"), rowFont, Color.WHITE));
            //Best practices identified?

            table.addCell(createCell("TBest practice 1", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getBestPracticesIdentified().get(0)), rowFont, Color.WHITE));

            table.addCell(createCell("Best practice 2 ", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getBestPracticesIdentified().get(1)), rowFont, Color.WHITE));

            table.addCell(createCell("Best practice 3", headerFont, new Color(63, 81, 181)));
            table.addCell(createCell(safe(monitoring.getBestPracticesIdentified().get(2)), rowFont, Color.WHITE));

            table.addCell(createCell("Remarks", headerFont, new Color(63, 81, 181)));
             table.addCell(createCell(safe(monitoring.getOverallObservation()), rowFont, Color.WHITE));

            document.add(table);
            document.close();

            // set headers here
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=program-monitoring.pdf");

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    private String safe(Object obj) {
        return obj != null ? obj.toString() : "";
    }

    private String safeListValue(List<String> list, int index) {
        if (list != null && list.size() > index && list.get(index) != null) {
            return list.get(index);
        }
        return "";
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

    private String getStarRating(double percentage) {
        int fullStars = (int) (percentage / 20);
        boolean hasHalfStar = (percentage % 20) >= 10;
        StringBuilder stars = new StringBuilder();

        for (int i = 0; i < fullStars; i++) {
            stars.append("★");
        }
        if (hasHalfStar) {
            stars.append("⯨");
        }
        while (stars.length() < 5) {
            stars.append("☆");
        }
        return stars.toString();
    }
}
