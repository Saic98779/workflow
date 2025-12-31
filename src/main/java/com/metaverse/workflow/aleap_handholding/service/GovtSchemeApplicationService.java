package com.metaverse.workflow.aleap_handholding.service;


import com.metaverse.workflow.aleap_handholding.repository.GovtSchemeApplicationRepository;
import com.metaverse.workflow.aleap_handholding.request_dto.GovtSchemeApplicationRequest;
import com.metaverse.workflow.aleap_handholding.response_dto.GovtSchemeApplicationResponse;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.NonTrainingActivity;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import com.metaverse.workflow.organization.repository.OrganizationRepository;

import com.metaverse.workflow.model.aleap_handholding.GovtSchemeApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GovtSchemeApplicationService {

    private final GovtSchemeApplicationRepository repository;
    private final OrganizationRepository organizationRepo;
    private final NonTrainingSubActivityRepository subActivityRepo;

    public WorkflowResponse save(GovtSchemeApplicationRequest request) throws DataException {
        Organization org = organizationRepo.findById(request.getOrganizationId())
                .orElseThrow(() -> new DataException("Organization not found", "ORG_NOT_FOUND", 400));
        NonTrainingSubActivity subActivity = subActivityRepo.findById(request.getNonTrainingSubActivityId())
                .orElseThrow(() -> new DataException("SubActivity not found", "SUB_ACTIVITY_NOT_FOUND", 400));
        NonTrainingActivity activity = subActivity.getNonTrainingActivity();

        GovtSchemeApplication entity = RequestMapper.mapToGovtSchemeApplication(request, activity, subActivity, org);
        GovtSchemeApplication saved = repository.save(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Govt Scheme Application saved successfully")
                .data(ResponseMapper.mapToGovtSchemeApplicationResponse(saved))
                .build();
    }

    public WorkflowResponse update(Long id, GovtSchemeApplicationRequest request) throws DataException {
        GovtSchemeApplication existing = repository.findById(id)
                .orElseThrow(() -> new DataException("Application not found", "APP_NOT_FOUND", 400));

        Organization org = organizationRepo.findById(request.getOrganizationId())
                .orElseThrow(() -> new DataException("Organization not found", "ORG_NOT_FOUND", 400));

         // Directly update fields using setters
        existing.setOrganization(org);
        existing.setApplicationNo(request.getApplicationNo());
        existing.setStatus(request.getStatus());
        existing.setApplicationDate(request.getApplicationDate() != null
                ? DateUtil.covertStringToDate(request.getApplicationDate())
                : null);
        existing.setTime(request.getTime());
        existing.setSanctionDetails(request.getSanctionDetails());
        existing.setSanctionDate(request.getSanctionDate() != null
                ? DateUtil.covertStringToDate(request.getSanctionDate())
                : null);
        existing.setSanctionedAmount(request.getSanctionedAmount());
        existing.setDetails(request.getDetails());

        // Save updated entity
        GovtSchemeApplication updated = repository.save(existing);

        return WorkflowResponse.builder()
                .status(200)
                .message("Govt Scheme Application updated successfully")
                .data(ResponseMapper.mapToGovtSchemeApplicationResponse(updated))
                .build();
    }


    public WorkflowResponse getById(Long id) throws DataException {
        GovtSchemeApplication entity = repository.findById(id)
                .orElseThrow(() -> new DataException("Application not found", "APP_NOT_FOUND", 404));

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(ResponseMapper.mapToGovtSchemeApplicationResponse(entity))
                .build();
    }

    public WorkflowResponse delete(Long id) throws DataException {
        GovtSchemeApplication entity = repository.findById(id)
                .orElseThrow(() -> new DataException("Application not found", "APP_NOT_FOUND", 404));

        repository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Application deleted successfully")
                .build();
    }

    public WorkflowResponse getBySubActivityId(Long subActivityId) {
        List<GovtSchemeApplicationResponse> response = repository
                .findByNonTrainingSubActivity_SubActivityId(subActivityId)
                .stream()
                .map(ResponseMapper::mapToGovtSchemeApplicationResponse)
                .collect(Collectors.toList());

        return WorkflowResponse.builder()
                .status(200)
                .message("Applications fetched successfully")
                .data(response)
                .build();
    }
}
