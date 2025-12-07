package com.metaverse.workflow.trainingandnontrainingtarget.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.trainingandnontrainingtarget.dtos.NonTrainingTargetsAndAchievementsResponse;
import com.metaverse.workflow.trainingandnontrainingtarget.dtos.TrainingTargetsAndAchievementsResponse;

import java.util.List;

public interface NonTrainingTargetsAndAchievementsService {
    List<NonTrainingTargetsAndAchievementsResponse> getTargetsAndAchievements(String year, Long agencyId);
    WorkflowResponse saveNonTrainingTarget(TargetRequest request)throws DataException;
    List<TargetResponse> getNonTrainingTargets(String year, Long agencyId);
}
