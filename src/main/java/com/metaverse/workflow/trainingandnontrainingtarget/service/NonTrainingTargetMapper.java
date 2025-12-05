package com.metaverse.workflow.trainingandnontrainingtarget.service;

import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.NonTrainingTargets;

public class NonTrainingTargetMapper {
    public static NonTrainingTargets mapToTrainingTarget(TargetRequest request, NonTrainingSubActivity subActivity) {
        NonTrainingTargets nonTrainingTarget = new NonTrainingTargets();
        nonTrainingTarget.setNonTrainingSubActivity(subActivity);
        nonTrainingTarget.setFinancialYear(request.getFinancialYear());

        nonTrainingTarget.setQ1Target(request.getQ1Target());
        nonTrainingTarget.setQ2Target(request.getQ2Target());
        nonTrainingTarget.setQ3Target(request.getQ3Target());
        nonTrainingTarget.setQ4Target(request.getQ4Target());

        nonTrainingTarget.setQ1Budget(request.getQ1Budget());
        nonTrainingTarget.setQ2Budget(request.getQ2Budget());
        nonTrainingTarget.setQ3Budget(request.getQ3Budget());
        nonTrainingTarget.setQ4Budget(request.getQ4Budget());

        return nonTrainingTarget;
    }

    public static Object mapToTrainingTargetResponse(NonTrainingTargets trainingTarget) {
        TargetResponse response = new TargetResponse();

        response.setTargetId(trainingTarget.getId());
        response.setSubActivityName(trainingTarget.getNonTrainingSubActivity().getSubActivityName());

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
