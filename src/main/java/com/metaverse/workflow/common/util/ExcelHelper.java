package com.metaverse.workflow.common.util;

import com.metaverse.workflow.model.*;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import com.metaverse.workflow.participant.repository.ParticipantTempRepository;
import com.metaverse.workflow.program.repository.ProgramRepository;
import com.metaverse.workflow.sector.repository.SectorRepository;
import org.apache.poi.ss.usermodel.*;
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

    public Map<String, Object> excelToParticipants(InputStream is, Long programId) {
        try (Workbook workbook = WorkbookFactory.create(is)) {
            if (workbook.getNumberOfSheets() == 0) {
                throw new RuntimeException("Excel file contains no sheets");
            }

            Sheet sheet = workbook.getSheetAt(0);

            if (sheet.getPhysicalNumberOfRows() == 0) {
                throw new RuntimeException("Excel sheet is empty");
            }

            Iterator<Row> rows = sheet.iterator();
            List<Participant> participants = new ArrayList<>();
            List<ParticipantTemp> tempParticipants = new ArrayList<>();
            List<Map<String, String>> notStoredRecords = new ArrayList<>();
            int rowNumber = 0;
            int batchSize = 500;

            Map<String, Organization> organizationCache = new HashMap<>();

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (++rowNumber < 4) continue;
                if (isRowEmpty(currentRow)) break;

                String organizationName = getCellValue(currentRow, 15);
                String organizationType = getCellValue(currentRow, 14);

                List<Organization> existingOrganizations = organizationRepository.findAllByOrganizationNameIgnoreCaseAndOrganizationType(organizationName, organizationType);
                boolean organizationExists = !existingOrganizations.isEmpty();

                if (organizationExists) {
                    Organization organization = existingOrganizations.get(0);
                    ParticipantTemp tempParticipant = parseTempParticipant(currentRow, organization, programId);
                    tempParticipants.add(tempParticipant);

                    Map<String, String> record = new HashMap<>();
                    record.put("participantName", getCellValue(currentRow, 0));
                    record.put("organizationName", organizationName);
                    record.put("reason", "Organization already exists");
                    notStoredRecords.add(record);
                } else {
                    Organization newOrg = createOrganizationFromRow(currentRow);
                    organizationRepository.save(newOrg);
                    organizationCache.put(organizationName.toLowerCase() + "::" + organizationType, newOrg);

                    Participant participant = parseParticipant(currentRow, programId, newOrg);
                    participants.add(participant);
                }

                if ((participants.size() + tempParticipants.size()) % batchSize == 0) {
                    saveParticipants(participants, tempParticipants);
                    participants.clear();
                    tempParticipants.clear();
                }
            }

            if (!participants.isEmpty() || !tempParticipants.isEmpty()) {
                saveParticipants(participants, tempParticipants);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("participantsStored", participants.size());
            result.put("tempParticipantsStored", tempParticipants.size());
            result.put("notStoredRecords", notStoredRecords);

            return result;

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
        } else {
            cache.put(key, existingOrganizations.get(0));
        }
        return existingOrganizations;
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;

        if (row.getFirstCellNum() == -1) return true;

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

            String latitude = getCellValue(row, 23);
            if (!latitude.isEmpty()) {
                try {
                    organization.setLatitude(Double.parseDouble(latitude));
                } catch (NumberFormatException e) {
                }
            }

            String longitude = getCellValue(row, 24);
            if (!longitude.isEmpty()) {
                try {
                    organization.setLongitude(Double.parseDouble(longitude));
                } catch (NumberFormatException e) {
                }
            }

            String contactNo = getCellValue(row, 22);
            if (!contactNo.isEmpty()) {
                try {
                    organization.setContactNo(Long.parseLong(contactNo));
                } catch (NumberFormatException e) {
                }
            }

            organization.setWebsite(getCellValue(row, 25));
            organization.setOwnerName(getCellValue(row, 26));

            String ownerContactNo = getCellValue(row, 27);
            if (!ownerContactNo.isEmpty()) {
                try {
                    organization.setOwnerContactNo(Long.parseLong(ownerContactNo.trim()));
                } catch (NumberFormatException e) {
                }
            }
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
        // Explicitly set participantId to null to allow auto-generation
        participant.setParticipantId(null);
        participant.setParticipantName(getCellValue(row, 0));

        String gender = getCellValue(row, 1);
        participant.setGender(gender.isEmpty() ? ' ' : gender.charAt(0));

        participant.setCategory(getCellValue(row, 3));

        String disability = getCellValue(row, 4);
        participant.setDisability(disability.isEmpty() ? ' ' : disability.charAt(0));

        participant.setEmail(getCellValue(row, 6));
        participant.setDesignation(getCellValue(row, 7));

        String participated = getCellValue(row, 8);
        participant.setIsParticipatedBefore(participated.isEmpty() ? ' ' : participated.charAt(0));

        participant.setPreviousParticipationDetails(getCellValue(row, 8));

        String preTraining = getCellValue(row, 9);
        participant.setPreTrainingAssessmentConducted(preTraining.isEmpty() ? ' ' : preTraining.charAt(0));

        String postTraining = getCellValue(row, 10);
        participant.setPostTrainingAssessmentConducted(postTraining.isEmpty() ? ' ' : postTraining.charAt(0));

        String certificate = getCellValue(row, 11);
        participant.setIsCertificateIssued(certificate.isEmpty() ? ' ' : certificate.charAt(0));

        participant.setCertificateIssueDate(parseDate(row, 12));
        participant.setNeedAssessmentMethodology(getCellValue(row, 13));
        participant.setOrganization(organization);
        participant.setPrograms(Collections.singletonList(programRepository.findById(programId).orElse(null)));
        return participant;
    }

    private ParticipantTemp parseTempParticipant(Row row, Organization organization, Long programId) {
        ParticipantTemp participant = new ParticipantTemp();
        participant.setParticipantName(getCellValue(row, 0));

        String gender = getCellValue(row, 1);
        participant.setGender(gender.isEmpty() ? ' ' : gender.charAt(0));

        participant.setCategory(getCellValue(row, 3));

        String disability = getCellValue(row, 4);
        participant.setDisability(disability.isEmpty() ? ' ' : disability.charAt(0));

        participant.setEmail(getCellValue(row, 6));
        participant.setDesignation(getCellValue(row, 7));

        String participated = getCellValue(row, 8);
        participant.setIsParticipatedBefore(participated.isEmpty() ? ' ' : participated.charAt(0));

        participant.setPreviousParticipationDetails(getCellValue(row, 9));

        String preTraining = getCellValue(row, 10);
        participant.setPreTrainingAssessmentConducted(preTraining.isEmpty() ? ' ' : preTraining.charAt(0));

        String postTraining = getCellValue(row, 11);
        participant.setPostTrainingAssessmentConducted(postTraining.isEmpty() ? ' ' : postTraining.charAt(0));

        String certificate = getCellValue(row, 12);
        participant.setIsCertificateIssued(certificate.isEmpty() ? ' ' : certificate.charAt(0));

        participant.setCertificateIssueDate(parseDate(row, 13));
        participant.setNeedAssessmentMethodology(getCellValue(row, 14));
        participant.setOrganization(organization);
        participant.setPrograms(Collections.singletonList(programRepository.findById(programId).orElse(null)));

        return participant;
    }

    private void saveParticipants(List<Participant> participants, List<ParticipantTemp> tempParticipants) {
        if (!tempParticipants.isEmpty()) {
            participantTempRepository.saveAll(tempParticipants);
        }
        if (!participants.isEmpty()) {
            participantRepository.saveAll(participants);
        }
    }

    private static String getCellValue(Row row, int index) {
        if (row == null) return "";
        Cell cell = row.getCell(index);
        return (cell == null) ? "" : cell.toString().trim();
    }

    private static Date parseDate(Row row, int index) {
        if (row == null) return null;
        try {
            String val = getCellValue(row, index);
            if (val != null && !val.isEmpty()) {
                return DATE_FORMAT.parse(val);
            }
        } catch (Exception e) {
        }
        return null;
    }

    private static String parseStringDate(Row row, int index) {
        if (row == null) return null;
        try {
            String val = getCellValue(row, index);
            if (val != null && !val.isEmpty()) {
                return String.valueOf(DATE_FORMAT.parse(val));
            }
        } catch (Exception e) {
        }
        return null;
    }
}
