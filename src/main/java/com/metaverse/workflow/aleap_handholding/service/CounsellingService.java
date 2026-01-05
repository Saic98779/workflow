package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.request_dto.CounsellingRequest;
import com.metaverse.workflow.aleap_handholding.repository.CounsellingRepository;
import com.metaverse.workflow.aleap_handholding.response_dto.CounsellingResponse;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.InfluencedParticipant;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.aleap_handholding.Counselling;
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
public class CounsellingService {

    private final HandholdingSupportService service;
    private final CounsellingRepository repository;
    private final OrganizationRepository organizationRepo;
    private final ParticipantRepository participantRepo;
    private final InfluencedParticipantRepository influencedParticipantRepository;

    public WorkflowResponse save(CounsellingRequest request) throws DataException {

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );
        Organization organization = organizationRepo.findById(request.getOrganizationId())
                .orElseThrow(() ->
                        new DataException(
                                "Organization not found with id " + request.getOrganizationId(),
                                "ORG_NOT_FOUND", 404
                        )
                );
        List<Participant> participants = participantRepo.findAllById(request.getParticipantIds());
        List<InfluencedParticipant> influencedParticipants = influencedParticipantRepository.findAllById(request.getInfluencedParticipantIds());

        Counselling entity = RequestMapper.mapToCounselling(
                request,
                support,
                organization,
                participants,
                influencedParticipants
        );

        Counselling saved = repository.save(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Counselling saved successfully")
                .data(ResponseMapper.mapToCounsellingResponse(saved))
                .build();
    }

    @Transactional
    public WorkflowResponse update(Long counsellingId, CounsellingRequest request) throws DataException {

        Counselling existing = repository.findById(counsellingId)
                .orElseThrow(() -> new DataException(
                        "Counselling not found with id " + counsellingId,
                        "COUNSELLING_NOT_FOUND",
                        404
                ));
        HandholdingSupport support = service.getOrCreateSupport(
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
        if (request.getParticipantIds() != null) {
            existing.setParticipants(
                    participantRepo.findAllById(request.getParticipantIds())
            );
        }

        existing.setSubjectDelivered(request.getSubjectDelivered());
        existing.setOriginalIdea(request.getOriginalIdea());
        existing.setFinalIdea(request.getFinalIdea());
        existing.setCounselledBy(request.getCounselledBy());

        if (request.getCounsellingDate() != null) {
            existing.setCounsellingDate(
                    DateUtil.covertStringToDate(request.getCounsellingDate())
            );
        }

        existing.setCounsellingTime(request.getCounsellingTime());

        Counselling updated = repository.save(existing);

        return WorkflowResponse.builder()
                .status(200)
                .message("Counselling updated successfully")
                .data(ResponseMapper.mapToCounsellingResponse(updated))
                .build();
    }

    public WorkflowResponse getById(Long counsellingId) throws DataException {

        Counselling counselling = repository.findById(counsellingId)
                .orElseThrow(() -> new DataException(
                        "Counselling not found with id " + counsellingId,
                        "COUNSELLING_NOT_FOUND",
                        404
                ));

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(ResponseMapper.mapToCounsellingResponse(counselling))
                .build();
    }

    public WorkflowResponse getAll() {

        List<CounsellingResponse> response = repository.findAll()
                .stream()
                .map(ResponseMapper::mapToCounsellingResponse)
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(response)
                .build();
    }

    public WorkflowResponse getByNonTrainingSubActivityId(Long subActivityId) {

        List<CounsellingResponse> response = repository
                .findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId)
                .stream()
                .map(ResponseMapper::mapToCounsellingResponse)
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(response)
                .build();
    }

    /* ========================= DELETE ========================= */

    public WorkflowResponse delete(Long counsellingId) throws DataException {

        Counselling counselling = repository.findById(counsellingId)
                .orElseThrow(() -> new DataException(
                        "Counselling not found with id " + counsellingId,
                        "COUNSELLING_NOT_FOUND",
                        404
                ));

        repository.delete(counselling);

        return WorkflowResponse.builder()
                .status(200)
                .message("Counselling deleted successfully")
                .build();
    }
}


