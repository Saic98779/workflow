package com.metaverse.workflow.ESDPTraining.service;


import com.metaverse.workflow.ESDPTraining.repository.ESDPTrainingRepository;
import com.metaverse.workflow.agency.service.AgencyService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.ESDPTraining;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.organization.service.OrganizationService;
import com.metaverse.workflow.participant.service.ParticipantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ESDPTrainingServiceAdepter implements  ESDPTrainingService {
    @Autowired
    ESDPTrainingRepository esdpTrainingRepository;
    @Autowired
    OrganizationService organizationService;
    @Autowired
    ParticipantService participantService;
    @Autowired
    AgencyService agencyService;

    @Override
    public WorkflowResponse saveESDPTrainingProgram(ESDPTrainingRequest esdpTrainingRequest) {
        log.info("save EsdpTraining apllication");
        Agency agency= agencyService.getAgencyById(esdpTrainingRequest.getAgencyId());
        Optional<Organization> organization=organizationService.getOrganizationById(esdpTrainingRequest.getOrganizationId());
        Optional<Participant> participant=participantService.getParticipantsById(esdpTrainingRequest.getParticipantId());
        if(!participant.isPresent() || !organization.isPresent()|| agency==null)
        {
            return  WorkflowResponse.builder().status(400).message("Participant Or Organization or agency Not  found").build();
        }
       ESDPTraining esdpTraining= ESDPTrainingRequestMapper.map(esdpTrainingRequest,organization.get(),participant.get(),agency);
      ESDPTraining response=  esdpTrainingRepository.save(esdpTraining);
        return WorkflowResponse.builder()
                .message("Training Program saved successfully")
                .status(200)
                .data(response)
                .build();
    }

    @Override
    public WorkflowResponse getESDPTrainingProgramData()
    {
        List<ESDPTraining> esdpTrainingList= esdpTrainingRepository.findAll();
        List<ESDPTrainingResponse>  esdpTrainingResponseList= esdpTrainingList.stream()
             .map(esdpTraining -> ESDPTrainingResponseMapper.map(esdpTraining)).collect(Collectors.toList());
        if(esdpTrainingResponseList.isEmpty())
            return WorkflowResponse.builder().status(400).message("Data is not available").build();

        return WorkflowResponse.builder().status(200).message("success").data(esdpTrainingResponseList).build();
    }
}
