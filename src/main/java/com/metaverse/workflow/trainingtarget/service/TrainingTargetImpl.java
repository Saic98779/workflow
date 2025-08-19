package com.metaverse.workflow.trainingtarget.service;

import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.TrainingTarget;
import com.metaverse.workflow.trainingtarget.repository.TrainingTargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingTargetImpl implements TrainingTargetService{

    @Autowired
    private TrainingTargetRepository trainingTargetRepository;

    @Autowired
    private AgencyRepository agencyRepository;

    @Override
    public WorkflowResponse getTrainingTargetsByAgencyId(Long agencyId) throws DataException {

        List<TrainingTarget> targets;
        if(agencyId==-1) {
            targets = trainingTargetRepository.findAll();
            if(targets.isEmpty()) return WorkflowResponse.builder().status(400).message("Target Data not found").build();
        }
        else if(!agencyRepository.existsById(agencyId))return WorkflowResponse.builder().status(400).message("agency not found").build();
        else{
            targets = trainingTargetRepository.findByAgencyId_AgencyId(agencyId);
            if (targets.isEmpty()) return WorkflowResponse.builder().status(400).message("Targets  not assigned for this agency").build();
        }

        return WorkflowResponse.builder().status(200).message("Success")
                .data(TrainingTargetResponseMapper.buildResponse(targets))
                .build();
    }
}
