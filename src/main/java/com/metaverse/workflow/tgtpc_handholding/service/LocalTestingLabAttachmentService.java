package com.metaverse.workflow.tgtpc_handholding.service;


import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.tgtpc_handholding.LocalTestingLabAttachment;
import com.metaverse.workflow.model.tgtpc_handholding.TGTPCHandholdingSupport;
import com.metaverse.workflow.tgtpc_handholding.repository.LocalTestingLabAttachmentRepository;
import com.metaverse.workflow.tgtpc_handholding.request_dto.LocalTestingLabAttachmentRequest;
import com.metaverse.workflow.tgtpc_handholding.response_dto.LocalTestingLabAttachmentResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocalTestingLabAttachmentService {

    private final TGTPCHandholdingMasterService tgtpcHandholdingMasterService;
    private final LocalTestingLabAttachmentRepository attachmentRepo;

    @Transactional
    public WorkflowResponse save(LocalTestingLabAttachmentRequest request) throws DataException {

        TGTPCHandholdingSupport support = tgtpcHandholdingMasterService.save(request.getTgtpcHandholdingSupportRequest());

        LocalTestingLabAttachment entity = HandholdingRequestMapper.mapToLocalTestingLabAttachment(request, support);
        entity = attachmentRepo.save(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Local Testing Lab Attachment saved successfully")
                .data(HandholdingResponseMapper.mapToLocalTestingLabAttachmentRes(entity))
                .build();
    }

    @Transactional
    public WorkflowResponse update(Long id, LocalTestingLabAttachmentRequest request) throws DataException {

        LocalTestingLabAttachment existing = attachmentRepo.findById(id).orElseThrow(() -> new DataException(
                "Local Testing Lab Attachment not found with id " + id,
                "LAB_ATTACHMENT_NOT_FOUND",
                404
        ));

        TGTPCHandholdingSupport support = tgtpcHandholdingMasterService.update(
                existing.getTgtpcHandholdingSupport().getTgtpcHandholdingSupportId(),
                request.getTgtpcHandholdingSupportRequest()
        );

        existing.setTgtpcHandholdingSupport(support);
        existing.setLabOrCfcName(request.getLabOrCfcName() != null ? request.getLabOrCfcName() : existing.getLabOrCfcName());
        existing.setPurposeOfAttachment(request.getPurposeOfAttachment() != null ? request.getPurposeOfAttachment() : existing.getPurposeOfAttachment());
        existing.setDateOfAttachment(request.getDateOfAttachment() != null ? DateUtil.covertStringToDate(request.getDateOfAttachment()) : existing.getDateOfAttachment());

        LocalTestingLabAttachment updated = attachmentRepo.save(existing);

        return WorkflowResponse.builder()
                .status(200)
                .message("Local Testing Lab Attachment updated successfully")
                .data(HandholdingResponseMapper.mapToLocalTestingLabAttachmentRes(updated))
                .build();
    }

    public WorkflowResponse getById(Long id) throws DataException {
        LocalTestingLabAttachment entity = attachmentRepo.findById(id).orElseThrow(() -> new DataException(
                "Local Testing Lab Attachment not found with id " + id,
                "LAB_ATTACHMENT_NOT_FOUND",
                404
        ));

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(HandholdingResponseMapper.mapToLocalTestingLabAttachmentRes(entity))
                .build();
    }

    public WorkflowResponse getBySubActivityId(Long subActivityId) {
        List<LocalTestingLabAttachmentResponse> response = attachmentRepo
                .findByTgtpcHandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId)
                .stream()
                .map(HandholdingResponseMapper::mapToLocalTestingLabAttachmentRes)
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(response)
                .build();
    }

    @Transactional
    public WorkflowResponse delete(Long id) throws DataException {
        LocalTestingLabAttachment entity = attachmentRepo.findById(id).orElseThrow(() -> new DataException(
                "Local Testing Lab Attachment not found with id " + id,
                "LAB_ATTACHMENT_NOT_FOUND",
                404
        ));
        attachmentRepo.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Local Testing Lab Attachment deleted successfully")
                .build();
    }
}

