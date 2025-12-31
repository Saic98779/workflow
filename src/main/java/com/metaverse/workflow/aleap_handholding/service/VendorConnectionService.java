package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.repository.VendorConnectionRepository;
import com.metaverse.workflow.aleap_handholding.request_dto.VendorConnectionRequest;
import com.metaverse.workflow.aleap_handholding.response_dto.VendorConnectionResponse;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.aleap_handholding.HandholdingSupport;
import com.metaverse.workflow.model.aleap_handholding.VendorConnection;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataUnit;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorConnectionService {

    private final HandholdingSupportService service;
    private final VendorConnectionRepository repository;


    public WorkflowResponse save(VendorConnectionRequest request) throws DataException {

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );

        VendorConnection entity =
                RequestMapper.mapToVendorConnection(request, support);

        VendorConnection saved = repository.save(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Vendor Connection saved successfully")
                .data(ResponseMapper.mapToVendorConnectionResponse(saved))
                .build();
    }

    @Transactional
    public WorkflowResponse update(Long id, VendorConnectionRequest request)
            throws DataException {

        VendorConnection existing = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Vendor Connection not found with id " + id,
                        "VENDOR_CONN_NOT_FOUND",
                        400
                ));

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );

        existing.setHandholdingSupport(support);
        existing.setVendorSuggested(request.getVendorSuggested());
        existing.setDetails(request.getDetails());
        existing.setCost(request.getCost());

        if (request.getQuotationDate() != null) {
            existing.setQuotationDate(
                    DateUtil.covertStringToDate(request.getQuotationDate())
            );
        }

        VendorConnection updated = repository.save(existing);

        return WorkflowResponse.builder()
                .status(200)
                .message("Vendor Connection updated successfully")
                .data(ResponseMapper.mapToVendorConnectionResponse(updated))
                .build();
    }

    public WorkflowResponse getById(Long id) throws DataException {

        VendorConnection entity = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Vendor Connection not found with id " + id,
                        "VENDOR_CONN_NOT_FOUND",
                        404
                ));

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(ResponseMapper.mapToVendorConnectionResponse(entity))
                .build();
    }

    public WorkflowResponse getByNonTrainingSubActivityId(Long subActivityId) {

        List<VendorConnectionResponse> response = repository
                .findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId)
                .stream()
                .map(ResponseMapper::mapToVendorConnectionResponse)
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(response)
                .build();
    }

    public WorkflowResponse delete(Long id) throws DataException {

        VendorConnection entity = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Vendor Connection not found with id " + id,
                        "VENDOR_CONN_NOT_FOUND",
                        400
                ));

        repository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Vendor Connection deleted successfully")
                .build();
    }
}
