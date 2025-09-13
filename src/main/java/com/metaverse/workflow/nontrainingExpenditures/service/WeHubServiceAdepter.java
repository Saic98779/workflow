package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.login.service.AuthRequest;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.nontrainingExpenditures.Dto.CorpusDebitFinancing;
import com.metaverse.workflow.nontrainingExpenditures.Dto.WeHubHandholdingRequest;
import com.metaverse.workflow.nontrainingExpenditures.Dto.WeHubSDGRequest;
import com.metaverse.workflow.nontrainingExpenditures.Dto.WeHubSelectedCompaniesRequest;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.WeHubHandholdingRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.WeHubSDGRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.WeHubSelectedCompaniesRepository;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import com.metaverse.workflow.organization.service.OrganizationResponse;
import com.metaverse.workflow.organization.service.OrganizationResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
@Transactional
public class WeHubServiceAdepter implements WeHubService {


    @Value("${tihcl.username}")
    private  String userName;

    @Value("${tihcl.password}")
    private String password;


    private final WeHubSelectedCompaniesRepository weHubSelectedCompaniesRepository;
    private final NonTrainingSubActivityRepository subActivityRepository;
    private final OrganizationRepository organizationRepository;
    private final WeHubHandholdingRepository weHubHandholdingRepository;
    private final WeHubSDGRepository weHubSDGRepository;

    @Override
    public WorkflowResponse create(WeHubSelectedCompaniesRequest request) throws DataException {
        NonTrainingSubActivity subActivity = subActivityRepository.findById(request.getSubActivityId())
                .orElseThrow(() ->new DataException("SubActivity not found","SUB_ACTIVITY_NOT_FOUND",400));
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() ->new DataException("Organization not found","ORGANIZATION_NOT_FOUND",400));

