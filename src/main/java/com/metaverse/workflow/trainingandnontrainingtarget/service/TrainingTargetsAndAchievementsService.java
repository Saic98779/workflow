package com.metaverse.workflow.trainingandnontrainingtarget.service;

import com.metaverse.workflow.trainingandnontrainingtarget.dtos.TrainingTargetsAndAchievementsResponse;

import java.util.List;

public interface TrainingTargetsAndAchievementsService {
    List<TrainingTargetsAndAchievementsResponse> getTargetsAndAchievements(String year, Long agencyId);
}
