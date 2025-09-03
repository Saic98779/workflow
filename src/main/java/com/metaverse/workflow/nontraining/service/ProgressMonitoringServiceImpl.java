package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.expenditure.repository.ProgramExpenditureRepository;
import com.metaverse.workflow.model.NonTrainingTargets;
import com.metaverse.workflow.model.TrainingTargets;
import com.metaverse.workflow.model.TargetsBase;
import com.metaverse.workflow.nontraining.dto.NonTrainingProgramDto;
import com.metaverse.workflow.nontraining.dto.ProgressMonitoringDto;
import com.metaverse.workflow.nontraining.dto.TrainingProgramDto;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingExpenditureRepository;
import com.metaverse.workflow.program.repository.ProgramRepository;
import com.metaverse.workflow.trainingandnontrainingtarget.repository.NonTrainingTargetRepository;
import com.metaverse.workflow.trainingandnontrainingtarget.repository.TrainingTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public ProgressMonitoringDto getAllTrainingAndNonTrainings(Long agencyId) {
        if (agencyId == null) {
            return ProgressMonitoringDto.builder()
                    .trainingPrograms(List.of())
                    .nonTrainingPrograms(List.of())
                    .build();
        }

        // --- Programs & Expenditure ---
        Map<Long, Long> programCounts = toLongMap(
                programRepository.countProgramsWithParticipantsBySubActivity(agencyId)
        );
        Map<Long, Double> trainingExp = toDoubleMap(
                programExpenditureRepository.sumExpenditureByAgencyGroupedBySubActivity(agencyId)
        );
        Map<Long, Double> nonTrainingExp = toDoubleMap(
                nonTrainingExpenditureRepository.sumExpenditureByAgencyGroupedBySubActivity(agencyId)
        );

        // --- Training summary ---
        Map<Long, TargetSummary<TrainingTargets>> trainingSummary =
                buildSummary(trainingTargetRepository.findByAgency_AgencyId(agencyId),
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
        Map<Long, TargetSummary<NonTrainingTargets>> nonTrainingSummary =
                buildSummary(nonTrainingTargetRepository
                                .findByNonTrainingSubActivity_NonTrainingActivity_Agency_AgencyId(agencyId),
                        t -> (t.getNonTrainingSubActivity() != null &&
                                t.getNonTrainingSubActivity().getNonTrainingActivity() != null)
                                ? t.getNonTrainingSubActivity().getNonTrainingActivity().getActivityId()
                                : null);

        List<NonTrainingProgramDto> nonTrainingPrograms = nonTrainingSummary.entrySet().stream()
                .map(e -> nonTrainingProgramMapper.nonTrainingProgramDtoMapper(
                        e.getValue().representative,
                        e.getValue().totalTargets,
                        e.getValue().totalBudget,
                        nonTrainingExp.getOrDefault(e.getKey(), 0.0)
                ))
                .toList();

        return ProgressMonitoringDto.builder()
                .trainingPrograms(trainingPrograms)
                .nonTrainingPrograms(nonTrainingPrograms)
                .build();
    }

    // --- Helpers ---
    private Map<Long, Long> toLongMap(List<Object[]> rows) {
        return Optional.ofNullable(rows).orElse(List.of()).stream()
                .filter(r -> r[0] != null && r[1] != null)
                .collect(Collectors.toMap(
                        r -> (Long) r[0],
                        r -> (Long) r[1],
                        Long::sum
                ));
    }

    private Map<Long, Double> toDoubleMap(List<Object[]> rows) {
        return Optional.ofNullable(rows).orElse(List.of()).stream()
                .filter(r -> r[0] != null && r[1] != null)
                .collect(Collectors.toMap(
                        r -> (Long) r[0],
                        r -> (Double) r[1],
                        Double::sum
                ));
    }

    private <T extends TargetsBase> Map<Long, TargetSummary<T>> buildSummary(
            List<T> targets, java.util.function.Function<T, Long> idExtractor) {

        return Optional.ofNullable(targets).orElse(List.of()).stream()
                .map(t -> new AbstractMap.SimpleEntry<>(idExtractor.apply(t), t))
                .filter(e -> e.getKey() != null)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new TargetSummary<>(e.getValue()),
                        TargetSummary::merge
                ));
    }

    // --- Inner helper class ---
    private static class TargetSummary<T extends TargetsBase> {
        final T representative;
        long totalTargets;
        double totalBudget;

        TargetSummary(T target) {
            this.representative = target;
            this.totalTargets = sumTargets(target);
            this.totalBudget = sumBudgets(target);
        }

        static <T extends TargetsBase> TargetSummary<T> merge(TargetSummary<T> a, TargetSummary<T> b) {
            a.totalTargets += b.totalTargets;
            a.totalBudget += b.totalBudget;
            return a; // keep first representative
        }

        private static long sumTargets(TargetsBase t) {
            return Optional.ofNullable(t.getQ1Target()).orElse(0L)
                    + Optional.ofNullable(t.getQ2Target()).orElse(0L)
                    + Optional.ofNullable(t.getQ3Target()).orElse(0L)
                    + Optional.ofNullable(t.getQ4Target()).orElse(0L);
        }

        private static double sumBudgets(TargetsBase t) {
            return Optional.ofNullable(t.getQ1Budget()).orElse(0.0)
                    + Optional.ofNullable(t.getQ2Budget()).orElse(0.0)
                    + Optional.ofNullable(t.getQ3Budget()).orElse(0.0)
                    + Optional.ofNullable(t.getQ4Budget()).orElse(0.0);
        }
    }
}
