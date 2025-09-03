package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.model.TrainingTargets;
import com.metaverse.workflow.nontraining.dto.TrainingProgramDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainingProgramMapper {


    public TrainingProgramDto trainingProgramDtoMapper(
            TrainingTargets trainingTargets, Long totalTargets, Double totalBudget, Long achievedCount, Double financialExpenditure) {

        return TrainingProgramDto.builder()
                .activity(trainingTargets.getSubActivity().getActivity().getActivityName())
                .subActivity(trainingTargets.getSubActivity().getSubActivityName())
                .subActivityId(trainingTargets.getSubActivity().getSubActivityId())
                .agency(trainingTargets.getAgency().getAgencyName())
                .budgetAllocated(totalBudget)
                .expenditure(financialExpenditure)
                .trainingTarget(totalTargets)
                .trainingAchievement(achievedCount)

                .trainingPercentage(
                        Math.round(((double) achievedCount / totalTargets) * 100 * 1000.0) / 1000.0
                )
                .expenditurePercentage(financialExpenditure!=0.0?
                        Math.round((financialExpenditure / totalBudget * 100 * 1000.0) / 1000.0 ):0.0)
                .build();
    }
}

