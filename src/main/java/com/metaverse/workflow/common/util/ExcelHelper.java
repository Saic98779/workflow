package com.metaverse.workflow.common.util;

import com.metaverse.workflow.model.*;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import com.metaverse.workflow.participant.repository.ParticipantTempRepository;
import com.metaverse.workflow.program.repository.ProgramRepository;
import com.metaverse.workflow.sector.repository.SectorRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ExcelHelper {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private ParticipantTempRepository participantTempRepository;

    @Autowired
    private SectorRepository sectorRepository;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

    public List<Participant> excelToParticipants(InputStream is, Long programId) {
        try (Workbook workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            List<Participant> participants = new ArrayList<>();
            List<ParticipantTemp> tempParticipants = new ArrayList<>();
            int rowNumber = 0;

            Map<String, Organization> organizationCache = new HashMap<>();

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (++rowNumber < 4) continue; // Start processing from row 2 (index 2)
                if (isRowEmpty(currentRow)) break;

                String organizationName = getCellValue(currentRow, 15);
                String organizationType = getCellValue(currentRow, 14);
                List<Organization> organization = getOrCreateOrganization(organizationName, organizationType, currentRow, organizationCache);

                if (organization.size()==1) {
                    Participant participant = parseParticipant(currentRow, programId, organization.get(0));
                    if (organization.size()>1) {
                        tempParticipants.add(parseTempParticipant(currentRow, null));
                    } else {
                        participants.add(participant);
                    }
                }
            }

            saveParticipants(participants, tempParticipants);
            return participants;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage(), e);
        }
    }

    private  List<Organization> getOrCreateOrganization(String organizationName, String organizationType, Row row, Map<String, Organization> cache) {
        String key = organizationName.toLowerCase() + "::" + organizationType;
        List<Organization> existingOrganizations = organizationRepository.findAllByOrganizationNameIgnoreCaseAndOrganizationType(organizationName, organizationType);
        if (existingOrganizations.isEmpty()) {
            Organization newOrg = createOrganizationFromRow(row);
            organizationRepository.save(newOrg);
            cache.put(key, newOrg);
        }
        cache.put(key, existingOrganizations.get(0));
        return existingOrganizations;
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK && cell.toString().trim().length() > 0) {
                return false;
            }
        }
        return true;
    }

    private Organization createOrganizationFromRow(Row row) {
        Organization organization = new Organization();
        try {
            organization.setOrganizationName(getCellValue(row, 15));
            organization.setOrganizationCategory(getCellValue(row, 38));
            organization.setOrganizationType(getCellValue(row, 14));
            organization.setUdyamregistrationNo(getCellValue(row, 39));
            organization.setDateOfRegistration(parseDate(row, 40));
            organization.setAreasOfWorking(getCellValue(row, 37));
            organization.setIncorporationDate(parseDate(row, 34));
            organization.setDateOfIssue(parseStringDate(row, 35));
            organization.setValidUpto(parseStringDate(row, 36));
            organization.setStateId(getCellValue(row, 17));
            organization.setDistId(getCellValue(row, 18));
            organization.setMandal(getCellValue(row, 19));
            organization.setStreetNo(getCellValue(row, 20));
            organization.setHouseNo(getCellValue(row, 21));
            organization.setLatitude(Double.parseDouble(getCellValue(row, 23)));
            organization.setLongitude(Double.parseDouble(getCellValue(row, 24)));
            organization.setContactNo(Long.parseLong(getCellValue(row, 24)));
            organization.setWebsite(getCellValue(row, 25));
            organization.setOwnerName(getCellValue(row, 26));
            organization.setOwnerContactNo(Long.parseLong(getCellValue(row, 27).trim()));
            organization.setOwnerEmail(getCellValue(row, 28));
            organization.setOwnerAddress(getCellValue(row, 29));
            organization.setNameOfTheSHG(getCellValue(row, 30));
            organization.setNameOfTheVO(getCellValue(row, 30));
            organization.setGramaPanchayat(getCellValue(row, 31));

            String sectorsStr = getCellValue(row, 1);
            if (!sectorsStr.trim().isEmpty()) {
                List<Sector> sectorList = Arrays.stream(sectorsStr.split(","))
                        .map(String::trim)
                        .filter(name -> !name.isEmpty())
                        .map(sectorRepository::findBySectorName)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                organization.setSectors(sectorList);
            }
            organization.setNatureOfStartup(getCellValue(row, 32));
            organization.setStartupCertificateNo(getCellValue(row, 33));

        }
        catch (NumberFormatException numberFormatException) {
            //
        }
        return organization;
    }

    private Participant parseParticipant(Row row, Long programId, Organization organization) {
        Participant participant = new Participant();
        participant.setParticipantName(getCellValue(row, 0));
        participant.setGender(getCellValue(row, 1).charAt(0));
        participant.setCategory(getCellValue(row, 3));
        participant.setDisability(getCellValue(row, 4).charAt(0));
        participant.setEmail(getCellValue(row, 6));
        participant.setDesignation(getCellValue(row, 7));
        participant.setIsParticipatedBefore(getCellValue(row, 8).charAt(0));
        participant.setPreviousParticipationDetails(getCellValue(row, 8));
        participant.setPreTrainingAssessmentConducted(getCellValue(row, 9).charAt(0));
        participant.setPostTrainingAssessmentConducted(getCellValue(row, 10).charAt(0));
        String a = getCellValue(row, 11);
        participant.setIsCertificateIssued(getCellValue(row, 11).charAt(0));
        participant.setCertificateIssueDate(parseDate(row, 12));
        participant.setNeedAssessmentMethodology(getCellValue(row, 13));
        participant.setOrganization(organization);
        participant.setPrograms(Collections.singletonList(programRepository.findById(programId).orElse(null)));
        return participant;
    }

    private ParticipantTemp parseTempParticipant(Row row, Organization organization) {
        ParticipantTemp participant = new ParticipantTemp();
        participant.setParticipantName(getCellValue(row, 0));
        participant.setGender(getCellValue(row, 1).charAt(0));
        participant.setCategory(getCellValue(row, 3));
        participant.setDisability(getCellValue(row, 4).charAt(0));
        participant.setEmail(getCellValue(row, 6));
        participant.setDesignation(getCellValue(row, 7));
        participant.setIsParticipatedBefore(getCellValue(row, 8).charAt(0));
        participant.setPreviousParticipationDetails(getCellValue(row, 9));
        participant.setPreTrainingAssessmentConducted(getCellValue(row, 10).charAt(0));
        participant.setPostTrainingAssessmentConducted(getCellValue(row, 11).charAt(0));
        participant.setIsCertificateIssued(getCellValue(row, 12).charAt(0));
        participant.setCertificateIssueDate(parseDate(row, 13));
        participant.setNeedAssessmentMethodology(getCellValue(row, 14));
        participant.setOrganization(organization);
        return participant;
    }

    private void saveParticipants(List<Participant> participants, List<ParticipantTemp> tempParticipants) {
        if (!tempParticipants.isEmpty()) {
            participantTempRepository.saveAll(tempParticipants);
        } else if (!participants.isEmpty()) {
            participantRepository.saveAll(participants);
        }
    }

    private static String getCellValue(Row row, int index) {
        Cell cell = row.getCell(index);
        return (cell == null) ? null : cell.toString().trim();
    }

    private static Date parseDate(Row row, int index) {
        try {
            String val = getCellValue(row, index);
            if (!val.isEmpty()) {
                return DATE_FORMAT.parse(val);
            }
        } catch (Exception e) {
            // Handle or log error
        }
        return null;
    }

    private static String parseStringDate(Row row, int index) {
        try {
            String val = getCellValue(row, index);
            if (!val.isEmpty()) {
                return String.valueOf(DATE_FORMAT.parse(val));
            }
        } catch (Exception e) {
            // Handle or log error
        }
        return null;
    }
}






























