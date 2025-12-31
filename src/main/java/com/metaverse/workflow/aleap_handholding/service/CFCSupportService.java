package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.repository.CFCSupportRepository;
import com.metaverse.workflow.aleap_handholding.request_dto.CFCSupportRequest;
import com.metaverse.workflow.aleap_handholding.response_dto.CFCSupportResponse;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.aleap_handholding.CFCSupport;
import com.metaverse.workflow.model.aleap_handholding.HandholdingSupport;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CFCSupportService {

    private final HandholdingSupportService service;
    private final CFCSupportRepository repository;

    public WorkflowResponse save(CFCSupportRequest request)
            throws DataException {

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );

        CFCSupport entity =
                RequestMapper.mapToCFCSupport(request, support);

        CFCSupport saved = repository.save(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("CFC Support saved successfully")
                .data(ResponseMapper.mapToCFCSupportResponse(saved))
                .build();
    }

    @Transactional
    public WorkflowResponse update(Long id, CFCSupportRequest request) throws DataException {

        CFCSupport existing = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "CFC Support not found with id " + id,
                        "CFC_SUPPORT_NOT_FOUND",
                        400
                ));
        existing.setVendorName(request.getVendorName());
        existing.setVendorContactNo(request.getVendorContactNo());
        existing.setVendorEmail(request.getVendorEmail());
        existing.setApproxCost(request.getApproxCost());

        CFCSupport updated = repository.save(existing);

        return WorkflowResponse.builder()
                .status(200)
                .message("CFC Support updated successfully")
                .data(ResponseMapper.mapToCFCSupportResponse(updated))
                .build();
    }

    public WorkflowResponse getById(Long id) throws DataException {

        CFCSupport entity = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "CFC Support not found with id " + id,
                        "CFC_SUPPORT_NOT_FOUND",
                        404
                ));

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(ResponseMapper.mapToCFCSupportResponse(entity))
                .build();
    }


    public WorkflowResponse getByNonTrainingSubActivityId(Long subActivityId) {

        List<CFCSupportResponse> response = repository
                .findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId)
                .stream()
                .map(ResponseMapper::mapToCFCSupportResponse)
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(response)
                .build();
    }

      public WorkflowResponse delete(Long id) throws DataException {

        CFCSupport entity = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "CFC Support not found with id " + id,
                        "CFC_SUPPORT_NOT_FOUND",
                        400
                ));

        repository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("CFC Support deleted successfully")
                .build();
    }
}
