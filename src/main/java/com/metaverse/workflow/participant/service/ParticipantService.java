package com.metaverse.workflow.participant.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.Participant;

import java.util.List;
import java.util.Optional;

public interface ParticipantService {
    public WorkflowResponse saveParticipant(ParticipantRequest request);

    public WorkflowResponse updateParticipant(ParticipantRequest request);

    public WorkflowResponse getParticipants();

    public WorkflowResponse getParticipantsByParticipantId(Long id);

    WorkflowResponse getParticipantsByMobileNo(Long mobileNo);

    Optional<Participant> getParticipantsById(Long Id);

    public WorkflowResponse getOrganizationByParticipant(Long participantId);

    public  WorkflowResponse getParticipantByTypeOfProgram(String typeOfProgram);

    List<Participant> getAllParticipants();

    Boolean isMobileNumberExists(Long mobileNo);

    void saveAll(List<Participant> participants);
}
