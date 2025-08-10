package com.metaverse.workflow.pdfandexcelgenerater.service;

import com.metaverse.workflow.districtswithmandals.repository.DistrictRepository;
import com.metaverse.workflow.districtswithmandals.repository.MandalRepositrory;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.participant.repository.ParticipantTempRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ParticipantTempExcel {

    @Autowired
    private ParticipantTempRepository participantTempRepository;

    @Autowired
    private DistrictRepository  districtRepository;

    @Autowired
    private MandalRepositrory mandalRepositrory;


    public void generateParticipantTempExcel(HttpServletResponse response, Long programId) throws IOException, DataException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        XSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        String[] headersrow1 = {
                "Mandatory", "Mandatory", "Mandatory", " ", "Mandatory", "",
                " ", " ", " ", " ", "", " ", "Mandatory if Certificate is Issued", " ",
                "Mandatory", "Mandatory if Organization Type is SHG / Start Up / MSME", "Mandatory if Organization Type is SHG / Start Up / MSME", "Mandatory if Organization Type is SHG / Start Up / MSME", "Mandatory if Organization Type is SHG / Start Up / MSME", "Mandatory if Organization Type is SHG / Start Up / MSME", "Mandatory if Organization Type is SHG / Start Up / MSME", " ", " ", "", "",
                "Mandatory if Organization Type is SHG / Start Up / MSME", " ", " ", "Mandatory if Organization Type is SHG / Start Up / MSME", "Mandatory if Organization Type is SHG / Start Up / MSME", " ", " ", "Mandatory if Organization Type is SHG", "Mandatory if Organization Type is SHG", "Mandatory if Organization Type is Start Up",
                "Mandatory if Organization Type is Start Up", "Mandatory if Organization Type is Start Up", "Mandatory if Organization Type is Start Up", "Mandatory if Organization Type is Start Up", " ", "Mandatory if Organization Type is MSME", " ", "Mandatory if Udyam Registration No is given", " "
        };

        String[] headersrow2 = {
                " ", "Male / Female / Transgender", "Must be 10 digits. Starting number can be 6/7/8/9", "OC/BC/SC/ST/Minority", "Yes/No",
                "Must be 12 digits. No spaces in between", " ", "Current Trade- if Organisation Type is Aspirant", "Yes / No", "Yes / No",
                "Yes / No", "Yes / No", "Please leave Empty if Certificate Issued is No", " ",
                "Aspirant / SHG / Start Up / MSME", " ", " ", " ", " ", " ", " ", " ", " ", "", "",
                "Can be same as Leader / Promoter / Director / Self Contact No", " ", "", "", "Can be same as Organization Contact No", "", "", "", "", "",
                " ", " ", "", " ", " ", "", " ", " ", ""
        };

        String[] headers = {
                "Participant Name*", "Gender*", "Mobile No*", "category", "disability",
                "aadharNo", "email", "Designation / Current Trade", "Previous Participation", "Pre-Training Assessment Conducted",
                "Post Training Assessment Conducted", "Is Certificate Issued", "certificateIssueDate", "Need Assessment Methodology",
                "OrganizationType", "Organization Name*", "Sectors*", "State*", "District*", "Mandal*", "Town / Village*", "Street No", "House No", "geoLocationLongitude", "geoLocationLatitude",
                "Organization Contact No", "email", "website", "Leader / Promoter / Director Name", "Leader / Promoter / Director Contact No", "Leader / Promoter / Director email", "Leader / Promoter / Director Address", "Name Of VO", "Grama Panchayat", "Nature Of Startup",
                "Certificate No", "Incorporation Date", "Date Of issue", "Valid Upto", "Areas Of Working", "Organization Category", "Udyam Registration Number", "Registration Date", "Name of the SHG"
        };

        List<ParticipantTemp> byProgramsProgramIdAndIsDeletedFalse = participantTempRepository.findByPrograms_ProgramIdAndIsDeletedFalse(programId);
        XSSFSheet sheet = workbook.createSheet("participant");

        // Create header row
        XSSFRow headerRow1 = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            XSSFCell cell = headerRow1.createCell(i);
            cell.setCellValue(headersrow1[i]);
            cell.setCellStyle(headerStyle);
        }
        XSSFRow headerRow2 = sheet.createRow(1);
        for (int i = 0; i < headers.length; i++) {
            XSSFCell cell = headerRow2.createCell(i);
            cell.setCellValue(headersrow2[i]);
            cell.setCellStyle(headerStyle);
        }
        XSSFRow headerRow = sheet.createRow(2);
        for (int i = 0; i < headers.length; i++) {
            XSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowIndex = 3;
        for(ParticipantTemp participantTemp : byProgramsProgramIdAndIsDeletedFalse) {
            // Fill data rows
            XSSFRow dataRow = sheet.createRow(rowIndex++);
            dataRow.createCell(0).setCellValue(participantTemp.getParticipantName() != null ? participantTemp.getParticipantName() : "");
            dataRow.createCell(1).setCellValue(participantTemp.getGender() != null ? participantTemp.getGender() + "" : "");
            dataRow.createCell(2).setCellValue(participantTemp.getMobileNo() != null ? participantTemp.getMobileNo() + "" : "");
            dataRow.createCell(3).setCellValue(participantTemp.getCategory() != null ? participantTemp.getCategory() + "" : "");
            dataRow.createCell(4).setCellValue(participantTemp.getDisability() != null ? participantTemp.getDisability() + "" : "");
            dataRow.createCell(5).setCellValue(participantTemp.getAadharNo() != null ? participantTemp.getAadharNo() + "" : "");
            dataRow.createCell(6).setCellValue(participantTemp.getEmail() != null ? participantTemp.getEmail() : "");
            dataRow.createCell(7).setCellValue(participantTemp.getDesignation() != null ? participantTemp.getDesignation() : "");
            dataRow.createCell(8).setCellValue(participantTemp.getPreviousParticipationDetails() != null ? participantTemp.getDesignation() : "");
            dataRow.createCell(9).setCellValue(participantTemp.getPreTrainingAssessmentConducted() != null ? participantTemp.getPreTrainingAssessmentConducted() + "" : "");
            dataRow.createCell(10).setCellValue(participantTemp.getPostTrainingAssessmentConducted() != null ? participantTemp.getPostTrainingAssessmentConducted() + "" : "");
            dataRow.createCell(11).setCellValue(participantTemp.getIsCertificateIssued() != null ? participantTemp.getIsCertificateIssued() + "" : "");
            dataRow.createCell(12).setCellValue(participantTemp.getCertificateIssueDate() != null ? participantTemp.getCertificateIssueDate().toString() : "");
            dataRow.createCell(13).setCellValue(participantTemp.getNeedAssessmentMethodology() != null ? participantTemp.getNeedAssessmentMethodology() : "");
            OrganizationTemp organizationTemp = participantTemp.getOrganizationTemp();
            if (organizationTemp != null) {
                dataRow.createCell(14).setCellValue(organizationTemp.getOrganizationType() != null ? organizationTemp.getOrganizationType() : "");
                dataRow.createCell(15).setCellValue(organizationTemp.getOrganizationName() != null ? organizationTemp.getOrganizationName() : "");

                 StringBuilder sector=new StringBuilder();
                if(!organizationTemp.getSectors().isEmpty()){
                    for (Sector organizationTempSector : organizationTemp.getSectors()) {
                        sector.append(organizationTempSector+", ");
                    }
                    int i = sector.lastIndexOf(",");
                    if (i != -1) {
                        sector.deleteCharAt(i);
                    }
                }
                dataRow.createCell(16).setCellValue(sector.toString());
                dataRow.createCell(17).setCellValue(organizationTemp.getStateId() != null ? organizationTemp.getStateId() : "");
                Optional<District> byId = districtRepository.findById(Integer.valueOf(organizationTemp.getDistId()));
                if(byId.isPresent()) {
                    District district = byId.get();
                    dataRow.createCell(18).setCellValue(district.getDistrictName() != null ? district.getDistrictName() : "");
                }
                Optional<Mandal> byId1 = mandalRepositrory.findById(Integer.valueOf(organizationTemp.getMandal()));
                if(byId1.isPresent()) {
                    Mandal mandal  = byId1.get();
                    dataRow.createCell(19).setCellValue(mandal.getMandalName() != null ? mandal.getMandalName() : "");
                }
                dataRow.createCell(20).setCellValue(organizationTemp.getTown() != null ? organizationTemp.getTown() : "");
                dataRow.createCell(21).setCellValue(organizationTemp.getStreetNo() != null ? organizationTemp.getStreetNo() : "");
                dataRow.createCell(22).setCellValue(organizationTemp.getHouseNo() != null ? organizationTemp.getHouseNo() : "");
                dataRow.createCell(23).setCellValue(organizationTemp.getLongitude() != null ? organizationTemp.getLongitude() + "" : "");
                dataRow.createCell(24).setCellValue(organizationTemp.getLatitude() != null ? organizationTemp.getLatitude() + "" : "");
                dataRow.createCell(25).setCellValue(organizationTemp.getContactNo() != null ? organizationTemp.getContactNo() + "" : "");
                dataRow.createCell(26).setCellValue(organizationTemp.getEmail() != null ? organizationTemp.getEmail() : "");
                dataRow.createCell(27).setCellValue(organizationTemp.getWebsite() != null ? organizationTemp.getWebsite() : "");
                dataRow.createCell(28).setCellValue(organizationTemp.getOwnerName() != null ? organizationTemp.getOwnerName() : "");
                dataRow.createCell(29).setCellValue(organizationTemp.getOwnerContactNo() != null ? organizationTemp.getOwnerContactNo() + "" : "");
                dataRow.createCell(30).setCellValue(organizationTemp.getOwnerEmail() != null ? organizationTemp.getOwnerEmail() : "");
                dataRow.createCell(31).setCellValue(organizationTemp.getOwnerAddress() != null ? organizationTemp.getOwnerAddress() : "");
                dataRow.createCell(32).setCellValue(organizationTemp.getNameOfTheVO() != null ? organizationTemp.getNameOfTheVO() : "");
                dataRow.createCell(33).setCellValue(organizationTemp.getGramaPanchayat() != null ? organizationTemp.getGramaPanchayat() : "");
                dataRow.createCell(34).setCellValue(organizationTemp.getNatureOfStartup() != null ? organizationTemp.getNatureOfStartup() : "");
                dataRow.createCell(35).setCellValue(organizationTemp.getStartupCertificateNo() != null ? organizationTemp.getStartupCertificateNo() : "");
                dataRow.createCell(36).setCellValue(organizationTemp.getIncorporationDate() != null ? organizationTemp.getStartupCertificateNo() : "");
                dataRow.createCell(37).setCellValue(organizationTemp.getDateOfIssue() != null ? organizationTemp.getDateOfIssue().toString() : "");
                dataRow.createCell(38).setCellValue(organizationTemp.getValidUpto() != null ? organizationTemp.getValidUpto().toString() : "");
                dataRow.createCell(39).setCellValue(organizationTemp.getAreasOfWorking() != null ? organizationTemp.getAreasOfWorking() : "");
                dataRow.createCell(40).setCellValue(organizationTemp.getOrganizationType() != null ? organizationTemp.getOrganizationType() : "");
                dataRow.createCell(41).setCellValue(organizationTemp.getUdyamregistrationNo() != null ? organizationTemp.getUdyamregistrationNo() : "");
                dataRow.createCell(42).setCellValue(organizationTemp.getDateOfRegistration() != null ? organizationTemp.getDateOfRegistration().toString() : "");
                dataRow.createCell(43).setCellValue(organizationTemp.getNameOfTheSHG() != null ? organizationTemp.getNameOfTheSHG() : "");
            }
        }
        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Final write
        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }
}
