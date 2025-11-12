package com.metaverse.workflow.organization.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Sector;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import com.metaverse.workflow.sector.repository.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    @Cacheable("organizations")
    public WorkflowResponse getOrganizations() {
		List<Organization> organizationList = repository.findAllByOrderByOrganizationNameAsc();
		return WorkflowResponse.builder().message("Success").status(200).data(OrganizationResponseMapper.mapOrganization(organizationList)).build();
	}

	@Override
	@Cacheable("organizations")
	public WorkflowResponse getOrganizations(int page, int size) {
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<Organization> organizations = repository.findAll(pageRequest);
		List<OrganizationResponse> organizationResponses =
				OrganizationResponseMapper.mapOrganization(organizations.getContent());

		return WorkflowResponse.builder()
				.message("Success")
				.status(200)
				.data(organizationResponses)
				.totalPages(organizations.getTotalPages())
				.totalElements(organizations.getTotalElements())
				.build();
	}


	@Override
	public Boolean isMobileNumberExists(Long mobileNo) {
		List<Organization> organization = repository.findByContactNo(mobileNo);
		if(organization.isEmpty())return false;
		return true;
	}

    @Autowired
    public WorkflowResponse getAllOrganizations(){
        return WorkflowResponse.builder().message("Success").status(200).data(repository.getAllOrganizations()).build();
    }
}
