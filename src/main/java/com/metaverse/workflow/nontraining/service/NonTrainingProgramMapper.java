package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.model.NonTrainingAchievement;
import com.metaverse.workflow.model.NonTrainingActivity;
import com.metaverse.workflow.nontraining.dto.NonTrainingProgramDto;
import com.metaverse.workflow.trainingandnontrainingtarget.repository.OutcomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NonTrainingProgramMapper {

    private final OutcomeRepository outcomeRepository;

    public NonTrainingProgramDto trainingProgramDtoMapper(NonTrainingActivity nonTrainingActivity, Long agencyId) {
        if (nonTrainingActivity == null || nonTrainingActivity.getAchievements().isEmpty()) {
            return null;
        }

        NonTrainingAchievement ach = nonTrainingActivity.getAchievements().get(0);

        Double percentage = null;

        if (ach.getFinancialTarget() != null && ach.getFinancialTarget() > 0
                && ach.getFinancialTargetAchievement() != null) {

            // Convert Target (Crores) → Rupees
            double targetInRupees = ach.getFinancialTarget() * 10000000; // 1 crore = 1,00,00,000 rupees
            double expenditureInRupees = ach.getFinancialTargetAchievement();

            percentage = (expenditureInRupees / targetInRupees) * 100;
        }

        // handle physical achievement logic separately
        String physicalAchievement;
//        if (ach.getPhysicalTargetAchievement().equals("1")) {
//            physicalAchievement = ach.getPhysicalTargetAchievement();
//        } else {
//            physicalAchievement = String.valueOf(outcomeRepository.getTotalOutcomesByAgency(agencyId));
//        }

        physicalAchievement = ach.getPhysicalTargetAchievement();

        return NonTrainingProgramDto.builder()
                .nonTrainingActivity(nonTrainingActivity.getActivityName())
                .physicalTarget(ach.getPhysicalTarget())
                .applicable("Not Initiated".equalsIgnoreCase(String.valueOf(ach.getPhysicalTargetAchievement()))
                        ? "Not Applicable" : "100.00%")
                .physicalAchievement(String.valueOf(physicalAchievement))
                .financialTarget(ach.getFinancialTarget())
                .financialExpenditure(ach.getFinancialTargetAchievement())
                .percentage(percentage)
                .build();
    }
}
