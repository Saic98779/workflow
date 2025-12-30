package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.dto.CounsellingRequest;
import com.metaverse.workflow.aleap_handholding.repository.CounsellingRepository;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.aleap_handholding.Counselling;
import com.metaverse.workflow.model.aleap_handholding.HandholdingSupport;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CounsellingService  {

    private final HandholdingSupportService service;
    private final CounsellingRepository repository;
    private final OrganizationRepository organizationRepo;
    private final ParticipantRepository participantRepo;

    public Counselling save(CounsellingRequest request) throws DataException {

        HandholdingSupport support =service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getNonTrainingAction()
        );

        Counselling c = new Counselling();
        c.setHandholdingSupport(support);
        c.setOrganization(organizationRepo.getReferenceById(request.getOrganizationId()));
        c.setParticipants(participantRepo.findAllById(request.getParticipantIds()));
        c.setSubjectDelivered(request.getSubjectDelivered());
        c.setOriginalIdea(request.getOriginalIdea());
        c.setFinalIdea(request.getFinalIdea());
        c.setCounsellingDate(DateUtil.covertStringToDate(request.getCounsellingDate()));
        c.setCounsellingTime(request.getCounsellingTime());
        c.setCounselledBy(request.getCounselledBy());

        return repository.save(c);
    }
}

