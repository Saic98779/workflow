package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.request_dto.FormalisationComplianceRequest;
import com.metaverse.workflow.common.response.WorkflowResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FormalisationComplianceService {

    WorkflowResponse create(FormalisationComplianceRequest request, MultipartFile file);

    WorkflowResponse update(Long id, FormalisationComplianceRequest request, MultipartFile file);

    WorkflowResponse getById(Long id);

    WorkflowResponse delete(Long id);

    WorkflowResponse getByNonTrainingSubActivityId(Long subActivityId);

}
