package com.metaverse.workflow.aleap_handholding.service;


import com.metaverse.workflow.aleap_handholding.dto.BusinessPlanRequest;
import com.metaverse.workflow.aleap_handholding.repository.BusinessPlanDetailsRepository;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.aleap_handholding.BusinessPlanDetails;
import com.metaverse.workflow.model.aleap_handholding.HandholdingSupport;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class BusinessPlanDetailsService {

    private final HandholdingSupportService service;
    private final BusinessPlanDetailsRepository repository;
    private final OrganizationRepository organizationRepo;
    private final ParticipantRepository participantRepo;

    public BusinessPlanDetails save(BusinessPlanRequest request) throws DataException {

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getNonTrainingAction()
        );

        BusinessPlanDetails b = new BusinessPlanDetails();
        b.setHandholdingSupport(support);
        b.setOrganization(organizationRepo.getReferenceById(request.getOrganizationId()));
        b.setParticipants(participantRepo.findAllById(request.getParticipantIds()));
        b.setPlanFileUploadPath(request.getPlanFileUploadPath());
        b.setBankName(request.getBankName());
        b.setBranchName(request.getBranchName());
        b.setBankRemarks(request.getBankRemarks());
        b.setCounsellingDate(DateUtil.covertStringToDate(request.getCounsellingDate()));
        b.setCounsellingTime(request.getCounsellingTime());
        b.setCounselledBy(request.getCounselledBy());

        return repository.save(b);
    }
}

