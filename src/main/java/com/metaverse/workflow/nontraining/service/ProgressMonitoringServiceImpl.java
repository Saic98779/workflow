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

import java.util.List;
import java.util.Map;

/**
 * Service for retrieving progress monitoring data for training and non-training programs.
 * Fetches program counts, expenditures, and target summaries by agency.
 */
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

    /**
     * Get all training and non-training program summaries for a given agency.
     * @param agencyId Agency ID to fetch data for
     * @return ProgressMonitoringDto containing training and non-training summaries
     */
    @Override
    public ProgressMonitoringDto getAllTrainingAndNonTrainings(Long agencyId) {
        if (agencyId == null) {
            return ProgressMonitoringDto.builder()
                    .trainingPrograms(List.of())
                    .nonTrainingPrograms(List.of())
                    .build();
        }

        // --- Programs & Expenditure ---
        Map<Long, Long> programCounts = ProgressMonitoringUtils.toLongMap(
                programRepository.countProgramsWithParticipantsBySubActivity(agencyId)
        );

        Map<Long, Double> trainingExp = ProgressMonitoringUtils.toDoubleMap(
                programExpenditureRepository.sumExpenditureByAgencyGroupedBySubActivity(agencyId)
        );

        Map<Long, Double> nonTrainingExp = ProgressMonitoringUtils.toDoubleMap(
                nonTrainingExpenditureRepository.sumExpenditureByAgencyGroupedBySubActivity(agencyId)
        );

        // --- Training summary ---
        Map<Long, ProgressMonitoringUtils.TargetSummary<TrainingTargets>> trainingSummary =
                ProgressMonitoringUtils.buildSummary(trainingTargetRepository.findByAgency_AgencyId(agencyId),
                        t -> t.getSubActivity() != null ? t.getSubActivity().getSubActivityId() : null);

        List<TrainingProgramDto> trainingPrograms = trainingSummary.entrySet().stream()
                .map(e -> trainingProgramMapper.trainingProgramDtoMapper(
                        e.getValue().representative,
                        e.getValue().totalTargets,
                        e.getValue().totalBudget,
                        programCounts.getOrDefault(e.getKey(), 0L),
                        trainingExp.getOrDefault(e.getKey(), 0.0)
                ))
                .toList();

        // --- Non-training summary ---
        Map<Long, ProgressMonitoringUtils.TargetSummary<NonTrainingTargets>> nonTrainingSummary =
                ProgressMonitoringUtils.buildSummary(nonTrainingTargetRepository
                                .findByNonTrainingSubActivity_NonTrainingActivity_Agency_AgencyId(agencyId),
                        t -> (t.getNonTrainingSubActivity() != null &&
                                t.getNonTrainingSubActivity().getNonTrainingActivity() != null)
                                ? t.getNonTrainingSubActivity().getNonTrainingActivity().getActivityId()
                                : null);

        List<NonTrainingProgramDto> nonTrainingPrograms = nonTrainingSummary.entrySet().stream()
                .map(e -> {
                    NonTrainingTargets target = e.getValue().representative;
                    double expenditure;
            // If activity is Contingency Fund, read from NonTrainingResources
                    if (target.getNonTrainingSubActivity() != null &&
                            "Contingency Fund".equalsIgnoreCase(
                                    target.getNonTrainingSubActivity().getNonTrainingActivity().getActivityName()
                            )) {
                        expenditure = nonTrainingResourcesRepository
                                .sumExpenditureByActivityName(
                                        target.getNonTrainingSubActivity().getNonTrainingActivity().getActivityName()
                                );
                    } else {
                        expenditure = nonTrainingExp.getOrDefault(e.getKey(), 0.0);
                    }
                    return nonTrainingProgramMapper.nonTrainingProgramDtoMapper(
                            target,
                            e.getValue().totalTargets,
                            e.getValue().totalBudget,
                            expenditure
                    );
                })
                .toList();

        return ProgressMonitoringDto.builder()
                .trainingPrograms(trainingPrograms)
                .nonTrainingPrograms(nonTrainingPrograms)
                .build();
    }
}
