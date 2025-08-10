package com.metaverse.workflow.common.util;

import com.metaverse.workflow.districtswithmandals.repository.DistrictRepository;
import com.metaverse.workflow.districtswithmandals.repository.MandalRepositrory;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import com.metaverse.workflow.organization.repository.OrganizationTempRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

    @Autowired
    private OrganizationTempRepository organizationTempRepository;

    @Autowired
    private DistrictRepository districtRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);


    public Map<String, Object> excelToParticipants(InputStream is, Long programId) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
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
//
//                    Map<String, String> record = new HashMap<>();
//                    record.put("participantName", getCellValue(currentRow, 0));
//                    record.put("organizationName", organizationName);
//                    record.put("reason", "Organization already exists, stored in OrganizationTemp");
//                    notStoredRecords.add(record);
                } else {
                    Organization newOrg = createOrganizationFromRow(currentRow);
                    organizationRepository.save(newOrg);
                    organizationCache.put(organizationName.toLowerCase() + "::" + organizationType, newOrg);

                    ParticipantTemp tempParticipant = parseTempParticipant(currentRow, newOrg, programId);
                    tempParticipants.add(tempParticipant);
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
        Organization org = new Organization();
        org.setOrganizationType(getCellValue(row, 14));          // OrganizationType
        org.setOrganizationName(getCellValue(row, 15));          // Organization Name*
        String sectorsStr = getCellValue(row, 16);
        if (!sectorsStr.isBlank()) {
            List<Sector> sectors =
                    Arrays.stream(sectorsStr.split(","))
                            .map(String::trim)
                            .filter(name -> !name.isEmpty())
                            .map(sectorRepository::findBySectorName)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
            org.setSectors(sectors);
        }
        org.setStateId(getCellValue(row, 17));                   // State*
        String district = getCellValue(row, 18);// District*
        String mandal = getCellValue(row, 19);// Mandal*
        List<District> allDistrict = districtRepository.findByDistrictName(district);
        allDistrict.forEach(s -> {
            if(s.getDistrictName().equalsIgnoreCase(district)){
                s.getMandals().forEach(m ->
                {
                    if(m.getMandalName().equalsIgnoreCase(mandal)){
                        org.setMandal(m.getMandalId()+"");
                        System.out.println(m.getMandalName());
                    }
                });
                org.setDistId(s.getDistrictId()+"");
            }
        });

        org.setTown(getCellValue(row, 20));                      // Town / Village*
        org.setStreetNo(getCellValue(row, 21));                  // Street No
        org.setHouseNo(getCellValue(row, 22));                   // House No
        String latitude = getCellValue(row, 24);
        if (!latitude.isEmpty()) {
            try {
                org.setLatitude(Double.parseDouble(latitude));
            } catch (NumberFormatException e) {
            }
        } // geoLocationLatitude


        String longitude = getCellValue(row, 23);
        if (!longitude.isEmpty()) {
            try {
                org.setLongitude(Double.parseDouble(longitude));
            } catch (NumberFormatException e) {
            }
        }                  // geoLocationLongitude
        String contactNo = getCellValue(row, 25);
        if (!contactNo.isEmpty()) {
            try {
                org.setContactNo(Long.parseLong(contactNo));
            } catch (NumberFormatException e) {
            }
        }// Organization Contact No*
        org.setEmail(getCellValue(row, 26));                     // org email
        org.setWebsite(getCellValue(row, 27));                   // website
        org.setOwnerName(getCellValue(row, 28));                 // Leader / Promoter / Director Name*
        String ownerContactNo = getCellValue(row, 29);
        if (!ownerContactNo.isEmpty()) {
            try {
                org.setOwnerContactNo(Long.parseLong(ownerContactNo.trim()));
            } catch (NumberFormatException e) {
            }
        }               // Leader / Promoter / Director Contact No*
        org.setOwnerEmail(getCellValue(row, 30));                // Leader / Promoter / Director email
        org.setOwnerAddress(getCellValue(row, 31));              // Leader / Promoter / Director Address
        org.setNameOfTheVO(getCellValue(row, 32));               // Name Of VO*
        org.setStartupCertificateNo(getCellValue(row, 35));
        org.setIncorporationDate(parseDate(row, 36));            // Incorporation Date*
        org.setDateOfIssue(com.metaverse.workflow.common.util.DateUtil.dateToString(parseDate(row, 37),"MM/dd/yyyy"));            // Date Of issue*
        org.setValidUpto(com.metaverse.workflow.common.util.DateUtil.dateToString(parseDate(row, 38),"MM/dd/yyyy"));              // Valid Upto*
        org.setAreasOfWorking(getCellValue(row, 39));            // Areas Of Working
        org.setOrganizationCategory(getCellValue(row, 40));      // Organization Category*
        org.setUdyamregistrationNo(getCellValue(row, 41));// Udyam Registration Number
        org.setDateOfRegistration(parseDate(row, 42));           // Registration Date
        org.setNameOfTheSHG(getCellValue(row, 43));
        org.setNatureOfStartup(getCellValue(row, 34));           // Nature Of Statup*
        org.setGramaPanchayat(getCellValue(row, 33));            // Grama Panchayat*

        return org;
    }

    private Long parseLong(Row row, int col) {
        String v = getCellValue(row, col);
        if (v.isBlank()) return null;
        try { return Long.parseLong(v.trim()); }
        catch (NumberFormatException ex) { return null; }
    }

    private Double parseDouble(Row row, int col) {
        String v = getCellValue(row, col);
        if (v.isBlank()) return null;
        try { return Double.parseDouble(v.trim()); }
        catch (NumberFormatException ex) { return null; }
    }

    private Participant parseParticipant(Row row,
                                         Long programId,
                                         Organization organization) {

        Participant p = new Participant();
        p.setParticipantId(null);
        p.setParticipantName(getCellValue(row, 0));

        String gender = getCellValue(row, 1);
        p.setGender(gender.isEmpty() ? ' ' : gender.charAt(0));
        p.setMobileNo(Long.valueOf(getCellValue(row, 2)));   // Mobile No*
        p.setCategory(getCellValue(row, 3));

        String disability = getCellValue(row, 4);
        p.setDisability(disability.isEmpty() ? ' ' : disability.charAt(0));
        p.setAadharNo(Long.valueOf(getCellValue(row, 5)));   // Aadhaar No (12 digits)
        p.setEmail(getCellValue(row, 6));
        p.setDesignation(getCellValue(row, 7));

        String participated = getCellValue(row, 8);
        p.setIsParticipatedBefore(participated.isEmpty() ? ' ' : participated.charAt(0));

        String preTraining = getCellValue(row, 9);
        p.setPreTrainingAssessmentConducted (preTraining.isEmpty() ? ' ' : preTraining.charAt(0));

        String postTraining = getCellValue(row, 10);
        p.setPostTrainingAssessmentConducted(postTraining.isEmpty() ? ' ' : postTraining.charAt(0));

        String certificate = getCellValue(row, 11);
        p.setIsCertificateIssued(certificate.isEmpty() ? ' ' : certificate.charAt(0));

        p.setCertificateIssueDate(parseDate(row, 12));
        p.setNeedAssessmentMethodology(getCellValue(row, 13));
        String prevPart = getCellValue(row, 8);
        p.setPreviousParticipationDetails(prevPart.isBlank() ? null : prevPart.charAt(0)+"");
        p.setOrganization(organization);
        p.setPrograms(Collections.singletonList(
                programRepository.findById(programId).orElse(null)));
        return p;
    }

    private Character firstChar(String s) {
        return (s == null || s.isBlank()) ? ' ' : s.trim().charAt(0);
    }

    private ParticipantTemp parseTempParticipant(Row row,
                                                 Organization organization,
                                                 Long programId) {
        ParticipantTemp p = new ParticipantTemp();

        p.setParticipantName(getCellValue(row, 0));
        String gender = getCellValue(row, 1);
        p.setGender(gender.isEmpty() ? ' ' : gender.charAt(0));

        p.setMobileNo(Long.valueOf(getCellValue(row, 2)));   // Mobile No*
        p.setCategory(getCellValue(row, 3));
        String disability = getCellValue(row, 4);
        p.setDisability(disability.isEmpty() ? ' ' : disability.charAt(0));

        p.setAadharNo(Long.valueOf(getCellValue(row, 5)));   // Aadhaar No (12 digits)
        p.setEmail(getCellValue(row, 6));
        p.setDesignation(getCellValue(row, 7));
        String participated = getCellValue(row, 8);
        p.setIsParticipatedBefore(participated.isEmpty() ? ' ' : participated.charAt(0));

        String preTraining = getCellValue(row, 9);
        p.setPreTrainingAssessmentConducted (preTraining.isEmpty() ? ' ' : preTraining.charAt(0));
        String postTraining = getCellValue(row, 10);
        p.setPostTrainingAssessmentConducted(postTraining.isEmpty() ? ' ' : postTraining.charAt(0));
        String certificate = getCellValue(row, 11);
        p.setIsCertificateIssued(certificate.isEmpty() ? ' ' : certificate.charAt(0));
        p.setCertificateIssueDate(parseDate(row, 12));
        p.setNeedAssessmentMethodology(getCellValue(row, 13));
        String prevPart = getCellValue(row, 8);
        p.setPreviousParticipationDetails(prevPart.isBlank() ? null : prevPart.charAt(0)+"");

        OrganizationTemp organizationTemp = createOrganizationTempFromOrganization(organization);
        p.setOrganizationTemp(organizationTemp);

        p.setPrograms(Collections.singletonList(
                programRepository.findById(programId).orElse(null)));
        return p;
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
        DataFormatter formatter = new DataFormatter();
        if (row == null) return "";
        Cell cell = row.getCell(index);
        return (cell == null) ? "" : formatter.formatCellValue(cell);
    }

    private static Date parseDate(Row row, int index) {
        if (row == null) return null;

        try {
            Cell cell = row.getCell(index);
            if (cell == null || cell.getCellType() == CellType.BLANK) return null;

            switch (cell.getCellType()) {
                case NUMERIC:
                    if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                        LocalDate localDate = cell.getDateCellValue()
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    }
                    break;

                case STRING:
                    String val = cell.getStringCellValue().trim();
                    if (!val.isEmpty()) {
                        LocalDate localDate = LocalDate.parse(val, DATE_FORMATTER); // e.g., "dd/MM/yyyy"
                        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    }
                    break;

                case FORMULA:
                    FormulaEvaluator evaluator = cell.getSheet().getWorkbook()
                            .getCreationHelper().createFormulaEvaluator();
                    CellValue cellValue = evaluator.evaluate(cell);

                    if (cellValue != null && cellValue.getCellType() == CellType.NUMERIC) {
                        double numericValue = cellValue.getNumberValue();
                        if (org.apache.poi.ss.usermodel.DateUtil.isValidExcelDate(numericValue)) {
                            LocalDate localDate = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(numericValue)
                                    .toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate();
                            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace(); // Optional
        }
        return null;
    }


    private static String parseStringDate(Row row, int index) {
        if (row == null) return null;
        try {
            String val = getCellValue(row, index);
            if (val != null && !val.isEmpty()) {
                return String.valueOf(DATE_FORMATTER.parse(val));
            }
        } catch (Exception e) {
        }
        return null;
    }

    private OrganizationTemp createOrganizationTempFromOrganization(Organization organization) {
        List<OrganizationTemp> existingOrgTemps = organizationTempRepository.findByOrganizationId(organization.getOrganizationId());
        if (!existingOrgTemps.isEmpty()) {
            return existingOrgTemps.get(0);
        }

        OrganizationTemp orgTemp = new OrganizationTemp();
        orgTemp.setOrganizationId(organization.getOrganizationId());
        orgTemp.setOrganizationName(organization.getOrganizationName());
        orgTemp.setOrganizationType(organization.getOrganizationType());
        orgTemp.setOrganizationCategory(organization.getOrganizationCategory());
        orgTemp.setUdyamregistrationNo(organization.getUdyamregistrationNo());
        orgTemp.setDateOfRegistration(organization.getDateOfRegistration());
        orgTemp.setStartupCertificateNo(organization.getStartupCertificateNo());
        orgTemp.setNatureOfStartup(organization.getNatureOfStartup());
        orgTemp.setAreasOfWorking(organization.getAreasOfWorking());
        orgTemp.setIncorporationDate(organization.getIncorporationDate());
        orgTemp.setDateOfIssue(organization.getDateOfIssue());
        orgTemp.setValidUpto(organization.getValidUpto());

        orgTemp.setStateId(organization.getStateId());
        orgTemp.setDistId(organization.getDistId());
        orgTemp.setMandal(organization.getMandal());

        orgTemp.setTown(organization.getTown());
        orgTemp.setStreetNo(organization.getStreetNo());
        orgTemp.setHouseNo(organization.getHouseNo());
        orgTemp.setLatitude(organization.getLatitude());
        orgTemp.setLongitude(organization.getLongitude());
        orgTemp.setContactNo(organization.getContactNo());
        orgTemp.setEmail(organization.getEmail());
        orgTemp.setWebsite(organization.getWebsite());
        orgTemp.setOwnerName(organization.getOwnerName());
        orgTemp.setOwnerContactNo(organization.getOwnerContactNo());
        orgTemp.setOwnerEmail(organization.getOwnerEmail());
        orgTemp.setOwnerAddress(organization.getOwnerAddress());
        orgTemp.setNameOfTheSHG(organization.getNameOfTheSHG());
        orgTemp.setNameOfTheVO(organization.getNameOfTheVO());
        orgTemp.setGramaPanchayat(organization.getGramaPanchayat());
        orgTemp.setSectors(organization.getSectors().stream().toList());

        return organizationTempRepository.save(orgTemp);
    }
}
