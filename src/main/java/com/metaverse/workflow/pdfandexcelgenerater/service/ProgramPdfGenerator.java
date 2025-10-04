package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.metaverse.workflow.agency.service.AgencyService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.program.service.ProgramResponse;
import com.metaverse.workflow.program.service.ProgramService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ProgramPdfGenerator  {

    @Autowired
    AgencyService agencyService;

    public ByteArrayInputStream generateProgramsPdf(HttpServletResponse response,Long agencyId) {

        WorkflowResponse programs = agencyService.getProgramByAgencyIdDropDown(agencyId);
        List<ProgramResponse> programList = (List<ProgramResponse>) programs.getData();
        Document document = new Document(PageSize.A4.rotate(), 20, 20, 30, 30);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Color.DARK_GRAY);
            Paragraph title = new Paragraph("Program Details Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(""));

            PdfPTable table = new PdfPTable(13);
            table.setWidthPercentage(100);
            table.setSpacingBefore(5f);
            table.setSpacingAfter(10f);


            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
            Stream.of("Sl.No","Program Name", "Activity Name",  "Sub Activity Name", "Agency Name",
                    "Start Date", "End Date", "Start Time", "End Time", "SPOC Name", "SPOC ContactNo",
                    "Program Location Name", "Kpi").forEach(headerTitle -> {
                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(new Color(63, 81, 181)); // Indigo
                header.setPadding(8);
                header.setPhrase(new Phrase(headerTitle, headerFont));
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(header);
            });
            Font rowFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.BLACK);
            boolean alternate = false;
            int slNo =0;
            for (ProgramResponse res : programList) {
                Color rowColor = alternate ? new Color(224, 224, 224) : Color.WHITE;
                table.addCell(createCell(safe(slNo), rowFont, rowColor));
                table.addCell(createCell(safe(res.getProgramTitle()), rowFont, rowColor));
                //table.addCell(createCell(safe(res.getActivityId()), rowFont, rowColor));
                table.addCell(createCell(safe(res.getActivityName()), rowFont, rowColor));
                //table.addCell(createCell(safe(res.getSubActivityId()), rowFont, rowColor));
                table.addCell(createCell(safe(res.getSubActivityName()), rowFont, rowColor));
               // table.addCell(createCell(safe(res.getAgencyId()), rowFont, rowColor));
                table.addCell(createCell(safe(res.getAgencyName()), rowFont, rowColor));
                table.addCell(createCell(safe(res.getStartDate()), rowFont, rowColor));
                table.addCell(createCell(safe(res.getEndDate()), rowFont, rowColor));
                table.addCell(createCell(safe(res.getStartTime()), rowFont, rowColor));
                table.addCell(createCell(safe(res.getEndTime()), rowFont, rowColor));
                table.addCell(createCell(safe(res.getSpocName()), rowFont, rowColor));
                table.addCell(createCell(safe(res.getSpocContactNo()), rowFont, rowColor));
                //table.addCell(createCell(safe(res.getProgramLocation()), rowFont, rowColor));
                table.addCell(createCell(safe(res.getProgramLocationName()), rowFont, rowColor));
                table.addCell(createCell(safe(res.getKpi()), rowFont, rowColor));
                alternate = !alternate;
                slNo++;
            }

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
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(bgColor);
        return cell;
    }
}