//    public List<Participant> excelToParticipants(InputStream is, Long programId) {
//        try {
//            Workbook workbook = new XSSFWorkbook(is);
//            Sheet sheet = workbook.getSheetAt(0);
//            Iterator<Row> rows = sheet.iterator();
//            List<Participant> participants = new ArrayList<>();
//            List<ParticipantTemp> tempParticipants = new ArrayList<>();
//            int rowNumber = 0;
//
//            while (rows.hasNext()) {
//                Row currentRow = rows.next();
//
//                if (rowNumber == 0) {
//                    rowNumber++;
//                    continue;
//                }
//
//                Organization organization = new Organization();
//                Participant participant = new Participant();
//                ParticipantTemp participantTemp = new ParticipantTemp();
//                List<Program> programList = new ArrayList<>();
//                String organizationName = getCellValue(currentRow, 1);
//                String organizationType = getCellValue(currentRow, 3);
//                List<Organization> organizationList = organizationRepository.findAllByOrganizationNameIgnoreCaseAndOrganizationType(organizationName,organizationType);
//                Program programRes = programRepository.findById(programId).orElse(null);
//
//                if (programRes != null) {
//                    programList.add(programRes);
//                }
//
//                String aadharVal = getCellValue(currentRow, 33);
//                double parsedDouble = Double.parseDouble(aadharVal);
//                long aadharNumber = (long) parsedDouble;
//
//                String mobileNo = getCellValue(currentRow, 34);
//                double mobileNumber = Double.parseDouble(mobileNo);
//                long mobile = (long) mobileNumber;
//
//                Date certificateDate = parseDate(currentRow, 42);
//
//                if(organizationList.isEmpty()) {
//                    organization.setOrganizationCategory(getCellValue(currentRow, 2));
//                    organization.setOrganizationType(getCellValue(currentRow, 3));
//                    organization.setUdyamregistrationNo(getCellValue(currentRow, 4));
//                    organization.setDateOfRegistration(parseDate(currentRow, 5));
//                    organization.setStartupCertificateNo(getCellValue(currentRow, 6));
//                    organization.setNatureOfStartup(getCellValue(currentRow, 7));
//                    organization.setAreasOfWorking(getCellValue(currentRow, 8));
//                    organization.setIncorporationDate(parseDate(currentRow, 9));
//                    organization.setDateOfIssue(parseStringDate(currentRow, 10));
//                    organization.setValidUpto(parseStringDate(currentRow, 11));
//                    organization.setStateId(getCellValue(currentRow, 12));
//                    organization.setDistId(getCellValue(currentRow, 13));
//                    organization.setMandal(getCellValue(currentRow, 14));
//                    organization.setStreetNo(getCellValue(currentRow, 15));
//                    organization.setHouseNo(getCellValue(currentRow, 16));
//                    double latitude = Double.parseDouble(getCellValue(currentRow, 17));
//                    double longitude = Double.parseDouble(getCellValue(currentRow, 18));
//                    organization.setLatitude(latitude);
//                    organization.setLongitude(longitude);
//                    long contactNo = Long.parseLong(getCellValue(currentRow, 19));
//                    organization.setContactNo(contactNo);
//                    organization.setWebsite(getCellValue(currentRow, 21));
//                    organization.setOwnerName(getCellValue(currentRow, 22));
//                    String ownerContactStr = getCellValue(currentRow, 23);
//                    long ownerContactNo = Long.parseLong(ownerContactStr.trim());
//                    organization.setOwnerContactNo(ownerContactNo);
//                    organization.setOwnerEmail(getCellValue(currentRow, 24));
//                    organization.setOwnerAddress(getCellValue(currentRow, 25));
//                    organization.setNameOfTheSHG(getCellValue(currentRow, 26));
//                    organization.setNameOfTheVO(getCellValue(currentRow, 27));
//                    organization.setGramaPanchayat(getCellValue(currentRow, 28));
//                    String sectorsStr = getCellValue(currentRow, 29);
//                    if (!sectorsStr.trim().isEmpty()) {
//                        List<Sector> sectorList = Arrays.stream(sectorsStr.split(","))
//                                .map(String::trim)
//                                .filter(name -> !name.isEmpty())
//                                .map(sectorName -> sectorRepository.findBySectorName(sectorName))
//                                .filter(Objects::nonNull)
//                                .collect(Collectors.toList());
//
//                        organization.setSectors(sectorList);
//                    }
//                    organizationRepository.save(organization);
//                  organizationList = organizationRepository.findAllByOrganizationNameIgnoreCaseAndOrganizationType(organizationName,organizationType);
//                }
//
//                if (organizationList.size() == 1) {
//                    participant.setParticipantName(getCellValue(currentRow, 0));
//                    participant.setGender(getCellValue(currentRow, 30).charAt(0));
//                    participant.setCategory(getCellValue(currentRow, 31));
//                    participant.setDisability(getCellValue(currentRow, 32).charAt(0));
//                    participant.setEmail(getCellValue(currentRow, 35));
//                    participant.setDesignation(getCellValue(currentRow, 36));
//                    participant.setIsParticipatedBefore(getCellValue(currentRow, 37).charAt(0));
//                    participant.setPreviousParticipationDetails(getCellValue(currentRow, 38));
//                    participant.setPreTrainingAssessmentConducted(getCellValue(currentRow, 39).charAt(0));
//                    participant.setPostTrainingAssessmentConducted(getCellValue(currentRow, 40).charAt(0));
//                    participant.setIsCertificateIssued(getCellValue(currentRow, 41).charAt(0));
//                    participant.setCertificateIssueDate(certificateDate);
//                    participant.setNeedAssessmentMethodology(getCellValue(currentRow, 43));
//                    if (!organizationList.isEmpty()) {
//                        participantTemp.setOrganization(organizationList.get(0));
//                    }
//                    participant.setPrograms(programList);
//
//                    participants.add(participant);
//
//                } else {
//                    participantTemp.setParticipantName(getCellValue(currentRow, 0));
//                    participantTemp.setGender(getCellValue(currentRow, 30).charAt(0));
//                    participantTemp.setCategory(getCellValue(currentRow, 31));
//                    participantTemp.setDisability(getCellValue(currentRow, 32).charAt(0));
//                    participantTemp.setAadharNo(aadharNumber);
//                    participantTemp.setMobileNo(mobile);
//                    participantTemp.setEmail(getCellValue(currentRow, 35));
//                    participantTemp.setDesignation(getCellValue(currentRow, 36));
//                    participantTemp.setIsParticipatedBefore(getCellValue(currentRow, 37).charAt(0));
//                    participantTemp.setPreviousParticipationDetails(getCellValue(currentRow, 38));
//                    participantTemp.setPreTrainingAssessmentConducted(getCellValue(currentRow, 39).charAt(0));
//                    participantTemp.setPostTrainingAssessmentConducted(getCellValue(currentRow, 40).charAt(0));
//                    participantTemp.setIsCertificateIssued(getCellValue(currentRow, 41).charAt(0));
//                    participantTemp.setCertificateIssueDate(certificateDate);
//                    participantTemp.setNeedAssessmentMethodology(getCellValue(currentRow, 43));
//                    if (!organizationList.isEmpty()) {
//                        participantTemp.setOrganization(organizationList.get(0));
//                    }
//                    tempParticipants.add(participantTemp);
//                }
//            }
//
//            workbook.close();
//
//            if (!tempParticipants.isEmpty()) {
//                participantTempRepository.saveAll(tempParticipants);
//            } else if (!participants.isEmpty()) {
//                participantRepository.saveAll(participants);
//            }
//
//            return participants;
//
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage(), e);
//        }
//    }
//
//    private static String getCellValue(Row row, int index) {
//        Cell cell = row.getCell(index);
//        return (cell == null) ? "" : cell.toString().trim();
//    }
//
//    private static Date parseDate(Row row, int index) {
//        try {
//            String val = getCellValue(row, index);
//            if (!val.isEmpty()) {
//                return new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).parse(val);
//            }
//        } catch (Exception e) {
//        }
//        return null;
//    }
//
//    private static String parseStringDate(Row row, int index) {
//        try {
//            String val = getCellValue(row, index);
//            if (!val.isEmpty()) {
//                return String.valueOf(new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).parse(val));
//            }
//        } catch (Exception e) {
//        }
//        return null;
//    }
//}
















