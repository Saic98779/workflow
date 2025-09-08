package com.metaverse.workflow.trainingandnontrainingtarget.service;

import com.metaverse.workflow.trainingandnontrainingtarget.dtos.NonTrainingTargetsAndAchievementsResponse;
import com.metaverse.workflow.trainingandnontrainingtarget.dtos.TrainingTargetsAndAchievementsResponse;

import java.util.List;

public interface NonTrainingTargetsAndAchievementsService {
    List<NonTrainingTargetsAndAchievementsResponse> getTargetsAndAchievements(String year, Long agencyId);
    List<NonTrainingTargetsAndAchievementsResponse> getTargetsAndAchievementsHI(String financialYear, Long agencyId);
    }
