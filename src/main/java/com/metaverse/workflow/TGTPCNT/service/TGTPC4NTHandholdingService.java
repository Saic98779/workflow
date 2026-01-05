package com.metaverse.workflow.TGTPCNT.service;


import com.metaverse.workflow.TGTPCNT.repository.TGTPC4NTHandholdingRepository;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.TGTPC4NTHandholding;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TGTPC4NTHandholdingService {

    private final TGTPC4NTHandholdingRepository repository;
    private final NonTrainingSubActivityRepository subActivityRepo;

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
        TGTPC4NTHandholding entity = TGTPC4NTHandholdingMapper.mapToEntity(request, subActivity);
        TGTPC4NTHandholding saved = repository.save(entity);
        return WorkflowResponse.builder()
                .status(200)
                .message("TGTPC4 NT Handholding saved successfully")
                .data(TGTPC4NTHandholdingMapper.mapToResponse(saved))
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
        TGTPC4NTHandholdingMapper.updateEntity(existing, request);
        TGTPC4NTHandholding updated = repository.save(existing);
        return WorkflowResponse.builder()
                .status(200)
                .message("TGTPC4 NT Handholding updated successfully")
                .data(TGTPC4NTHandholdingMapper.mapToResponse(updated))
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
                .data(TGTPC4NTHandholdingMapper.mapToResponse(entity))
                .build();
    }

    public WorkflowResponse getBySubActivityId(Long subActivityId) {
        List<TGTPC4NTHandholdingResponse> response =
                repository.findByNonTrainingSubActivity_SubActivityId(subActivityId)
                        .stream()
                        .map(TGTPC4NTHandholdingMapper::mapToResponse)
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