package com.metaverse.workflow.tgtpc_handholding.service;


import com.metaverse.workflow.tgtpc_handholding.repository.TGTPCNTReportsRepository;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.tgtpc_handholding.TGTPCNTReports;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import com.metaverse.workflow.tgtpc_handholding.request_dto.TGTPCNTReportsRequest;
import com.metaverse.workflow.tgtpc_handholding.response_dto.TGTPCNTReportsResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TGTPCNTReportsService {

    private final TGTPCNTReportsRepository repository;
    private final NonTrainingSubActivityRepository subActivityRepo;
    public WorkflowResponse save(TGTPCNTReportsRequest request) throws DataException {



       NonTrainingSubActivity subActivity= subActivityRepo.findById(request.getNonTrainingSubActivityId())
                .orElseThrow(() ->
                        new DataException("SubActivity not found with id " + request.getNonTrainingSubActivityId(),
                                "SUB_ACTIVITY_NOT_FOUND",
                                400
                        ));
        TGTPCNTReports entity = HandholdingRequestMapper.mapToEntity(request,subActivity);
        TGTPCNTReports saved = repository.save(entity);
        return WorkflowResponse.builder()
                .status(200)
                .message("TGTPC NT Report saved successfully")
                .data(HandholdingResponseMapper.mapToResponse(saved))
                .build();
    }

    @Transactional
    public WorkflowResponse update(Long reportId, TGTPCNTReportsRequest request) throws DataException {

        TGTPCNTReports existing = repository.findById(reportId)
                .orElseThrow(() -> new DataException(
                        "TGTPC NT Report not found with id " + reportId,
                        "TGTPC_REPORT_NOT_FOUND",
                        404
                ));

        HandholdingRequestMapper.updateEntity(existing, request);
        TGTPCNTReports updated = repository.save(existing);

        return WorkflowResponse.builder()
                .status(200)
                .message("TGTPC NT Report updated successfully")
                .data(HandholdingResponseMapper.mapToResponse(updated))
                .build();
    }

    public WorkflowResponse getById(Long reportId) throws DataException {

        TGTPCNTReports entity = repository.findById(reportId)
                .orElseThrow(() -> new DataException(
                        "TGTPC NT Report not found with id " + reportId,
                        "TGTPC_REPORT_NOT_FOUND",
                        404
                ));

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(HandholdingResponseMapper.mapToResponse(entity))
                .build();
    }

    public WorkflowResponse getBySubActivityId(Long subActivityId) {

        List<TGTPCNTReportsResponse> response = repository.findByNonTrainingSubActivity_SubActivityId(subActivityId)
                .stream()
                .map(HandholdingResponseMapper::mapToResponse)
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(response)
                .build();
    }

    public WorkflowResponse delete(Long reportId) throws DataException {

        TGTPCNTReports entity = repository.findById(reportId)
                .orElseThrow(() -> new DataException(
                        "TGTPC NT Report not found with id " + reportId,
                        "TGTPC_REPORT_NOT_FOUND",
                        404
                ));

        repository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("TGTPC NT Report deleted successfully")
                .build();
    }
}