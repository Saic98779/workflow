package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.model.NonTrainingAchievement;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.NonTrainingTargets;
import com.metaverse.workflow.nontraining.dto.PhysicalFinancialDto;
import com.metaverse.workflow.nontraining.repository.NonTrainingAchievementRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
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

    @Autowired
    private NonTrainingSubActivityRepository nonTrainingSubActivityRepository;

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

       Optional<NonTrainingAchievement> existing = nonTrainingAchievementRepository.findByNonTrainingSubActivity_SubActivityId(updatedRequest.getNonTrainingSubActivityId());
       if(existing.isPresent()) {
           NonTrainingAchievement nonTrainingAchievement = existing.get();
           nonTrainingAchievement.setPhysicalTargetAchievement(updatedRequest.getPhysicalTargetAchievement());
           nonTrainingAchievement.setFinancialTargetAchievement(updatedRequest.getFinancialTargetAchievement());
           NonTrainingAchievement saved = nonTrainingAchievementRepository.save(nonTrainingAchievement);
           return Optional.of(saved);
       }else {
           NonTrainingAchievement achievement = new NonTrainingAchievement();
           Optional<NonTrainingSubActivity> byId = nonTrainingSubActivityRepository.findById(updatedRequest.getNonTrainingSubActivityId());
           if(byId.isPresent()) {
               achievement.setPhysicalTargetAchievement(updatedRequest.getPhysicalTargetAchievement());
               achievement.setFinancialTargetAchievement(updatedRequest.getFinancialTargetAchievement());
               NonTrainingSubActivity nonTrainingSubActivity = byId.get();
               achievement.setNonTrainingSubActivity(nonTrainingSubActivity);
               achievement.setNonTrainingActivity(nonTrainingSubActivity.getNonTrainingActivity());
               NonTrainingAchievement save = nonTrainingAchievementRepository.save(achievement);
               return Optional.of(save);
           }

       }
       return Optional.empty();
    }

}