//    public List<Participant> excelToParticipants(InputStream is, Long programId) {
//        try {
//            Workbook workbook = new XSSFWorkbook(is);
//            Sheet sheet = workbook.getSheetAt(0);
//            Iterator<Row> rows = sheet.iterator();
//            List<Participant> participants = new ArrayList<>();
//            List<ParticipantTemp> tempParticipants = new ArrayList<>();
//            int rowNumber = 0;
//
//            while (rows.hasNext()) {
//                Row currentRow = rows.next();
//
//                if (rowNumber == 0) {
//                    rowNumber++;
//                    continue;
//                }
//
//                Participant participant = new Participant();
//                ParticipantTemp participantTemp = new ParticipantTemp();
//                List<Program> programList = new ArrayList<>();
//                String organizationName = getCellValue(currentRow, 1);
//
//                List<Organization> organizationList = organizationRepository.findAllByOrganizationNameIgnoreCase(organizationName);
//
//                Program programRes = programRepository.findById(programId).get();
//                if(null != programRes) {
//                    programList.add(programRes);
//                }
//
//                if (organizationList.size() == 1) {
//                    participant.setParticipantName(getCellValue(currentRow, 0));
//                    participant.setGender(getCellValue(currentRow, 1).charAt(2));
//                    participant.setCategory(getCellValue(currentRow, 3));
//                    participant.setDisability(getCellValue(currentRow, 4).charAt(0));
//                    String aadharVal = getCellValue(currentRow, 5);
//                    double parsedDouble = Double.parseDouble(aadharVal);
//                    long aadharNumber = (long) parsedDouble;
//                    participantTemp.setAadharNo(aadharNumber);
//                    String mobileNo = getCellValue(currentRow, 6);
//                    double mobileNumber = Double.parseDouble(mobileNo);
//                    long mobile = (long) mobileNumber;
//                    participantTemp.setMobileNo(mobile);
//                    participant.setEmail(getCellValue(currentRow, 7));
//                    participant.setDesignation(getCellValue(currentRow, 8));
//                    participant.setIsParticipatedBefore(getCellValue(currentRow, 9).charAt(0));
//                    participant.setPreviousParticipationDetails(getCellValue(currentRow, 10));
//                    participant.setPreTrainingAssessmentConducted(getCellValue(currentRow, 11).charAt(0));
//                    participant.setPostTrainingAssessmentConducted(getCellValue(currentRow, 12).charAt(0));
//                    participant.setIsCertificateIssued(getCellValue(currentRow, 13).charAt(0));
//                    String dateStr = getCellValue(currentRow, 14);
//                    if (!dateStr.isEmpty()) {
//                        Date date = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).parse(dateStr);
//                        participantTemp.setCertificateIssueDate(date);
//                    }
//
//                    participant.setNeedAssessmentMethodology(getCellValue(currentRow, 15));
//                    participant.setOrganization(organizationList.get(0));
//                    participant.setOrganization(organizationList.get(0)!=null ? organizationList.get(0) : null);
//                    participant.setPrograms(programList);
//                    participants.add(participant);
//                }
//                else {
//                    participantTemp.setParticipantName(getCellValue(currentRow, 0));
//                    participantTemp.setGender(getCellValue(currentRow, 1).charAt(2));
//                    participantTemp.setCategory(getCellValue(currentRow, 3));
//                    participantTemp.setDisability(getCellValue(currentRow, 4).charAt(0));
//                    String aadharVal = getCellValue(currentRow, 5);
//                    double parsedDouble = Double.parseDouble(aadharVal);
//                    long aadharNumber = (long) parsedDouble;
//                    participantTemp.setAadharNo(aadharNumber);
//                    String mobileNo = getCellValue(currentRow, 6);
//                    double mobileNumber = Double.parseDouble(mobileNo);
//                    long mobile = (long) mobileNumber;
//                    participantTemp.setMobileNo(mobile);
//                    participantTemp.setEmail(getCellValue(currentRow, 7));
//                    participantTemp.setDesignation(getCellValue(currentRow, 8));
//                    participantTemp.setIsParticipatedBefore(getCellValue(currentRow, 9).charAt(0));
//                    participantTemp.setPreviousParticipationDetails(getCellValue(currentRow, 10));
//                    participantTemp.setPreTrainingAssessmentConducted(getCellValue(currentRow, 11).charAt(0));
//                    participantTemp.setPostTrainingAssessmentConducted(getCellValue(currentRow, 12).charAt(0));
//                    participantTemp.setIsCertificateIssued(getCellValue(currentRow, 13).charAt(0));
//                    String dateStr = getCellValue(currentRow, 14);
//                    if (!dateStr.isEmpty()) {
//                        Date date = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).parse(dateStr);
//                        participantTemp.setCertificateIssueDate(date);
//                    }
//                    participantTemp.setNeedAssessmentMethodology(getCellValue(currentRow, 15));
//                    participantTemp.setOrganization(organizationList.get(0));
//                    tempParticipants.add(participantTemp);
//                }
//            }
//
//            workbook.close();
//            if(tempParticipants.size() > 1) {
//                participantTempRepository.saveAll(tempParticipants);
//            }
//            else {
//                participantRepository.save(participants.get(0));
//            }
//            return participants;
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
//        }
//    }

//    private static String getCellValue(Row row, int index) {
//        Cell cell = row.getCell(index);
//        return (cell == null) ? "" : cell.toString().trim();
//    }
//}
