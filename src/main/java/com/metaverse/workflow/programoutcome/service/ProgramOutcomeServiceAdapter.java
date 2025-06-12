package com.metaverse.workflow.programoutcome.service;

import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.outcomes.*;
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
import java.util.Arrays;
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
    @Autowired
    ConsortiaTenderRepository consortiaTenderRepository;
    @Autowired
    OEMRepository oemRepository;
    @Autowired
    PMFMESchemeRepository pmfmeSchemeRepository;
    @Autowired
    PMViswakarmaReposiroty pmViswakarmaReposiroty;
    @Autowired
    VendorDevelopmentRepository vendorDevelopmentRepository;
    @Autowired
    ScStHubRepository scStHubRepository;
    @Autowired
    SIDBIAspireRepository sidbiAspireRepository;
    @Autowired
    GeMRegistrationRepository geMRegistrationRepository;
    @Autowired
    DesignRightsRepository designRightsRepository;
    @Autowired
    CopyRightsRepository copyRightsRepository;
    @Autowired
    GreeningOfMSMERepository greeningOfMSMERepository;

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
                case "GeMTransaction": {
                    GeMRegistration gemRegistration = geMRegistrationRepository.findByParticipantParticipantId(participantId);
                    if (gemRegistration == null )
                        return WorkflowResponse.builder()
                                .status(400)
                                .message("Gem Registration not completed")
                                .build();
                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("GeM Registration Id"))
                                    .fieldName("gemRegistrationId").fieldType("label")
                                    .fieldValue(gemRegistration.getGemRegistrationId())
                                    .build()
                    );
                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("Registered As"))
                                    .fieldName("registeredAs").fieldType("dropdown")
                                    .fieldOptions(Arrays.asList("Buyer","Seller"))
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
                case "CGTMSETransaction":
                {
                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("purpose"))
                                    .fieldName("purpose")
                                    .fieldType("dropdown")
                                    .fieldOptions(Arrays.asList(
                                            "New enterprise", "UpGradation", "Business expansion",
                                            "Term loan", "Working capital needs", "Service sector",
                                            "Revival of sick units", "trading activities"
                                    ))
                                    .build()
                    );
                    break;
                }
                case "PMS":
                {
                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("Purpose Of Loan"))
                                    .fieldName("purposeOfLoan")
                                    .fieldType("dropdown")
                                    .fieldOptions(Arrays.asList(
                                            "Cluster based development support", "Credit linked capital subsidy",
                                            "Establishment of new unit", "Technical Up Gradation",
                                            "Working capital support", "Skill development support"
                                    ))
                                    .build()
                    );
                    break;
                }
                case "Lean":
                {
                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("Certification Type"))
                                    .fieldName("certificationType")
                                    .fieldType("dropdown")
                                    .fieldOptions(Arrays.asList(
                                            "Basic", "Intermediate", "Advanced"
                                    ))
                                    .build()
                    );
                    break;
                }
                case "ConsortiaTender":
                {
                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("Consortia Member Type"))
                                    .fieldName("consortiaMemberType")
                                    .fieldType("dropdown")
                                    .fieldOptions(Arrays.asList(
                                            "Member" , "Lead member"
                                    ))
                                    .build()
                    );
                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("Tender Outcome"))
                                    .fieldName("tenderOutcome")
                                    .fieldType("dropdown")
                                    .fieldOptions(Arrays.asList(
                                            "Awarded" , "Not Awarded"
                                    ))
                                    .build()
                    );
                    break;
                }
                case "GreeningOfMSME":
                {
                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("Type Of Trainings Received"))
                                    .fieldName("typeOfTrainingReceived")
                                    .fieldType("array")
                                    .build()
                    );
                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("Purpose Of Loan Utilised"))
                                    .fieldName("purposeOfLoanUtilised")
                                    .fieldType("dropdown")
                                    .fieldOptions(Arrays.asList(
                                            "Energy Efficiency Technology", "Renewable Energy Technology",
                                            "Water Conservation and Management Technology", "Waste Management and Recycling Technology",
                                            "Cleaner Production Technology", "Pollution Control Technology",
                                            "Sustainable Packaging Technology", "Digital and Smart Technology",
                                            "Green Building Technology", "Eco-Product and Eco-Design Technology"
                                    ))
                                    .build()
                    );
                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("parameter 1"))
                                    .fieldName("parameter1")
                                    .fieldType("dropdown")
                                    .fieldOptions(Arrays.asList(
                                            "Energy consumption (Kwh/month)",
                                            "Renewable energy generated (Kwh/month)",
                                            "Fresh water saved through conservation (Kilolitres/month)",
                                            "Solid waste diverted from landfill (Tn/month)",
                                            "Raw material saved (Kgs/month)",
                                            "Reduction in air pollutant emissions (mg/Nm3)",
                                            "Reduction in plastic packaging material used (Kg/month)",
                                            "Energy use via automation (Kwh/month)",
                                            "Reduction in building energy use (Kwh/month)",
                                            "Quantity of eco-friendly products developed (units/month)"
                                    ))
                                    .build()
                    );
                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("parameter 2"))
                                    .fieldName("parameter2")
                                    .fieldType("dropdown")
                                    .fieldOptions(Arrays.asList(
                                            "Cost of energy (Rs. In lakhs / month)",
                                            "No. of solar panels installed",
                                            "Volume of recycled water reused annually (KL/month)",
                                            "Quantity of compost produced from organic waste (Tons/month)",
                                            "Hazardous waste reduction (Kgs/month)",
                                            "Volume of effluent treated annually (KL/month)",
                                            "Volume of recycled packaged material used (Kg/month)",
                                            "Downtime reduction in hours annually (Hours/month)",
                                            "Daylight hours used for operations (Hours/month)",
                                            "Reduction in raw materials used per eco-product (Grams/unit)"
                                    ))
                                    .build()
                    );
                    break;
                }
                case "PMViswakarma":
                {
                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("Purpose Of Utilisation"))
                                    .fieldName("purposeOfUtilisation")
                                    .fieldType("dropdown")
                                    .fieldOptions(Arrays.asList(
                                            "Working Capital" , "Renovation" , "Equipment"
                                    ))
                                    .build()
                    );
                    break;
                }
                case "VendorDevelopment":
                {
                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("Name Of Buyers Interested"))
                                    .fieldName("nameOfBuyersInterested")
                                    .fieldType("array")
                                    .build()
                    );
                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("Name of the Portal"))
                                    .fieldName("portalName")
                                    .fieldType("dropdown")
                                    .fieldOptions(Arrays.asList(
                                            "GEM","ONDC","OEM"
                                    ))
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
                    .message("Internal server error...").build();
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
            case "GeMRegistration": {
                GeMRegistrationRequest request = parser.parse(data, GeMRegistrationRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));
                if (geMRegistrationRepository.existsByParticipant_ParticipantId(request.getParticipantId())) {
                    return WorkflowResponse.builder()
                            .message("GeM registration already exists for the given participant.")
                            .data(geMRegistrationRepository.findByParticipantParticipantId(request.getParticipantId()))
                            .build();
                }
                geMRegistrationRepository.save(OutcomeRequestMapper.mapGeMRegistration(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "GeMTransaction": {
                GeMTransactionRequest request = parser.parse(data, GeMTransactionRequest.class);
                GeMRegistration gemRegistration = geMRegistrationRepository.findByParticipantParticipantId(request.getParticipantId());
                if(gemRegistration == null)
                    return WorkflowResponse.builder().status(400).message("Invalid Gem Registration").build();
                geMTransactionRepository.save(OutcomeRequestMapper.mapGeMTransaction(request,gemRegistration));
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
            case "ConsortiaTender": {
                ConsortiaTenderRequest request = parser.parse(data, ConsortiaTenderRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                consortiaTenderRepository.save(OutcomeRequestMapper.mapConsortiaTender(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "OEM": {
                OEMRequest request = parser.parse(data, OEMRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));
                oemRepository.save(OutcomeRequestMapper.mapOem(request, agency, participant, organization));

                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "PMFMEScheme": {
                PMFMESchemeRequest request = parser.parse(data, PMFMESchemeRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                pmfmeSchemeRepository.save(OutcomeRequestMapper.mapPmfmseScheme(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "PMViswakarma": {
                PMViswakarmaRequest request = parser.parse(data, PMViswakarmaRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                pmViswakarmaReposiroty.save(OutcomeRequestMapper.mapPMViswakarma(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "VendorDevelopment": {
                VendorDevelopmentRequest request = parser.parse(data, VendorDevelopmentRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                vendorDevelopmentRepository.save(OutcomeRequestMapper.mapVendorDevelopment(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "ScStHub": {
                ScStHubRequest request = parser.parse(data, ScStHubRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                scStHubRepository.save(OutcomeRequestMapper.mapScStHub(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "SIDBIAspire": {
                SIDBIAspireRequest request = parser.parse(data, SIDBIAspireRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                sidbiAspireRepository.save(OutcomeRequestMapper.mapSidbiAspire(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "DesignRights": {
                DesignRightsRequest request = parser.parse(data, DesignRightsRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                designRightsRepository.save(OutcomeRequestMapper.mapDesignRights(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "CopyRights": {
                CopyRightsRequest request = parser.parse(data, CopyRightsRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                copyRightsRepository.save(OutcomeRequestMapper.mapCopyRights(request, agency, participant, organization));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "GreeningOfMSME": {
                GreeningOfMSMERequest request = parser.parse(data, GreeningOfMSMERequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                        .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                greeningOfMSMERepository.save(OutcomeRequestMapper.mapGreeningOfMSME(request, agency, participant, organization));
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
