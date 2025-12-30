package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.dto.SectorAdvisoryRequest;
import com.metaverse.workflow.aleap_handholding.repository.SectorAdvisoryRepository;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.aleap_handholding.HandholdingSupport;
import com.metaverse.workflow.model.aleap_handholding.SectorAdvisory;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SectorAdvisoryService {

    private final HandholdingSupportService service;
    private final SectorAdvisoryRepository repository;
    private final OrganizationRepository organizationRepo;
    private final ParticipantRepository participantRepo;

    public SectorAdvisory save(SectorAdvisoryRequest request) throws DataException {

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getNonTrainingAction()
        );

        SectorAdvisory s = new SectorAdvisory();
        s.setHandholdingSupport(support);
        s.setOrganization(organizationRepo.getReferenceById(request.getOrganizationId()));
        s.setParticipants(participantRepo.findAllById(request.getParticipantIds()));
        s.setAdviseDetails(request.getAdviseDetails());
        s.setCounsellingDate(DateUtil.covertStringToDate(request.getCounsellingDate()));
        s.setCounsellingTime(request.getCounsellingTime());
        s.setCounselledBy(request.getCounselledBy());

        return repository.save(s);
    }
}

