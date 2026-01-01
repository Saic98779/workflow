package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.request_dto.SectorAdvisoryRequest;
import com.metaverse.workflow.aleap_handholding.repository.SectorAdvisoryRepository;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.aleap_handholding.HandholdingSupport;
import com.metaverse.workflow.model.aleap_handholding.SectorAdvisory;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectorAdvisoryService {

    private final HandholdingSupportService service;
    private final SectorAdvisoryRepository repository;
    private final OrganizationRepository organizationRepo;
    private final ParticipantRepository participantRepo;

    @Transactional
    public WorkflowResponse save(SectorAdvisoryRequest request) throws DataException {

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(), request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(), request.getHandHoldingType()
        );

        Organization organization = organizationRepo.findById(request.getOrganizationId())
                .orElseThrow(() -> new DataException(
                        "Organization not found with id " + request.getOrganizationId(),
                        "ORG_NOT_FOUND",
                        400
                ));

        List<Participant> participants = participantRepo.findAllById(request.getParticipantIds());
        SectorAdvisory entity =
                RequestMapper.mapToSectorAdvisory(
                        request, support, organization, participants
                );
        SectorAdvisory saved = repository.save(entity);
        return WorkflowResponse.builder()
                .status(200)
                .message("Sector advisory saved successfully")
                .data(ResponseMapper.mapToSectorAdvisoryResponse(saved))
                .build();
    }
    @Transactional
    public WorkflowResponse update(Long id, SectorAdvisoryRequest request) throws DataException {

        SectorAdvisory existing = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Sector advisory not found with id " + id,
                        "SECTOR_ADVISORY_NOT_FOUND",
                        400
                ));

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );

        existing.setHandholdingSupport(support);
        existing.setOrganization(
                organizationRepo.getReferenceById(request.getOrganizationId())
        );
        existing.setParticipants(
                participantRepo.findAllById(request.getParticipantIds())
        );
        existing.setAdviseDetails(request.getAdviseDetails());
        existing.setCounselledBy(request.getCounselledBy());
        existing.setCounsellingTime(request.getCounsellingTime());
        existing.setCounsellingDate(
                DateUtil.covertStringToDate(request.getCounsellingDate())
        );

        SectorAdvisory updated = repository.save(existing);

        return WorkflowResponse.builder()
                .status(200)
                .message("Sector advisory updated successfully")
                .data(ResponseMapper.mapToSectorAdvisoryResponse(updated))
                .build();
    }
    public WorkflowResponse getById(Long id) throws DataException {

        SectorAdvisory advisory = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Sector advisory not found with id " + id,
                        "SECTOR_ADVISORY_NOT_FOUND",
                        400
                ));

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(ResponseMapper.mapToSectorAdvisoryResponse(advisory))
                .build();
    }
    public WorkflowResponse getByNonTrainingSubActivityId(Long subActivityId) {

        List<SectorAdvisory> list =
                repository.findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId);

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(
                        list.stream()
                                .map(ResponseMapper::mapToSectorAdvisoryResponse)
                                .toList()
                )
                .build();
    }

    @Transactional
    public WorkflowResponse delete(Long id) throws DataException {

        SectorAdvisory advisory = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Sector advisory not found with id " + id,
                        "SECTOR_ADVISORY_NOT_FOUND",
                        400
                ));

        repository.delete(advisory);

        return WorkflowResponse.builder()
                .status(200)
                .message("Sector advisory deleted successfully")
                .build();
    }




}

