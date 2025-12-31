package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.repository.GovtSchemeFinanceRepository;
import com.metaverse.workflow.aleap_handholding.request_dto.GovtSchemeFinanceRequest;
import com.metaverse.workflow.aleap_handholding.response_dto.GovtSchemeFinanceResponse;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.aleap_handholding.GovtSchemeFinance;
import com.metaverse.workflow.model.aleap_handholding.HandholdingSupport;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GovtSchemeFinanceService {

    private final HandholdingSupportService service;
    private final GovtSchemeFinanceRepository repository;


    public WorkflowResponse save(GovtSchemeFinanceRequest request) throws DataException {

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );

        GovtSchemeFinance entity =
                RequestMapper.mapToGovtSchemeFinance(request, support);

        GovtSchemeFinance saved = repository.save(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Govt Scheme Finance saved successfully")
                .data(ResponseMapper.mapToGovtSchemeFinanceResponse(saved))
                .build();
    }


    @Transactional
    public WorkflowResponse update(Long id, GovtSchemeFinanceRequest request)
            throws DataException {

        GovtSchemeFinance existing = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Govt Scheme Finance not found with id " + id,
                        "GOVT_SCHEME_NOT_FOUND",
                        400
                ));

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );
        existing.setHandholdingSupport(support);
        existing.setSchemeName(request.getSchemeName());
        existing.setStatus(request.getStatus());
        existing.setSanctionedAmount(request.getSanctionedAmount());
        existing.setDetails(request.getDetails());
        if (request.getSanctionDate() != null) {
            existing.setSanctionDate(DateUtil.covertStringToDate(request.getSanctionDate()));
        }

        GovtSchemeFinance updated = repository.save(existing);

        return WorkflowResponse.builder()
                .status(200)
                .message("Govt Scheme Finance updated successfully")
                .data(ResponseMapper.mapToGovtSchemeFinanceResponse(updated))
                .build();
    }


    public WorkflowResponse getById(Long id) throws DataException {

        GovtSchemeFinance entity = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Govt Scheme Finance not found with id " + id,
                        "GOVT_SCHEME_NOT_FOUND",
                        404
                ));

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(ResponseMapper.mapToGovtSchemeFinanceResponse(entity))
                .build();
    }

    public WorkflowResponse getByNonTrainingSubActivityId(Long subActivityId) {

        List<GovtSchemeFinanceResponse> response = repository
                .findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId)
                .stream()
                .map(ResponseMapper::mapToGovtSchemeFinanceResponse)
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(response)
                .build();
    }


    public WorkflowResponse delete(Long id) throws DataException {

        GovtSchemeFinance entity = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Govt Scheme Finance not found with id " + id,
                        "GOVT_SCHEME_NOT_FOUND",
                        400
                ));

        repository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Govt Scheme Finance deleted successfully")
                .build();
    }
}
