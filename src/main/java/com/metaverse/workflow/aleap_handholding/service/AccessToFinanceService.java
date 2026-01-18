package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.repository.AccessToFinanceRepository;
import com.metaverse.workflow.aleap_handholding.request_dto.AccessToFinanceRequest;
import com.metaverse.workflow.aleap_handholding.response_dto.AccessToFinanceResponse;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.InfluencedParticipant;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.aleap_handholding.AccessToFinance;
import com.metaverse.workflow.model.aleap_handholding.HandholdingSupport;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import com.metaverse.workflow.participant.repository.InfluencedParticipantRepository;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessToFinanceService {

    private final HandholdingSupportService supportService;
    private final AccessToFinanceRepository repository;
    private final OrganizationRepository organizationRepo;
    private final ParticipantRepository participantRepo;
    private final InfluencedParticipantRepository influencedParticipantRepository;

    public WorkflowResponse save(AccessToFinanceRequest request) throws DataException {

        HandholdingSupport support = supportService.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );

        Organization organization = organizationRepo.getReferenceById(request.getOrganizationId());
        List<Participant> participants = participantRepo.findAllById(request.getParticipantIds());
        List<InfluencedParticipant> influencedParticipants = influencedParticipantRepository.findAllById(request.getInfluencedParticipantIds());

        AccessToFinance entity = RequestMapper.mapToAccessToFinance(
                request,
                support,
                organization,
                participants,
                influencedParticipants
        );

        AccessToFinance saved = repository.save(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Access to Finance saved successfully")
                .data(ResponseMapper.mapToAccessToFinanceResponse(saved))
                .build();
    }

    @Transactional
    public WorkflowResponse update(Long id, AccessToFinanceRequest request) throws DataException {

        AccessToFinance existing = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Access to Finance not found with id " + id,
                        "ACCESS_TO_FINANCE_NOT_FOUND",
                        404
                ));

        HandholdingSupport support = supportService.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );
        existing.setHandholdingSupport(support);

        if (request.getOrganizationId() != null) {
            existing.setOrganization(
                    organizationRepo.getReferenceById(request.getOrganizationId())
            );
        }

        existing.setAccessToFinanceType(request.getAccessToFinanceType());

        existing.setSchemeName(request.getSchemeName());
        existing.setGovtApplicationStatus(
                request.getGovtApplicationStatus() != null
                        ? AccessToFinance.ApplicationStatus.valueOf(request.getGovtApplicationStatus())
                        : null
        );
        existing.setGovtSanctionDate(DateUtil.covertStringToDate(request.getGovtSanctionDate()));
        existing.setGovtSanctionedAmount(request.getGovtSanctionedAmount());
        existing.setGovtDetails(request.getGovtDetails());

        existing.setInstitutionName(request.getInstitutionName());
        existing.setBranchName(request.getBranchName());
        existing.setDprSubmissionDate(DateUtil.covertStringToDate(request.getDprSubmissionDate()));
        existing.setBankApplicationStatus(
                request.getBankApplicationStatus() != null
                        ? AccessToFinance.ApplicationStatus.valueOf(request.getBankApplicationStatus())
                        : null
        );
        existing.setBankSanctionDate(DateUtil.covertStringToDate(request.getBankSanctionDate()));
        existing.setBankSanctionedAmount(request.getBankSanctionedAmount());
        existing.setBankDetails(request.getBankDetails());

        existing.setCounselledBy(request.getCounselledBy());
        existing.setCounsellingDate(DateUtil.covertStringToDate(request.getCounsellingDate()));
        existing.setSubjectDelivered(request.getSubjectDelivered());

        AccessToFinance updated = repository.save(existing);

        return WorkflowResponse.builder()
                .status(200)
                .message("Access to Finance updated successfully")
                .data(ResponseMapper.mapToAccessToFinanceResponse(updated))
                .build();
    }

    public WorkflowResponse getById(Long id) throws DataException {

        AccessToFinance entity = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Access to Finance not found with id " + id,
                        "ACCESS_TO_FINANCE_NOT_FOUND",
                        404
                ));

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(ResponseMapper.mapToAccessToFinanceResponse(entity))
                .build();
    }

    public WorkflowResponse getByNonTrainingSubActivityId(Long subActivityId) {
        List<AccessToFinanceResponse> list = repository.findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId)
                .stream()
                .map(ResponseMapper::mapToAccessToFinanceResponse)
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(list)
                .build();
    }

    public WorkflowResponse delete(Long id) throws DataException {

        AccessToFinance entity = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Access to Finance not found with id " + id,
                        "ACCESS_TO_FINANCE_NOT_FOUND",
                        404
                ));

        repository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Deleted successfully")
                .build();
    }
}
