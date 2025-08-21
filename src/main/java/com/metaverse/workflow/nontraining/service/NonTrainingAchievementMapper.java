package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.model.NonTrainingAchievement;
import com.metaverse.workflow.nontraining.dto.PhysicalFinancialDto;

public class NonTrainingAchievementMapper {


    public  static PhysicalFinancialDto PhysicalFinancialDtoMapper(NonTrainingAchievement nonTrainingAchievement){
      if(nonTrainingAchievement != null){
         return PhysicalFinancialDto.builder().nonTrainingAchievementId(nonTrainingAchievement.getNonTrainingAchievementId())
                  .nonTrainingActivityId(nonTrainingAchievement.getNonTrainingActivity().getActivityId())
                  .physicalTargetAchievement(nonTrainingAchievement.getPhysicalTargetAchievement())
                  .physicalTarget(nonTrainingAchievement.getPhysicalTarget())
                  .financialTargetAchievement(nonTrainingAchievement.getFinancialTargetAchievement())
                  .financialTarget(nonTrainingAchievement.getFinancialTarget())
                  .build();
      }else {
          return null;
      }
    }
}
