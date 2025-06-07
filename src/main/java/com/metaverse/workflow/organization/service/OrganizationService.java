package com.metaverse.workflow.organization.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.Organization;

import java.util.List;
import java.util.Optional;

public interface OrganizationService {
	
	public WorkflowResponse saveOrganization(OrganizationRequest organizationRequest);
	
	public Optional<Organization> getOrganizationById(Long organizationId);

	public WorkflowResponse getOrganizations();

	Boolean isMobileNumberExists(Long mobileNo);
}
