package com.metaverse.workflow.participant.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.InfluencedParticipant;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import com.metaverse.workflow.participant.repository.InfluencedParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InfluencedParticipantService {

    private final InfluencedParticipantRepository repository;
    private final OrganizationRepository organizationRepository;

    public WorkflowResponse create(InfluencedParticipantDto influencedDto) throws DataException {

        Organization organization = organizationRepository.findById(influencedDto.getOrganizationId())
                .orElseThrow(() -> new DataException(
                        "Organization not found with ID: " + influencedDto.getOrganizationId(),
                        "ORG_NOT_FOUND",
                        400));

        InfluencedParticipant participant = mapToEntity(influencedDto);

        participant.setOrganization(organization);

        InfluencedParticipant saved = repository.save(participant);

        return WorkflowResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Participant created successfully")
                .data(mapToDto(saved))
                .build();
    }


    public WorkflowResponse getAll() {
        List<InfluencedParticipant> result = repository.findAll();
        List<InfluencedParticipantDto> dtoList = result.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return WorkflowResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Participants fetched successfully")
                .data(dtoList)
                .build();
    }

    public WorkflowResponse getById(Long id) throws DataException {
        InfluencedParticipant participant = repository.findById(id)
                .orElseThrow(() -> new DataException("Participant not found with ID: " + id,"400"));
        return WorkflowResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Participant fetched successfully")
                .data(mapToDto(participant))
                .build();
    }

    public WorkflowResponse update(Long id, InfluencedParticipantDto dto) throws DataException {
        InfluencedParticipant existing = repository.findById(id)
                .orElseThrow(() -> new DataException("Participant not found with ID: " + id, "400"));

        existing.setParticipantName(dto.getParticipantName());
        existing.setGender(dto.getGender());
        existing.setCategory(dto.getCategory());
        existing.setDisability(dto.getDisability());
        existing.setAadharNo(dto.getAadharNo());
        existing.setMobileNo(dto.getMobileNo());
        existing.setEmail(dto.getEmail());
        existing.setDesignation(dto.getDesignation());

        if (dto.getOrganizationId() != null) {
            Organization org = organizationRepository.findById(dto.getOrganizationId())
                    .orElseThrow(() -> new DataException("Organization not found with ID: " + dto.getOrganizationId(), "400"));
            existing.setOrganization(org);
        }

        InfluencedParticipant updated = repository.save(existing);

        return WorkflowResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Participant updated successfully")
                .data(mapToDto(updated))
                .build();
    }


    public WorkflowResponse delete(Long id) throws DataException {
        InfluencedParticipant existing = repository.findById(id)
                .orElseThrow(() -> new DataException("Participant not found with ID: " + id,"400"));
        repository.delete(existing);
        return WorkflowResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Participant deleted successfully")
                .data(null)
                .totalPages(0)
                .totalElements(0)
                .build();
    }

    private InfluencedParticipantDto mapToDto(InfluencedParticipant entity) {
        return InfluencedParticipantDto.builder()
                .influencedId(entity.getInfluencedId())
                .participantName(entity.getParticipantName())
                .gender(entity.getGender())
                .category(entity.getCategory())
                .disability(entity.getDisability())
                .aadharNo(entity.getAadharNo())
                .mobileNo(entity.getMobileNo())
                .email(entity.getEmail())
                .designation(entity.getDesignation())
                .organizationId(entity.getOrganization() != null ? entity.getOrganization().getOrganizationId() : null)
                .build();
    }

    private InfluencedParticipant mapToEntity(InfluencedParticipantDto dto) {
        InfluencedParticipant entity = new InfluencedParticipant();
        entity.setParticipantName(dto.getParticipantName());
        entity.setGender(dto.getGender());
        entity.setCategory(dto.getCategory());
        entity.setDisability(dto.getDisability());
        entity.setAadharNo(dto.getAadharNo());
        entity.setMobileNo(dto.getMobileNo());
        entity.setEmail(dto.getEmail());
        entity.setDesignation(dto.getDesignation());

        if (dto.getOrganizationId() != null) {
            Organization org = new Organization();
            org.setOrganizationId(dto.getOrganizationId());
            entity.setOrganization(org);
        }

        return entity;
    }
}