        WeHubSelectedCompanies entity = WeHubMapper.toEntity(request, subActivity,organization);
        WeHubSelectedCompanies savedEntity =   weHubSelectedCompaniesRepository.save(entity);
        return WorkflowResponse.builder()
                .status(200)
                .message("Selected Companies saved Successfully")
                .data(WeHubMapper.toResponse(savedEntity))
                .build();
    }

    @Override
    @Transactional
    public WorkflowResponse update(Long candidateId, WeHubSelectedCompaniesRequest request) throws DataException {
        WeHubSelectedCompanies entity = weHubSelectedCompaniesRepository.findById(candidateId)
                .orElseThrow(() ->new DataException("Selected Company not found","SELECTED_COMPANY_NOT_FOUND",400));

        entity.setUdhyamDpiitRegistrationNo(request.getUdhyamDpiitRegistrationNo());
        entity.setApplicationReceivedDate(DateUtil.covertStringToDate(request.getApplicationReceivedDate()));
        entity.setApplicationSource(request.getApplicationSource());
        entity.setShortlistingDate(DateUtil.covertStringToDate(request.getShortlistingDate()));
        entity.setNeedAssessmentDate(DateUtil.covertStringToDate(request.getNeedAssessmentDate()));
        entity.setCandidateFinalised(request.getCandidateFinalised());
        entity.setCohortName(request.getCohortName());
        entity.setBaselineAssessmentDate(DateUtil.covertStringToDate(request.getBaselineAssessmentDate()));

        WeHubSelectedCompanies updatedEntity =  weHubSelectedCompaniesRepository.save(entity);
        return WorkflowResponse.builder()
                .status(200)
                .message("Selected Companies Updated Successfully")
                .data(WeHubMapper.toResponse(updatedEntity))
                .build();
    }



    @Override
    public WorkflowResponse getBySubActivityId(Long subActivityId) throws DataException {
        List<WeHubSelectedCompanies> companies = weHubSelectedCompaniesRepository
                .findByNonTrainingSubActivity_SubActivityId(subActivityId)
                .orElseThrow(() -> new DataException("SubActivity not found", "SUB_ACTIVITY_NOT_FOUND", 400));

        return WorkflowResponse.builder()
                .status(200)
                .message("Selected Companies fetched successfully")
                .data(companies.stream()
                        .map(WeHubMapper::toResponse)
                        .collect(Collectors.toList()))
                .build();
    }


    @Override
    public WorkflowResponse delete(Long candidateId) throws DataException {
        WeHubSelectedCompanies entity = weHubSelectedCompaniesRepository.findById(candidateId)
                .orElseThrow(() -> new DataException("Selected Company not found", "SELECTED_COMPANY_NOT_FOUND", 400));

        weHubSelectedCompaniesRepository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Selected Company deleted successfully")
                .build();
    }

    @Override
    public WorkflowResponse getById(Long candidateId) throws DataException {
        WeHubSelectedCompanies entity = weHubSelectedCompaniesRepository.findById(candidateId)
                .orElseThrow(() -> new DataException("Selected Company not found", "SELECTED_COMPANY_NOT_FOUND", 400));
        return WorkflowResponse.builder()
                .status(200)
                .message("Selected Company fetched successfully")
                .data(WeHubMapper.toResponse(entity))
                .build();
    }

    @Override
    public WorkflowResponse getSelectedOrganization() {
        List<WeHubSelectedCompanies> companies = weHubSelectedCompaniesRepository.findAll();

        List<Organization> organizations = companies.stream()
                .map(WeHubSelectedCompanies::getOrganization)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<OrganizationResponse> responses = OrganizationResponseMapper.mapOrganization(organizations);

        return WorkflowResponse.builder()
                .status(200)
                .message("Organizations fetched successfully")
                .data(responses)
                .build();
    }


    @Override
    public WorkflowResponse createHandholding(WeHubHandholdingRequest request) throws DataException {
        NonTrainingSubActivity subActivity = subActivityRepository.findById(request.getSubActivityId())
                .orElseThrow(() -> new DataException("SubActivity not found", "SUB_ACTIVITY_NOT_FOUND", 400));

        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new DataException("Organization not found", "ORGANIZATION_NOT_FOUND", 400));

        WeHubHandholding entity = WeHubMapper.mapToWehubHandholdingReq(request, organization, subActivity);
        WeHubHandholding savedEntity = weHubHandholdingRepository.save(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("WeHub Handholding saved successfully")
                .data(WeHubMapper.mapToWehubHandholdingRes(savedEntity))
                .build();
    }

    @Override
    public WorkflowResponse updateHandholding(Long handholdingId, WeHubHandholdingRequest request) throws DataException {
        WeHubHandholding entity = weHubHandholdingRepository.findById(handholdingId)
                .orElseThrow(() -> new DataException("WeHub Handholding not found", "HANDHOLDING_NOT_FOUND", 404));

        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new DataException("Organization not found", "ORGANIZATION_NOT_FOUND", 400));

        NonTrainingSubActivity subActivity = subActivityRepository.findById(request.getSubActivityId())
                .orElseThrow(() -> new DataException("Sub Activity not found", "SUB_ACTIVITY_NOT_FOUND", 400));


        WeHubHandholding updatedEntity = WeHubMapper.mapToWehubHandholdingReq(request, organization, subActivity);
        updatedEntity.setHandholdingId(entity.getHandholdingId());
        updatedEntity.setCreatedOn(entity.getCreatedOn());

        WeHubHandholding saved = weHubHandholdingRepository.save(updatedEntity);

        return WorkflowResponse.builder()
                .status(200)
                .message("WeHub Handholding updated successfully")
                .data(WeHubMapper.mapToWehubHandholdingRes(saved))
                .build();
    }

    @Override
    public WorkflowResponse getHandholdingBySubActivityId(Long subActivityId) throws DataException {
        List<WeHubHandholding> handholdings = weHubHandholdingRepository.findByNonTrainingSubActivity_SubActivityId(subActivityId);

        if (handholdings.isEmpty()) {
            throw new DataException("SubActivity not found or has no handholding records",
                    "SUB_ACTIVITY_NOT_FOUND", 400);
        }

        return WorkflowResponse.builder()
                .status(200)
                .message("WeHub Handholding records fetched successfully")
                .data(handholdings.stream()
                        .map(WeHubMapper::mapToWehubHandholdingRes)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public WorkflowResponse deleteHandholding(Long handholdingId) throws DataException {
        WeHubHandholding entity = weHubHandholdingRepository.findById(handholdingId)
                .orElseThrow(() -> new DataException("WeHub Handholding not found", "HANDHOLDING_NOT_FOUND", 400));

        weHubHandholdingRepository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("WeHub Handholding deleted successfully")
                .build();
    }


    @Override
    public WorkflowResponse getHandholdingById(Long handholdingId) throws DataException {
        WeHubHandholding entity = weHubHandholdingRepository.findById(handholdingId)
                .orElseThrow(() -> new DataException("WeHub Handholding not found", "HANDHOLDING_NOT_FOUND", 400));

        return WorkflowResponse.builder()
                .status(200)
                .message("WeHub Handholding fetched successfully")
                .data(WeHubMapper.mapToWehubHandholdingRes(entity))
                .build();
    }

    @Override
    public WorkflowResponse createWeHubSDG(WeHubSDGRequest request) throws DataException {
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new DataException("Organization not found", "ORGANIZATION_NOT_FOUND", 400));

        NonTrainingSubActivity activity = subActivityRepository.findById(request.getNonTrainingSubActivityId())
                .orElseThrow(() -> new DataException("NonTrainingActivity not found", "NON_TRAINING_ACTIVITY_NOT_FOUND", 400));

        WeHubSDG entity = WeHubMapper.mapToEeHubSDGReq(request, organization, activity);
        WeHubSDG savedEntity = weHubSDGRepository.save(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("WeHub SDG saved successfully")
                .data(WeHubMapper.mapToEeHubSDGRes(savedEntity))
                .build();
    }

    @Override
    @Transactional
    public WorkflowResponse updateWeHubSDG(Long eeHubSDGId, WeHubSDGRequest request) throws DataException {
        WeHubSDG entity = weHubSDGRepository.findById(eeHubSDGId)
                .orElseThrow(() -> new DataException("WeHub SDG not found", "WEHUB_SDG_NOT_FOUND", 400));

        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new DataException("Organization not found", "ORGANIZATION_NOT_FOUND", 400));


        entity.setOrganization(organization);
         entity.setAdoptionStatus(request.getAdoptionStatus());
        entity.setTechnologyAdopted(request.getTechnologyAdopted());
        entity.setEnvCompCert(request.getEnvCompCert());
        entity.setDateOfCert(DateUtil.covertStringToDate(request.getDateOfCert()));

        WeHubSDG updatedEntity = weHubSDGRepository.save(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("WeHub SDG updated successfully")
                .data(WeHubMapper.mapToEeHubSDGRes(updatedEntity))
                .build();
    }

    @Override
    public WorkflowResponse getWeHubSDGByActivityId(Long nonTrainingActivityId) throws DataException {
        List<WeHubSDG> entities = weHubSDGRepository
                .findByNonTrainingSubActivity_SubActivityId(nonTrainingActivityId)
                .orElseThrow(() -> new DataException("NonTrainingActivity not found", "NON_TRAINING_ACTIVITY_NOT_FOUND", 400));

        return WorkflowResponse.builder()
                .status(200)
                .message("WeHub SDG records fetched successfully")
                .data(entities.stream()
                        .map(WeHubMapper::mapToEeHubSDGRes)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public WorkflowResponse deleteWeHubSDG(Long eeHubSDGId) throws DataException {
        WeHubSDG entity = weHubSDGRepository.findById(eeHubSDGId)
                .orElseThrow(() -> new DataException("WeHub SDG not found", "WEHUB_SDG_NOT_FOUND", 400));

        weHubSDGRepository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("WeHub SDG deleted successfully")
                .build();
    }

    @Override
    public WorkflowResponse getWeHubSDGById(Long eeHubSDGId) throws DataException {
        WeHubSDG entity = weHubSDGRepository.findById(eeHubSDGId)
                .orElseThrow(() -> new DataException("WeHub SDG not found", "WEHUB_SDG_NOT_FOUND", 400));

        return WorkflowResponse.builder()
                .status(200)
                .message("WeHub SDG fetched successfully")
                .data(WeHubMapper.mapToEeHubSDGRes(entity))
                .build();
    }


    @Override
    public List<CorpusDebitFinancing> corpusDebitFinancing() {

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://tihcl.com/tihcl/api/tihcl/corpusDebitFinancing";
        String loginUrl= "https://tihcl.com/tihcl/api/auth/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AuthRequest> entity = new HttpEntity<>(new AuthRequest(userName,password),headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    loginUrl,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );
            String token = (String) response.getBody().get("token");

            HttpHeaders corpusHeaders = new HttpHeaders();
            corpusHeaders.setBearerAuth(token);

            HttpEntity<Void> corpusEntity = new HttpEntity<>(corpusHeaders);

            ResponseEntity<List<CorpusDebitFinancing>> corpusResponse = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    corpusEntity,
                    new ParameterizedTypeReference<List<CorpusDebitFinancing>>(){}
            );
            return  corpusResponse.getBody();
        } catch (RestClientException e) {
            System.err.println(e.getMessage());
            return   List.of();
        }
    }



}
