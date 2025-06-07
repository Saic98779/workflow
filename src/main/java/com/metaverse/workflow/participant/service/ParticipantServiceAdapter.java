package com.metaverse.workflow.participant.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.organization.service.OrganizationResponse;
import com.metaverse.workflow.organization.service.OrganizationResponseMapper;
import com.metaverse.workflow.organization.service.OrganizationService;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import com.metaverse.workflow.program.repository.ProgramRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ParticipantServiceAdapter implements ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ProgramRepository programRepository;

    @Override
    public WorkflowResponse saveParticipant(ParticipantRequest request) {
        Participant participantByMobileNo = participantRepository.findByMobileNo(request.getMobileNo());
        if(participantByMobileNo != null)
            return WorkflowResponse.builder().status(400).message("This Mobile Number is already Registered").build();
        List<Program> programList = null;
        Optional<Organization> organization = Optional.empty();
        Participant participant;
        if (request.getProgramIds() != null && request.getProgramIds().size() > 0) {
            programList = new ArrayList<>();
            Set<Long> programIds = request.getProgramIds();
            for (Long id : programIds) {
                Optional<Program> program = programRepository.findById(id);
                if (program.isPresent()) {
                    programList.add(program.get());
                }
            }
            if (programList.size() == 0)
                return WorkflowResponse.builder().status(400).message("Programs not found").build();
        }
        if (request.getOrganizationId() != null) {
            organization = organizationService.getOrganizationById(request.getOrganizationId());
            if (!organization.isPresent())
                return WorkflowResponse.builder().status(400).message("Organization not found").build();
        }
        participant = ParticipantRequestMapper.map(request, organization.isPresent() ? organization.get() : null, programList);
        Participant savedParticipant = participantRepository.save(participant);
        ParticipantResponse participantResponse = ParticipantResponseMapper.map(savedParticipant);
        return WorkflowResponse.builder().status(200).message("Success").data(participantResponse).build();
    }

    @Override
    @Transactional
    public WorkflowResponse updateParticipant(ParticipantRequest request) {
        Optional<Participant> existingParticipantOpt = participantRepository.findById(request.getParticipantId());

        if (!existingParticipantOpt.isPresent()) {
            return WorkflowResponse.builder()
                    .status(404)
                    .message("Participant not found with ID: " + request.getParticipantId())
                    .build();
        }

        Participant existingParticipant = ParticipantRequestMapper.mapUpdate(request,existingParticipantOpt.get());

        // Update Organization
        if (request.getOrganizationId() != null) {
            Optional<Organization> organizationOpt = organizationService.getOrganizationById(request.getOrganizationId());
            if (organizationOpt.isPresent()) {
                existingParticipant.setOrganization(organizationOpt.get());
            } else {
                return WorkflowResponse.builder().status(400).message("Organization not found").build();
            }
        }

        // Update Programs
        if (request.getProgramIds() != null && !request.getProgramIds().isEmpty()) {
            List<Program> programList = programRepository.findAllById(request.getProgramIds()); // Fetch managed entities
            if (programList.isEmpty()) {
                return WorkflowResponse.builder().status(400).message("Programs not found").build();
            }
            existingParticipant.setPrograms(programList);
        }

        // Save updated participant
        Participant updatedParticipant = participantRepository.save(existingParticipant);
        ParticipantResponse participantResponse = ParticipantResponseMapper.map(updatedParticipant);

        return WorkflowResponse.builder().status(200).message("Participant updated successfully").data(participantResponse).build();
    }

    @Override
    public WorkflowResponse getParticipants() {
        List<Participant> participantList = participantRepository.findAll();
        List<ParticipantResponse> response = participantList != null ? participantList.stream().map(participant -> ParticipantResponseMapper.map(participant)).collect(Collectors.toList()) : null;
        return WorkflowResponse.builder().message("Success").status(200).data(response).build();
    }

    @Override
    public WorkflowResponse getParticipantsByMobileNo(Long mobileNo) {

        Participant participant = participantRepository.findByMobileNo(mobileNo);
        if(participant == null)return WorkflowResponse.builder().message("Participant Not found").status(400).build();
       // if(participant.getOrganization() == null)return WorkflowResponse.builder().message("Participant ia na aspirant not tieUp with any organization").status(400).build();
        ParticipantResponseForESDPTraining response= ParticipantResponseMapper.mapForESDPTraining(participant);
        return WorkflowResponse.builder().message("Success").status(200).data(response).build();
    }


    @Override
    public Optional<Participant> getParticipantsById(Long id) {
        Optional<Participant> participant = participantRepository.findById(id);
        return participant;
    }

    @Override
    public WorkflowResponse getParticipantsByParticipantId(Long id) {
        Optional<Participant> participant = getParticipantsById(id);
        if(!participant.isPresent())return WorkflowResponse.builder().status(400).message("Participant Not Found").build();
        ParticipantResponse response=ParticipantResponseMapper.map(participant.get());
        return WorkflowResponse.builder().status(200).message("Success").data(response).build();
    }



    @Override
    public WorkflowResponse getOrganizationByParticipant(Long participantId) {

        Optional<Participant> participants = participantRepository.findById(participantId);
        if (!participants.isPresent()) return WorkflowResponse.builder().message("Not Found").status(400).build();
        OrganizationResponse organizationResponse = OrganizationResponseMapper.map(participants.get().getOrganization());
        return WorkflowResponse.builder()
                .message("Organization Details").
                status(200)
                .data(organizationResponse)
                .build();


    }

    public WorkflowResponse getParticipantByTypeOfProgram(String typeOfProgram) {
        log.info("getparticipant on mobaile number");
        List<Participant> participantList = participantRepository.findByProgramType(typeOfProgram);
        if (participantList.isEmpty())
            return WorkflowResponse.builder()
                .message("Participant not found for this program.")
                .status(400).build();
        List<ParticipantResponse> response = participantList.stream().map(participant -> ParticipantResponseMapper.map(participant)).collect(Collectors.toList());
        return WorkflowResponse.builder().message("Success")
                .status(200).data(response).build();
    }

    @Override
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    @Override
    public Boolean isMobileNumberExists(Long mobileNo) {
        Participant participant = participantRepository.findByMobileNo(mobileNo);
        if(participant == null)return false;
        return true;
    }

    public void saveAll(List<Participant> participants) {
        participantRepository.saveAll(participants);
    }

}
