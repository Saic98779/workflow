package com.metaverse.workflow.programoutcome.service;

import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.outcomes.ONDCRegistration;
import com.metaverse.workflow.model.outcomes.ProgramOutcomeTable;
import com.metaverse.workflow.model.outcomes.TReDSRegistration;
import com.metaverse.workflow.model.outcomes.ZEDCertification;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import com.metaverse.workflow.programoutcome.dto.*;
import com.metaverse.workflow.programoutcome.repository.*;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProgramOutcomeServiceAdapter implements ProgramOutcomeService {

    @Autowired
    ProgramOutcomeTableRepository programOutcomeTableRepository;

    @Autowired
    ONDCRegistrationRepository ondcRegistrationRepository;

    @Autowired
    ONDCTransactionRepository ondcTransactionRepository;

    @Autowired
    UdyamRegistrationRepository udyamRegistrationRepository;

    @Autowired
    CGTMSETransactionRepository cgtmseTransactionRepository;

    @Autowired
    GeMTransactionRepository geMTransactionRepository;
    @Autowired
    AgencyRepository agencyRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    TReDSRegistrationRepository tredsRegistrationRepository;

    @Autowired
    TReDSTransactionRepository tredsTransactionRepository;

    @Autowired
    PMEGPRepository pmegpRepository;

    @Autowired
    PMMYRepository pmmyRepository;

    @Autowired
    PMSRepository pmsRepository;

    @Autowired
    ICSchemeRepository icSchemeRepository;

    @Autowired
    NSICRepository nsicRepository;
    @Autowired
    PatentsRepository patentsRepository;
    @Autowired
    GIProductRepository giProductRepository;
    @Autowired
    BarcodeRepository barcodeRepository;
    @Autowired
    TreadMarkRepository treadMarkRepository;
    @Autowired
    LeanRepository leanRepository;
    @Autowired
    ZEDCertificationRepository zedCertificationRepository;


    @Override
    public List<ProgramOutcomeTable> getProgramOutcomeTables() {
        return programOutcomeTableRepository.findAll();
    }

    @Override
    public WorkflowResponse getOutcomeDetails(Long participantId, String outcome, String type) {
        String className = "com.metaverse.workflow.programoutcome.dto." + outcome + "Request";
        try {
            Field[] fields = Class.forName(className).getFields();
            List<OutcomeDetails.OutcomeDataSet> columnList = new ArrayList<>();
            for (Field field : fields) {
                columnList.add(
                        OutcomeDetails.OutcomeDataSet.builder()
                                .fieldDisplayName(getFieldDisplayName(field.getName()))
                                .fieldName(field.getName())
                                .fieldType(getFieldType(field))
                                .build()
                );
            }
            switch (outcome) {
                case "ONDCTransaction": {

                    List<ONDCRegistration> ondcRegistration = ondcRegistrationRepository.findByParticipantId(participantId);
                    if (ondcRegistration == null || ondcRegistration.isEmpty())
                        return WorkflowResponse.builder()
                                .status(400)
                                .message("ONDC Registration not completed")
                                .build();
                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("ONDC Registration No"))
                                    .fieldName("ondcRegistrationNo").fieldType("label")
                                    .fieldValue(ondcRegistration.get(0).getOndcRegistrationNo())
                                    .build()
                    );
                    break;
                }
                case "TReDSTransaction": {
                    List<TReDSRegistration> tReDSRegistrations = tredsRegistrationRepository.findByParticipantId(participantId);
                    if (tReDSRegistrations == null || tReDSRegistrations.isEmpty())
                        return WorkflowResponse.builder()
                                .status(400)
                                .message("TReDS Registration not completed")
                                .build();

                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("TReDS Registration No"))
                                    .fieldName("tredsRegistrationNo").fieldType("label")
                                    .fieldValue(tReDSRegistrations.get(0).getTredsRegistrationNo())
                                    .build()
                    );
                    break;
                }
                case "ZEDCertification": {
                    List<ZEDCertification> zedCertifications = zedCertificationRepository.findByParticipantId(participantId);

                    String currentType = zedCertifications != null && !zedCertifications.isEmpty()
                            ? zedCertifications.get(0).getZedCertificationType()
                            : null;


                    if ("Silver".equalsIgnoreCase(type) && !"Bronze".equalsIgnoreCase(currentType)) {
                        return WorkflowResponse.builder()
                                .status(400)
                                .message("Bronze certification is not added yet. Please complete Bronze before applying for Silver.")
                                .build();
                    }

                    if ("Gold".equalsIgnoreCase(type) && !"Silver".equalsIgnoreCase(currentType)) {
                        return WorkflowResponse.builder()
                                .status(400)
                                .message("Silver certification is not added yet. Please complete Silver before applying for Gold.")
                                .build();
                    }


                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("Zed Certifications Type"))
                                    .fieldName("zedCertificationType")
                                    .fieldType("label")
                                    .fieldValue(currentType == null ? "Bronze" : currentType)
                                    .build()
                    );

                    break;
                }

            }
            return WorkflowResponse.builder().status(200)
                    .message("Success")
                    .data(OutcomeDetails.builder().outcomeForm(columnList).build())
                    .build();
        } catch (ClassNotFoundException ex) {
            log.error("Invalid out come name");
            return WorkflowResponse.builder().status(500)
                    .message("Internal server error").build();
        }
    }

    @Override
    public WorkflowResponse saveOutCome(String outcomeName, String data) throws ParseException, DataException {
        String status = "";
        JSONParser parser = new JSONParser();
        switch (outcomeName) {
            case "ONDCRegistration": {
                ONDCRegistrationRequest request = parser.parse(data, ONDCRegistrationRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));
                if (ondcRegistrationRepository.existsByParticipant_ParticipantId(request.getParticipantId())) {
                    return WorkflowResponse.builder()
                            .message("ONDC registration already exists for the given participant.")
                            .data(ondcRegistrationRepository.findByParticipantId(request.getParticipantId()))
                            .build();
                }
                ondcRegistrationRepository.save(OutcomeRequestMapper.mapOndcRegistration(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "ONDCTransaction": {
                ONDCTransactionRequest ondcTransactionRequest = parser.parse(data, ONDCTransactionRequest.class);
                List<ONDCRegistration> ondcRegistrationList = ondcRegistrationRepository.findByParticipantId(ondcTransactionRequest.getParticipantId());
                if (ondcRegistrationList == null || ondcRegistrationList.isEmpty())
                    return WorkflowResponse.builder().status(400).message("Invalid Ondc Registration").build();
                ondcTransactionRepository.save(OutcomeRequestMapper.mapOndcTransaction(ondcTransactionRequest, ondcRegistrationList.get(0)));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "UdyamRegistration": {
                UdyamRegistrationRequest request = parser.parse(data, UdyamRegistrationRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));
                if (udyamRegistrationRepository.existsByParticipant_ParticipantId(request.getParticipantId())) {
                    return WorkflowResponse.builder()
                            .message("Udyam registration already exists for the given participant.")
                            .data(udyamRegistrationRepository.findByParticipantParticipantId(request.getParticipantId()))
                            .build();
                }
                udyamRegistrationRepository.save(OutcomeRequestMapper.mapUdyamRegistration(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "CGTMSETransaction": {
                CGTMSETransactionRequest request = parser.parse(data, CGTMSETransactionRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));
                cgtmseTransactionRepository.save(OutcomeRequestMapper.mapCGTMSETransaction(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "GeMTransaction": {
                GeMTransactionRequest request = parser.parse(data, GeMTransactionRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));
                if (geMTransactionRepository.existsByParticipant_ParticipantId(request.getParticipantId())) {
                    return WorkflowResponse.builder()
                            .message("Udyam registration already exists for the given participant.")
                            .data(geMTransactionRepository.findByParticipantParticipantId(request.getParticipantId()))
                            .build();
                }
                geMTransactionRepository.save(OutcomeRequestMapper.mapGeMTransaction(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "TReDSRegistration": {
                TReDSRegistrationRequest request = parser.parse(data, TReDSRegistrationRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));
                if (tredsRegistrationRepository.existsByParticipant_ParticipantId(request.getParticipantId())) {
                    return WorkflowResponse.builder()
                            .message("TReDS registration already exists for the given participant.")
                            .data(tredsRegistrationRepository.findByParticipantId(request.getParticipantId()))
                            .build();
                }
                tredsRegistrationRepository.save(OutcomeRequestMapper.mapTredsRegistration(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "TReDSTransaction": {
                TReDSTransactionRequest tredsTransactionRequest = parser.parse(data, TReDSTransactionRequest.class);
                List<TReDSRegistration> tredsRegistrationList = tredsRegistrationRepository.findByParticipantId(tredsTransactionRequest.getParticipantId());
                if (tredsRegistrationList == null || tredsRegistrationList.isEmpty())
                    return WorkflowResponse.builder().status(400).message("Invalid TReDS Registration").build();
                tredsTransactionRepository.save(OutcomeRequestMapper.mapTredsTransaction(tredsTransactionRequest, tredsRegistrationList.get(0)));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "PMEGP": {
                PMEGPRequest request = parser.parse(data, PMEGPRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                pmegpRepository.save(OutcomeRequestMapper.mapPmegp(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "PMMY": {
                PMMYRequest request = parser.parse(data, PMMYRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));
                pmmyRepository.save(OutcomeRequestMapper.mapPmmy(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "PMS": {
                PMSRequest request = parser.parse(data, PMSRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));
                pmsRepository.save(OutcomeRequestMapper.mapPms(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "ICScheme": {
                ICSchemeRequest request = parser.parse(data, ICSchemeRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));
                icSchemeRepository.save(OutcomeRequestMapper.mapIcScheme(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "NSIC": {
                NSICRequest request = parser.parse(data, NSICRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));
                nsicRepository.save(OutcomeRequestMapper.mapNsic(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "Patents": {
                PatentsRequest request = parser.parse(data, PatentsRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));
                patentsRepository.save(OutcomeRequestMapper.mapPatents(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "GIProduct": {
                GIProductRequest request = parser.parse(data, GIProductRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));
                giProductRepository.save(OutcomeRequestMapper.mapGIProduct(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "Barcode": {
                BarcodeRequest request = parser.parse(data, BarcodeRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));
                barcodeRepository.save(OutcomeRequestMapper.mapBarcode(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "TreadMark": {
                TreadMarkRequest request = parser.parse(data, TreadMarkRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));
                treadMarkRepository.save(OutcomeRequestMapper.mapTreadMark(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "Lean": {
                LeanRequest request = parser.parse(data, LeanRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));
                leanRepository.save(OutcomeRequestMapper.mapLean(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "ZEDCertification": {
                ZEDCertificationRequest request = parser.parse(data, ZEDCertificationRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                zedCertificationRepository.save(OutcomeRequestMapper.mapZEDCertification(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
        }
        return WorkflowResponse.builder().status(200).message("Success").data(status).build();
    }

    @Override
    public WorkflowResponse getOutcomeDetailsByName(String outcome) {
        switch (outcome) {
            case "ONDCRegistration": {
                List<ONDCRegistration> list = ondcRegistrationRepository.findAll();
            }
        }
        return null;
    }

    private String getFieldType(Field field) {
        Class outcomeClass = field.getType();
        if (field.getName().endsWith("Date") || field.getName().startsWith("date")) {
            return "date";
        } else if (outcomeClass.getName().equals("java.lang.String")) {
            return "text";
        } else if (outcomeClass.getName().equals("java.lang.Long") || outcomeClass.getName().equals("java.lang.Integer")) {
            return "number";
        } else if (outcomeClass.getName().equals("java.lang.Double") || outcomeClass.getName().equals("java.lang.Float")) {
            return "decimal";
        } else if (outcomeClass.getName().equals("java.lang.Character")) {
            return "character";
        } else if (field.getName().startsWith("is")) {
            return "radio button";
        }
        return "";
    }

    private String getFieldDisplayName(String fieldName) {
        String displayname = "";
        if (fieldName != null && !fieldName.isEmpty()) {
            displayname = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            displayname = displayname.replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2");
        }
        return displayname;
    }


}
