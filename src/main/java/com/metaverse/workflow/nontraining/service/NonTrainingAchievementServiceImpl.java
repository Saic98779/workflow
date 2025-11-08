package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.model.NonTrainingAchievement;
import com.metaverse.workflow.model.NonTrainingTargets;
import com.metaverse.workflow.nontraining.dto.PhysicalFinancialDto;
import com.metaverse.workflow.nontraining.repository.NonTrainingAchievementRepository;
import com.metaverse.workflow.trainingandnontrainingtarget.repository.NonTrainingTargetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NonTrainingAchievementServiceImpl implements NonTrainingAchievementService {

    @Autowired
    private NonTrainingAchievementRepository nonTrainingAchievementRepository;

    @Autowired
    private NonTrainingTargetRepository nonTrainingTargetRepository;

    @Override
    public PhysicalFinancialDto getPhysicalFinancial(Long subActivityId) {

        List<NonTrainingTargets>  physicalFinancialTargets= nonTrainingTargetRepository.findByNonTrainingSubActivity_subActivityId(subActivityId);
        NonTrainingAchievement achievement = nonTrainingAchievementRepository.findByNonTrainingSubActivity_subActivityId(subActivityId);
        if (physicalFinancialTargets != null)
            return NonTrainingAchievementMapper.PhysicalFinancialDtoMapper(subActivityId, physicalFinancialTargets, achievement);
        return null;
    }

    public Optional<NonTrainingAchievement> updateNonTrainingAchievement(Long nonTrainingAchievementId, PhysicalFinancialDto updatedRequest) {
        if (updatedRequest == null || updatedRequest.getNonTrainingSubActivityId() == null)
            return Optional.empty();

        NonTrainingAchievement existing = nonTrainingAchievementRepository
                .findById(nonTrainingAchievementId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Non Training Sub Activity found with id " + updatedRequest.getNonTrainingSubActivityId()
                ));
        existing.setPhysicalTargetAchievement(updatedRequest.getPhysicalTargetAchievement());
        existing.setFinancialTargetAchievement(updatedRequest.getFinancialTargetAchievement());
        NonTrainingAchievement saved = nonTrainingAchievementRepository.save(existing);
        return Optional.of(saved);
    }

}
