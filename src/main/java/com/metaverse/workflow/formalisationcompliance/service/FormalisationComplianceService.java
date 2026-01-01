package com.metaverse.workflow.formalisationcompliance.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.formalisationcompliance.dto.FormalisationComplianceRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;

@Service
public interface FormalisationComplianceService {

    WorkflowResponse create(FormalisationComplianceRequest request, MultipartFile file);

    WorkflowResponse update(Long id, FormalisationComplianceRequest request, MultipartFile file);

    WorkflowResponse getById(Long id);

    WorkflowResponse delete(Long id);

    WorkflowResponse getByNonTrainingSubActivityId(Long subActivityId);

}
