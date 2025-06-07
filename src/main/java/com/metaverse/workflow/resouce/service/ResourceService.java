package com.metaverse.workflow.resouce.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.Resource;

import java.util.List;

public interface ResourceService {
	
	public ResourceResponse saveResource(ResourceRequest resourceRequest);
	public WorkflowResponse getResources();
	List<Resource> getResourceEntities();
	WorkflowResponse updateResources(ResourceRequest resourceRequest,Long resourceId) throws DataException;
	WorkflowResponse deleteResources(Long resourceId) throws DataException;

}
