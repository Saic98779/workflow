package com.metaverse.workflow.trainingtarget.service;

import com.metaverse.workflow.model.TrainingTarget;

import java.util.List;

public class TrainingTargetResponseMapper {

    public static TrainingTargetResponse mapTrainingTarget(TrainingTarget request) {
        return TrainingTargetResponse.builder()
                .trainingTargetId(request.getTrainingTargetId())
                .financialYear(request.getFinancialYear())
                .q1(request.getQ1())
                .q2(request.getQ2())
                .q3(request.getQ3())
                .q4(request.getQ4())
                .yearlyTarget(request.getQ1() + request.getQ2() + request.getQ3() + request.getQ4())
                .agencyName(request.getAgency().getAgencyName())
                .activityName(request.getActivity().getActivityName())
                .build();
    }

    public static TrainingTargetSummaryResponse buildResponse(List<TrainingTarget> targets) {
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
