package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.expenditure.repository.ProgramExpenditureRepository;
import com.metaverse.workflow.model.NonTrainingTargets;
import com.metaverse.workflow.model.TrainingTargets;
import com.metaverse.workflow.nontraining.dto.NonTrainingProgramDto;
import com.metaverse.workflow.nontraining.dto.ProgressMonitoringDto;
import com.metaverse.workflow.nontraining.dto.TrainingProgramDto;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingExpenditureRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingResourceRepository;
import com.metaverse.workflow.program.repository.ProgramRepository;
import com.metaverse.workflow.trainingandnontrainingtarget.repository.NonTrainingTargetRepository;
import com.metaverse.workflow.trainingandnontrainingtarget.repository.TrainingTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProgressMonitoringServiceImpl implements ProgressMonitoringService {

    private final ProgramRepository programRepository;
    private final TrainingProgramMapper trainingProgramMapper;
    private final NonTrainingProgramMapper nonTrainingProgramMapper;
    private final ProgramExpenditureRepository programExpenditureRepository;
    private final NonTrainingExpenditureRepository nonTrainingExpenditureRepository;
    private final NonTrainingTargetRepository nonTrainingTargetRepository;
    private final TrainingTargetRepository trainingTargetRepository;
    private final NonTrainingResourceRepository nonTrainingResourcesRepository;

    @Override
    public ProgressMonitoringDto getAllTrainingAndNonTrainings(Long agencyId) {
        // --- Fetch all necessary data once ---
        Map<Long, Long> programCounts = ProgressMonitoringUtils.toLongMap(
                programRepository.countProgramsWithParticipantsBySubActivity(agencyId)
        );

        Map<Long, Double> trainingExp = ProgressMonitoringUtils.toDoubleMap(
                programExpenditureRepository.sumExpenditureByAgencyGroupedBySubActivity(agencyId)
        );

        Map<Long, Double> nonTrainingExp = ProgressMonitoringUtils.toDoubleMap(
                nonTrainingExpenditureRepository.sumExpenditureByAgencyGroupedBySubActivity(agencyId)
        );

        // --- Training Programs ---
        Map<Long, ProgressMonitoringUtils.TargetSummary<TrainingTargets>> trainingSummary =
                ProgressMonitoringUtils.buildSummary(
                        trainingTargetRepository.findByAgency_AgencyId(agencyId),
                        t -> t.getSubActivity() != null ? t.getSubActivity().getSubActivityId() : null
                );

        List<TrainingProgramDto> trainingPrograms = new ArrayList<>();
        trainingSummary.forEach((key, summary) -> trainingPrograms.add(
                trainingProgramMapper.trainingProgramDtoMapper(
                        summary.representative,
                        summary.totalTargets,
                        summary.totalBudget,
                        programCounts.getOrDefault(key, 0L),
                        trainingExp.getOrDefault(key, 0.0)
                )
        ));

        // --- Non-Training Programs ---
        Map<Long, ProgressMonitoringUtils.TargetSummary<NonTrainingTargets>> nonTrainingSummary =
                ProgressMonitoringUtils.buildSummary(
                        nonTrainingTargetRepository
                                .findByNonTrainingSubActivity_NonTrainingActivity_Agency_AgencyId(agencyId),
                        t -> t.getNonTrainingSubActivity() != null &&
                                t.getNonTrainingSubActivity().getNonTrainingActivity() != null
                                ? t.getNonTrainingSubActivity().getNonTrainingActivity().getActivityId()
                                : null
                );

        List<NonTrainingProgramDto> nonTrainingPrograms = new ArrayList<>();
        nonTrainingSummary.forEach((key, summary) -> {
            NonTrainingTargets target = summary.representative;
            double expenditure = nonTrainingExp.getOrDefault(key, 0.0);

            if (target.getNonTrainingSubActivity() != null &&
                    "Contingency Fund".equalsIgnoreCase(
                            target.getNonTrainingSubActivity().getNonTrainingActivity().getActivityName()
                    )) {
                Double resourceExp = nonTrainingResourcesRepository
                        .sumExpenditureByActivityName(
                                target.getNonTrainingSubActivity().getNonTrainingActivity().getActivityName()
                        );
                expenditure = resourceExp != null ? resourceExp : 0.0;
            }

            nonTrainingPrograms.add(
                    nonTrainingProgramMapper.nonTrainingProgramDtoMapper(
                            target,
                            summary.totalTargets,
                            summary.totalBudget,
                            expenditure
                    )
            );
        });

        return ProgressMonitoringDto.builder()
                .trainingPrograms(trainingPrograms)
                .nonTrainingPrograms(nonTrainingPrograms)
                .build();
    }
}