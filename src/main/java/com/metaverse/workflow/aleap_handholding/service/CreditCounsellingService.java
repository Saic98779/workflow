package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.repository.CreditCounsellingRepository;
import com.metaverse.workflow.aleap_handholding.request_dto.CreditCounsellingRequest;
import com.metaverse.workflow.aleap_handholding.response_dto.CreditCounsellingResponse;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.aleap_handholding.CreditCounselling;
import com.metaverse.workflow.model.aleap_handholding.HandholdingSupport;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditCounsellingService {

    private final HandholdingSupportService service;
    private final CreditCounsellingRepository repository;


    public WorkflowResponse save(CreditCounsellingRequest request) throws DataException {

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );

        CreditCounselling entity =
                RequestMapper.mapToCreditCounselling(request, support);

        CreditCounselling saved = repository.save(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Credit Counselling saved successfully")
                .data(ResponseMapper.mapToCreditCounsellingResponse(saved))
                .build();
    }


    @Transactional
    public WorkflowResponse update(Long creditCounsellingId, CreditCounsellingRequest request)
            throws DataException {

        CreditCounselling existing = repository.findById(creditCounsellingId)
                .orElseThrow(() -> new DataException(
                        "Credit Counselling not found with id " + creditCounsellingId,
                        "CREDIT_COUNSELLING_NOT_FOUND",
                        400
                ));

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );

        existing.setHandholdingSupport(support);

        existing.setCounselledBy(request.getCounselledBy());
        existing.setSubjectDelivered(request.getSubjectDelivered());

        if (request.getCounsellingDate() != null) {
            existing.setCounsellingDate(
                    DateUtil.covertStringToDate(request.getCounsellingDate())
            );
        }

        CreditCounselling updated = repository.save(existing);

        return WorkflowResponse.builder()
                .status(200)
                .message("Credit Counselling updated successfully")
                .data(ResponseMapper.mapToCreditCounsellingResponse(updated))
                .build();
    }

    public WorkflowResponse getById(Long creditCounsellingId) throws DataException {

        CreditCounselling entity = repository.findById(creditCounsellingId)
                .orElseThrow(() -> new DataException(
                        "Credit Counselling not found with id " + creditCounsellingId,
                        "CREDIT_COUNSELLING_NOT_FOUND",
                        404
                ));

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(ResponseMapper.mapToCreditCounsellingResponse(entity))
                .build();
    }

    public WorkflowResponse getByNonTrainingSubActivityId(Long subActivityId) {

        List<CreditCounsellingResponse> response = repository
                .findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId)
                .stream()
                .map(ResponseMapper::mapToCreditCounsellingResponse)
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(response)
                .build();
    }

    public WorkflowResponse delete(Long creditCounsellingId) throws DataException {

        CreditCounselling entity = repository.findById(creditCounsellingId)
                .orElseThrow(() -> new DataException(
                        "Credit Counselling not found with id " + creditCounsellingId,
                        "CREDIT_COUNSELLING_NOT_FOUND",
                        400
                ));

        repository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Credit Counselling deleted successfully")
                .build();
    }
}
