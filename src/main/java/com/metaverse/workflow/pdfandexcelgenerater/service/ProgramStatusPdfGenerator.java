package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.metaverse.workflow.expenditure.repository.BulkExpenditureTransactionRepository;
import com.metaverse.workflow.expenditure.repository.ProgramExpenditureRepository;
import com.metaverse.workflow.model.BulkExpenditureTransaction;
import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.model.ProgramExpenditure;
import com.metaverse.workflow.program.repository.ProgramRepository;
import com.metaverse.workflow.program.repository.ProgramRescheduleRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ProgramStatusPdfGenerator {

    private final ProgramRepository programRepository;
    private final ProgramExpenditureRepository expenditureRepository;
    private final BulkExpenditureTransactionRepository transactionRepository;
    private final ProgramRescheduleRepository programRescheduleRepository;

    public void generateProgramsPdf(HttpServletResponse response, Long agencyId) throws IOException, DocumentException {
        List<Program> programList;

        if (agencyId == -1) {
            programList = programRepository.findAll();
        } else {
            programList = programRepository.findByAgencyAgencyId(agencyId);
        }

        // ✅ Sort programs by participant count (descending order)
        programList.sort((p1, p2) -> Integer.compare(
                p2.getParticipants() != null ? p2.getParticipants().size() : 0,
                p1.getParticipants() != null ? p1.getParticipants().size() : 0
        ));

        // Set response properties
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Program_Status_Report.pdf");

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Title
        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("Program Status Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(15f);
        document.add(title);

        // Table headers
        String[] headers = {
                "S.No", "Name of the IA", "Budget Head", "Name Of The Program", "Program Location", "Start Date",
                "Program End Date", "No of sessions added", "No of resource persons participated",
                "No of participants added", "Attendance updated for No participants",
                "No of images uploaded", "No of Media images uploaded",
                "Total Expenditure updated", "Final submission status", "No of times rescheduled"
        };

        PdfPTable table = new PdfPTable(headers.length);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        // Header style
        Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD);
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setBackgroundColor(Color.LIGHT_GRAY);
            headerCell.setPadding(5);
            table.addCell(headerCell);
        }

        // Data rows
        Font dataFont = new Font(Font.HELVETICA, 9);
        int serialNo = 1;

        for (Program program : programList) {
            if (program.getParticipants() == null || program.getParticipants().isEmpty()) {
                continue;
            }

            List<ProgramExpenditure> expenditureList =
                    expenditureRepository.findByAgency_AgencyIdAndProgram_ProgramId(
                            program.getAgency().getAgencyId(), program.getProgramId());

            List<BulkExpenditureTransaction> transactionList =
                    transactionRepository.findByProgram_ProgramId(program.getProgramId());

            // Table data cells
            table.addCell(new Phrase(String.valueOf(serialNo++), dataFont)); // S.No
            table.addCell(new Phrase(program.getAgency().getAgencyName(), dataFont));
            table.addCell(new Phrase(program.getProgramType(), dataFont));
            table.addCell(new Phrase(program.getProgramTitle(), dataFont));
            table.addCell(new Phrase(program.getLocation().getLocationName(), dataFont));

            table.addCell(new Phrase(program.getStartDate() != null ? program.getStartDate().toString().substring(0, 10) : "", dataFont));
            table.addCell(new Phrase(program.getEndDate() != null ? program.getEndDate().toString().substring(0, 10) : "", dataFont));

            table.addCell(new Phrase(String.valueOf(program.getProgramSessionList().size()), dataFont));

            long resourceCount = program.getProgramSessionList().stream()
                    .map(session -> session.getResource() != null ? session.getResource().getResourceId() : null)
                    .filter(Objects::nonNull)
                    .distinct()
                    .count();
            table.addCell(new Phrase(String.valueOf(resourceCount), dataFont));

            int participantCount = program.getParticipants().size();
            table.addCell(new Phrase(String.valueOf(participantCount), dataFont));
            table.addCell(new Phrase(String.valueOf(participantCount), dataFont)); // Attendance (same count here)

            long imageCount = program.getProgramSessionList().stream()
                    .mapToLong(session ->
                            (session.getImage1() != null ? 1 : 0) +
                                    (session.getImage2() != null ? 1 : 0) +
                                    (session.getImage3() != null ? 1 : 0) +
                                    (session.getImage4() != null ? 1 : 0) +
                                    (session.getImage5() != null ? 1 : 0)
                    ).sum();
            table.addCell(new Phrase(String.valueOf(imageCount), dataFont));

            table.addCell(new Phrase(String.valueOf(program.getMediaCoverageList().size()), dataFont));

            double totalExp = expenditureList.stream().mapToDouble(ProgramExpenditure::getCost).sum()
                    + transactionList.stream().mapToDouble(BulkExpenditureTransaction::getAllocatedCost).sum();
            table.addCell(new Phrase(String.format("%.2f", totalExp), dataFont));

            table.addCell(new Phrase(program.getStatus().equals("Program Expenditure Updated") ? "YES" : "NO", dataFont));

            int rescheduleCount = programRescheduleRepository.findByProgram_ProgramId(program.getProgramId()).size();
            table.addCell(new Phrase(String.valueOf(rescheduleCount), dataFont));
        }

        document.add(table);
        document.close();
    }
}
