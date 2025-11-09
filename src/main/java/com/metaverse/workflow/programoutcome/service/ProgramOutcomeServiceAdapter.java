package com.metaverse.workflow.programoutcome.service;

import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.model.outcomes.*;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import com.metaverse.workflow.participant.repository.InfluencedParticipantRepository;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import com.metaverse.workflow.programoutcome.dto.*;
import com.metaverse.workflow.programoutcome.repository.*;
import com.metaverse.workflow.programoutcometargets.repository.PhysicalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProgramOutcomeServiceAdapter implements ProgramOutcomeService {


    private final ProgramOutcomeTableRepository programOutcomeTableRepository;
    private final ONDCRegistrationRepository ondcRegistrationRepository;
    private final ONDCTransactionRepository ondcTransactionRepository;
    private final UdyamRegistrationRepository udyamRegistrationRepository;
    private final CGTMSETransactionRepository cgtmseTransactionRepository;
    private final GeMTransactionRepository geMTransactionRepository;
    private final AgencyRepository agencyRepository;
    private final OrganizationRepository organizationRepository;
    private final ParticipantRepository participantRepository;
    private final TReDSRegistrationRepository tredsRegistrationRepository;
    private final TReDSTransactionRepository tredsTransactionRepository;
    private final PMEGPRepository pmegpRepository;
    private final PMMYRepository pmmyRepository;
    private final InfluencedParticipantRepository influencedParticipantRepository;
    private final PMSRepository pmsRepository;
    private final ICSchemeRepository icSchemeRepository;
    private final NSICRepository nsicRepository;
    private final PatentsRepository patentsRepository;
    private final GIProductRepository giProductRepository;
    private final BarcodeRepository barcodeRepository;
    private final TreadMarkRepository treadMarkRepository;
    private final LeanRepository leanRepository;
    private final ZEDCertificationRepository zedCertificationRepository;
    private final ConsortiaTenderRepository consortiaTenderRepository;
    private final OEMRepository oemRepository;
    private final PMFMESchemeRepository pmfmeSchemeRepository;
    private final PMViswakarmaReposiroty pmViswakarmaReposiroty;
    private final VendorDevelopmentRepository vendorDevelopmentRepository;
    private final ScStHubRepository scStHubRepository;
    private final SIDBIAspireRepository sidbiAspireRepository;
    private final GeMRegistrationRepository geMRegistrationRepository;
    private final DesignRightsRepository designRightsRepository;
    private final CopyRightsRepository copyRightsRepository;
    private final GreeningOfMSMERepository greeningOfMSMERepository;
    private final PhysicalRepository physicalRepository;
    private final ECommerceRegistrationRepository eCommerceRegistrationRepository;
    private final ECommerceTransactionRepository eCommerceTransactionRepository;
    private final LoanRepository loanRepository;
    private final ImportSubsititutionRepository importSubsititutionRepository;
    private final ExportPromotionRepository exportPromotionRepository;
    private final SkillUpgradationRepository skillUpgradationRepository;

    @Override
    public List<ProgramOutcomeTable> getProgramOutcomeTables() {
        return programOutcomeTableRepository.findAll();
    }

    @Override
    public WorkflowResponse getOutcomeDetails(Long participantId, String outcome, String type, Boolean isInfluenced) {
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
                    List<ONDCRegistration> ondcRegistration;
                    if (isInfluenced) {
                        ondcRegistration = ondcRegistrationRepository.findByInfluencedParticipant_InfluencedId(participantId);
                    } else {
                        ondcRegistration = ondcRegistrationRepository.findByParticipantId(participantId);
                    }
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
                    GeMRegistration gemRegistration;
                    if (isInfluenced) {
                        gemRegistration = geMRegistrationRepository.findByInfluencedParticipant_InfluencedId(participantId);
                    } else {
                        gemRegistration = geMRegistrationRepository.findByParticipantParticipantId(participantId);
                    }
                    geMRegistrationRepository.findByParticipantParticipantId(participantId);
                    if (gemRegistration == null)
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
                                    .fieldOptions(Arrays.asList("Buyer", "Seller"))
                                    .build()
                    );
                    break;
                }
                case "TReDSTransaction": {
                    List<TReDSRegistration> tReDSRegistrations;
                    if (isInfluenced) {
                        tReDSRegistrations = tredsRegistrationRepository.findByInfluencedParticipant_InfluencedId(participantId);
                    } else {
                        tReDSRegistrations = tredsRegistrationRepository.findByParticipantId(participantId);
                    }

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
                    List<ZEDCertification> zedCertifications;
                    if (isInfluenced) {
                        zedCertifications = zedCertificationRepository.findByInfluencedParticipant_InfluencedId(participantId);
                    } else {
                        zedCertifications = zedCertificationRepository.findByParticipantId(participantId);
                    }

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
                                    .fieldValue(currentType == null ? "Bronze" : type)
                                    .build()
                    );

                    break;
                }
                case "CGTMSETransaction": {
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
                case "PMS": {
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

                case "Lean": {
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
                case "SkillUpgradation": {
                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("Type Of Trainings Received"))
                                    .fieldName("typeOfTrainingReceived")
                                    .fieldType("array")
                                    .build()
                    );
                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("Type Of Loan"))
                                    .fieldName("loanType")
                                    .fieldType("dropdown")
                                    .fieldOptions(Arrays.asList(
                                            "Term Loan", "Working Capital Loan"
                                    ))
                                    .build()
                    );
                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("Purpose of Loan utilized"))
                                    .fieldName("loanPurpose")
                                    .fieldType("dropdown")
                                    .fieldOptions(Arrays.asList(
                                            "Product Diversivation", "Upgrading of Machinery"
                                    ))
                                    .build()
                    );
                    break;
                }
                case "ConsortiaTender": {
                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("Consortia Member Type"))
                                    .fieldName("consortiaMemberType")
                                    .fieldType("dropdown")
                                    .fieldOptions(Arrays.asList(
                                            "Member", "Lead member"
                                    ))
                                    .build()
                    );
                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("Tender Outcome"))
                                    .fieldName("tenderOutcome")
                                    .fieldType("dropdown")
                                    .fieldOptions(Arrays.asList(
                                            "Awarded", "Not Awarded"
                                    ))
                                    .build()
                    );
                    break;
                }
                case "Barcode": {

                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("Type of Market"))
                                    .fieldName("typeOfMarket")
                                    .fieldType("dropdown")
                                    .fieldOptions(Arrays.asList(
                                            "Online", "Export", "Local"
                                    ))
                                    .build()
                    );
                    break;
                }
                case "GreeningOfMSME": {
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
                case "PMViswakarma": {
                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("Purpose Of Utilisation"))
                                    .fieldName("purposeOfUtilisation")
                                    .fieldType("dropdown")
                                    .fieldOptions(Arrays.asList(
                                            "Working Capital", "Renovation", "Equipment"
                                    ))
                                    .build()
                    );
                    break;
                }
                case "VendorDevelopment": {
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
                                            "GEM", "ONDC", "OEM"
                                    ))
                                    .build()
                    );
                    break;
                }
                case "NSIC": {
                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("Procurement Type"))
                                    .fieldName("typeOfProcurement")
                                    .fieldType("dropdown")
                                    .fieldOptions(Arrays.asList(
                                            "GEM", "ONDC", "OEM"
                                    ))
                                    .build()
                    );
                    break;
                }
                case "PMMY": {
                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("Category"))
                                    .fieldName("category")
                                    .fieldType("dropdown")
                                    .fieldOptions(Arrays.asList(
                                            "Sishu", "Kishor", "Tarun"
                                    ))
                                    .build()
                    );
                    break;
                }
                case "ECommerceTransaction": {
                    ECommerceRegistration eCommerceRegistration;
                    if (isInfluenced) {
                        eCommerceRegistration = eCommerceRegistrationRepository.findByInfluencedParticipant_InfluencedId(participantId);
                    } else {
                        eCommerceRegistration = eCommerceRegistrationRepository.findByParticipant_ParticipantId(participantId);
                    }

                    if (eCommerceRegistration == null)
                        return WorkflowResponse.builder()
                                .status(400)
                                .message("eCommerce Registration not completed")
                                .build();

                    columnList.add(
                            OutcomeDetails.OutcomeDataSet.builder()
                                    .fieldDisplayName(getFieldDisplayName("eCommerce Registration No"))
                                    .fieldName("registrationDetails").fieldType("label")
                                    .fieldValue(eCommerceRegistration.getRegistrationDetails())
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


                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));
                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));

                    if (ondcRegistrationRepository.existsByInfluencedParticipant_InfluencedId(request.getInfluencedId())) {
                        return WorkflowResponse.builder()
                                .status(400)
                                .message("ONDC registration already exists for the given influenced participant.")
                                .build();
                    }

                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));

                    if (ondcRegistrationRepository.existsByParticipant_ParticipantId(request.getParticipantId())) {
                        return WorkflowResponse.builder()
                                .status(400)
                                .message("ONDC registration already exists for the given participant.")
                                .build();
                    }
                }

                ondcRegistrationRepository.save(OutcomeRequestMapper.mapOndcRegistration(request, agency, participant, organization, influencedParticipant));
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

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));

                    if (udyamRegistrationRepository.existsByInfluencedParticipant_InfluencedId(request.getInfluencedId())) {
                        return WorkflowResponse.builder()
                                .status(400)
                                .message("Udyam registration already exists for the given influenced participant.")
                                .build();
                    }

                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));

                    if (udyamRegistrationRepository.existsByParticipant_ParticipantId(request.getParticipantId())) {
                        return WorkflowResponse.builder()
                                .status(400)
                                .message("Udyam registration already exists for the given participant.")
                                .build();
                    }
                }

                udyamRegistrationRepository.save(
                        OutcomeRequestMapper.mapUdyamRegistration(request, agency, participant, organization, influencedParticipant)
                );

                status = outcomeName + " Saved Successfully.";
                break;
            }

            case "CGTMSETransaction": {
                CGTMSETransactionRequest request = parser.parse(data, CGTMSETransactionRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                // Check whether registration is for InfluencedParticipant or normal Participant
                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }
                cgtmseTransactionRepository.save(OutcomeRequestMapper.mapCGTMSETransaction(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "GeMRegistration": {
                GeMRegistrationRequest request = parser.parse(data, GeMRegistrationRequest.class);

                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));

                    if (geMRegistrationRepository.existsByInfluencedParticipant_InfluencedId(request.getInfluencedId())) {
                        return WorkflowResponse.builder()
                                .status(400)
                                .message("GeM registration already exists for the given influenced participant.")
                                .build();
                    }

                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));

                    if (geMRegistrationRepository.existsByParticipant_ParticipantId(request.getParticipantId())) {
                        return WorkflowResponse.builder()
                                .status(400)
                                .message("GeM registration already exists for the given participant.")
                                .build();
                    }
                }

                geMRegistrationRepository.save(
                        OutcomeRequestMapper.mapGeMRegistration(request, agency, participant, organization, influencedParticipant)
                );

                status = outcomeName + " Saved Successfully.";
                break;
            }

            case "GeMTransaction": {
                GeMTransactionRequest request = parser.parse(data, GeMTransactionRequest.class);
                GeMRegistration gemRegistration = geMRegistrationRepository.findByParticipantParticipantId(request.getParticipantId());
                if (gemRegistration == null)
                    return WorkflowResponse.builder().status(400).message("Invalid Gem Registration").build();
                geMTransactionRepository.save(OutcomeRequestMapper.mapGeMTransaction(request, gemRegistration));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "TReDSRegistration": {
                TReDSRegistrationRequest request = parser.parse(data, TReDSRegistrationRequest.class);

                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));

                    if (tredsRegistrationRepository.existsByInfluencedParticipant_InfluencedId(request.getInfluencedId())) {
                        return WorkflowResponse.builder()
                                .status(400)
                                .message("TReDS registration already exists for the given influenced participant.")
                                .build();
                    }

                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));

                    if (tredsRegistrationRepository.existsByParticipant_ParticipantId(request.getParticipantId())) {
                        return WorkflowResponse.builder()
                                .status(400)
                                .message("TReDS registration already exists for the given participant.")
                                .build();
                    }
                }

                tredsRegistrationRepository.save(
                        OutcomeRequestMapper.mapTredsRegistration(request, agency, participant, organization, influencedParticipant)
                );

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

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                // Check whether registration is for InfluencedParticipant or normal Participant
                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }

                pmegpRepository.save(OutcomeRequestMapper.mapPmegp(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "PMMY": {
                PMMYRequest request = parser.parse(data, PMMYRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));


                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                // Check whether registration is for InfluencedParticipant or normal Participant
                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }
                pmmyRepository.save(OutcomeRequestMapper.mapPmmy(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "PMS": {
                PMSRequest request = parser.parse(data, PMSRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                // Check whether registration is for InfluencedParticipant or normal Participant
                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }
                pmsRepository.save(OutcomeRequestMapper.mapPms(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "ICScheme": {
                ICSchemeRequest request = parser.parse(data, ICSchemeRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                // Check whether registration is for InfluencedParticipant or normal Participant
                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }
                icSchemeRepository.save(OutcomeRequestMapper.mapIcScheme(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "NSIC": {
                NSICRequest request = parser.parse(data, NSICRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                // Check whether registration is for InfluencedParticipant or normal Participant
                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }
                nsicRepository.save(OutcomeRequestMapper.mapNsic(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "Patents": {
                PatentsRequest request = parser.parse(data, PatentsRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));
                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                // Check whether registration is for InfluencedParticipant or normal Participant
                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }
                patentsRepository.save(OutcomeRequestMapper.mapPatents(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "GIProduct": {
                GIProductRequest request = parser.parse(data, GIProductRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                // Check whether registration is for InfluencedParticipant or normal Participant
                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }
                giProductRepository.save(OutcomeRequestMapper.mapGIProduct(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "Barcode": {
                BarcodeRequest request = parser.parse(data, BarcodeRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                // Check whether registration is for InfluencedParticipant or normal Participant
                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }
                barcodeRepository.save(OutcomeRequestMapper.mapBarcode(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "TreadMark": {
                TreadMarkRequest request = parser.parse(data, TreadMarkRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));
                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                // Check whether registration is for InfluencedParticipant or normal Participant
                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }
                treadMarkRepository.save(OutcomeRequestMapper.mapTreadMark(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "Lean": {
                LeanRequest request = parser.parse(data, LeanRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                // Check whether registration is for InfluencedParticipant or normal Participant
                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }
                leanRepository.save(OutcomeRequestMapper.mapLean(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "ZEDCertification": {
                ZEDCertificationRequest request = parser.parse(data, ZEDCertificationRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                // Check whether registration is for InfluencedParticipant or normal Participant
                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }

                zedCertificationRepository.save(OutcomeRequestMapper.mapZEDCertification(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "ConsortiaTender": {
                ConsortiaTenderRequest request = parser.parse(data, ConsortiaTenderRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                // Check whether registration is for InfluencedParticipant or normal Participant
                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }
                consortiaTenderRepository.save(OutcomeRequestMapper.mapConsortiaTender(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "OEM": {
                OEMRequest request = parser.parse(data, OEMRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                // Check whether registration is for InfluencedParticipant or normal Participant
                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }
                oemRepository.save(OutcomeRequestMapper.mapOem(request, agency, participant, organization, influencedParticipant));

                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "PMFMEScheme": {
                PMFMESchemeRequest request = parser.parse(data, PMFMESchemeRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                // Check whether registration is for InfluencedParticipant or normal Participant
                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }
                pmfmeSchemeRepository.save(OutcomeRequestMapper.mapPmfmseScheme(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "PMViswakarma": {
                PMViswakarmaRequest request = parser.parse(data, PMViswakarmaRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                // Check whether registration is for InfluencedParticipant or normal Participant
                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }

                pmViswakarmaReposiroty.save(OutcomeRequestMapper.mapPMViswakarma(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "ImportSubsititution": {
                ImportSubsititutionRequest request = parser.parse(data, ImportSubsititutionRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }

                importSubsititutionRepository.save(
                        OutcomeRequestMapper.mapImportSubsititution(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "SkillUpgradation": {
                SkillUpgradationRequest request = parser.parse(data, SkillUpgradationRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }

                skillUpgradationRepository.save(
                        OutcomeRequestMapper.mapESDPTraining(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "ExportPromotion": {
                ExportPromotionRequest request = parser.parse(data, ExportPromotionRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }

                exportPromotionRepository.save(
                        OutcomeRequestMapper.mapExportPromotion(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }


            case "VendorDevelopment": {
                VendorDevelopmentRequest request = parser.parse(data, VendorDevelopmentRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                // Check whether registration is for InfluencedParticipant or normal Participant
                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }

                vendorDevelopmentRepository.save(OutcomeRequestMapper.mapVendorDevelopment(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "ScStHub": {
                ScStHubRequest request = parser.parse(data, ScStHubRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                // Check whether registration is for InfluencedParticipant or normal Participant
                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }
                scStHubRepository.save(OutcomeRequestMapper.mapScStHub(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "SIDBIAspire": {
                SIDBIAspireRequest request = parser.parse(data, SIDBIAspireRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                // Check whether registration is for InfluencedParticipant or normal Participant
                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }
                sidbiAspireRepository.save(OutcomeRequestMapper.mapSidbiAspire(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "DesignRights": {
                DesignRightsRequest request = parser.parse(data, DesignRightsRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                // Check whether registration is for InfluencedParticipant or normal Participant
                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }
                designRightsRepository.save(OutcomeRequestMapper.mapDesignRights(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "CopyRights": {
                CopyRightsRequest request = parser.parse(data, CopyRightsRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                // Check whether registration is for InfluencedParticipant or normal Participant
                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }
                copyRightsRepository.save(OutcomeRequestMapper.mapCopyRights(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "GreeningOfMSME": {
                GreeningOfMSMERequest request = parser.parse(data, GreeningOfMSMERequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                // Check whether registration is for InfluencedParticipant or normal Participant
                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }
                greeningOfMSMERepository.save(OutcomeRequestMapper.mapGreeningOfMSME(request, agency, participant, organization, influencedParticipant));
                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "ECommerceRegistration": {
                ECommerceRegistrationRequest request = parser.parse(data, ECommerceRegistrationRequest.class);

                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));

                    if (eCommerceRegistrationRepository.existsByInfluencedParticipant_InfluencedId(request.getInfluencedId())) {
                        return WorkflowResponse.builder()
                                .status(400)
                                .message("E-Commerce registration already exists for the given influenced participant.")
                                .build();
                    }

                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));

                    if (eCommerceRegistrationRepository.existsByParticipant_ParticipantId(request.getParticipantId())) {
                        return WorkflowResponse.builder()
                                .status(400)
                                .message("E-Commerce registration already exists for the given participant.")
                                .build();
                    }
                }

                eCommerceRegistrationRepository.save(
                        OutcomeRequestMapper.mapECommerceRegistration(request, agency, participant, organization, influencedParticipant)
                );

                status = outcomeName + " Saved Successfully.";
                break;
            }

            case "ECommerceTransaction": {
                ECommerceTransactionRequest eCommerceTransactionRequest = parser.parse(data, ECommerceTransactionRequest.class);

                ECommerceRegistration eCommerceRegistration =
                        eCommerceRegistrationRepository.findByParticipant_ParticipantId(eCommerceTransactionRequest.getParticipantId());

                if (eCommerceRegistration == null) {
                    return WorkflowResponse.builder()
                            .status(400)
                            .message("Invalid E-Commerce Registration")
                            .build();
                }

                eCommerceTransactionRepository.save(
                        OutcomeRequestMapper.mapECommerceTransaction(eCommerceTransactionRequest, eCommerceRegistration)
                );

                status = outcomeName + " Saved Successfully.";
                break;
            }
            case "Loan": {
                LoanRequest request = parser.parse(data, LoanRequest.class);
                Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                        .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

                Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                        .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

                Participant participant = null;
                InfluencedParticipant influencedParticipant = null;

                // Check whether registration is for InfluencedParticipant or normal Participant
                if (Boolean.TRUE.equals(request.getIsInfluenced())) {
                    influencedParticipant = influencedParticipantRepository.findById(
                                    request.getInfluencedId() == null ? 0 : request.getInfluencedId())
                            .orElseThrow(() -> new DataException("Influenced Participant data not found",
                                    "INFLUENCED-PARTICIPANT-DATA-NOT-FOUND", 400));
                } else {
                    participant = participantRepository.findById(
                                    request.getParticipantId() == null ? 0 : request.getParticipantId())
                            .orElseThrow(() -> new DataException("Participant data not found",
                                    "PARTICIPANT-DATA-NOT-FOUND", 400));
                }
                loanRepository.save(OutcomeRequestMapper.mapLoan(request, agency, participant, organization, influencedParticipant));
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

    public WorkflowResponse getApiForOutcomes(Long agencyId, Long outcomeId) {
        List<Long> integers = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L);
        List<Object> lists = new ArrayList<>();
        Boolean b = false;

        if (agencyId == -1) {
            for (Long i : integers) {
                Object apiForOutcomes1 = getApiForOutcomes1(i, outcomeId);
                if (outcomeId == null || outcomeId == -1) {
                    List<?> list = (List<?>) apiForOutcomes1;
                    if (!list.isEmpty())
                        lists.add(list);
                } else {
                    lists.add(apiForOutcomes1);
                }
            }
        } else {
            lists.add(getApiForOutcomes1(agencyId, outcomeId));
        }

        if (agencyId.equals(-1L) && outcomeId.equals(-1L)) {
            List<OutComesTargetsAndAchievementDto> flatList = lists.stream()
                    .flatMap(o -> ((List<OutComesTargetsAndAchievementDto>) o).stream())
                    .toList();

            Map<String, List<OutComesTargetsAndAchievementDto>> grouped =
                    flatList.stream().collect(Collectors.groupingBy(OutComesTargetsAndAchievementDto::getOutComeName));

            return WorkflowResponse.builder()
                    .data(grouped).status(200)
                    .message("Fetched Successfully").build();
        }

        return WorkflowResponse.builder()
                .data(lists).status(200)
                .message("Fetched Successfully").build();
    }

    public <T> T getApiForOutcomes1(Long agencyId, Long outcomeId) {

        List<ProgramOutcomeTable> allOutcomeEntities = programOutcomeTableRepository.findAll();
        List<Agency> allAgencies = agencyRepository.findAll();
        List<OutComesTargetsAndAchievementDto> outComesTargetsAndAchiDtos = new ArrayList<>();

        List<Agency> agenciesToProcess = (agencyId == -1) ? allAgencies
                : allAgencies.stream().filter(a -> a.getAgencyId().equals(agencyId)).toList();

        for (Agency agency : agenciesToProcess) {
            for (ProgramOutcomeTable outcome : allOutcomeEntities) {

                // Skip if specific outcomeId is passed
                if (outcomeId != -1 && outcomeId != null && !outcome.getOutcomeTableId().equals(outcomeId.intValue())) {
                    continue;
                }

                List<PhysicalTarget> physicalTarget = physicalRepository
                        .findByAgencyAgencyIdAndProgramOutcomeTableOutcomeTableId(
                                agency.getAgencyId(), outcome.getOutcomeTableId());
                double targetTotal = physicalTarget.stream().mapToDouble(physicalTarget3 -> physicalTarget3.getQ1() + physicalTarget3.getQ2() + physicalTarget3.getQ3() + physicalTarget3.getQ4()).sum();

                long influencerAchievement = 0L;
                long participantAchievement = 0L;

                switch (outcome.getOutcomeTableName()) {
                    case "ONDCRegistration":
                        List<ONDCRegistration> ondc = ondcRegistrationRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> ondcCount = ondc.stream().filter(b -> b.getIsInfluenced() != null).collect(Collectors.groupingBy(ONDCRegistration::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = ondcCount.getOrDefault(true, 0L);
                        participantAchievement = ondcCount.getOrDefault(false, 0L);
                        break;
                    case "ONDCTransaction":
                        List<ONDCTransaction> ondcTransactions = ondcTransactionRepository.findByOndcRegistration_Agency_AgencyId(agency.getAgencyId());

                        Map<Boolean, Long> ondcTransactionCount = ondcTransactions.stream()
                                .collect(Collectors.groupingBy(
                                        t -> {
                                            ONDCRegistration reg = t.getOndcRegistration();
                                            return reg != null && Boolean.TRUE.equals(reg.getIsInfluenced());
                                        },
                                        Collectors.counting()
                                ));
                        influencerAchievement = ondcTransactionCount.getOrDefault(true, 0L);
                        participantAchievement = ondcTransactionCount.getOrDefault(false, 0L);
                        break;

                    case "UdyamRegistration":
                        List<UdyamRegistration> udyam = udyamRegistrationRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> udyamCount = udyam.stream().filter(b -> b.getIsInfluenced() != null).collect(Collectors.groupingBy(UdyamRegistration::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = udyamCount.getOrDefault(true, 0L);
                        participantAchievement = udyamCount.getOrDefault(false, 0L);
                        break;

                    case "TReDSRegistration":
                        List<TReDSRegistration> treds = tredsRegistrationRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> tredsCount = treds.stream().filter(b -> b.getIsInfluenced() != null).collect(Collectors.groupingBy(TReDSRegistration::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = tredsCount.getOrDefault(true, 0L);
                        participantAchievement = tredsCount.getOrDefault(false, 0L);
                        break;

                    case "TReDSTransaction":
                        List<TReDSTransaction> tReDSTransactions = tredsTransactionRepository.findByTredsRegistration_Agency_AgencyId(agency.getAgencyId());

                        Map<Boolean, Long> tReDSTransactionsCount = tReDSTransactions.stream()
                                .collect(Collectors.groupingBy(
                                        t -> {
                                            TReDSRegistration reg = t.getTredsRegistration();
                                            return reg != null && Boolean.TRUE.equals(reg.getIsInfluenced());
                                        },
                                        Collectors.counting()
                                ));
                        influencerAchievement = tReDSTransactionsCount.getOrDefault(true, 0L);
                        participantAchievement = tReDSTransactionsCount.getOrDefault(false, 0L);
                        break;

                    case "PMEGP":
                        List<PMEGP> pmegps = pmegpRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> pmegpsCount = pmegps.stream().filter(b -> b.getIsInfluenced() != null).collect(Collectors.groupingBy(PMEGP::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = pmegpsCount.getOrDefault(true, 0L);
                        participantAchievement = pmegpsCount.getOrDefault(false, 0L);
                        break;

                    case "PMMY":
                        List<PMMY> pmmies = pmmyRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> pmmiesCount = pmmies.stream().filter(b -> b.getIsInfluenced() != null).collect(Collectors.groupingBy(PMMY::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = pmmiesCount.getOrDefault(true, 0L);
                        participantAchievement = pmmiesCount.getOrDefault(false, 0L);
                        break;

                    case "PMS":
                        List<PMS> pmsList = pmsRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> pmsCount = pmsList.stream().filter(b -> b.getIsInfluenced() != null).collect(Collectors.groupingBy(PMS::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = pmsCount.getOrDefault(true, 0L);
                        participantAchievement = pmsCount.getOrDefault(false, 0L);
                        break;

                    case "ICScheme":
                        List<ICScheme> icSchemes = icSchemeRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> icCount = icSchemes.stream().filter(b -> b.getIsInfluenced() != null).collect(Collectors.groupingBy(ICScheme::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = icCount.getOrDefault(true, 0L);
                        participantAchievement = icCount.getOrDefault(false, 0L);
                        break;

                    case "NSIC":
                        List<NSIC> nsics = nsicRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> nsicCount = nsics.stream().filter(b -> b.getIsInfluenced() != null).collect(Collectors.groupingBy(NSIC::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = nsicCount.getOrDefault(true, 0L);
                        participantAchievement = nsicCount.getOrDefault(false, 0L);
                        break;

                    case "Patents":
                        List<Patents> patents = patentsRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> patentsCount = patents.stream().filter(b -> b.getIsInfluenced() != null).collect(Collectors.groupingBy(Patents::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = patentsCount.getOrDefault(true, 0L);
                        participantAchievement = patentsCount.getOrDefault(false, 0L);
                        break;

                    case "GIProduct":
                        List<GIProduct> giProducts = giProductRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> giCount = giProducts.stream().filter(b -> b.getIsInfluenced() != null).collect(Collectors.groupingBy(GIProduct::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = giCount.getOrDefault(true, 0L);
                        participantAchievement = giCount.getOrDefault(false, 0L);
                        break;

                    case "Barcode":
                        List<Barcode> barcodes = barcodeRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> barcodeCount = barcodes.stream().filter(b -> b.getIsInfluenced() != null).filter(b -> b.getIsInfluenced() != null).collect(Collectors.groupingBy(Barcode::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = barcodeCount.getOrDefault(true, 0L);
                        participantAchievement = barcodeCount.getOrDefault(false, 0L);
                        break;

                    case "CGTMSETransaction":
                        List<CGTMSETransaction> cgtmseTransactions = cgtmseTransactionRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> cgtmseTransactionsCount = cgtmseTransactions.stream().filter(b -> b.getIsInfluenced() != null).collect(Collectors.groupingBy(CGTMSETransaction::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = cgtmseTransactionsCount.getOrDefault(true, 0L);
                        participantAchievement = cgtmseTransactionsCount.getOrDefault(false, 0L);
                        break;

                    case "TreadMark":
                        List<TreadMark> treadMarks = treadMarkRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> treadCount = treadMarks.stream().filter(b -> b.getIsInfluenced() != null).collect(Collectors.groupingBy(TreadMark::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = treadCount.getOrDefault(true, 0L);
                        participantAchievement = treadCount.getOrDefault(false, 0L);
                        break;

                    case "Lean":
                        List<Lean> leans = leanRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> leanCount = leans.stream().filter(b -> b.getIsInfluenced() != null).collect(Collectors.groupingBy(Lean::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = leanCount.getOrDefault(true, 0L);
                        participantAchievement = leanCount.getOrDefault(false, 0L);
                        break;

                    case "PMViswakarma":
                        List<PMViswakarma> pmViswakarmas = pmViswakarmaReposiroty.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> pmVisCount = pmViswakarmas.stream().filter(b -> b.getIsInfluenced() != null).collect(Collectors.groupingBy(PMViswakarma::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = pmVisCount.getOrDefault(true, 0L);
                        participantAchievement = pmVisCount.getOrDefault(false, 0L);
                        break;

                    case "GeMRegistration":
                        List<GeMRegistration> geMRegs = geMRegistrationRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> geMCount = geMRegs.stream().filter(b -> b.getIsInfluenced() != null).collect(Collectors.groupingBy(GeMRegistration::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = geMCount.getOrDefault(true, 0L);
                        participantAchievement = geMCount.getOrDefault(false, 0L);
                        break;
                    case "GeMTransaction":
                        List<GeMTransaction> geMTransactions = geMTransactionRepository.findByGemRegistration_Agency_AgencyId(agency.getAgencyId());
                        Map<Boolean, Long> geMTransactionsCount = geMTransactions.stream()
                                .collect(Collectors.groupingBy(
                                        t -> {
                                            GeMRegistration reg = t.getGemRegistration();
                                            return reg != null && Boolean.TRUE.equals(reg.getIsInfluenced());
                                        },
                                        Collectors.counting()
                                ));
                        influencerAchievement = geMTransactionsCount.getOrDefault(true, 0L);
                        participantAchievement = geMTransactionsCount.getOrDefault(false, 0L);
                        break;

                    case "OEM":
                        List<OEM> oems = oemRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> oemCount = oems.stream().filter(b -> b.getIsInfluenced() != null).collect(Collectors.groupingBy(OEM::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = oemCount.getOrDefault(true, 0L);
                        participantAchievement = oemCount.getOrDefault(false, 0L);
                        break;

                    case "PMFMEScheme":
                        List<PMFMEScheme> pmfmes = pmfmeSchemeRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> pmfmeCount = pmfmes.stream().filter(b -> b.getIsInfluenced() != null).collect(Collectors.groupingBy(PMFMEScheme::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = pmfmeCount.getOrDefault(true, 0L);
                        participantAchievement = pmfmeCount.getOrDefault(false, 0L);
                        break;

                    case "ConsortiaTender":
                        List<ConsortiaTender> consortias = consortiaTenderRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> consortiaCount = consortias.stream().filter(b -> b.getIsInfluenced() != null).collect(Collectors.groupingBy(ConsortiaTender::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = consortiaCount.getOrDefault(true, 0L);
                        participantAchievement = consortiaCount.getOrDefault(false, 0L);
                        break;

                    case "DesignRight":
                        List<DesignRights> designRights = designRightsRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> designCount = designRights.stream().filter(b -> b.getIsInfluenced() != null).collect(Collectors.groupingBy(DesignRights::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = designCount.getOrDefault(true, 0L);
                        participantAchievement = designCount.getOrDefault(false, 0L);
                        break;

                    case "CopyRight":
                        List<CopyRights> copyRights = copyRightsRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> copyCount = copyRights.stream().filter(b -> b.getIsInfluenced() != null).collect(Collectors.groupingBy(CopyRights::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = copyCount.getOrDefault(true, 0L);
                        participantAchievement = copyCount.getOrDefault(false, 0L);
                        break;

                    case "Loan":
                        List<Loan> loans = loanRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> loanCount = loans.stream().filter(b -> b.getIsInfluenced() != null).collect(Collectors.groupingBy(Loan::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = loanCount.getOrDefault(true, 0L);
                        participantAchievement = loanCount.getOrDefault(false, 0L);
                        break;
                    case "ZEDCertification":
                        switch (outcome.getOutcomeTableDisplayName()) {
                            case "ZED Certification Bronze" -> {
                                List<ZEDCertification> bronzeCerts =
                                        zedCertificationRepository.findByAgencyAgencyIdAndZedCertificationType(agency.getAgencyId(), "Bronze");
                                Map<Boolean, Long> bronzeCount = bronzeCerts.stream().filter(c -> c.getIsInfluenced() != null)
                                        .collect(Collectors.groupingBy(ZEDCertification::getIsInfluenced, Collectors.counting()));
                                influencerAchievement = bronzeCount.getOrDefault(true, 0L);
                                participantAchievement = bronzeCount.getOrDefault(false, 0L);
                            }

                            case "ZED Certification Silver" -> {
                                List<ZEDCertification> silverCerts =
                                        zedCertificationRepository.findByAgencyAgencyIdAndZedCertificationType(agency.getAgencyId(), "Silver");
                                Map<Boolean, Long> silverCount = silverCerts.stream().filter(c -> c.getIsInfluenced() != null)
                                        .collect(Collectors.groupingBy(ZEDCertification::getIsInfluenced, Collectors.counting()));
                                influencerAchievement = silverCount.getOrDefault(true, 0L);
                                participantAchievement = silverCount.getOrDefault(false, 0L);
                            }

                            case "ZED Certification Gold" -> {
                                List<ZEDCertification> goldCerts =
                                        zedCertificationRepository.findByAgencyAgencyIdAndZedCertificationType(agency.getAgencyId(), "Gold");
                                Map<Boolean, Long> goldCount = goldCerts.stream().filter(c -> c.getIsInfluenced() != null)
                                        .collect(Collectors.groupingBy(ZEDCertification::getIsInfluenced, Collectors.counting()));
                                influencerAchievement = goldCount.getOrDefault(true, 0L);
                                participantAchievement = goldCount.getOrDefault(false, 0L);
                            }

                            default -> {
                                influencerAchievement = 0L;
                                participantAchievement = 0L;
                            }
                        }

                        break;
                    case "ECommerceRegistration": {
                        List<ECommerceRegistration> eCommRegs = eCommerceRegistrationRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> eCommCount = eCommRegs.stream()
                                .filter(r -> r.getIsInfluenced() != null)
                                .collect(Collectors.groupingBy(ECommerceRegistration::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = eCommCount.getOrDefault(true, 0L);
                        participantAchievement = eCommCount.getOrDefault(false, 0L);
                        break;
                    }

                    case "ECommerceTransaction": {
                        List<ECommerceTransaction> eCommTransactions = eCommerceTransactionRepository.findByEcommerceRegistration_Agency_AgencyId(agency.getAgencyId());
                        Map<Boolean, Long> eCommTransactionsCount = eCommTransactions.stream()
                                .collect(Collectors.groupingBy(
                                        t -> {
                                            ECommerceRegistration reg = t.getEcommerceRegistration();
                                            return reg != null && Boolean.TRUE.equals(reg.getIsInfluenced());
                                        },
                                        Collectors.counting()
                                ));
                        influencerAchievement = eCommTransactionsCount.getOrDefault(true, 0L);
                        participantAchievement = eCommTransactionsCount.getOrDefault(false, 0L);
                        break;
                    }

                    case "ExportPromotion": {
                        List<ExportPromotion> exportPromotions = exportPromotionRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> exportPromotionCount = exportPromotions.stream()
                                .filter(r -> r.getIsInfluenced() != null)
                                .collect(Collectors.groupingBy(ExportPromotion::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = exportPromotionCount.getOrDefault(true, 0L);
                        participantAchievement = exportPromotionCount.getOrDefault(false, 0L);
                        break;
                    }

                    case "SkillUpgradation": {
                        List<SkillUpgradation> skillUpgradations = skillUpgradationRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> skillCount = skillUpgradations.stream()
                                .filter(r -> r.getIsInfluenced() != null)
                                .collect(Collectors.groupingBy(SkillUpgradation::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = skillCount.getOrDefault(true, 0L);
                        participantAchievement = skillCount.getOrDefault(false, 0L);
                        break;
                    }

                    case "ImportSubsititution": {
                        List<ImportSubsititution> importSubs = importSubsititutionRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> importSubsCount = importSubs.stream()
                                .filter(r -> r.getIsInfluenced() != null)
                                .collect(Collectors.groupingBy(ImportSubsititution::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = importSubsCount.getOrDefault(true, 0L);
                        participantAchievement = importSubsCount.getOrDefault(false, 0L);
                        break;
                    }
                    case "VendorDevelopment": {
                        List<VendorDevelopment> vendorDevelopments = vendorDevelopmentRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> vendorCount = vendorDevelopments.stream()
                                .filter(v -> v.getIsInfluenced() != null)
                                .collect(Collectors.groupingBy(VendorDevelopment::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = vendorCount.getOrDefault(true, 0L);
                        participantAchievement = vendorCount.getOrDefault(false, 0L);
                        break;
                    }

                    case "ScStHub": {
                        List<ScStHub> scstHubs = scStHubRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> scstCount = scstHubs.stream()
                                .filter(s -> s.getIsInfluenced() != null)
                                .collect(Collectors.groupingBy(ScStHub::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = scstCount.getOrDefault(true, 0L);
                        participantAchievement = scstCount.getOrDefault(false, 0L);
                        break;
                    }

                    case "SIDBIAspire": {
                        List<SIDBIAspire> sidbiAspires = sidbiAspireRepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> sidbiCount = sidbiAspires.stream()
                                .filter(s -> s.getIsInfluenced() != null)
                                .collect(Collectors.groupingBy(SIDBIAspire::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = sidbiCount.getOrDefault(true, 0L);
                        participantAchievement = sidbiCount.getOrDefault(false, 0L);
                        break;
                    }

                    case "GreeningOfMSME": {
                        List<GreeningOfMSME> greeningList = greeningOfMSMERepository.findByAgencyAgencyId(agency.getAgencyId());
                        Map<Boolean, Long> greenCount = greeningList.stream()
                                .filter(g -> g.getIsInfluenced() != null)
                                .collect(Collectors.groupingBy(GreeningOfMSME::getIsInfluenced, Collectors.counting()));
                        influencerAchievement = greenCount.getOrDefault(true, 0L);
                        participantAchievement = greenCount.getOrDefault(false, 0L);
                        break;
                    }



                    default:
                        influencerAchievement = 0L;
                        participantAchievement = 0L;
                }

                outComesTargetsAndAchiDtos.add(OutComesTargetsAndAchievementDto.builder()
                        .agencyName(agency.getAgencyName())
                        .outComeName(outcome.getOutcomeTableDisplayName())
                        .outComeTarget((long) targetTotal)
                        .outComeParticipantAchievement(participantAchievement)
                        .outComeInfluencerAchievement(influencerAchievement)
                        .build());
            }
        }

        // agencyId=-1 and outcomeId=-1
        if (agencyId == -1 && (outcomeId == -1 || outcomeId == null)) {
            Map<String, List<OutComesTargetsAndAchievementDto>> map = new LinkedHashMap<>();
            for (ProgramOutcomeTable outcome : allOutcomeEntities) {
                List<OutComesTargetsAndAchievementDto> list = outComesTargetsAndAchiDtos.stream()
                        .filter(d -> d.getOutComeName().equals(outcome.getOutcomeTableDisplayName()))
                        .toList();
                map.put(outcome.getOutcomeTableDisplayName(), list);
            }
            return (T) map;
        }
        return (T) outComesTargetsAndAchiDtos;
    }


    public WorkflowResponse getOutcomeData(String outcomeName, Long agencyId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        OutcomeResponseDTO responseDTO;
        Page<?> resultPage = Page.empty();

        switch (outcomeName) {

            case "ONDCRegistration": {
                Page<ONDCRegistration> ondcPage =
                        ondcRegistrationRepository.findByAgency_AgencyId(agencyId, pageable);

                List<ONDCRegistrationDTO> body = ondcPage.getContent().stream().map(reg -> ONDCRegistrationDTO.builder()
                        .id(reg.getOndcRegistrationId())
                        .ondcRegistrationNo(reg.getOndcRegistrationNo())
                        .ondcRegistrationDate(reg.getOndcRegistrationDate())
                        .isInfluenced(reg.getIsInfluenced())
                        .agencyName(reg.getAgency() != null ? reg.getAgency().getAgencyName() : null)
                        .participantName(reg.getParticipant() != null ? reg.getParticipant().getParticipantName() : null)
                        .organizationName(reg.getOrganization() != null ? reg.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(reg.getInfluencedParticipant() != null
                                ? reg.getInfluencedParticipant().getParticipantName() : null)
                        .mobileNo(reg.getParticipant() != null ? reg.getParticipant().getMobileNo() : null)
                        .createdOn(reg.getCreatedOn())
                        .updatedOn(reg.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("ondcRegistrationNo", "ONDC Registration No"),
                        new OutcomeDetails.OutcomeDataSet("ondcRegistrationDate", "ONDC Registration Date"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("mobileNo", "Mobile Number"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder().headers(headers).body(body).build();
                resultPage = ondcPage;
                break;
            }

            case "ONDCTransaction": {
                Page<ONDCTransaction> pageData =
                        ondcTransactionRepository.findByOndcRegistration_Agency_AgencyId(agencyId, pageable);

                List<ONDCTransactionDTO> body = pageData.getContent().stream().map(tx -> ONDCTransactionDTO.builder()
                        .id(tx.getOndcTransactionId())
                        .ondcRegistrationNo(tx.getOndcRegistration() != null ? tx.getOndcRegistration().getOndcRegistrationNo() : null)
                        .productName(tx.getProductName())
                        .transactionDate(tx.getTransactionDate())
                        .productQuantity(tx.getProductQuantity())
                        .productUnits(tx.getProductUnits())
                        .transactionType(tx.getTransactionType())
                        .transactionValue(tx.getTransactionValue())
                        .createdOn(tx.getCreatedOn())
                        .updatedOn(tx.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("ondcRegistrationNo", "ONDC Registration No"),
                        new OutcomeDetails.OutcomeDataSet("productName", "Product Name"),
                        new OutcomeDetails.OutcomeDataSet("transactionDate", "Transaction Date"),
                        new OutcomeDetails.OutcomeDataSet("productQuantity", "Quantity"),
                        new OutcomeDetails.OutcomeDataSet("productUnits", "Units"),
                        new OutcomeDetails.OutcomeDataSet("transactionType", "Transaction Type"),
                        new OutcomeDetails.OutcomeDataSet("transactionValue", "Value (₹)"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder().headers(headers).body(body).build();
                resultPage = pageData;
                break;
            }

            case "UdyamRegistration": {
                Page<UdyamRegistration> pageData =
                        udyamRegistrationRepository.findByAgency_AgencyId(agencyId, pageable);

                List<UdyamRegistrationDTO> body = pageData.getContent().stream().map(reg -> UdyamRegistrationDTO.builder()
                        .id(reg.getUdyamRegistrationId())
                        .udyamRegistrationNo(reg.getUdyamRegistrationNo())
                        .udyamRegistrationDate(reg.getUdyamRegistationDate())
                        .isInfluenced(reg.getIsInfluenced())
                        .agencyName(reg.getAgency() != null ? reg.getAgency().getAgencyName() : null)
                        .participantName(reg.getParticipant() != null ? reg.getParticipant().getParticipantName() : null)
                        .organizationName(reg.getOrganization() != null ? reg.getOrganization().getOrganizationName() : null)
                        .createdOn(reg.getCreatedOn())
                        .updatedOn(reg.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("udyamRegistrationNo", "Udyam Registration No"),
                        new OutcomeDetails.OutcomeDataSet("udyamRegistrationDate", "Registration Date"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder().headers(headers).body(body).build();
                resultPage = pageData;
                break;
            }

            case "CGTMSETransaction": {
                Page<CGTMSETransaction> pageData =
                        cgtmseTransactionRepository.findByAgency_AgencyId(agencyId, pageable);

                List<CGTMSETransactionDTO> body = pageData.getContent().stream().map(t -> CGTMSETransactionDTO.builder()
                        .id(t.getCgtmseTransactionId())
                        .productName(t.getProductName())
                        .purpose(t.getPurpose())
                        .valueReleased(t.getValueReleased())
                        .approvalDate(t.getApprovalDate())
                        .employmentMale(t.getEmploymentMale())
                        .employmentFemale(t.getEmploymentFemale())
                        .agencyName(t.getAgency() != null ? t.getAgency().getAgencyName() : null)
                        .participantName(t.getParticipant() != null ? t.getParticipant().getParticipantName() : null)
                        .organizationName(t.getOrganization() != null ? t.getOrganization().getOrganizationName() : null)
                        .createdOn(t.getCreatedOn())
                        .updatedOn(t.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("productName", "Product Name"),
                        new OutcomeDetails.OutcomeDataSet("purpose", "Purpose"),
                        new OutcomeDetails.OutcomeDataSet("valueReleased", "Value Released (Lakhs)"),
                        new OutcomeDetails.OutcomeDataSet("approvalDate", "Approval Date"),
                        new OutcomeDetails.OutcomeDataSet("employmentMale", "Employment (Male)"),
                        new OutcomeDetails.OutcomeDataSet("employmentFemale", "Employment (Female)"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder().headers(headers).body(body).build();
                resultPage = pageData;
                break;
            }

            case "PMEGP": {
                Page<PMEGP> pageData =
                        pmegpRepository.findByAgency_AgencyId(agencyId, pageable);

                List<PMEGPDTO> body = pageData.getContent().stream().map(p -> PMEGPDTO.builder()
                        .id(p.getPmegpId())
                        .loanAmountReleased(p.getLoanAmountReleased())
                        .govtSubsidy(p.getGovtSubsidy())
                        .beneficiaryContribution(p.getBeneficiaryContribution())
                        .totalAmountReleased(p.getTotalAmountReleased())
                        .businessTurnover(p.getBusinessTurnover())
                        .numberOfPersonsEmployed(p.getNumberOfPersonsEmployed())
                        .agencyName(p.getAgency() != null ? p.getAgency().getAgencyName() : null)
                        .participantName(p.getParticipant() != null ? p.getParticipant().getParticipantName() : null)
                        .organizationName(p.getOrganization() != null ? p.getOrganization().getOrganizationName() : null)
                        .createdOn(p.getCreatedOn())
                        .updatedOn(p.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("loanAmountReleased", "Loan Amount Released (Lakhs)"),
                        new OutcomeDetails.OutcomeDataSet("govtSubsidy", "Govt Subsidy (Lakhs)"),
                        new OutcomeDetails.OutcomeDataSet("beneficiaryContribution", "Beneficiary Contribution (Lakhs)"),
                        new OutcomeDetails.OutcomeDataSet("totalAmountReleased", "Total Amount (Lakhs)"),
                        new OutcomeDetails.OutcomeDataSet("businessTurnover", "Business Turnover"),
                        new OutcomeDetails.OutcomeDataSet("numberOfPersonsEmployed", "Persons Employed"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder().headers(headers).body(body).build();
                resultPage = pageData;
                break;
            }

            case "GIProduct": {
                Page<GIProduct> pageData =
                        giProductRepository.findByAgency_AgencyId(agencyId, pageable);

                List<GIProductDTO> body = pageData.getContent().stream().map(g -> GIProductDTO.builder()
                        .id(g.getGiProductId())
                        .companyName(g.getCompanyName())
                        .location(g.getLocation())
                        .industry(g.getIndustry())
                        .giProductName(g.getGiProductName())
                        .giRegistrationNumber(g.getGiRegistrationNumber())
                        .dateOfGIRegistration(g.getDateOfGIRegistration())
                        .jurisdictionCovered(g.getJurisdictionCovered())
                        .revenueAfterGICertification(g.getRevenueAfterGICertification())
                        .dateOfExport(g.getDateOfExport())
                        .valueOfExport(g.getValueOfExport())
                        .countryExported(g.getCountryExported())
                        .retailPartnership(g.getRetailPartnership())
                        .valueOfSupply(g.getValueOfSupply())
                        .dateOfSupply(g.getDateOfSupply())
                        .totalJobsCreated(g.getTotalJobsCreated())
                        .franchiseOutletsOpened(g.getFranchiseOutletsOpened())
                        .annualRoyaltyEarnings(g.getAnnualRoyaltyEarnings())
                        .isInfluenced(g.getIsInfluenced())
                        .agencyName(g.getAgency() != null ? g.getAgency().getAgencyName() : null)
                        .participantName(g.getParticipant() != null ? g.getParticipant().getParticipantName() : null)
                        .organizationName(g.getOrganization() != null ? g.getOrganization().getOrganizationName() : null)
                        .createdOn(g.getCreatedOn())
                        .updatedOn(g.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("companyName", "Company Name"),
                        new OutcomeDetails.OutcomeDataSet("location", "Location"),
                        new OutcomeDetails.OutcomeDataSet("industry", "Industry"),
                        new OutcomeDetails.OutcomeDataSet("giProductName", "GI Product Name"),
                        new OutcomeDetails.OutcomeDataSet("giRegistrationNumber", "GI Registration Number"),
                        new OutcomeDetails.OutcomeDataSet("dateOfGIRegistration", "Date of GI Registration"),
                        new OutcomeDetails.OutcomeDataSet("jurisdictionCovered", "Jurisdiction Covered"),
                        new OutcomeDetails.OutcomeDataSet("revenueAfterGICertification", "Revenue After GI Certification"),
                        new OutcomeDetails.OutcomeDataSet("dateOfExport", "Date of Export"),
                        new OutcomeDetails.OutcomeDataSet("valueOfExport", "Value of Export"),
                        new OutcomeDetails.OutcomeDataSet("countryExported", "Country Exported"),
                        new OutcomeDetails.OutcomeDataSet("retailPartnership", "Retail Partnership"),
                        new OutcomeDetails.OutcomeDataSet("valueOfSupply", "Value of Supply"),
                        new OutcomeDetails.OutcomeDataSet("dateOfSupply", "Date of Supply"),
                        new OutcomeDetails.OutcomeDataSet("totalJobsCreated", "Jobs Created"),
                        new OutcomeDetails.OutcomeDataSet("franchiseOutletsOpened", "Franchise Outlets Opened"),
                        new OutcomeDetails.OutcomeDataSet("annualRoyaltyEarnings", "Annual Royalty Earnings"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );



                responseDTO = OutcomeResponseDTO.builder().headers(headers).body(body).build();
                resultPage = pageData;
                break;
            }

            case "Barcode": {
                Page<Barcode> pageData =
                        barcodeRepository.findByAgency_AgencyId(agencyId, pageable);

                List<BarcodeDTO> body = pageData.getContent().stream().map(b -> BarcodeDTO.builder()
                        .id(b.getBarcodeId())
                        .typeOfMarket(b.getTypeOfMarket())
                        .barCodeType(b.getBarCodeType())
                        .gs1RegistrationNumber(b.getGs1RegistrationNumber())
                        .barCodeCoverage(b.getBarCodeCoverage())
                        .revenueFromBarCodeIntegration(b.getRevenueFromBarCodeIntegration())
                        .onlineMarketRegistered(b.getOnlineMarketRegistered())
                        .dateOfRegistration(b.getDateOfRegistration())
                        .valueOfTransaction(b.getValueOfTransaction())
                        .dateOfExport(b.getDateOfExport())
                        .valueOfExport(b.getValueOfExport())
                        .countryExported(b.getCountryExported())
                        .isInfluenced(b.getIsInfluenced())
                        .agencyName(b.getAgency() != null ? b.getAgency().getAgencyName() : null)
                        .participantName(b.getParticipant() != null ? b.getParticipant().getParticipantName() : null)
                        .organizationName(b.getOrganization() != null ? b.getOrganization().getOrganizationName() : null)
                        .createdOn(b.getCreatedOn())
                        .updatedOn(b.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("typeOfMarket", "Type of Market"),
                        new OutcomeDetails.OutcomeDataSet("barCodeType", "Bar Code Type"),
                        new OutcomeDetails.OutcomeDataSet("gs1RegistrationNumber", "GS1 Registration Number"),
                        new OutcomeDetails.OutcomeDataSet("barCodeCoverage", "Bar Code Coverage"),
                        new OutcomeDetails.OutcomeDataSet("revenueFromBarCodeIntegration", "Revenue from Bar Code Integration"),
                        new OutcomeDetails.OutcomeDataSet("onlineMarketRegistered", "Online Market Registered"),
                        new OutcomeDetails.OutcomeDataSet("dateOfRegistration", "Date of Registration"),
                        new OutcomeDetails.OutcomeDataSet("valueOfTransaction", "Value of Transaction"),
                        new OutcomeDetails.OutcomeDataSet("dateOfExport", "Date of Export"),
                        new OutcomeDetails.OutcomeDataSet("valueOfExport", "Value of Export"),
                        new OutcomeDetails.OutcomeDataSet("countryExported", "Country Exported"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder().headers(headers).body(body).build();
                resultPage = pageData;
                break;
            }

            case "GeMTransaction": {
                Page<GeMTransaction> pageData =
                        geMTransactionRepository.findByGemRegistration_Agency_AgencyId(agencyId, pageable);

                List<GeMTransactionDTO> body = pageData.getContent().stream().map(tx -> GeMTransactionDTO.builder()
                        .id(tx.getGemTransactionId())
                        .gemRegistrationId(tx.getGemRegistration() != null ? tx.getGemRegistration().getGemRegistrationId() : null)
                        .procurementDate(tx.getProcurementDate())
                        .productName(tx.getProductName())
                        .unitOfMeasurement(tx.getUnitOfMeasurement())
                        .registeredAs(tx.getRegisteredAs())
                        .quantity(tx.getQuantity())
                        .productValue(tx.getProductValue())
                        .agencyName(tx.getGemRegistration() != null && tx.getGemRegistration().getAgency() != null
                                ? tx.getGemRegistration().getAgency().getAgencyName() : null)
                        .participantName(tx.getGemRegistration() != null && tx.getGemRegistration().getParticipant() != null
                                ? tx.getGemRegistration().getParticipant().getParticipantName() : null)
                        .organizationName(tx.getGemRegistration() != null && tx.getGemRegistration().getOrganization() != null
                                ? tx.getGemRegistration().getOrganization().getOrganizationName() : null)
                        .createdOn(tx.getCreatedOn())
                        .updatedOn(tx.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("gemRegistrationId", "GeM Registration ID"),
                        new OutcomeDetails.OutcomeDataSet("procurementDate", "Procurement Date"),
                        new OutcomeDetails.OutcomeDataSet("productName", "Product Name"),
                        new OutcomeDetails.OutcomeDataSet("unitOfMeasurement", "Unit of Measurement"),
                        new OutcomeDetails.OutcomeDataSet("registeredAs", "Registered As"),
                        new OutcomeDetails.OutcomeDataSet("quantity", "Quantity"),
                        new OutcomeDetails.OutcomeDataSet("productValue", "Product Value"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }

            case "TReDSRegistration": {
                Page<TReDSRegistration> pageData =
                        tredsRegistrationRepository.findByAgency_AgencyId(agencyId, pageable);

                List<TReDSRegistrationDTO> body = pageData.getContent().stream().map(r -> TReDSRegistrationDTO.builder()
                        .id(r.getTredsRegistrationId())
                        .tredsRegistrationNo(r.getTredsRegistrationNo())
                        .tredsRegistrationDate(r.getTredsRegistrationDate())
                        .isInfluenced(r.getIsInfluenced())
                        .agencyName(r.getAgency() != null ? r.getAgency().getAgencyName() : null)
                        .participantName(r.getParticipant() != null ? r.getParticipant().getParticipantName() : null)
                        .organizationName(r.getOrganization() != null ? r.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(r.getInfluencedParticipant() != null
                                ? r.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(r.getCreatedOn())
                        .updatedOn(r.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("tredsRegistrationNo", "TReDS Registration No"),
                        new OutcomeDetails.OutcomeDataSet("tredsRegistrationDate", "TReDS Registration Date"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }

            case "TReDSTransaction": {
                Page<TReDSTransaction> pageData =
                        tredsTransactionRepository.findByTredsRegistration_Agency_AgencyId(agencyId, pageable);

                List<TReDSTransactionDTO> body = pageData.getContent().stream().map(t -> TReDSTransactionDTO.builder()
                        .id(t.getTredsTransactionId())
                        .tredsRegistrationNo(t.getTredsRegistration() != null ? t.getTredsRegistration().getTredsRegistrationNo() : null)
                        .tredsTransactionDate(t.getTredsTransactionDate())
                        .invoiceNumber(t.getInvoiceNumber())
                        .buyerName(t.getBuyerName())
                        .tredsPlatformUsed(t.getTredsPlatformUsed())
                        .invoiceAmount(t.getInvoiceAmount())
                        .bidOpeningDate(t.getBidOpeningDate())
                        .winnerFinancier(t.getWinnerFinancier())
                        .discountRateOffered(t.getDiscountRateOffered())
                        .discountingFeeFor60Days(t.getDiscountingFeeFor60Days())
                        .finalPayoutToMsme(t.getFinalPayoutToMsme())
                        .paymentSettlementDate(t.getPaymentSettlementDate())
                        .buyerDueDateToPay(t.getBuyerDueDateToPay())
                        .repaymentDate(t.getRepaymentDate())
                        .agencyName(t.getTredsRegistration() != null && t.getTredsRegistration().getAgency() != null
                                ? t.getTredsRegistration().getAgency().getAgencyName() : null)
                        .participantName(t.getTredsRegistration() != null && t.getTredsRegistration().getParticipant() != null
                                ? t.getTredsRegistration().getParticipant().getParticipantName() : null)
                        .organizationName(t.getTredsRegistration() != null && t.getTredsRegistration().getOrganization() != null
                                ? t.getTredsRegistration().getOrganization().getOrganizationName() : null)
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("tredsRegistrationNo", "TReDS Registration No"),
                        new OutcomeDetails.OutcomeDataSet("tredsTransactionDate", "TReDS Transaction Date"),
                        new OutcomeDetails.OutcomeDataSet("invoiceNumber", "Invoice Number"),
                        new OutcomeDetails.OutcomeDataSet("buyerName", "Buyer Name"),
                        new OutcomeDetails.OutcomeDataSet("tredsPlatformUsed", "TReDS Platform Used"),
                        new OutcomeDetails.OutcomeDataSet("invoiceAmount", "Invoice Amount"),
                        new OutcomeDetails.OutcomeDataSet("bidOpeningDate", "Bid Opening Date"),
                        new OutcomeDetails.OutcomeDataSet("winnerFinancier", "Winner Financier"),
                        new OutcomeDetails.OutcomeDataSet("discountRateOffered", "Discount Rate Offered"),
                        new OutcomeDetails.OutcomeDataSet("discountingFeeFor60Days", "Discounting Fee (60 Days)"),
                        new OutcomeDetails.OutcomeDataSet("finalPayoutToMsme", "Final Payout to MSME"),
                        new OutcomeDetails.OutcomeDataSet("paymentSettlementDate", "Payment Settlement Date"),
                        new OutcomeDetails.OutcomeDataSet("buyerDueDateToPay", "Buyer Due Date to Pay"),
                        new OutcomeDetails.OutcomeDataSet("repaymentDate", "Repayment Date"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }

            case "PMMY": {
                Page<PMMY> pageData = pmmyRepository.findByAgency_AgencyId(agencyId, pageable);

                List<PMMYDTO> body = pageData.getContent().stream().map(p -> PMMYDTO.builder()
                        .id(p.getPmmyId())
                        .category(p.getCategory())
                        .loanAmountReleased(p.getLoanAmountReleased())
                        .loanSanctionedDate(p.getLoanSanctionedDate())
                        .groundingDate(p.getGroundingDate())
                        .businessTurnover(p.getBusinessTurnover())
                        .marketLinkageDate(p.getMarketLinkageDate())
                        .marketVolume(p.getMarketVolume())
                        .units(p.getUnits())
                        .marketValue(p.getMarketValue())
                        .productMarketedName(p.getProductMarketedName())
                        .isInfluenced(p.getIsInfluenced())
                        .agencyName(p.getAgency() != null ? p.getAgency().getAgencyName() : null)
                        .participantName(p.getParticipant() != null ? p.getParticipant().getParticipantName() : null)
                        .organizationName(p.getOrganization() != null ? p.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(p.getInfluencedParticipant() != null
                                ? p.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(p.getCreatedOn())
                        .updatedOn(p.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("category", "Category"),
                        new OutcomeDetails.OutcomeDataSet("loanAmountReleased", "Loan Amount Released (₹)"),
                        new OutcomeDetails.OutcomeDataSet("loanSanctionedDate", "Loan Sanctioned Date"),
                        new OutcomeDetails.OutcomeDataSet("groundingDate", "Grounding Date"),
                        new OutcomeDetails.OutcomeDataSet("businessTurnover", "Business Turnover"),
                        new OutcomeDetails.OutcomeDataSet("marketLinkageDate", "Market Linkage Date"),
                        new OutcomeDetails.OutcomeDataSet("marketVolume", "Market Volume (MT)"),
                        new OutcomeDetails.OutcomeDataSet("units", "Units"),
                        new OutcomeDetails.OutcomeDataSet("marketValue", "Market Value (₹)"),
                        new OutcomeDetails.OutcomeDataSet("productMarketedName", "Product Marketed Name"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }

            case "PMS": {
                Page<PMS> pageData = pmsRepository.findByAgency_AgencyId(agencyId, pageable);

                List<PMSDTO> body = pageData.getContent().stream().map(p -> PMSDTO.builder()
                        .id(p.getPmsId())
                        .businessTurnover(p.getBusinessTurnover())
                        .loanNumber(p.getLoanNumber())
                        .purposeOfLoan(p.getPurposeOfLoan())
                        .amountOfLoanReleased(p.getAmountOfLoanReleased())
                        .dateOfLoanReleased(p.getDateOfLoanReleased())
                        .employmentCreatedDirect(p.getEmploymentCreatedDirect())
                        .employmentCreatedInDirect(p.getEmploymentCreatedInDirect())
                        .repaymentAmount(p.getRepaymentAmount())
                        .dateOfRepayment(p.getDateOfRepayment())
                        .isUpiOrQrAvailable(p.getIsUpiOrQrAvailable())
                        .onlinePlatformUsed(p.getOnlinePlatformUsed())
                        .dateOfGrounding(p.getDateOfGrounding())
                        .revenue(p.getRevenue())
                        .isInfluenced(p.getIsInfluenced())
                        .agencyName(p.getAgency() != null ? p.getAgency().getAgencyName() : null)
                        .participantName(p.getParticipant() != null ? p.getParticipant().getParticipantName() : null)
                        .organizationName(p.getOrganization() != null ? p.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(p.getInfluencedParticipant() != null
                                ? p.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(p.getCreatedOn())
                        .updatedOn(p.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("businessTurnover", "Business Turnover"),
                        new OutcomeDetails.OutcomeDataSet("loanNumber", "Loan Number"),
                        new OutcomeDetails.OutcomeDataSet("purposeOfLoan", "Purpose of Loan"),
                        new OutcomeDetails.OutcomeDataSet("amountOfLoanReleased", "Loan Amount Released"),
                        new OutcomeDetails.OutcomeDataSet("dateOfLoanReleased", "Loan Release Date"),
                        new OutcomeDetails.OutcomeDataSet("employmentCreatedDirect", "Employment Created (Direct)"),
                        new OutcomeDetails.OutcomeDataSet("employmentCreatedInDirect", "Employment Created (Indirect)"),
                        new OutcomeDetails.OutcomeDataSet("repaymentAmount", "Repayment Amount"),
                        new OutcomeDetails.OutcomeDataSet("dateOfRepayment", "Repayment Date"),
                        new OutcomeDetails.OutcomeDataSet("isUpiOrQrAvailable", "UPI/QR Available"),
                        new OutcomeDetails.OutcomeDataSet("onlinePlatformUsed", "Online Platform Used"),
                        new OutcomeDetails.OutcomeDataSet("dateOfGrounding", "Date of Grounding"),
                        new OutcomeDetails.OutcomeDataSet("revenue", "Revenue"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }

            case "ICScheme": {
                Page<ICScheme> pageData = icSchemeRepository.findByAgency_AgencyId(agencyId, pageable);

                List<ICSchemeDTO> body = pageData.getContent().stream().map(i -> ICSchemeDTO.builder()
                        .id(i.getIcSchemeId())
                        .industryName(i.getIndustryName())
                        .location(i.getLocation())
                        .typeOfMsme(i.getTypeOfMsme())
                        .annualTurnover(i.getAnnualTurnover())
                        .domesticSales(i.getDomesticSales())
                        .investment(i.getInvestment())
                        .isInfluenced(i.getIsInfluenced())
                        .agencyName(i.getAgency() != null ? i.getAgency().getAgencyName() : null)
                        .participantName(i.getParticipant() != null ? i.getParticipant().getParticipantName() : null)
                        .organizationName(i.getOrganization() != null ? i.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(i.getInfluencedParticipant() != null
                                ? i.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(i.getCreatedOn())
                        .updatedOn(i.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("industryName", "Industry Name"),
                        new OutcomeDetails.OutcomeDataSet("location", "Location"),
                        new OutcomeDetails.OutcomeDataSet("typeOfMsme", "Type of MSME"),
                        new OutcomeDetails.OutcomeDataSet("annualTurnover", "Annual Turnover (₹)"),
                        new OutcomeDetails.OutcomeDataSet("domesticSales", "Domestic Sales (₹)"),
                        new OutcomeDetails.OutcomeDataSet("investment", "Investment (₹)"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }

            case "NSIC": {
                Page<NSIC> pageData = nsicRepository.findByAgency_AgencyId(agencyId, pageable);

                List<NSICDTO> body = pageData.getContent().stream().map(n -> NSICDTO.builder()
                        .id(n.getNsicId())
                        .govtAgencyProcured(n.getGovtAgencyProcured())
                        .dateOfProcurement(n.getDateOfProcurement())
                        .typeOfProcurement(n.getTypeOfProcurement())
                        .typeOfProductSupplied(n.getTypeOfProductSupplied())
                        .valueOfProcurement(n.getValueOfProcurement())
                        .costSavingsTender(n.getCostSavingsTender())
                        .isInfluenced(n.getIsInfluenced())
                        .agencyName(n.getAgency() != null ? n.getAgency().getAgencyName() : null)
                        .participantName(n.getParticipant() != null ? n.getParticipant().getParticipantName() : null)
                        .organizationName(n.getOrganization() != null ? n.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(n.getInfluencedParticipant() != null
                                ? n.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(n.getCreatedOn())
                        .updatedOn(n.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("govtAgencyProcured", "Government Agency Procured"),
                        new OutcomeDetails.OutcomeDataSet("dateOfProcurement", "Date of Procurement"),
                        new OutcomeDetails.OutcomeDataSet("typeOfProcurement", "Type of Procurement"),
                        new OutcomeDetails.OutcomeDataSet("typeOfProductSupplied", "Type of Product Supplied"),
                        new OutcomeDetails.OutcomeDataSet("valueOfProcurement", "Value of Procurement (₹)"),
                        new OutcomeDetails.OutcomeDataSet("costSavingsTender", "Cost Savings in Tender (₹)"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }

            case "Patents": {
                Page<Patents> pageData = patentsRepository.findByAgency_AgencyId(agencyId, pageable);

                List<PatentsDTO> body = pageData.getContent().stream().map(p -> PatentsDTO.builder()
                        .id(p.getPatentId())
                        .nameOfPatent(p.getNameOfPatent())
                        .typeOfPatent(p.getTypeOfPatent())
                        .patentNumber(p.getPatentNumber())
                        .patentIssueDate(p.getPatentIssueDate())
                        .patentCoverage(p.getPatentCoverage())
                        .annualRevenue(p.getAnnualRevenue())
                        .dateOfExport(p.getDateOfExport())
                        .valueOfExport(p.getValueOfExport())
                        .countryOfExport(p.getCountryOfExport())
                        .totalJobsCreated(p.getTotalJobsCreated())
                        .nameOfAward(p.getNameOfAward())
                        .dateOfAward(p.getDateOfAward())
                        .isInfluenced(p.getIsInfluenced())
                        .agencyName(p.getAgency() != null ? p.getAgency().getAgencyName() : null)
                        .participantName(p.getParticipant() != null ? p.getParticipant().getParticipantName() : null)
                        .organizationName(p.getOrganization() != null ? p.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(p.getInfluencedParticipant() != null
                                ? p.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(p.getCreatedOn())
                        .updatedOn(p.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("nameOfPatent", "Name of Patent"),
                        new OutcomeDetails.OutcomeDataSet("typeOfPatent", "Type of Patent"),
                        new OutcomeDetails.OutcomeDataSet("patentNumber", "Patent Number"),
                        new OutcomeDetails.OutcomeDataSet("patentIssueDate", "Patent Issue Date"),
                        new OutcomeDetails.OutcomeDataSet("patentCoverage", "Patent Coverage"),
                        new OutcomeDetails.OutcomeDataSet("annualRevenue", "Annual Revenue (₹)"),
                        new OutcomeDetails.OutcomeDataSet("dateOfExport", "Date of Export"),
                        new OutcomeDetails.OutcomeDataSet("valueOfExport", "Value of Export (₹)"),
                        new OutcomeDetails.OutcomeDataSet("countryOfExport", "Country of Export"),
                        new OutcomeDetails.OutcomeDataSet("totalJobsCreated", "Jobs Created"),
                        new OutcomeDetails.OutcomeDataSet("nameOfAward", "Award Name"),
                        new OutcomeDetails.OutcomeDataSet("dateOfAward", "Date of Award"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }
            case "TreadMark": {
                Page<TreadMark> pageData = treadMarkRepository.findByAgency_AgencyId(agencyId, pageable);

                List<TreadMarkDTO> body = pageData.getContent().stream().map(t -> TreadMarkDTO.builder()
                        .id(t.getTreadMarkId())
                        .nameOfTradMark(t.getNameOfTradMark())
                        .trademarkClass(t.getTrademarkClass())
                        .tradeMarkRegistrationNo(t.getTradeMarkRegistrationNo())
                        .dateOfRegistration(t.getDateOfRegistration())
                        .jurisdictionCovered(t.getJurisdictionCovered())
                        .annualRevenueAfterRegistration(t.getAnnualRevenueAfterRegistration())
                        .dateOfExport(t.getDateOfExport())
                        .valueOfExport(t.getValueOfExport())
                        .countryOfExport(t.getCountryOfExport())
                        .retailPartnership(t.getRetailPartnership())
                        .valueOfSupply(t.getValueOfSupply())
                        .dateOfSupply(t.getDateOfSupply())
                        .totalJobsCreated(t.getTotalJobsCreated())
                        .noOfFranchiseOutletsOpened(t.getNoOfFranchiseOutletsOpened())
                        .annualRoyaltyEarningsFromFranchise(t.getAnnualRoyaltyEarningsFromFranchise())
                        .isInfluenced(t.getIsInfluenced())
                        .agencyName(t.getAgency() != null ? t.getAgency().getAgencyName() : null)
                        .participantName(t.getParticipant() != null ? t.getParticipant().getParticipantName() : null)
                        .organizationName(t.getOrganization() != null ? t.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(t.getInfluencedParticipant() != null
                                ? t.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(t.getCreatedOn())
                        .updatedOn(t.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("nameOfTradMark", "Name of Trademark"),
                        new OutcomeDetails.OutcomeDataSet("trademarkClass", "Trademark Class"),
                        new OutcomeDetails.OutcomeDataSet("tradeMarkRegistrationNo", "Trademark Registration No"),
                        new OutcomeDetails.OutcomeDataSet("dateOfRegistration", "Date of Registration"),
                        new OutcomeDetails.OutcomeDataSet("jurisdictionCovered", "Jurisdiction Covered"),
                        new OutcomeDetails.OutcomeDataSet("annualRevenueAfterRegistration", "Annual Revenue After Registration (₹)"),
                        new OutcomeDetails.OutcomeDataSet("dateOfExport", "Date of Export"),
                        new OutcomeDetails.OutcomeDataSet("valueOfExport", "Value of Export (₹)"),
                        new OutcomeDetails.OutcomeDataSet("countryOfExport", "Country of Export"),
                        new OutcomeDetails.OutcomeDataSet("retailPartnership", "Retail Partnership"),
                        new OutcomeDetails.OutcomeDataSet("valueOfSupply", "Value of Supply (₹)"),
                        new OutcomeDetails.OutcomeDataSet("dateOfSupply", "Date of Supply"),
                        new OutcomeDetails.OutcomeDataSet("totalJobsCreated", "Jobs Created"),
                        new OutcomeDetails.OutcomeDataSet("noOfFranchiseOutletsOpened", "Franchise Outlets Opened"),
                        new OutcomeDetails.OutcomeDataSet("annualRoyaltyEarningsFromFranchise", "Annual Royalty Earnings (₹)"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }

            case "Lean": {
                Page<Lean> pageData = leanRepository.findByAgency_AgencyId(agencyId, pageable);

                List<LeanDTO> body = pageData.getContent().stream().map(l -> LeanDTO.builder()
                        .id(l.getLeanId())
                        .certificationType(l.getCertificationType())
                        .dateOfCertification(l.getDateOfCertification())
                        .isLeanConsultantAppointed(l.getIsLeanConsultantAppointed())
                        .dateOfAppointed(l.getDateOfAppointed())
                        .rawMaterialWastage(l.getRawMaterialWastage())
                        .rawMaterialWastageUnits(l.getRawMaterialWastageUnits())
                        .productionOutput(l.getProductionOutput())
                        .productionOutputUnits(l.getProductionOutputUnits())
                        .powerUsage(l.getPowerUsage())
                        .powerUsageUnits(l.getPowerUsageUnits())
                        .isInfluenced(l.getIsInfluenced())
                        .agencyName(l.getAgency() != null ? l.getAgency().getAgencyName() : null)
                        .participantName(l.getParticipant() != null ? l.getParticipant().getParticipantName() : null)
                        .organizationName(l.getOrganization() != null ? l.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(l.getInfluencedParticipant() != null
                                ? l.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(l.getCreatedOn())
                        .updatedOn(l.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("certificationType", "Certification Type"),
                        new OutcomeDetails.OutcomeDataSet("dateOfCertification", "Date of Certification"),
                        new OutcomeDetails.OutcomeDataSet("isLeanConsultantAppointed", "Lean Consultant Appointed"),
                        new OutcomeDetails.OutcomeDataSet("dateOfAppointed", "Date of Consultant Appointment"),
                        new OutcomeDetails.OutcomeDataSet("rawMaterialWastage", "Raw Material Wastage"),
                        new OutcomeDetails.OutcomeDataSet("rawMaterialWastageUnits", "Wastage Units"),
                        new OutcomeDetails.OutcomeDataSet("productionOutput", "Production Output"),
                        new OutcomeDetails.OutcomeDataSet("productionOutputUnits", "Output Units"),
                        new OutcomeDetails.OutcomeDataSet("powerUsage", "Power Usage"),
                        new OutcomeDetails.OutcomeDataSet("powerUsageUnits", "Power Usage Units"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }
            case "PMViswakarma": {
                Page<PMViswakarma> pageData = pmViswakarmaReposiroty.findByAgency_AgencyId(agencyId, pageable);

                List<PMViswakarmaDTO> body = pageData.getContent().stream().map(p -> PMViswakarmaDTO.builder()
                        .id(p.getPmViswakarmaId())
                        .artisanCategory(p.getArtisanCategory())
                        .dateOfTraining(p.getDateOfTraining())
                        .certificateIssueDate(p.getCertificateIssueDate())
                        .dateOfCreditAvailed(p.getDateOfCreditAvailed())
                        .amountOfCreditAvailed(p.getAmountOfCreditAvailed())
                        .purposeOfUtilisation(p.getPurposeOfUtilisation())
                        .monthlyIncomeAfterCredit(p.getMonthlyIncomeAfterCredit())
                        .isInfluenced(p.getIsInfluenced())
                        .agencyName(p.getAgency() != null ? p.getAgency().getAgencyName() : null)
                        .participantName(p.getParticipant() != null ? p.getParticipant().getParticipantName() : null)
                        .organizationName(p.getOrganization() != null ? p.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(p.getInfluencedParticipant() != null
                                ? p.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(p.getCreatedOn())
                        .updatedOn(p.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("artisanCategory", "Artisan Category"),
                        new OutcomeDetails.OutcomeDataSet("dateOfTraining", "Date of Training"),
                        new OutcomeDetails.OutcomeDataSet("certificateIssueDate", "Certificate Issue Date"),
                        new OutcomeDetails.OutcomeDataSet("dateOfCreditAvailed", "Date of Credit Availed"),
                        new OutcomeDetails.OutcomeDataSet("amountOfCreditAvailed", "Amount of Credit Availed (₹)"),
                        new OutcomeDetails.OutcomeDataSet("purposeOfUtilisation", "Purpose of Utilisation"),
                        new OutcomeDetails.OutcomeDataSet("monthlyIncomeAfterCredit", "Monthly Income After Credit (₹)"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }

            case "VendorDevelopment": {
                Page<VendorDevelopment> pageData = vendorDevelopmentRepository.findByAgency_AgencyId(agencyId, pageable);

                List<VendorDevelopmentDTO> body = pageData.getContent().stream().map(v -> VendorDevelopmentDTO.builder()
                        .dateOfParticipation(v.getDateOfParticipation())
                        .vdpProgramName(v.getVdpProgramName())
                        .productShowcased(v.getProductShowcased())
                        .nameOfBuyersInterested(v.getNameOfBuyersInterested())
                        .vendorRegisteredWith(v.getVendorRegisteredWith())
                        .iseProcurementRegistered(v.getIseProcurementRegistered())
                        .portalName(v.getPortalName())
                        .isDigitalCatalogCreated(v.getIsDigitalCatalogCreated())
                        .dateOfSupply(v.getDateOfSupply())
                        .volumeOfSupply(v.getVolumeOfSupply())
                        .units(v.getUnits())
                        .valueOfSupply(v.getValueOfSupply())
                        .monthlyTurnover(v.getMonthlyTurnover())
                        .isInfluenced(v.getIsInfluenced())
                        .agencyName(v.getAgency() != null ? v.getAgency().getAgencyName() : null)
                        .participantName(v.getParticipant() != null ? v.getParticipant().getParticipantName() : null)
                        .organizationName(v.getOrganization() != null ? v.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(v.getInfluencedParticipant() != null
                                ? v.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(v.getCreatedOn())
                        .updatedOn(v.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("dateOfParticipation", "Date of Participation"),
                        new OutcomeDetails.OutcomeDataSet("vdpProgramName", "VDP Program Name"),
                        new OutcomeDetails.OutcomeDataSet("productShowcased", "Product Showcased"),
                        new OutcomeDetails.OutcomeDataSet("nameOfBuyersInterested", "Buyers Interested"),
                        new OutcomeDetails.OutcomeDataSet("vendorRegisteredWith", "Vendor Registered With"),
                        new OutcomeDetails.OutcomeDataSet("iseProcurementRegistered", "e-Procurement Registered"),
                        new OutcomeDetails.OutcomeDataSet("portalName", "Portal Name"),
                        new OutcomeDetails.OutcomeDataSet("isDigitalCatalogCreated", "Digital Catalog Created"),
                        new OutcomeDetails.OutcomeDataSet("dateOfSupply", "Date of Supply"),
                        new OutcomeDetails.OutcomeDataSet("volumeOfSupply", "Volume of Supply"),
                        new OutcomeDetails.OutcomeDataSet("units", "Units"),
                        new OutcomeDetails.OutcomeDataSet("valueOfSupply", "Value of Supply (₹)"),
                        new OutcomeDetails.OutcomeDataSet("monthlyTurnover", "Monthly Turnover (₹)"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }
            case "ScStHub": {
                Page<ScStHub> pageData = scStHubRepository.findByAgency_AgencyId(agencyId, pageable);

                List<ScStHubDTO> body = pageData.getContent().stream().map(s -> ScStHubDTO.builder()
                        .id(s.getScStHubId())
                        .supportAvailedUnderNSSH(s.getSupportAvailedUnderNSSH())
                        .trainingName(s.getTrainingName())
                        .trainingCompletedDate(s.getTrainingCompletedDate())
                        .certificationName(s.getCertificationName())
                        .certificationReceivedDate(s.getCertificationReceivedDate())
                        .marketLinkageCompanyName(s.getMarketLinkageCompanyName())
                        .marketLinkageDate(s.getMarketLinkageDate())
                        .marketLinkageValue(s.getMarketLinkageValue())
                        .marketLinkageVolume(s.getMarketLinkageVolume())
                        .vendorRegistrationWithPSUOrOEM(s.getVendorRegistrationWithPSUOrOEM())
                        .tenderParticipatedName(s.getTenderParticipatedName())
                        .handholdingAgency(s.getHandholdingAgency())
                        .creditLinkageDate(s.getCreditLinkageDate())
                        .creditLinkageAmount(s.getCreditLinkageAmount())
                        .monthlyRevenue(s.getMonthlyRevenue())
                        .keyChallengesFaced(s.getKeyChallengesFaced())
                        .isInfluenced(s.getIsInfluenced())
                        .agencyName(s.getAgency() != null ? s.getAgency().getAgencyName() : null)
                        .participantName(s.getParticipant() != null ? s.getParticipant().getParticipantName() : null)
                        .organizationName(s.getOrganization() != null ? s.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(s.getInfluencedParticipant() != null
                                ? s.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(s.getCreatedOn())
                        .updatedOn(s.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("supportAvailedUnderNSSH", "Support Availed Under NSSH"),
                        new OutcomeDetails.OutcomeDataSet("trainingName", "Training Name"),
                        new OutcomeDetails.OutcomeDataSet("trainingCompletedDate", "Training Completed Date"),
                        new OutcomeDetails.OutcomeDataSet("certificationName", "Certification Name"),
                        new OutcomeDetails.OutcomeDataSet("certificationReceivedDate", "Certification Received Date"),
                        new OutcomeDetails.OutcomeDataSet("marketLinkageCompanyName", "Market Linkage Company Name"),
                        new OutcomeDetails.OutcomeDataSet("marketLinkageDate", "Market Linkage Date"),
                        new OutcomeDetails.OutcomeDataSet("marketLinkageValue", "Market Linkage Value (₹)"),
                        new OutcomeDetails.OutcomeDataSet("marketLinkageVolume", "Market Linkage Volume"),
                        new OutcomeDetails.OutcomeDataSet("vendorRegistrationWithPSUOrOEM", "Vendor Registration With PSU/OEM"),
                        new OutcomeDetails.OutcomeDataSet("tenderParticipatedName", "Tender Participated Name"),
                        new OutcomeDetails.OutcomeDataSet("handholdingAgency", "Handholding Agency"),
                        new OutcomeDetails.OutcomeDataSet("creditLinkageDate", "Credit Linkage Date"),
                        new OutcomeDetails.OutcomeDataSet("creditLinkageAmount", "Credit Linkage Amount (₹)"),
                        new OutcomeDetails.OutcomeDataSet("monthlyRevenue", "Monthly Revenue (₹)"),
                        new OutcomeDetails.OutcomeDataSet("keyChallengesFaced", "Key Challenges Faced"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }

            case "SIDBIAspire": {
                Page<SIDBIAspire> pageData = sidbiAspireRepository.findByAgency_AgencyId(agencyId, pageable);

                List<SIDBIAspireDTO> body = pageData.getContent().stream().map(a -> SIDBIAspireDTO.builder()
                        .id(a.getSidbiAspireId())
                        .applicationSubmissionDate(a.getApplicationSubmissionDate())
                        .dateSanctionUnderAspire(a.getDateSanctionUnderAspire())
                        .fundingSupportReceived(a.getFundingSupportReceived())
                        .incubationPartnerName(a.getIncubationPartnerName())
                        .fundingType(a.getFundingType())
                        .supportAmount(a.getSupportAmount())
                        .machinerySetupDate(a.getMachinerySetupDate())
                        .productionStartedDate(a.getProductionStartedDate())
                        .production(a.getProduction())
                        .productionUnits(a.getProductionUnits())
                        .marketLinkageEnabled(a.getMarketLinkageEnabled())
                        .marketLinkageDate(a.getMarketLinkageDate())
                        .marketLinkageVolume(a.getMarketLinkageVolume())
                        .marketLinkageValue(a.getMarketLinkageValue())
                        .turnover(a.getTurnover())
                        .isInfluenced(a.getIsInfluenced())
                        .agencyName(a.getAgency() != null ? a.getAgency().getAgencyName() : null)
                        .participantName(a.getParticipant() != null ? a.getParticipant().getParticipantName() : null)
                        .organizationName(a.getOrganization() != null ? a.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(a.getInfluencedParticipant() != null
                                ? a.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(a.getCreatedOn())
                        .updatedOn(a.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("applicationSubmissionDate", "Application Submission Date"),
                        new OutcomeDetails.OutcomeDataSet("dateSanctionUnderAspire", "Sanction Date Under Aspire"),
                        new OutcomeDetails.OutcomeDataSet("fundingSupportReceived", "Funding Support Received"),
                        new OutcomeDetails.OutcomeDataSet("incubationPartnerName", "Incubation Partner Name"),
                        new OutcomeDetails.OutcomeDataSet("fundingType", "Funding Type"),
                        new OutcomeDetails.OutcomeDataSet("supportAmount", "Support Amount (₹)"),
                        new OutcomeDetails.OutcomeDataSet("machinerySetupDate", "Machinery Setup Date"),
                        new OutcomeDetails.OutcomeDataSet("productionStartedDate", "Production Started Date"),
                        new OutcomeDetails.OutcomeDataSet("production", "Production"),
                        new OutcomeDetails.OutcomeDataSet("productionUnits", "Production Units"),
                        new OutcomeDetails.OutcomeDataSet("marketLinkageEnabled", "Market Linkage Enabled"),
                        new OutcomeDetails.OutcomeDataSet("marketLinkageDate", "Market Linkage Date"),
                        new OutcomeDetails.OutcomeDataSet("marketLinkageVolume", "Market Linkage Volume"),
                        new OutcomeDetails.OutcomeDataSet("marketLinkageValue", "Market Linkage Value (₹)"),
                        new OutcomeDetails.OutcomeDataSet("turnover", "Turnover (₹)"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }
            case "GeMRegistration": {
                Page<GeMRegistration> pageData =
                        geMRegistrationRepository.findByAgency_AgencyId(agencyId, pageable);

                List<GeMRegistrationDTO> body = pageData.getContent().stream().map(g -> GeMRegistrationDTO.builder()
                        .id(g.getGemId())
                        .gemRegistrationId(g.getGemRegistrationId())
                        .gemRegistrationDate(g.getGemRegistrationDate())
                        .isInfluenced(g.getIsInfluenced())
                        .agencyName(g.getAgency() != null ? g.getAgency().getAgencyName() : null)
                        .participantName(g.getParticipant() != null ? g.getParticipant().getParticipantName() : null)
                        .organizationName(g.getOrganization() != null ? g.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(g.getInfluencedParticipant() != null
                                ? g.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(g.getCreatedOn())
                        .updatedOn(g.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("gemRegistrationId", "GeM Registration ID"),
                        new OutcomeDetails.OutcomeDataSet("gemRegistrationDate", "Registration Date"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();

                resultPage = pageData;
                break;
            }
            case "OEM": {
                Page<OEM> pageData = oemRepository.findByAgency_AgencyId(agencyId, pageable);

                List<OEMDTO> body = pageData.getContent().stream().map(o -> OEMDTO.builder()
                        .id(o.getOemId())
                        .oemRegistrationDate(o.getOemRegistrationDate())
                        .oemRegistrationNumber(o.getOemRegistrationNumber())
                        .oemTargeted(o.getOemTargeted())
                        .oemVendorCode(o.getOemVendorCode())
                        .productsSupplied(o.getProductsSupplied())
                        .vendorRegistrationDate(o.getVendorRegistrationDate())
                        .firstPurchaseOrderDate(o.getFirstPurchaseOrderDate())
                        .firstPOValue(o.getFirstPOValue())
                        .currentMonthlySupplyValue(o.getCurrentMonthlySupplyValue())
                        .isCertificationStatus(o.getIsCertificationStatus())
                        .machineryUpGradation(o.getMachineryUpGradation())
                        .oemAuditScore(o.getOemAuditScore())
                        .isInfluenced(o.getIsInfluenced())
                        .agencyName(o.getAgency() != null ? o.getAgency().getAgencyName() : null)
                        .participantName(o.getParticipant() != null ? o.getParticipant().getParticipantName() : null)
                        .organizationName(o.getOrganization() != null ? o.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(o.getInfluencedParticipant() != null
                                ? o.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(o.getCreatedOn())
                        .updatedOn(o.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("oemRegistrationDate", "OEM Registration Date"),
                        new OutcomeDetails.OutcomeDataSet("oemRegistrationNumber", "OEM Registration Number"),
                        new OutcomeDetails.OutcomeDataSet("oemTargeted", "OEM Targeted"),
                        new OutcomeDetails.OutcomeDataSet("oemVendorCode", "OEM Vendor Code"),
                        new OutcomeDetails.OutcomeDataSet("productsSupplied", "Products Supplied"),
                        new OutcomeDetails.OutcomeDataSet("vendorRegistrationDate", "Vendor Registration Date"),
                        new OutcomeDetails.OutcomeDataSet("firstPurchaseOrderDate", "First Purchase Order Date"),
                        new OutcomeDetails.OutcomeDataSet("firstPOValue", "First Purchase Order Value (₹)"),
                        new OutcomeDetails.OutcomeDataSet("currentMonthlySupplyValue", "Current Monthly Supply Value (₹)"),
                        new OutcomeDetails.OutcomeDataSet("isCertificationStatus", "Certification Status"),
                        new OutcomeDetails.OutcomeDataSet("machineryUpGradation", "Machinery Upgradation"),
                        new OutcomeDetails.OutcomeDataSet("oemAuditScore", "OEM Audit Score"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }
            case "PMFMEScheme": {
                Page<PMFMEScheme> pageData = pmfmeSchemeRepository.findByAgency_AgencyId(agencyId, pageable);

                List<PMFMESchemeDTO> body = pageData.getContent().stream().map(p -> PMFMESchemeDTO.builder()
                        .id(p.getPmfmeId())
                        .dateOfApplicationSubmission(p.getDateOfApplicationSubmission())
                        .loanSanctioned(p.getLoanSanctioned())
                        .grantReceived(p.getGrantReceived())
                        .workingCapitalAvailed(p.getWorkingCapitalAvailed())
                        .dateOfApprovalUnderPMFME(p.getDateOfApprovalUnderPMFME())
                        .isCommonFacilityCentreUsed(p.getIsCommonFacilityCentreUsed())
                        .isBrandingMarketingSupportAvailed(p.getIsBrandingMarketingSupportAvailed())
                        .supportDetails(p.getSupportDetails())
                        .productionCapacity(p.getProductionCapacity())
                        .isCertificationSupportAvailed(p.getIsCertificationSupportAvailed())
                        .dateOfMarketLinkage(p.getDateOfMarketLinkage())
                        .volumeOfMarketLinkage(p.getVolumeOfMarketLinkage())
                        .units(p.getUnits())
                        .valueOfMarketLinkage(p.getValueOfMarketLinkage())
                        .monthlyTurnover(p.getMonthlyTurnover())
                        .turnoverChange(p.getTurnoverChange())
                        .productionCapacityChange(p.getProductionCapacityChange())
                        .brandingOrMarketingSupportChange(p.getBrandingOrMarketingSupportChange())
                        .isInfluenced(p.getIsInfluenced())
                        .agencyName(p.getAgency() != null ? p.getAgency().getAgencyName() : null)
                        .participantName(p.getParticipant() != null ? p.getParticipant().getParticipantName() : null)
                        .organizationName(p.getOrganization() != null ? p.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(p.getInfluencedParticipant() != null
                                ? p.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(p.getCreatedOn())
                        .updatedOn(p.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("dateOfApplicationSubmission", "Application Submission Date"),
                        new OutcomeDetails.OutcomeDataSet("loanSanctioned", "Loan Sanctioned (₹)"),
                        new OutcomeDetails.OutcomeDataSet("grantReceived", "Grant Received (₹)"),
                        new OutcomeDetails.OutcomeDataSet("workingCapitalAvailed", "Working Capital Availed (₹)"),
                        new OutcomeDetails.OutcomeDataSet("dateOfApprovalUnderPMFME", "Approval Date Under PMFME"),
                        new OutcomeDetails.OutcomeDataSet("isCommonFacilityCentreUsed", "Used Common Facility Centre"),
                        new OutcomeDetails.OutcomeDataSet("isBrandingMarketingSupportAvailed", "Branding/Marketing Support Availed"),
                        new OutcomeDetails.OutcomeDataSet("supportDetails", "Support Details"),
                        new OutcomeDetails.OutcomeDataSet("productionCapacity", "Production Capacity (MTs)"),
                        new OutcomeDetails.OutcomeDataSet("isCertificationSupportAvailed", "Certification Support Availed"),
                        new OutcomeDetails.OutcomeDataSet("dateOfMarketLinkage", "Market Linkage Date"),
                        new OutcomeDetails.OutcomeDataSet("volumeOfMarketLinkage", "Market Linkage Volume"),
                        new OutcomeDetails.OutcomeDataSet("units", "Units"),
                        new OutcomeDetails.OutcomeDataSet("valueOfMarketLinkage", "Market Linkage Value (₹)"),
                        new OutcomeDetails.OutcomeDataSet("monthlyTurnover", "Monthly Turnover (₹)"),
                        new OutcomeDetails.OutcomeDataSet("turnoverChange", "Turnover Change"),
                        new OutcomeDetails.OutcomeDataSet("productionCapacityChange", "Production Capacity Change"),
                        new OutcomeDetails.OutcomeDataSet("brandingOrMarketingSupportChange", "Branding/Marketing Support Change"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }
            case "ConsortiaTender": {
                Page<ConsortiaTender> pageData = consortiaTenderRepository.findByAgency_AgencyId(agencyId, pageable);

                List<ConsortiaTenderDTO> body = pageData.getContent().stream().map(c -> ConsortiaTenderDTO.builder()
                        .id(c.getConsortiaTenderId())
                        .productOrServiceOffered(c.getProductOrServiceOffered())
                        .consortiaMemberType(c.getConsortiaMemberType())
                        .consortiaName(c.getConsortiaName())
                        .dateOfJoiningConsortia(c.getDateOfJoiningConsortia())
                        .tenderParticipatedName(c.getTenderParticipatedName())
                        .departmentTenderIssued(c.getDepartmentTenderIssued())
                        .tenderId(c.getTenderId())
                        .tenderValue(c.getTenderValue())
                        .tenderOutcome(c.getTenderOutcome())
                        .workOrderIssueDate(c.getWorkOrderIssueDate())
                        .isOrderExecuted(c.getIsOrderExecuted())
                        .challengesFaced(c.getChallengesFaced())
                        .isInfluenced(c.getIsInfluenced())
                        .agencyName(c.getAgency() != null ? c.getAgency().getAgencyName() : null)
                        .participantName(c.getParticipant() != null ? c.getParticipant().getParticipantName() : null)
                        .organizationName(c.getOrganization() != null ? c.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(c.getInfluencedParticipant() != null
                                ? c.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(c.getCreatedOn())
                        .updatedOn(c.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("productOrServiceOffered", "Product / Service Offered"),
                        new OutcomeDetails.OutcomeDataSet("consortiaMemberType", "Consortia Member Type"),
                        new OutcomeDetails.OutcomeDataSet("consortiaName", "Consortia Name"),
                        new OutcomeDetails.OutcomeDataSet("dateOfJoiningConsortia", "Date of Joining Consortia"),
                        new OutcomeDetails.OutcomeDataSet("tenderParticipatedName", "Tender Participated Name"),
                        new OutcomeDetails.OutcomeDataSet("departmentTenderIssued", "Department Tender Issued"),
                        new OutcomeDetails.OutcomeDataSet("tenderId", "Tender ID"),
                        new OutcomeDetails.OutcomeDataSet("tenderValue", "Tender Value (₹)"),
                        new OutcomeDetails.OutcomeDataSet("tenderOutcome", "Tender Outcome"),
                        new OutcomeDetails.OutcomeDataSet("workOrderIssueDate", "Work Order Issue Date"),
                        new OutcomeDetails.OutcomeDataSet("isOrderExecuted", "Order Executed"),
                        new OutcomeDetails.OutcomeDataSet("challengesFaced", "Challenges Faced"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }

            case "DesignRights": {
                Page<DesignRights> pageData = designRightsRepository.findByAgency_AgencyId(agencyId, pageable);

                List<DesignRightsDTO> body = pageData.getContent().stream().map(d -> DesignRightsDTO.builder()
                        .id(d.getDesignRightsId())
                        .dateOfApplication(d.getDateOfApplication())
                        .dateOfDesignRightsGranted(d.getDateOfDesignRightsGranted())
                        .certificationNumber(d.getCertificationNumber())
                        .typeOfDesignRegistered(d.getTypeOfDesignRegistered())
                        .revenueFromDesignProducts(d.getRevenueFromDesignProducts())
                        .isAwardedForDesignProtection(d.getIsAwardedForDesignProtection())
                        .dateOfAwarded(d.getDateOfAwarded())
                        .nameOfAward(d.getNameOfAward())
                        .dateOfExport(d.getDateOfExport())
                        .valueOfExport(d.getValueOfExport())
                        .volumeOfExport(d.getVolumeOfExport())
                        .units(d.getUnits())
                        .isInfluenced(d.getIsInfluenced())
                        .agencyName(d.getAgency() != null ? d.getAgency().getAgencyName() : null)
                        .participantName(d.getParticipant() != null ? d.getParticipant().getParticipantName() : null)
                        .organizationName(d.getOrganization() != null ? d.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(d.getInfluencedParticipant() != null
                                ? d.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(d.getCreatedOn())
                        .updatedOn(d.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("dateOfApplication", "Date of Application"),
                        new OutcomeDetails.OutcomeDataSet("dateOfDesignRightsGranted", "Date of Design Rights Granted"),
                        new OutcomeDetails.OutcomeDataSet("certificationNumber", "Certification Number"),
                        new OutcomeDetails.OutcomeDataSet("typeOfDesignRegistered", "Type of Design Registered"),
                        new OutcomeDetails.OutcomeDataSet("revenueFromDesignProducts", "Revenue from Design Products (₹)"),
                        new OutcomeDetails.OutcomeDataSet("isAwardedForDesignProtection", "Awarded for Design Protection"),
                        new OutcomeDetails.OutcomeDataSet("dateOfAwarded", "Date of Award"),
                        new OutcomeDetails.OutcomeDataSet("nameOfAward", "Name of Award"),
                        new OutcomeDetails.OutcomeDataSet("dateOfExport", "Date of Export"),
                        new OutcomeDetails.OutcomeDataSet("valueOfExport", "Value of Export (₹)"),
                        new OutcomeDetails.OutcomeDataSet("volumeOfExport", "Volume of Export"),
                        new OutcomeDetails.OutcomeDataSet("units", "Units"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }
            case "CopyRights": {
                Page<CopyRights> pageData = copyRightsRepository.findByAgency_AgencyId(agencyId, pageable);

                List<CopyRightsDTO> body = pageData.getContent().stream().map(c -> CopyRightsDTO.builder()
                        .id(c.getCopyRightsId())
                        .dateOfApplicationFiled(c.getDateOfApplicationFiled())
                        .typeOfIntellectualWorkRegistered(c.getTypeOfIntellectualWorkRegistered())
                        .registrationCertificateReceivedDate(c.getRegistrationCertificateReceivedDate())
                        .registrationCertificateNumber(c.getRegistrationCertificateNumber())
                        .numberOfProductsProtected(c.getNumberOfProductsProtected())
                        .nameOfProductProtected(c.getNameOfProductProtected())
                        .revenueFromCopyrightedMaterial(c.getRevenueFromCopyrightedMaterial())
                        .marketValueAfterCopyright(c.getMarketValueAfterCopyright())
                        .isInfluenced(c.getIsInfluenced())
                        .agencyName(c.getAgency() != null ? c.getAgency().getAgencyName() : null)
                        .participantName(c.getParticipant() != null ? c.getParticipant().getParticipantName() : null)
                        .organizationName(c.getOrganization() != null ? c.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(c.getInfluencedParticipant() != null
                                ? c.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(c.getCreatedOn())
                        .updatedOn(c.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("dateOfApplicationFiled", "Date of Application Filed"),
                        new OutcomeDetails.OutcomeDataSet("typeOfIntellectualWorkRegistered", "Type of Intellectual Work Registered"),
                        new OutcomeDetails.OutcomeDataSet("registrationCertificateReceivedDate", "Certificate Received Date"),
                        new OutcomeDetails.OutcomeDataSet("registrationCertificateNumber", "Certificate Number"),
                        new OutcomeDetails.OutcomeDataSet("numberOfProductsProtected", "No. of Products Protected"),
                        new OutcomeDetails.OutcomeDataSet("nameOfProductProtected", "Name of Product Protected"),
                        new OutcomeDetails.OutcomeDataSet("revenueFromCopyrightedMaterial", "Revenue from Copyrighted Material (₹)"),
                        new OutcomeDetails.OutcomeDataSet("marketValueAfterCopyright", "Market Value After Copyright (₹)"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }
            case "GreeningOfMSME": {
                Page<GreeningOfMSME> pageData = greeningOfMSMERepository.findByAgency_AgencyId(agencyId, pageable);

                List<GreeningOfMSMEDTO> body = pageData.getContent().stream().map(g -> GreeningOfMSMEDTO.builder()
                        .id(g.getGreeningOfMSME())
                        .typeOfIntervention(g.getTypeOfIntervention())
                        .typeOfPrototypeProposed(g.getTypeOfPrototypeProposed())
                        .typeOfTrainingsReceived(g.getTypeOfTrainingsReceived())
                        .trainingCompletionDate(g.getTrainingCompletionDate())
                        .businessPlanSubmissionDate(g.getBusinessPlanSubmissionDate())
                        .amountSanctionedDate(g.getAmountSanctionedDate())
                        .amountReleasedDate(g.getAmountReleasedDate())
                        .amountReleased(g.getAmountReleased())
                        .nameOfBankProvidedLoan(g.getNameOfBankProvidedLoan())
                        .dateOfGrounding(g.getDateOfGrounding())
                        .purposeOfLoanUtilised(g.getPurposeOfLoanUtilised())
                        .parameter1(g.getParameter1())
                        .parameter2(g.getParameter2())
                        .parameter1Value(g.getParameter1Value())
                        .parameter1Units(g.getParameter1Units())
                        .parameter2Value(g.getParameter2Value())
                        .parameter2Units(g.getParameter2Units())
                        .productionPerHour(g.getProductionPerHour())
                        .isInfluenced(g.getIsInfluenced())
                        .agencyName(g.getAgency() != null ? g.getAgency().getAgencyName() : null)
                        .participantName(g.getParticipant() != null ? g.getParticipant().getParticipantName() : null)
                        .organizationName(g.getOrganization() != null ? g.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(g.getInfluencedParticipant() != null
                                ? g.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(g.getCreatedOn())
                        .updatedOn(g.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("typeOfIntervention", "Type of Intervention"),
                        new OutcomeDetails.OutcomeDataSet("typeOfPrototypeProposed", "Type of Prototype Proposed"),
                        new OutcomeDetails.OutcomeDataSet("typeOfTrainingsReceived", "Type of Trainings Received"),
                        new OutcomeDetails.OutcomeDataSet("trainingCompletionDate", "Training Completion Date"),
                        new OutcomeDetails.OutcomeDataSet("businessPlanSubmissionDate", "Business Plan Submission Date"),
                        new OutcomeDetails.OutcomeDataSet("amountSanctionedDate", "Amount Sanctioned Date"),
                        new OutcomeDetails.OutcomeDataSet("amountReleasedDate", "Amount Released Date"),
                        new OutcomeDetails.OutcomeDataSet("amountReleased", "Amount Released (₹ Lakhs)"),
                        new OutcomeDetails.OutcomeDataSet("nameOfBankProvidedLoan", "Bank Provided Loan"),
                        new OutcomeDetails.OutcomeDataSet("dateOfGrounding", "Date of Grounding"),
                        new OutcomeDetails.OutcomeDataSet("purposeOfLoanUtilised", "Purpose of Loan Utilised"),
                        new OutcomeDetails.OutcomeDataSet("parameter1", "Parameter 1"),
                        new OutcomeDetails.OutcomeDataSet("parameter1Value", "Parameter 1 Value"),
                        new OutcomeDetails.OutcomeDataSet("parameter1Units", "Parameter 1 Units"),
                        new OutcomeDetails.OutcomeDataSet("parameter2", "Parameter 2"),
                        new OutcomeDetails.OutcomeDataSet("parameter2Value", "Parameter 2 Value"),
                        new OutcomeDetails.OutcomeDataSet("parameter2Units", "Parameter 2 Units"),
                        new OutcomeDetails.OutcomeDataSet("productionPerHour", "Production per Hour"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }
            case "ECommerceRegistration": {
                Page<ECommerceRegistration> pageData = eCommerceRegistrationRepository.findByAgency_AgencyId(agencyId, pageable);

                List<ECommerceRegistrationDTO> body = pageData.getContent().stream().map(e -> ECommerceRegistrationDTO.builder()
                        .id(e.getId())
                        .platformName(e.getPlatformName())
                        .dateOfOnboarding(e.getDateOfOnboarding())
                        .registrationDetails(e.getRegistrationDetails())
                        .isInfluenced(e.getIsInfluenced())
                        .agencyName(e.getAgency() != null ? e.getAgency().getAgencyName() : null)
                        .participantName(e.getParticipant() != null ? e.getParticipant().getParticipantName() : null)
                        .organizationName(e.getOrganization() != null ? e.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(e.getInfluencedParticipant() != null
                                ? e.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(e.getCreatedOn())
                        .updatedOn(e.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("platformName", "E-Commerce Platform Name"),
                        new OutcomeDetails.OutcomeDataSet("dateOfOnboarding", "Date of Onboarding"),
                        new OutcomeDetails.OutcomeDataSet("registrationDetails", "Registration Details"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }
            case "ECommerceTransaction": {
                Page<ECommerceTransaction> pageData =
                        eCommerceTransactionRepository.findByEcommerceRegistration_Agency_AgencyId(agencyId, pageable);

                List<ECommerceTransactionDTO> body = pageData.getContent().stream().map(tx -> ECommerceTransactionDTO.builder()
                        .id(tx.getId())
                        .fromDate(tx.getFromDate())
                        .toDate(tx.getToDate())
                        .numberOfTransactions(tx.getNumberOfTransactions())
                        .totalBusinessAmount(tx.getTotalBusinessAmount())
                        .isInfluenced(tx.getIsInfluenced())
                        .registrationDetails(tx.getEcommerceRegistration() != null ? tx.getEcommerceRegistration().getRegistrationDetails() : null)

                        .agencyName(tx.getAgency() != null ? tx.getAgency().getAgencyName() : null)
                        .participantName(tx.getParticipant() != null ? tx.getParticipant().getParticipantName() : null)
                        .organizationName(tx.getOrganization() != null ? tx.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(tx.getInfluencedParticipant() != null
                                ? tx.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(tx.getCreatedOn())
                        .updatedOn(tx.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("registrationDetails", "Registration Details"),
                        new OutcomeDetails.OutcomeDataSet("fromDate", "From Date"),
                        new OutcomeDetails.OutcomeDataSet("toDate", "To Date"),
                        new OutcomeDetails.OutcomeDataSet("numberOfTransactions", "Number of Transactions"),
                        new OutcomeDetails.OutcomeDataSet("totalBusinessAmount", "Total Business Amount (₹)"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }

            case "Loan": {
                Page<Loan> pageData = loanRepository.findByAgency_AgencyId(agencyId, pageable);

                List<LoanDTO> body = pageData.getContent().stream().map(l -> LoanDTO.builder()
                        .id(l.getId())
                        .bankName(l.getBankName())
                        .loanAmount(l.getLoanAmount())
                        .dateOfFirstDisbursement(l.getDateOfFirstDisbursement())
                        .disbursementAmount(l.getDisbursementAmount())
                        .isInfluenced(l.getIsInfluenced())
                        .agencyName(l.getAgency() != null ? l.getAgency().getAgencyName() : null)
                        .participantName(l.getParticipant() != null ? l.getParticipant().getParticipantName() : null)
                        .organizationName(l.getOrganization() != null ? l.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(l.getInfluencedParticipant() != null
                                ? l.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(l.getCreatedOn())
                        .updatedOn(l.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("bankName", "Bank Name"),
                        new OutcomeDetails.OutcomeDataSet("loanAmount", "Loan Amount (₹ Lakhs)"),
                        new OutcomeDetails.OutcomeDataSet("dateOfFirstDisbursement", "Date of First Disbursement"),
                        new OutcomeDetails.OutcomeDataSet("disbursementAmount", "Disbursement Amount (₹ Lakhs)"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }
            case "ExportPromotion": {
                Page<ExportPromotion> pageData = exportPromotionRepository.findByAgency_AgencyId(agencyId, pageable);

                List<ExportPromotionDTO> body = pageData.getContent().stream().map(e -> ExportPromotionDTO.builder()
                        .id(e.getId())
                        .sectorName(e.getSectorName())
                        .productName(e.getProductName())
                        .exportImportLicenceNo(e.getExportImportLicenceNo())
                        .mappingWithInternationalBuyer(e.getMappingWithInternationalBuyer())
                        .monthlyTurnoverInLakhs(e.getMonthlyTurnoverInLakhs())
                        .isExport(e.getIsExport())
                        .exportDate(e.getExportDate())
                        .exportValueInLakhs(e.getExportValueInLakhs())
                        .exportVolumeInMts(e.getExportVolumeInMts())
                        .isInfluenced(e.getIsInfluenced())
                        .agencyName(e.getAgency() != null ? e.getAgency().getAgencyName() : null)
                        .participantName(e.getParticipant() != null ? e.getParticipant().getParticipantName() : null)
                        .organizationName(e.getOrganization() != null ? e.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(e.getInfluencedParticipant() != null
                                ? e.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(e.getCreatedOn())
                        .updatedOn(e.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("sectorName", "Sector Name"),
                        new OutcomeDetails.OutcomeDataSet("productName", "Product Name"),
                        new OutcomeDetails.OutcomeDataSet("exportImportLicenceNo", "Export/Import Licence No"),
                        new OutcomeDetails.OutcomeDataSet("mappingWithInternationalBuyer", "Mapped with International Buyer"),
                        new OutcomeDetails.OutcomeDataSet("monthlyTurnoverInLakhs", "Monthly Turnover (₹ Lakhs)"),
                        new OutcomeDetails.OutcomeDataSet("isExport", "Is Export"),
                        new OutcomeDetails.OutcomeDataSet("exportDate", "Export Date"),
                        new OutcomeDetails.OutcomeDataSet("exportValueInLakhs", "Export Value (₹ Lakhs)"),
                        new OutcomeDetails.OutcomeDataSet("exportVolumeInMts", "Export Volume (MTs)"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }
            case "SkillUpgradation": {
                Page<SkillUpgradation> pageData =
                        skillUpgradationRepository.findByAgency_AgencyId(agencyId, pageable);

                List<SkillUpgradationDTO> body = pageData.getContent().stream().map(su -> SkillUpgradationDTO.builder()
                        .id(su.getId())
                        .typeOfTrainingReceived(su.getTypeOfTrainingReceived())
                        .trainingCompletionDate(su.getTrainingCompletionDate())
                        .businessPlanSubmissionDate(su.getBusinessPlanSubmissionDate())
                        .amountSanctionedDate(su.getAmountSanctionedDate())
                        .amountReleasedDate(su.getAmountReleasedDate())
                        .amountReleasedInLakhs(su.getAmountReleasedInLakhs())
                        .bankProvidedLoan(su.getBankProvidedLoan())
                        .loanType(su.getLoanType())
                        .loanPurpose(su.getLoanPurpose())
                        .groundingDate(su.getGroundingDate())
                        .sectorType(su.getSectorType())
                        .monthlyTurnoverInLakhs(su.getMonthlyTurnoverInLakhs())
                        .isInfluenced(su.getIsInfluenced())
                        .agencyName(su.getAgency() != null ? su.getAgency().getAgencyName() : null)
                        .participantName(su.getParticipant() != null ? su.getParticipant().getParticipantName() : null)
                        .organizationName(su.getOrganization() != null ? su.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(su.getInfluencedParticipant() != null
                                ? su.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(su.getCreatedOn())
                        .updatedOn(su.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("typeOfTrainingReceived", "Type of Training Received"),
                        new OutcomeDetails.OutcomeDataSet("trainingCompletionDate", "Training Completion Date"),
                        new OutcomeDetails.OutcomeDataSet("businessPlanSubmissionDate", "Business Plan Submission Date"),
                        new OutcomeDetails.OutcomeDataSet("amountSanctionedDate", "Amount Sanctioned Date"),
                        new OutcomeDetails.OutcomeDataSet("amountReleasedDate", "Amount Released Date"),
                        new OutcomeDetails.OutcomeDataSet("amountReleasedInLakhs", "Amount Released (₹ Lakhs)"),
                        new OutcomeDetails.OutcomeDataSet("bankProvidedLoan", "Bank Provided Loan"),
                        new OutcomeDetails.OutcomeDataSet("loanType", "Loan Type"),
                        new OutcomeDetails.OutcomeDataSet("loanPurpose", "Loan Purpose"),
                        new OutcomeDetails.OutcomeDataSet("groundingDate", "Grounding Date"),
                        new OutcomeDetails.OutcomeDataSet("sectorType", "Sector Type"),
                        new OutcomeDetails.OutcomeDataSet("monthlyTurnoverInLakhs", "Monthly Turnover (₹ Lakhs)"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }
            case "ImportSubsititution": {
                Page<ImportSubsititution> pageData = importSubsititutionRepository.findByAgency_AgencyId(agencyId, pageable);

                List<ImportSubstitutionDTO> body = pageData.getContent().stream().map(i -> ImportSubstitutionDTO.builder()
                        .id(i.getId())
                        .sectorName(i.getSectorName())
                        .productName(i.getProductName())
                        .prototypeSelected(i.getPrototypeSelected())
                        .businessPlanSubmissionDate(i.getBusinessPlanSubmissionDate())
                        .amountSanctionedDate(i.getAmountSanctionedDate())
                        .amountReleasedDate(i.getAmountReleasedDate())
                        .amountReleasedInLakhs(i.getAmountReleasedInLakhs())
                        .bankProvidedLoan(i.getBankProvidedLoan())
                        .groundingDate(i.getGroundingDate())
                        .monthlyTurnoverInLakhs(i.getMonthlyTurnoverInLakhs())
                        .marketOfProduct(i.getMarketOfProduct())
                        .marketDate(i.getMarketDate())
                        .marketValueInLakhs(i.getMarketValueInLakhs())
                        .marketVolumeInMts(i.getMarketVolumeInMts())
                        .isInfluenced(i.getIsInfluenced())
                        .agencyName(i.getAgency() != null ? i.getAgency().getAgencyName() : null)
                        .participantName(i.getParticipant() != null ? i.getParticipant().getParticipantName() : null)
                        .organizationName(i.getOrganization() != null ? i.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(i.getInfluencedParticipant() != null
                                ? i.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(i.getCreatedOn())
                        .updatedOn(i.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("sectorName", "Sector Name"),
                        new OutcomeDetails.OutcomeDataSet("productName", "Product Name"),
                        new OutcomeDetails.OutcomeDataSet("prototypeSelected", "Prototype Selected"),
                        new OutcomeDetails.OutcomeDataSet("businessPlanSubmissionDate", "Business Plan Submission Date"),
                        new OutcomeDetails.OutcomeDataSet("amountSanctionedDate", "Amount Sanctioned Date"),
                        new OutcomeDetails.OutcomeDataSet("amountReleasedDate", "Amount Released Date"),
                        new OutcomeDetails.OutcomeDataSet("amountReleasedInLakhs", "Amount Released (₹ Lakhs)"),
                        new OutcomeDetails.OutcomeDataSet("bankProvidedLoan", "Bank Provided Loan"),
                        new OutcomeDetails.OutcomeDataSet("groundingDate", "Grounding Date"),
                        new OutcomeDetails.OutcomeDataSet("monthlyTurnoverInLakhs", "Monthly Turnover (₹ Lakhs)"),
                        new OutcomeDetails.OutcomeDataSet("marketOfProduct", "Market of Product"),
                        new OutcomeDetails.OutcomeDataSet("marketDate", "Market Date"),
                        new OutcomeDetails.OutcomeDataSet("marketValueInLakhs", "Market Value (₹ Lakhs)"),
                        new OutcomeDetails.OutcomeDataSet("marketVolumeInMts", "Market Volume (MTs)"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();
                resultPage = pageData;
                break;
            }
            case "ZEDCertification": {
                Page<ZEDCertification> pageData = zedCertificationRepository.findByAgency_AgencyId(agencyId, pageable);

                List<ZEDCertificationDTO> body = pageData.getContent().stream().map(z -> ZEDCertificationDTO.builder()
                        .id(z.getZedCertificateRegistrationId())
                        .ownerName(z.getOwnerName())
                        .nicCode(z.getNicCode())
                        .unitAddress(z.getUnitAddress())
                        .certificationDate(z.getCertificationDate())
                        .zedCertificationId(z.getZedCertificationId())
                        .zedCertificationType(z.getZedCertificationType())
                        .turnover(z.getTurnover())
                        .energyConsumptionKwhHr(z.getEnergyConsumptionKwhHr())
                        .isInfluenced(z.getIsInfluenced())
                        .agencyName(z.getAgency() != null ? z.getAgency().getAgencyName() : null)
                        .participantName(z.getParticipant() != null ? z.getParticipant().getParticipantName() : null)
                        .organizationName(z.getOrganization() != null ? z.getOrganization().getOrganizationName() : null)
                        .influencedParticipantName(z.getInfluencedParticipant() != null
                                ? z.getInfluencedParticipant().getParticipantName()
                                : null)
                        .createdOn(z.getCreatedOn())
                        .updatedOn(z.getUpdatedOn())
                        .build()).toList();

                List<OutcomeDetails.OutcomeDataSet> headers = List.of(
                        new OutcomeDetails.OutcomeDataSet("ownerName", "Owner Name"),
                        new OutcomeDetails.OutcomeDataSet("nicCode", "NIC Code"),
                        new OutcomeDetails.OutcomeDataSet("unitAddress", "Unit Address"),
                        new OutcomeDetails.OutcomeDataSet("certificationDate", "Certification Date"),
                        new OutcomeDetails.OutcomeDataSet("zedCertificationId", "ZED Certification ID"),
                        new OutcomeDetails.OutcomeDataSet("zedCertificationType", "ZED Certification Type"),
                        new OutcomeDetails.OutcomeDataSet("turnover", "Turnover (₹ Lakhs)"),
                        new OutcomeDetails.OutcomeDataSet("energyConsumptionKwhHr", "Energy Consumption (KWh/hr)"),
                        new OutcomeDetails.OutcomeDataSet("isInfluenced", "Influenced"),
                        new OutcomeDetails.OutcomeDataSet("agencyName", "Agency Name"),
                        new OutcomeDetails.OutcomeDataSet("participantName", "Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("organizationName", "Organization Name"),
                        new OutcomeDetails.OutcomeDataSet("influencedParticipantName", "Influenced Participant Name"),
                        new OutcomeDetails.OutcomeDataSet("createdOn", "Created On"),
                        new OutcomeDetails.OutcomeDataSet("updatedOn", "Updated On")
                );

                responseDTO = OutcomeResponseDTO.builder()
                        .headers(headers)
                        .body(body)
                        .build();

                return WorkflowResponse.builder()
                        .status(200)
                        .message("Success")
                        .data(responseDTO)
                        .totalElements(pageData.getTotalElements())
                        .totalPages(pageData.getTotalPages())
                        .build();
            }

            default:
                throw new IllegalArgumentException("Invalid outcome name: " + outcomeName);
        }

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(Map.of("outcome", responseDTO))
                .totalElements(resultPage.getTotalElements())
                .totalPages(resultPage.getTotalPages())
                .build();
    }

}
