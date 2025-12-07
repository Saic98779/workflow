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

        response.setPhysicalTargetQ1(trainingTarget.getQ1Target());
        response.setPhysicalTargetQ2(trainingTarget.getQ2Target());
        response.setPhysicalTargetQ3(trainingTarget.getQ3Target());
        response.setPhysicalTargetQ4(trainingTarget.getQ4Target());

        response.setFinancialTargetQ1(trainingTarget.getQ1Budget());
        response.setFinancialTargetQ2(trainingTarget.getQ2Budget());
        response.setFinancialTargetQ3(trainingTarget.getQ3Budget());
        response.setFinancialTargetQ4(trainingTarget.getQ4Budget());

        response.setFinancialYear(trainingTarget.getFinancialYear());

        return response;
    }
}
