package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.repository.TradeFairParticipationRepository;
import com.metaverse.workflow.aleap_handholding.request_dto.TradeFairParticipationRequest;
import com.metaverse.workflow.aleap_handholding.response_dto.TradeFairParticipationResponse;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.aleap_handholding.HandholdingSupport;
import com.metaverse.workflow.model.aleap_handholding.TradeFairParticipation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeFairParticipationService {

    private final HandholdingSupportService service;
    private final TradeFairParticipationRepository repository;

    public WorkflowResponse save(TradeFairParticipationRequest request)
            throws DataException {

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );

        TradeFairParticipation entity =
                RequestMapper.mapToTradeFairParticipation(request, support);

        TradeFairParticipation saved = repository.save(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Trade Fair Participation saved successfully")
                .data(ResponseMapper.mapToTradeFairParticipationResponse(saved))
                .build();
    }

    @Transactional
    public WorkflowResponse update(Long id, TradeFairParticipationRequest request)
            throws DataException {

        TradeFairParticipation existing = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Trade Fair Participation not found with id " + id,
                        "TRADE_FAIR_NOT_FOUND",
                        404
                ));

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );
        existing.setHandholdingSupport(support);
        existing.setEventLocation(request.getEventLocation());
        existing.setOrganizedBy(request.getOrganizedBy());
        if (request.getEventType() != null) {
            existing.setEventType(request.getEventType());
        }
        if (request.getEventDate() != null) {
            existing.setEventDate(DateUtil.covertStringToDate(request.getEventDate()));
        }
        TradeFairParticipation updated = repository.save(existing);
        return WorkflowResponse.builder()
                .status(200)
                .message("Trade Fair Participation updated successfully")
                .data(ResponseMapper.mapToTradeFairParticipationResponse(updated))
                .build();
    }

    public WorkflowResponse getById(Long id) throws DataException {

        TradeFairParticipation entity = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Trade Fair Participation not found with id " + id,
                        "TRADE_FAIR_NOT_FOUND",
                        404
                ));

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(ResponseMapper.mapToTradeFairParticipationResponse(entity))
                .build();
    }

    public WorkflowResponse getByNonTrainingSubActivityId(Long subActivityId) {

        List<TradeFairParticipationResponse> response = repository
                .findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId)
                .stream()
                .map(ResponseMapper::mapToTradeFairParticipationResponse)
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(response)
                .build();
    }

    public WorkflowResponse delete(Long id) throws DataException {

        TradeFairParticipation entity = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Trade Fair Participation not found with id " + id,
                        "TRADE_FAIR_NOT_FOUND",
                        404
                ));

        repository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Trade Fair Participation deleted successfully")
                .build();
    }
}
