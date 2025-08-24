package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.model.NonTrainingAchievement;
import com.metaverse.workflow.nontraining.dto.PhysicalFinancialDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface NonTrainingAchievementService {
    PhysicalFinancialDto getPhysicalFinancial(Long activityId);
    Optional<NonTrainingAchievement> updateNonTrainingAchievement(Long nonTrainingAchievementId, PhysicalFinancialDto request);
}
