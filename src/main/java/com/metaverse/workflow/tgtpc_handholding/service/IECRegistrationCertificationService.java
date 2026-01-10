package com.metaverse.workflow.tgtpc_handholding.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.tgtpc_handholding.IECRegistrationCertification;
import com.metaverse.workflow.model.tgtpc_handholding.TGTPCHandholdingSupport;
import com.metaverse.workflow.tgtpc_handholding.repository.IECRegistrationCertificationRepository;
import com.metaverse.workflow.tgtpc_handholding.request_dto.IECRegistrationCertificationRequest;
import com.metaverse.workflow.tgtpc_handholding.response_dto.IECRegistrationCertificationResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IECRegistrationCertificationService {

    private final TGTPCHandholdingMasterService tgtpcHandholdingMasterService;
    private final IECRegistrationCertificationRepository iecRepo;

    @Transactional
    public WorkflowResponse save(IECRegistrationCertificationRequest request) throws DataException {

        TGTPCHandholdingSupport support = tgtpcHandholdingMasterService.save(request.getTgtpcHandholdingSupportRequest());
        IECRegistrationCertification entity =
                HandholdingRequestMapper.mapToIECRegistrationCertification(request, support);
        entity = iecRepo.save(entity);
        return WorkflowResponse.builder()
                .status(200)
                .message("IEC Registration/Certification saved successfully")
                .data(HandholdingResponseMapper.mapToIECRegistrationCertificationRes(entity))
                .build();
    }

    @Transactional
    public WorkflowResponse update(Long id, IECRegistrationCertificationRequest request) throws DataException {

        IECRegistrationCertification existing = iecRepo.findById(id).orElseThrow(() -> new DataException(
                "IEC Record not found with id " + id,
                "IEC_RECORD_NOT_FOUND",
                404
        ));
        TGTPCHandholdingSupport support = tgtpcHandholdingMasterService.update(
                existing.getTgtpcHandholdingSupport().getTgtpcHandholdingSupportId(),
                request.getTgtpcHandholdingSupportRequest());

        existing.setTgtpcHandholdingSupport(support);
        existing.setSupportType(request.getSupportType());
        existing.setIecRegistrationNumber(request.getIecRegistrationNumber());
        existing.setRegistrationDate(DateUtil.covertStringToDate(request.getRegistrationDate()));
        existing.setCertificationName(request.getCertificationName());
        existing.setCertificateNumber(request.getCertificateNumber());
        existing.setCertificateDate(DateUtil.covertStringToDate(request.getCertificateDate()));

        IECRegistrationCertification updated = iecRepo.save(existing);

        return WorkflowResponse.builder()
                .status(200)
                .message("IEC Registration/Certification updated successfully")
                .data(HandholdingResponseMapper.mapToIECRegistrationCertificationRes(updated))
                .build();
    }

    public WorkflowResponse getById(Long id) throws DataException {
        IECRegistrationCertification entity = iecRepo.findById(id)
                .orElseThrow(() -> new DataException(
                        "IEC Record not found with id " + id,
                        "IEC_RECORD_NOT_FOUND",
                        404
                ));
        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(HandholdingResponseMapper.mapToIECRegistrationCertificationRes(entity))
                .build();
    }

    public WorkflowResponse getBySubActivityId(Long subActivityId) {
        List<IECRegistrationCertificationResponse> response =
                iecRepo.findByTgtpcHandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId)
                        .stream()
                        .map(HandholdingResponseMapper::mapToIECRegistrationCertificationRes)
                        .toList();
        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(response)
                .build();
    }

    @Transactional
    public WorkflowResponse delete(Long id) throws DataException {
        IECRegistrationCertification entity = iecRepo.findById(id).orElseThrow(() -> new DataException(
                "IEC Record not found with id " + id,
                "IEC_RECORD_NOT_FOUND",
                404
        ));
        iecRepo.delete(entity);
        return WorkflowResponse.builder()
                .status(200)
                .message("IEC Registration/Certification deleted successfully")
                .build();
    }
}
