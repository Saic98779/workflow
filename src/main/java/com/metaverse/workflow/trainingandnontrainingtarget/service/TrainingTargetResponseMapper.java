package com.metaverse.workflow.trainingandnontrainingtarget.service;

import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.SubActivity;
import com.metaverse.workflow.model.TrainingTargets;

import java.util.List;

public class TrainingTargetResponseMapper {

    public static TrainingTargetResponse mapTrainingTarget(TrainingTargets request) {
        return TrainingTargetResponse.builder()
                .trainingTargetId(request.getTrainingTargetId())
                .financialYear(request.getFinancialYear())
                .q1(request.getQ1Target())
                .q2(request.getQ2Target())
                .q3(request.getQ3Target())
                .q4(request.getQ4Target())
                .yearlyTarget(request.getQ1Target() + request.getQ2Target() + request.getQ3Target() + request.getQ4Target())
                .agencyName(request.getAgency().getAgencyName())
//                .activityName(request.getActivity().getActivityName())
                .build();
    }

    public static TrainingTargetSummaryResponse buildResponse(List<TrainingTargets> targets) {
        List<TrainingTargetResponse> responses = targets.stream()
                .map(TrainingTargetResponseMapper::mapTrainingTarget)
                .toList();

        List<String> headers = responses.stream()
                .map(TrainingTargetResponse::getFinancialYear)
                .distinct()
                .toList();

        Double overallTarget = responses.stream()
                .mapToDouble(TrainingTargetResponse::getYearlyTarget)
                .sum();

        return TrainingTargetSummaryResponse.builder()
                .financialYearHeaders(headers)
                .overallTarget(overallTarget)
                .financialYear(responses)
                .build();
    }

    public static TrainingTargets mapToTrainingTarget(TargetRequest request, Agency agency, SubActivity subActivity) {

        TrainingTargets trainingTarget = new TrainingTargets();
        trainingTarget.setAgency(agency);
        trainingTarget.setSubActivity(subActivity);
        trainingTarget.setFinancialYear(request.getFinancialYear());

        trainingTarget.setQ1Target(request.getQ1Target());
        trainingTarget.setQ2Target(request.getQ2Target());
        trainingTarget.setQ3Target(request.getQ3Target());
        trainingTarget.setQ4Target(request.getQ4Target());

        trainingTarget.setQ1Budget(request.getQ1Budget());
        trainingTarget.setQ2Budget(request.getQ2Budget());
        trainingTarget.setQ3Budget(request.getQ3Budget());
        trainingTarget.setQ4Budget(request.getQ4Budget());

        return trainingTarget;
    }
    public static TargetResponse mapToTrainingTargetResponse(TrainingTargets trainingTarget) {

        TargetResponse response = new TargetResponse();

        response.setTargetId(trainingTarget.getTrainingTargetId());
        response.setAgencyName(trainingTarget.getAgency().getAgencyName()); // or getAgencyName() based on your model
        response.setSubActivityName(trainingTarget.getSubActivity().getSubActivityName()); // or getName()

        response.setQ1Target(trainingTarget.getQ1Target());
        response.setQ2Target(trainingTarget.getQ2Target());
        response.setQ3Target(trainingTarget.getQ3Target());
        response.setQ4Target(trainingTarget.getQ4Target());

        response.setQ1Budget(trainingTarget.getQ1Budget());
        response.setQ2Budget(trainingTarget.getQ2Budget());
        response.setQ3Budget(trainingTarget.getQ3Budget());
        response.setQ4Budget(trainingTarget.getQ4Budget());

        response.setFinancialYear(trainingTarget.getFinancialYear());

        return response;
    }

}
