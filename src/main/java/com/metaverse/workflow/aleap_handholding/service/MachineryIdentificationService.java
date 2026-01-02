package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.repository.MachineryIdentificationRepository;
import com.metaverse.workflow.aleap_handholding.request_dto.MachineryIdentificationRequest;
import com.metaverse.workflow.aleap_handholding.response_dto.MachineryIdentificationResponse;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.aleap_handholding.HandholdingSupport;
import com.metaverse.workflow.model.aleap_handholding.MachineryIdentification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MachineryIdentificationService {

    private final HandholdingSupportService service;
    private final MachineryIdentificationRepository repository;

    public WorkflowResponse save(MachineryIdentificationRequest request)
            throws DataException {

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );

        MachineryIdentification entity =
                RequestMapper.mapToMachineryIdentification(request, support);

        MachineryIdentification saved = repository.save(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Machinery Identification saved successfully")
                .data(ResponseMapper.mapToMachineryIdentificationResponse(saved))
                .build();
    }

    @Transactional
    public WorkflowResponse update(Long id, MachineryIdentificationRequest request)
            throws DataException {

        MachineryIdentification existing = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Machinery Identification not found with id " + id,
                        "MACHINERY_NOT_FOUND",
                        400
                ));

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );

        existing.setHandholdingSupport(support);
        existing.setRequirement(request.getRequirement());
        existing.setExistingMachinery(request.getExistingMachinery());
        existing.setSuggestedMachinery(request.getSuggestedMachinery());
        existing.setManufacturer(request.getManufacturer());
        existing.setPlaceOfInstallation(request.getPlaceOfInstallation());
        existing.setCostOfMachinery(request.getCostOfMachinery());

        if (request.getGroundingDate() != null) {
            existing.setGroundingDate(
                    DateUtil.covertStringToDate(request.getGroundingDate())
            );
        }

        MachineryIdentification updated = repository.save(existing);

        return WorkflowResponse.builder()
                .status(200)
                .message("Machinery Identification updated successfully")
                .data(ResponseMapper.mapToMachineryIdentificationResponse(updated))
                .build();
    }

    /* ================= GET BY ID ================= */

    public WorkflowResponse getById(Long id) throws DataException {

        MachineryIdentification entity = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Machinery Identification not found with id " + id,
                        "MACHINERY_NOT_FOUND",
                        404
                ));

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(ResponseMapper.mapToMachineryIdentificationResponse(entity))
                .build();
    }

    /* ================= GET BY SUB ACTIVITY ================= */

    public WorkflowResponse getByNonTrainingSubActivityId(Long subActivityId) {

        List<MachineryIdentificationResponse> response = repository
                .findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId)
                .stream()
                .map(ResponseMapper::mapToMachineryIdentificationResponse)
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(response)
                .build();
    }

    public WorkflowResponse delete(Long id) throws DataException {

        MachineryIdentification entity = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Machinery Identification not found with id " + id,
                        "MACHINERY_NOT_FOUND",
                        400
                ));

        repository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Machinery Identification deleted successfully")
                .build();
    }
}
