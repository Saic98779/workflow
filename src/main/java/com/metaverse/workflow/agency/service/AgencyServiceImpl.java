package com.metaverse.workflow.agency.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.program.repository.ProgramRepository;
import com.metaverse.workflow.program.service.ProgramResponse;
import com.metaverse.workflow.program.service.ProgramResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.model.Agency;

@Service
public class AgencyServiceImpl implements AgencyService{

	@Autowired
	private AgencyRepository agencyRepository;

	@Autowired
	private ProgramRepository programRepository;
	
	@Override
	public String saveAgency(Agency agency) {
		Agency registeredAgency = agencyRepository.save(agency);
		return "AgencyDetails saved With id: "+registeredAgency.getAgencyId();
	}

	@Override
	public Agency getAgencyById(Long agencyId) {
		Optional<Agency> findById = agencyRepository.findById(agencyId);
		if(findById.isPresent())
		{
			return findById.get();
		}
		
		return null;
	}

	public Page<Program> getProgramsByAgencyIdPaginated(Long agencyId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Program> programs = programRepository.findByAgencyAgencyId(agencyId, pageable);
		return programs;
	}

	@Override
	public WorkflowResponse getAgencies() {
		List<Agency> agencyList = agencyRepository.findAll();
		List<AgencyResponse> response = agencyList != null ? agencyList.stream().map(agency -> AgencyResponseMapper.map(agency)).collect(Collectors.toList()) : null;
		return WorkflowResponse.builder().message("Success").status(200).data(response).build();
	}

	@Override
	public List<Agency> getAllAgencies() {
		return agencyRepository.findAll();
	}

	@Override
	public WorkflowResponse getProgramByAgencyIdDropDown(Long agencyId) {
		List<Program> programList = programRepository.findByAgencyAgencyId(agencyId);
		List<ProgramResponse> responses =programList!= null ? programList.stream().map(ProgramResponseMapper::map).collect(Collectors.toList()) : null ;
		return WorkflowResponse.builder().message("Success").status(200).data(responses).build();
	}

}
