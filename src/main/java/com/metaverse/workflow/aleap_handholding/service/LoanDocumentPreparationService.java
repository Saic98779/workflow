package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.repository.LoanDocumentPreparationRepository;
import com.metaverse.workflow.aleap_handholding.request_dto.LoanDocumentPreparationRequest;
import com.metaverse.workflow.aleap_handholding.response_dto.LoanDocumentPreparationResponse;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.aleap_handholding.HandholdingSupport;
import com.metaverse.workflow.model.aleap_handholding.LoanDocumentPreparation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanDocumentPreparationService {

    private final HandholdingSupportService service;
    private final LoanDocumentPreparationRepository repository;

    public WorkflowResponse save(LoanDocumentPreparationRequest request)
            throws DataException {

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );

        LoanDocumentPreparation entity =
                RequestMapper.mapToLoanDocumentPreparation(request, support);

        LoanDocumentPreparation saved = repository.save(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Loan Document Preparation saved successfully")
                .data(ResponseMapper.mapToLoanDocumentPreparationResponse(saved))
                .build();
    }

    @Transactional
    public WorkflowResponse update(Long id, LoanDocumentPreparationRequest request)
            throws DataException {

        LoanDocumentPreparation existing = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Loan Document Preparation not found with id " + id,
                        "LOAN_DOC_NOT_FOUND",
                        400
                ));

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );

        existing.setHandholdingSupport(support);
        existing.setDetails(request.getDetails());

        LoanDocumentPreparation updated = repository.save(existing);

        return WorkflowResponse.builder()
                .status(200)
                .message("Loan Document Preparation updated successfully")
                .data(ResponseMapper.mapToLoanDocumentPreparationResponse(updated))
                .build();
    }

    public WorkflowResponse getById(Long id) throws DataException {

        LoanDocumentPreparation entity = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Loan Document Preparation not found with id " + id,
                        "LOAN_DOC_NOT_FOUND",
                        404
                ));

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(ResponseMapper.mapToLoanDocumentPreparationResponse(entity))
                .build();
    }

    public WorkflowResponse getByNonTrainingSubActivityId(Long subActivityId) {

        List<LoanDocumentPreparationResponse> response = repository
                .findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId)
                .stream()
                .map(ResponseMapper::mapToLoanDocumentPreparationResponse)
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(response)
                .build();
    }

    public WorkflowResponse delete(Long id) throws DataException {

        LoanDocumentPreparation entity = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Loan Document Preparation not found with id " + id,
                        "LOAN_DOC_NOT_FOUND",
                        400
                ));

        repository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Loan Document Preparation deleted successfully")
                .build();
    }
}
