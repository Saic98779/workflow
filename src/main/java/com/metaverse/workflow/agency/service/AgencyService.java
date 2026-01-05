package com.metaverse.workflow.agency.service;

import com.metaverse.workflow.agency.dto.ParticipantsDto;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.Program;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AgencyService {

	public String saveAgency(Agency agency);

	public WorkflowResponse getAgencies();

	List<Agency> getAllAgencies();

	Page<Program> getProgramsByAgencyIdPaginated(Long id, int page, int size);

	Agency getAgencyById(Long id);

	WorkflowResponse getAllLocationByAgencyId(Long agencyId, int page, int size);

	WorkflowResponse getProgramByAgencyIdDropDown(Long agencyId);

	WorkflowResponse getProgramsDistrictsAndAgency(Long id, String district);

    List<ParticipantsDto> getParticipantIdAndNameByAgencyId(Long agencyId);
}
