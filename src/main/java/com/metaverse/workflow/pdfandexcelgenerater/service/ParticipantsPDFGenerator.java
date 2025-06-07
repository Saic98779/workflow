package com.metaverse.workflow.pdfandexcelgenerater.service;


import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.participant.service.ParticipantResponse;
import com.metaverse.workflow.program.service.ProgramResponse;
import com.metaverse.workflow.program.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ParticipantsPDFGenerator {

    @Autowired
    ProgramService programService;


    public ByteArrayInputStream generateProgramParticipantPdf( Long programId) {
        WorkflowResponse program = programService.getProgramById(programId);
        ProgramResponse programData = (ProgramResponse) program.getData();

        WorkflowResponse participants = programService.getProgramParticipantsDropDown(programId);
        List<ParticipantResponse> participantList = (List<ParticipantResponse>) participants.getData();
        Document document = new Document(PageSize.A4.rotate(), 20, 20, 30, 30);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Color.DARK_GRAY);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.BLACK);
            Color headerBgColor = new Color(63, 81, 181);
            Color altRowColor = new Color(224, 224, 224);


            Paragraph progDetailsTitle = new Paragraph("Program Details", titleFont);
            progDetailsTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(progDetailsTitle);
            document.add(new Paragraph(" "));

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



            Paragraph participantTitle = new Paragraph("Participant Details", titleFont);
            participantTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(participantTitle);
            document.add(new Paragraph(" "));


           // document.setPageSize(PageSize.A4.rotate());

            PdfPTable table = new PdfPTable(19);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);


            Stream.of(
                    "Name", "Gender", "Category", "Disability", "Aadhar No", "Mobile No", "Email",
                    "Designation", "Participated Before", "Previous Participation Details",
                    "Pre-Training Assessment", "Post-Training Assessment", "Certificate Issued",
                    "Certificate Issue Date", "Need Assessment Methodology", "Organization",
                    "District", "Mandal", "VO Name"
            ).forEach(header -> {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(headerBgColor);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            });

            boolean alternate = false;
            for (ParticipantResponse p : participantList) {
                Color bg = alternate ? altRowColor : Color.WHITE;

                table.addCell(createCell(p.getParticipantName(), dataFont, bg));
                table.addCell(createCell(String.valueOf(p.getGender()), dataFont, bg));
                table.addCell(createCell(p.getCategory(), dataFont, bg));
                table.addCell(createCell(String.valueOf(p.getDisability()), dataFont, bg));
                table.addCell(createCell(String.valueOf(p.getAadharNo()), dataFont, bg));
                table.addCell(createCell(String.valueOf(p.getMobileNo()), dataFont, bg));
                table.addCell(createCell(p.getEmail(), dataFont, bg));
                table.addCell(createCell(p.getDesignation(), dataFont, bg));
                table.addCell(createCell(String.valueOf(p.getIsParticipatedBefore()), dataFont, bg));
                table.addCell(createCell(p.getPreviousParticipationDetails(), dataFont, bg));
                table.addCell(createCell(String.valueOf(p.getPreTrainingAssessmentConducted()), dataFont, bg));
                table.addCell(createCell(String.valueOf(p.getPostTrainingAssessmentConducted()), dataFont, bg));
                table.addCell(createCell(String.valueOf(p.getIsCertificateIssued()), dataFont, bg));
                table.addCell(createCell(p.getCertificateIssueDate(), dataFont, bg));
                table.addCell(createCell(p.getNeedAssessmentMethodology(), dataFont, bg));
                table.addCell(createCell(p.getOrganizationName(), dataFont, bg));
                table.addCell(createCell(p.getDistrict(), dataFont, bg));
                table.addCell(createCell(p.getMandal(), dataFont, bg));
                table.addCell(createCell(p.getNameOfVO(), dataFont, bg));

                alternate = !alternate;
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

