package com.metaverse.workflow.organization.service;

import java.util.List;
import java.util.Optional;

import com.metaverse.workflow.agency.service.AgencyResponseMapper;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.Sector;
import com.metaverse.workflow.resouce.service.ResourceResponse;
import com.metaverse.workflow.sector.repository.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.organization.repository.OrganizationRepository;

@Service
public class OrganizationServiceImpl implements OrganizationService {

	@Autowired
	private OrganizationRepository repository;
	@Autowired
	private SectorRepository sectorRepository;
	@Override
	public WorkflowResponse saveOrganization(OrganizationRequest organizationRequest) {
		List<Sector> sectors = sectorRepository.findAllById(organizationRequest.getSectorIds());
		if(sectors.size()!=organizationRequest.getSectorIds().size())
			return WorkflowResponse.builder().message("Some sectors are not found").status(400).build();
		Organization organization = OrganizationRequestMapper.map(organizationRequest,sectors);
		//Organization organization = OrganizationRequestMapper.map(organizationRequest);
		Organization SavedOrganization = repository.save(organization);
		OrganizationResponse response= OrganizationResponseMapper.map(SavedOrganization);
		return WorkflowResponse.builder().message("Oraganization saved successfully").status(200).data(response).build();
	}
	@Override
	public Optional<Organization> getOrganizationById(Long organizationId) {
		return repository.findById(organizationId);
	}

	@Override
	public WorkflowResponse getOrganizations() {
		List<Organization> organizationList = repository.findAllByOrderByOrganizationNameAsc();
		List<OrganizationResponse> response = OrganizationResponseMapper.mapOrganization(organizationList);
		return WorkflowResponse.builder().message("Success").status(200).data(response).build();
	}

	@Override
	public Boolean isMobileNumberExists(Long mobileNo) {
		List<Organization> organization = repository.findByContactNo(mobileNo);
		if(organization.isEmpty())return false;
		return true;
	}

}
