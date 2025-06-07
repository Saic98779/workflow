package com.metaverse.workflow.programrawmaterial.service;

import com.metaverse.workflow.common.response.WorkflowResponse;

public interface ProgramRawMaterialService {
    WorkflowResponse rawMaterialByProgramId(Long programId, int page, int size);
    WorkflowResponse updateProgramRawMaterial(ProgramRawMaterialRequest request);
}
