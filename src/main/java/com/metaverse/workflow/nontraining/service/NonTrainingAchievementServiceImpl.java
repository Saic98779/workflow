package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.model.NonTrainingAchievement;
import com.metaverse.workflow.nontraining.dto.PhysicalFinancialDto;
import com.metaverse.workflow.nontraining.repository.NonTrainingAchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NonTrainingAchievementServiceImpl implements NonTrainingAchievementService {

    @Autowired
    private NonTrainingAchievementRepository nonTrainingAchievementRepository;

    @Override
    public PhysicalFinancialDto getPhysicalFinancial(Long activityId) {

        NonTrainingAchievement physicalFinancialData = nonTrainingAchievementRepository.findByNonTrainingActivity_activityId(activityId);
        if (physicalFinancialData != null)
           return NonTrainingAchievementMapper.PhysicalFinancialDtoMapper(physicalFinancialData);
        return null;
    }

}
