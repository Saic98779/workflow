package com.metaverse.workflow.trainingandnontrainingtarget.service;

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
                .activityName(request.getActivity().getActivityName())
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
}
