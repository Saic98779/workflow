package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.model.NonTrainingAchievement;
import com.metaverse.workflow.nontraining.dto.PhysicalFinancialDto;
import com.metaverse.workflow.nontraining.repository.NonTrainingAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Optional<NonTrainingAchievement> updateNonTrainingAchievement(Long nonTrainingAchievementId, PhysicalFinancialDto updatedRequest) {
        if (updatedRequest == null || updatedRequest.getNonTrainingAchievementId() == null)
            return Optional.empty();

        NonTrainingAchievement existing = nonTrainingAchievementRepository
                .findById(nonTrainingAchievementId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Non Training Achievement not found with id " + updatedRequest.getNonTrainingAchievementId()
                ));

        existing.setPhysicalTarget(updatedRequest.getPhysicalTarget());
        existing.setPhysicalTargetAchievement(updatedRequest.getPhysicalTargetAchievement());
        existing.setFinancialTarget(updatedRequest.getFinancialTarget());
        existing.setFinancialTargetAchievement(updatedRequest.getFinancialTargetAchievement());
        NonTrainingAchievement saved = nonTrainingAchievementRepository.save(existing);
        return Optional.of(saved);
    }

}
