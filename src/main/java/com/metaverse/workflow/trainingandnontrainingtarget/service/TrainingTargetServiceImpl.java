package com.metaverse.workflow.trainingandnontrainingtarget.service;

import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.TrainingTargets;
import com.metaverse.workflow.trainingandnontrainingtarget.repository.TrainingTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingTargetServiceImpl implements TrainingTargetService {

    private final TrainingTargetRepository trainingTargetRepository;

    private final AgencyRepository agencyRepository;

    @Override
    public WorkflowResponse getTrainingTargetsByAgencyId(Long agencyId) throws DataException {

        List<TrainingTargets> targets;
        if(agencyId==-1) {
            targets = trainingTargetRepository.findAll();
            if(targets.isEmpty()) return WorkflowResponse.builder().status(400).message("Target Data not found").build();
        }
        else if(!agencyRepository.existsById(agencyId))return WorkflowResponse.builder().status(400).message("agency not found").build();
        else{
            targets = trainingTargetRepository.findByAgency_AgencyId(agencyId);
            if (targets.isEmpty()) return WorkflowResponse.builder().status(400).message("Targets  not assigned for this agency").build();
        }

        return WorkflowResponse.builder().status(200).message("Success")
                .data(TrainingTargetResponseMapper.buildResponse(targets))
                .build();
    }
}
