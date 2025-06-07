package com.metaverse.workflow.resouce.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.metaverse.workflow.agency.service.AgencyResponse;
import com.metaverse.workflow.agency.service.AgencyResponseMapper;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.ProgramSession;
import com.metaverse.workflow.program.repository.ProgramRepository;
import com.metaverse.workflow.program.repository.ProgramSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.metaverse.workflow.agency.service.AgencyService;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.Resource;
import com.metaverse.workflow.resouce.repository.ResourceRepository;

@Service
public class ResourceServiceImpl implements ResourceService {

	@Autowired
	private ResourceRepository repository;
	@Autowired
	private AgencyService agencyService;
	@Autowired
	private ProgramSessionRepository sessionRepo;
	@Override
	public ResourceResponse saveResource(ResourceRequest resourceRequest) {
		List<Agency> agencies = new ArrayList<>();
		Set<Long> agencyIds = resourceRequest.getAgencyIds();
		for (Long id : agencyIds) {
			Agency agency = agencyService.getAgencyById(id);
			if (agency != null) {
				agencies.add(agency);
			}
		}
		Resource resource = ResourceRequestMapper.map(resourceRequest, agencies);
		Resource savedResource = repository.save(resource);
		return ResourceResponseMapper.map(savedResource);
	}

	@Override
	public WorkflowResponse getResources() {
		List<Resource> resourceList = repository.findAll();
		List<ResourceResponse> response = resourceList != null ? resourceList.stream().map(resource -> ResourceResponseMapper.map(resource)).collect(Collectors.toList()) : null;
		return WorkflowResponse.builder().message("Success").status(200).data(response).build();
	}

	@Override
	public List<Resource> getResourceEntities() {
		return repository.findAll();
	}

	@Override
	public WorkflowResponse updateResources(ResourceRequest request, Long resourceId) throws DataException {
		Resource resources=repository.findById(resourceId)
				.orElseThrow(() -> new DataException("Resource data not found", "RESOURCE-DATA-NOT-FOUND", 400));
		List<Agency> agencies = new ArrayList<>();
		Set<Long> agencyIds = request.getAgencyIds();
		for (Long id : agencyIds) {
			Agency agency = agencyService.getAgencyById(id);
			if (agency != null) {
				agencies.add(agency);
			}
		}
		resources.setAgency(agencies);
		resources.setMobileNo(request.getMobileNo());
		resources.setEmail(request.getEmail());
		resources.setOrganizationName(request.getOrganizationName());
		resources.setQualification(request.getQualification());
		resources.setDesignation(request.getDesignation());
		resources.setSpecialization(request.getSpecialization());
		resources.setBriefDescription(request.getBriefDescription());
		resources.setGender(request.getGender());
		resources.setIsVIP(request.getIsVIP());
		return WorkflowResponse.builder()
				.message("Success")
				.status(200)
				.data(ResourceResponseMapper.map(repository.save(resources)))
				.build();
	}

	@Override
	public WorkflowResponse deleteResources(Long resourceId) throws DataException {
		Resource resources=repository.findById(resourceId)
				.orElseThrow(() -> new DataException("Resource data not found", "RESOURCE-DATA-NOT-FOUND", 400));
		if (sessionRepo.existsByResource_ResourceId(resourceId)) {
			return WorkflowResponse.builder()
					.message("Cannot delete: A session already exists for the resource with ID: " + resourceId)
					.status(200)
					.build();
		}
		repository.deleteById(resourceId);
		return WorkflowResponse.builder()
				.message("Resource deleted successfully.")
				.status(200)
				.build();
	}


}
