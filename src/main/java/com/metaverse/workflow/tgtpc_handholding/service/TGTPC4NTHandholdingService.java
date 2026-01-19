package com.metaverse.workflow.tgtpc_handholding.service;


import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import com.metaverse.workflow.tgtpc_handholding.repository.TGTPC4NTHandholdingRepository;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.tgtpc_handholding.TGTPC4NTHandholding;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import com.metaverse.workflow.tgtpc_handholding.request_dto.TGTPC4NTHandholdingRequest;
import com.metaverse.workflow.tgtpc_handholding.response_dto.TGTPC4NTHandholdingResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TGTPC4NTHandholdingService {

    private final TGTPC4NTHandholdingRepository repository;
    private final NonTrainingSubActivityRepository subActivityRepo;
    private final OrganizationRepository organizationRepo;

    public WorkflowResponse save(TGTPC4NTHandholdingRequest request) throws DataException {
        NonTrainingSubActivity subActivity = subActivityRepo.findById(
                request.getNonTrainingSubActivityId()
        ).orElseThrow(() ->
                new DataException(
                        "SubActivity not found with id " + request.getNonTrainingSubActivityId(),
                        "SUB_ACTIVITY_NOT_FOUND",
                        400
                )
        );
        Organization organization = organizationRepo.findById(request.getOrganizationId())
                .orElseThrow(() -> new DataException(
                        "Organization not found with id " + request.getOrganizationId(),
                        "ORG_NOT_FOUND",
                        400
                ));
        TGTPC4NTHandholding entity = HandholdingRequestMapper.mapToEntity(request, subActivity,organization);
        TGTPC4NTHandholding saved = repository.save(entity);
        return WorkflowResponse.builder()
                .status(200)
                .message("TGTPC4 NT Handholding saved successfully")
                .data(HandholdingResponseMapper.mapToResponse(saved))
                .build();
    }

    @Transactional
    public WorkflowResponse update(Long handholdingId, TGTPC4NTHandholdingRequest request) throws DataException {
        TGTPC4NTHandholding existing = repository.findById(handholdingId)
                .orElseThrow(() -> new DataException(
                        "TGTPC4 NT Handholding not found with id " + handholdingId,
                        "TGTPC4_HANDHOLDING_NOT_FOUND",
                        404
                ));
        HandholdingRequestMapper.updateEntity(existing, request);
        TGTPC4NTHandholding updated = repository.save(existing);
        return WorkflowResponse.builder()
                .status(200)
                .message("TGTPC4 NT Handholding updated successfully")
                .data(HandholdingResponseMapper.mapToResponse(updated))
                .build();
    }

    public WorkflowResponse getById(Long handholdingId) throws DataException {
        TGTPC4NTHandholding entity = repository.findById(handholdingId)
                .orElseThrow(() -> new DataException(
                        "TGTPC4 NT Handholding not found with id " + handholdingId,
                        "TGTPC4_HANDHOLDING_NOT_FOUND",
                        404
                ));
        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(HandholdingResponseMapper.mapToResponse(entity))
                .build();
    }

    public WorkflowResponse getBySubActivityId(Long subActivityId) {
        List<TGTPC4NTHandholdingResponse> response =
                repository.findByNonTrainingSubActivity_SubActivityId(subActivityId)
                        .stream()
                        .map(HandholdingResponseMapper::mapToResponse)
                        .toList();
        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(response)
                .build();
    }

    public WorkflowResponse delete(Long handholdingId) throws DataException {
        TGTPC4NTHandholding entity = repository.findById(handholdingId)
                .orElseThrow(() -> new DataException(
                        "TGTPC4 NT Handholding not found with id " + handholdingId,
                        "TGTPC4_HANDHOLDING_NOT_FOUND",
                        404
                ));
        repository.delete(entity);
        return WorkflowResponse.builder()
                .status(200)
                .message("TGTPC4 NT Handholding deleted successfully")
                .build();
    }
}