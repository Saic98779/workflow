package com.metaverse.workflow.tgtpc_handholding.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.InfluencedParticipant;
import com.metaverse.workflow.model.tgtpc_handholding.TGTPCHandholdingSupport;
import com.metaverse.workflow.tgtpc_handholding.repository.TGTPCHandholdingSupportRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import com.metaverse.workflow.participant.repository.InfluencedParticipantRepository;
import com.metaverse.workflow.tgtpc_handholding.request_dto.TGTPCHandholdingSupportRequest;
import com.metaverse.workflow.tgtpc_handholding.response_dto.TGTPCHandholdingSupportResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TGTPCHandholdingMasterService {

    private final TGTPCHandholdingSupportRepository supportRepo;
    private final NonTrainingSubActivityRepository subActivityRepo;
    private final OrganizationRepository organizationRepo;
    private final ParticipantRepository participantRepo;
    private final InfluencedParticipantRepository influencedParticipantRepo;

    @Transactional
    public TGTPCHandholdingSupport save(TGTPCHandholdingSupportRequest request) throws DataException {

        NonTrainingSubActivity subActivity = subActivityRepo.findById(request.getSubActivityId())
                .orElseThrow(() -> new DataException(
                        "Sub-Activity not found with id " + request.getSubActivityId(),
                        "SUB_ACTIVITY_NOT_FOUND",
                        400
                ));

        Organization organization = organizationRepo.findById(request.getOrganizationId())
                .orElseThrow(() -> new DataException(
                        "Organization not found with id " + request.getOrganizationId(),
                        "ORG_NOT_FOUND",
                        400
                ));

        List<Participant> participants = participantRepo.findAllById(request.getParticipantIds());
        List<InfluencedParticipant> influencedParticipants = influencedParticipantRepo.findAllById(request.getInfluencedParticipantIds());

        TGTPCHandholdingSupport support = HandholdingRequestMapper.mapToTGTPCHandholdingSupport(
                request, subActivity, organization, participants, influencedParticipants
        );

        return support = supportRepo.save(support);

    }

    @Transactional
    public TGTPCHandholdingSupport update(Long supportId, TGTPCHandholdingSupportRequest request) throws DataException {

        TGTPCHandholdingSupport existing = supportRepo.findById(supportId)
                .orElseThrow(() -> new DataException(
                        "TGTPC Handholding Support not found with id " + supportId,
                        "HANDHOLDING_NOT_FOUND",
                        404
                ));

        NonTrainingSubActivity subActivity = subActivityRepo.findById(request.getSubActivityId())
                .orElseThrow(() -> new DataException(
                        "Sub-Activity not found with id " + request.getSubActivityId(),
                        "SUB_ACTIVITY_NOT_FOUND",
                        400
                ));

        Organization organization = organizationRepo.findById(request.getOrganizationId())
                .orElseThrow(() -> new DataException(
                        "Organization not found with id " + request.getOrganizationId(),
                        "ORG_NOT_FOUND",
                        400
                ));

        List<Participant> participants = participantRepo.findAllById(request.getParticipantIds());
        List<InfluencedParticipant> influencedParticipants = influencedParticipantRepo.findAllById(request.getInfluencedParticipantIds());

        HandholdingRequestMapper.updateEntity(existing, request, subActivity, organization, participants, influencedParticipants);

        return supportRepo.save(existing);
    }

    public WorkflowResponse getById(Long supportId) throws DataException {
        TGTPCHandholdingSupport support = supportRepo.findById(supportId)
                .orElseThrow(() -> new DataException(
                        "TGTPC Handholding Support not found with id " + supportId,
                        "HANDHOLDING_NOT_FOUND",
                        404
                ));

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(HandholdingResponseMapper.mapToTGTPCHandholdingSupportResponse(support))
                .build();
    }

    public WorkflowResponse getBySubActivityId(Long subActivityId) throws DataException {
        List<TGTPCHandholdingSupportResponse> response = supportRepo
                .findByNonTrainingSubActivity_SubActivityId(subActivityId)
                .stream()
                .map(HandholdingResponseMapper::mapToTGTPCHandholdingSupportResponse)
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(response)
                .build();
    }

    @Transactional
    public WorkflowResponse delete(Long supportId) throws DataException {
        TGTPCHandholdingSupport support = supportRepo.findById(supportId)
                .orElseThrow(() -> new DataException(
                        "TGTPC Handholding Support not found with id " + supportId,
                        "HANDHOLDING_NOT_FOUND",
                        404
                ));

        supportRepo.delete(support);

        return WorkflowResponse.builder()
                .status(200)
                .message("TGTPC Handholding Support deleted successfully")
                .build();
    }
}

