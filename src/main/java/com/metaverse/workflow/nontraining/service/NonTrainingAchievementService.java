package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.nontraining.dto.PhysicalFinancialDto;
import org.springframework.stereotype.Service;

@Service
public interface NonTrainingAchievementService {
    PhysicalFinancialDto getPhysicalFinancial(Long activityId);
}
