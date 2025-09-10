package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.model.NonTrainingAchievement;
import com.metaverse.workflow.model.NonTrainingTargets;
import com.metaverse.workflow.nontraining.dto.PhysicalFinancialDto;

import java.util.List;
import java.util.Optional;

public class NonTrainingAchievementMapper {


    public  static PhysicalFinancialDto PhysicalFinancialDtoMapper(NonTrainingAchievement nonTrainingAchievement, List<NonTrainingTargets> nonTrainingTargets){
      if(nonTrainingAchievement != null){

          Long QTarget= 0L;
          Double QBudget = 0.0;

          for(NonTrainingTargets targets : nonTrainingTargets){
              QTarget += Optional.ofNullable(targets.getQ1Target()).orElse(0L)
                      + Optional.ofNullable(targets.getQ2Target()).orElse(0L)
                      + Optional.ofNullable(targets.getQ3Target()).orElse(0L)
                      + Optional.ofNullable(targets.getQ4Target()).orElse(0L);

              QBudget += Optional.ofNullable(targets.getQ1Budget()).orElse(0.0)
                      + Optional.ofNullable(targets.getQ2Budget()).orElse(0.0)
                      + Optional.ofNullable(targets.getQ3Budget()).orElse(0.0)
                      + Optional.ofNullable(targets.getQ4Budget()).orElse(0.0);

          }
         return PhysicalFinancialDto.builder().nonTrainingAchievementId(nonTrainingAchievement.getNonTrainingAchievementId())
                  .nonTrainingActivityId(nonTrainingAchievement.getNonTrainingActivity().getActivityId())
                  .physicalTargetAchievement(nonTrainingAchievement.getPhysicalTargetAchievement())
                 .financialTarget(QBudget)
                 .physicalTarget(QTarget)
                  .financialTargetAchievement(nonTrainingAchievement.getFinancialTargetAchievement())
                  .build();
      }else {
          return null;
      }
    }
}
