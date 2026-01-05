package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.request_dto.FeasibilityInputRequest;
import com.metaverse.workflow.aleap_handholding.request_dto.MarketStudyRequest;
import com.metaverse.workflow.aleap_handholding.repository.FeasibilityInputRepository;
import com.metaverse.workflow.aleap_handholding.repository.MarketStudyRepository;
import com.metaverse.workflow.aleap_handholding.response_dto.MarketStudyResponse;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.InfluencedParticipant;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.aleap_handholding.FeasibilityInput;
import com.metaverse.workflow.model.aleap_handholding.HandholdingSupport;
import com.metaverse.workflow.model.aleap_handholding.MarketStudy;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import com.metaverse.workflow.participant.repository.InfluencedParticipantRepository;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketStudyService {
    private final HandholdingSupportService service;
    private final OrganizationRepository organizationRepo;
    private final ParticipantRepository participantRepo;
    private final MarketStudyRepository marketStudyRepository;
    private final FeasibilityInputRepository inputRepository;
    private final InfluencedParticipantRepository influencedParticipantRepository;

    public WorkflowResponse save(MarketStudyRequest request) throws DataException {

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );

        Organization organization = organizationRepo.findById(request.getOrganizationId())
                .orElseThrow(() -> new DataException(
                        "Organization not found with id " + request.getOrganizationId(),
                        "ORG_NOT_FOUND",
                        400
                ));

        List<Participant> participants = participantRepo.findAllById(request.getParticipantIds());
        List<InfluencedParticipant> influencedParticipants = influencedParticipantRepository.findAllById(request.getInfluencedParticipantIds());

        MarketStudy entity = RequestMapper.mapToMarketStudy(
                request,
                support,
                organization,
                participants,
                influencedParticipants
        );

        MarketStudy saved = marketStudyRepository.save(entity);
        return WorkflowResponse.builder()
                .status(200)
                .message("Market Study saved successfully")
                .data(ResponseMapper.mapToMarketStudyResponse(saved))
                .build();
    }

    @Transactional
    public WorkflowResponse update(Long id, MarketStudyRequest request)
            throws DataException {

        MarketStudy existing = marketStudyRepository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Market Study not found with id " + id,
                        "MARKET_STUDY_NOT_FOUND",
                        400
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

        existing.setCounselledBy(request.getCounselledBy());
        existing.setCounsellingTime(request.getCounsellingTime());

        if (request.getDateOfStudy() != null) {
            existing.setDateOfStudy(
                    DateUtil.covertStringToDate(request.getDateOfStudy())
            );
        }

        if (request.getCounsellingDate() != null) {
            existing.setCounsellingDate(
                    DateUtil.covertStringToDate(request.getCounsellingDate())
            );
        }

        MarketStudy updated = marketStudyRepository.save(existing);

        return WorkflowResponse.builder()
                .status(200)
                .message("Market Study updated successfully")
                .data(ResponseMapper.mapToMarketStudyResponse(updated))
                .build();
    }


    public WorkflowResponse getById(Long id) throws DataException {

        MarketStudy entity = marketStudyRepository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Market Study not found with id " + id,
                        "MARKET_STUDY_NOT_FOUND",
                        404
                ));

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(ResponseMapper.mapToMarketStudyResponse(entity))
                .build();
    }

    public WorkflowResponse getByNonTrainingSubActivityId(Long subActivityId) {

        List<MarketStudyResponse> response = marketStudyRepository
                .findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId)
                .stream()
                .map(ResponseMapper::mapToMarketStudyResponse)
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(response)
                .build();
    }

    public WorkflowResponse delete(Long id) throws DataException {

        MarketStudy entity = marketStudyRepository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Market Study not found with id " + id,
                        "MARKET_STUDY_NOT_FOUND",
                        400
                ));

        marketStudyRepository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Market Study deleted successfully")
                .build();
    }

@Transactional
    public WorkflowResponse saveFeasibilityInput(FeasibilityInputRequest request) throws DataException {

        MarketStudy study = marketStudyRepository.findById(request.getMarketStudyId())
                .orElseThrow(() -> new DataException(
                        "Market study not found with id " + request.getMarketStudyId(),
                        "MARKET_STUDY_NOT_FOUND",
                        400
                ));

        FeasibilityInput input =
                RequestMapper.mapToFeasibilityInput(request, study);

        FeasibilityInput saved = inputRepository.save(input);

        return WorkflowResponse.builder()
                .status(200)
                .message("Feasibility input saved successfully")
                .data(ResponseMapper.mapToFeasibilityInputResponse(saved))
                .build();
    }
    @Transactional
    public WorkflowResponse updateFeasibilityInput(Long id, FeasibilityInputRequest request) throws DataException {

        FeasibilityInput existing = inputRepository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Feasibility input not found with id " + id,
                        "FEASIBILITY_INPUT_NOT_FOUND",
                        400
                ));

        if (request.getMarketStudyId() != null) {
            MarketStudy study = marketStudyRepository.findById(request.getMarketStudyId())
                    .orElseThrow(() -> new DataException(
                            "Market study not found with id " + request.getMarketStudyId(),
                            "MARKET_STUDY_NOT_FOUND",
                            400
                    ));
            existing.setMarketStudy(study);
        }

        existing.setInputDetails(request.getInputDetails());
        existing.setSource(request.getSource());
        existing.setSector(request.getSector());
        existing.setFeasibilityActivity(request.getFeasibilityActivity());

        FeasibilityInput updated = inputRepository.save(existing);

        return WorkflowResponse.builder()
                .status(200)
                .message("Feasibility input updated successfully")
                .data(ResponseMapper.mapToFeasibilityInputResponse(updated))
                .build();
    }


    public WorkflowResponse getByFeasibilityInputId(Long id) throws DataException {

        FeasibilityInput input = inputRepository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Feasibility input not found with id " + id,
                        "FEASIBILITY_INPUT_NOT_FOUND",
                        400
                ));

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(ResponseMapper.mapToFeasibilityInputResponse(input))
                .build();
    }

    public WorkflowResponse getByFeasibilityInputSubActivityId(Long subActivityId) {

        List<FeasibilityInput> list =
                inputRepository.findByMarketStudy_HandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId);

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(
                        list.stream()
                                .map(ResponseMapper::mapToFeasibilityInputResponse)
                                .toList()
                )
                .build();
    }

    @Transactional
    public WorkflowResponse deleteFeasibilityInput(Long id) throws DataException {

        FeasibilityInput input = inputRepository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Feasibility input not found with id " + id,
                        "FEASIBILITY_INPUT_NOT_FOUND",
                        400
                ));

        inputRepository.delete(input);

        return WorkflowResponse.builder()
                .status(200)
                .message("Feasibility input deleted successfully")
                .build();
    }
}