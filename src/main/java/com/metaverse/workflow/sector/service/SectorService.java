package com.metaverse.workflow.sector.service;

import com.metaverse.workflow.common.response.WorkflowResponse;

public interface SectorService {

    WorkflowResponse saveSector(SectorRequest request);

    WorkflowResponse getAllSectors();
}
