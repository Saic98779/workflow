package com.metaverse.workflow.trainingandnontrainingtarget.service;

import com.metaverse.workflow.activity.repository.SubActivityRepository;
import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.SubActivity;
import com.metaverse.workflow.model.TrainingTargets;
import com.metaverse.workflow.trainingandnontrainingtarget.repository.TrainingTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingTargetServiceImpl implements TrainingTargetService {

    private final TrainingTargetRepository trainingTargetRepository;
    private final SubActivityRepository subActivityRepository;
    private final AgencyRepository agencyRepository;

    @Override
    public WorkflowResponse getTrainingTargetsByAgencyId(Long agencyId) throws DataException {

        List<TrainingTargets> targets = new ArrayList<>();
        if (agencyId == -1) {
            targets = trainingTargetRepository.findAll();
            if (targets.isEmpty())
                return WorkflowResponse.builder().status(400).message("Target Data not found").build();
        } else if (!agencyRepository.existsById(agencyId))
            return WorkflowResponse.builder().status(400).message("agency not found").build();
        else {
//            targets = trainingTargetRepository.findByAgency_AgencyId(agencyId);
            if (targets.isEmpty())
                return WorkflowResponse.builder().status(400).message("Targets  not assigned for this agency").build();
        }

        return WorkflowResponse.builder().status(200).message("Success")
                .data(TrainingTargetResponseMapper.buildResponse(targets))
                .build();
    }

    @Override
    public WorkflowResponse saveTrainingTarget(TargetRequest request) throws DataException {

        Agency agency = agencyRepository.findById(request.getAgencyId())
                .orElseThrow(() -> new DataException("Agency not found", "AGENCY_NOT_FOUND", 400));

        SubActivity subActivity = subActivityRepository.findById(request.getSubActivityId())
                .orElseThrow(() -> new RuntimeException("SubActivity not found"));

        Optional<TrainingTargets> existingTargetOpt =
                trainingTargetRepository.findByAgencyAndSubActivityAndFinancialYear(
                        agency, subActivity, request.getFinancialYear()
                );

        TrainingTargets trainingTarget;
        String message;

        if (existingTargetOpt.isPresent()) {
            trainingTarget = existingTargetOpt.get();

            if (request.getQ1Target() != null) trainingTarget.setQ1Target(request.getQ1Target());
            if (request.getQ2Target() != null) trainingTarget.setQ2Target(request.getQ2Target());
            if (request.getQ3Target() != null) trainingTarget.setQ3Target(request.getQ3Target());
            if (request.getQ4Target() != null) trainingTarget.setQ4Target(request.getQ4Target());

            if (request.getQ1Budget() != null) trainingTarget.setQ1Budget(request.getQ1Budget());
            if (request.getQ2Budget() != null) trainingTarget.setQ2Budget(request.getQ2Budget());
            if (request.getQ3Budget() != null) trainingTarget.setQ3Budget(request.getQ3Budget());
            if (request.getQ4Budget() != null) trainingTarget.setQ4Budget(request.getQ4Budget());

            message = "Target updated successfully";
        } else {
            trainingTarget = TrainingTargetResponseMapper.mapToTrainingTarget(request, agency, subActivity);
            message = "Target saved successfully";
        }
        trainingTargetRepository.save(trainingTarget);
         return WorkflowResponse.builder()
                 .data(TrainingTargetResponseMapper.mapToTrainingTargetResponse(trainingTarget))
                 .message(message)
                 .status(200)
                 .build();
    }

    @Override
    public WorkflowResponse getQuaterTrainingTargetsByAgencyId(Long agencyId) throws DataException {

        List<TrainingTargets> trainingTarget = trainingTargetRepository.findByAgency_AgencyId(agencyId);

        return WorkflowResponse.builder()
                .data(trainingTarget.stream()
                        .map(TrainingTargetResponseMapper::mapToTrainingTargetResponse)
                        .collect(Collectors.toList()))
                .message("Target fetched successfully")
                .status(200)
                .build();
    }

}
