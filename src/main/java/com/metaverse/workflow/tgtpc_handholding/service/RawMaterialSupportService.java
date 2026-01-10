package com.metaverse.workflow.tgtpc_handholding.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.tgtpc_handholding.RawMaterialSupport;
import com.metaverse.workflow.model.tgtpc_handholding.TGTPCHandholdingSupport;
import com.metaverse.workflow.tgtpc_handholding.repository.RawMaterialSupportRepository;
import com.metaverse.workflow.tgtpc_handholding.request_dto.RawMaterialSupportRequest;
import com.metaverse.workflow.tgtpc_handholding.response_dto.RawMaterialSupportResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RawMaterialSupportService {

    private final TGTPCHandholdingMasterService tgtpcHandholdingMasterService;
    private final RawMaterialSupportRepository rawMaterialRepo;

    @Transactional
    public WorkflowResponse save(RawMaterialSupportRequest request) throws DataException {

        TGTPCHandholdingSupport support = tgtpcHandholdingMasterService.save(request.getTgtpcHandholdingSupportRequest());

        RawMaterialSupport entity = HandholdingRequestMapper.mapToRawMaterialSupport(request, support);
        entity = rawMaterialRepo.save(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Raw Material Support saved successfully")
                .data(HandholdingResponseMapper.mapToRawMaterialSupportRes(entity))
                .build();
    }

    @Transactional
    public WorkflowResponse update(Long id, RawMaterialSupportRequest request) throws DataException {

        RawMaterialSupport existing = rawMaterialRepo.findById(id).orElseThrow(() -> new DataException(
                "Raw Material Support not found with id " + id,
                "RAW_MATERIAL_NOT_FOUND",
                404
        ));

        TGTPCHandholdingSupport support = tgtpcHandholdingMasterService.update(
                existing.getTgtpcHandholdingSupport().getTgtpcHandholdingSupportId(),
                request.getTgtpcHandholdingSupportRequest()
        );

        existing.setTgtpcHandholdingSupport(support);
        existing.setRawMaterialDetails(request.getRawMaterialDetails() != null ? request.getRawMaterialDetails() : existing.getRawMaterialDetails());
        existing.setFirstDateOfSupply(request.getFirstDateOfSupply() != null ? DateUtil.covertStringToDate(request.getFirstDateOfSupply()) : existing.getFirstDateOfSupply());
        existing.setCost(request.getCost() != null ? request.getCost() : existing.getCost());

        RawMaterialSupport updated = rawMaterialRepo.save(existing);

        return WorkflowResponse.builder()
                .status(200)
                .message("Raw Material Support updated successfully")
                .data(HandholdingResponseMapper.mapToRawMaterialSupportRes(updated))
                .build();
    }

    public WorkflowResponse getById(Long id) throws DataException {
        RawMaterialSupport entity = rawMaterialRepo.findById(id).orElseThrow(() -> new DataException(
                "Raw Material Support not found with id " + id,
                "RAW_MATERIAL_NOT_FOUND",
                404
        ));

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(HandholdingResponseMapper.mapToRawMaterialSupportRes(entity))
                .build();
    }

    public WorkflowResponse getBySubActivityId(Long subActivityId) {
        List<RawMaterialSupportResponse> response = rawMaterialRepo
                .findByTgtpcHandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId)
                .stream()
                .map(HandholdingResponseMapper::mapToRawMaterialSupportRes)
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(response)
                .build();
    }

    @Transactional
    public WorkflowResponse delete(Long id) throws DataException {
        RawMaterialSupport entity = rawMaterialRepo.findById(id).orElseThrow(() -> new DataException(
                "Raw Material Support not found with id " + id,
                "RAW_MATERIAL_NOT_FOUND",
                404
        ));
        rawMaterialRepo.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Raw Material Support deleted successfully")
                .build();
    }
}
