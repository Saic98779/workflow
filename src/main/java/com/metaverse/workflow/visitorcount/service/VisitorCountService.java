package com.metaverse.workflow.visitorcount.service;

import com.metaverse.workflow.common.response.WorkflowResponse;

public interface VisitorCountService {

    /**
     * Get current visitor count
     * @return response with current visitor count
     */
    WorkflowResponse getVisitorCount();

    /**
     * Update visitor count to a specific value
     * @param request the visitor count request with new total
     * @return response with updated visitor count
     */
    WorkflowResponse updateVisitorCount(VisitorCountRequest request);

}

