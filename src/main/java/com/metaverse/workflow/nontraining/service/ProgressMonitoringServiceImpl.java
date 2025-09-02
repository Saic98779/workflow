package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.expenditure.repository.ProgramExpenditureRepository;
import com.metaverse.workflow.model.*;
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
    private final TrainingProgramMapper trainingProgramMapper;   // for Training
    private final NonTrainingProgramMapper nonTrainingProgramMapper;
    private final ProgramExpenditureRepository programExpenditureRepository;
    private final NonTrainingExpenditureRepository nonTrainingExpenditureRepository;
    private final NonTrainingTargetRepository nonTrainingTargetRepository;
    private final TrainingTargetRepository trainingTargetRepository;

    @Override
    public ProgressMonitoringDto getAllTrainingAndNonTrainings(Long agencyId) {

        if (agencyId == null) {
            return ProgressMonitoringDto.builder()
                    .nonTrainingPrograms(Collections.emptyList())
                    .trainingPrograms(Collections.emptyList())
                    .build();
        }

        // Fetch program counts and expenditures
        List<Object[]> programsCountWithOneParticipant = Optional.ofNullable(
                        programRepository.countProgramsWithParticipantsByActivity(agencyId))
                .orElse(Collections.emptyList());

        List<Object[]> expenditure = Optional.ofNullable(
                        programExpenditureRepository.sumExpenditureByAgencyGroupedByActivity(agencyId))
                .orElse(Collections.emptyList());

        List<Object[]> nonTrainingExpenditure = Optional.ofNullable(
                        nonTrainingExpenditureRepository.sumExpenditureByAgencyGroupedByActivity(agencyId))
                .orElse(Collections.emptyList());

        // Map activityId -> program count
        Map<Long, Long> activityCountByPrograms = programsCountWithOneParticipant.stream()
                .filter(r -> r[0] != null && r[1] != null)
                .collect(Collectors.toMap(
                        r -> (Long) r[0],   // activityId
                        r -> (Long) r[1]    // count
                ));

        // Map activityId -> expenditure
        Map<Long, Double> expenditureByActivity = expenditure.stream()
                .filter(row -> row[0] != null && row[1] != null)
                .collect(Collectors.toMap(
                        row -> (Long) row[0],   // activityId
                        row -> (Double) row[1]  // sum of cost
                ));

        Map<Long, Double> nonTrainingExpenditureByActivity = nonTrainingExpenditure.stream()
                .filter(row -> row[0] != null && row[1] != null)
                .collect(Collectors.toMap(
                        row -> (Long) row[0],   // non-training activityId
                        row -> (Double) row[1]  // sum of cost
                ));

        // Fetch targets
        List<NonTrainingTargets> nonTrainingTargets = Optional.ofNullable(
                        nonTrainingTargetRepository.findByNonTrainingActivity_Agency_AgencyId(agencyId))
                .orElse(Collections.emptyList());

        List<TrainingTargets> trainingTargets = Optional.ofNullable(
                        trainingTargetRepository.findByActivity_Agency_AgencyId(agencyId))
                .orElse(Collections.emptyList());

        List<TrainingTargets> trainingTargetsList = trainingTargetRepository.findByAgency_AgencyId(agencyId);
/*
        // Map activityId -> total training targets
        Map<Long, Long> activityIdToTotalTargetsMap = trainingTargetsList.stream()
                .filter(tt -> tt.getActivity() != null && tt.getActivity().getActivityId() != null)
                .collect(Collectors.groupingBy(
                        tt -> tt.getActivity().getActivityId(),
                        Collectors.summingLong(tt -> {
                            long q1 = tt.getQ1Target() != null ? tt.getQ1Target() : 0;
                            long q2 = tt.getQ2Target() != null ? tt.getQ2Target() : 0;
                            long q3 = tt.getQ3Target() != null ? tt.getQ3Target() : 0;
                            long q4 = tt.getQ4Target() != null ? tt.getQ4Target() : 0;
                            return q1 + q2 + q3 + q4;
                        })
                ));

        // Map activityId -> total training budget
        Map<Long, Double> activityIdToTotalBudgetMap = trainingTargetsList.stream()
                .filter(tt -> tt.getActivity() != null && tt.getActivity().getActivityId() != null)
                .collect(Collectors.groupingBy(
                        tt -> tt.getActivity().getActivityId(),
                        Collectors.summingDouble(tt -> {
                            double q1 = tt.getQ1Budget() != null ? tt.getQ1Budget() : 0;
                            double q2 = tt.getQ2Budget() != null ? tt.getQ2Budget() : 0;
                            double q3 = tt.getQ3Budget() != null ? tt.getQ3Budget() : 0;
                            double q4 = tt.getQ4Budget() != null ? tt.getQ4Budget() : 0;
                            return q1 + q2 + q3 + q4;
                        })
                ));

        // Map non-training activityId -> total targets
        Map<Long, Long> nonActivityIdToTotalTargetsMap = nonTrainingTargets.stream()
                .filter(tt -> tt.getNonTrainingActivity() != null && tt.getNonTrainingActivity().getActivityId() != null)
                .collect(Collectors.groupingBy(
                        tt -> tt.getNonTrainingActivity().getActivityId(),
                        Collectors.summingLong(tt -> {
                            long q1 = tt.getQ1Target() != null ? tt.getQ1Target() : 0;
                            long q2 = tt.getQ2Target() != null ? tt.getQ2Target() : 0;
                            long q3 = tt.getQ3Target() != null ? tt.getQ3Target() : 0;
                            long q4 = tt.getQ4Target() != null ? tt.getQ4Target() : 0;
                            return q1 + q2 + q3 + q4;
                        })
                ));

        // Map non-training activityId -> total budget
        Map<Long, Double> nonActivityIdToTotalBudgetMap = nonTrainingTargets.stream()
                .filter(tt -> tt.getNonTrainingActivity() != null && tt.getNonTrainingActivity().getActivityId() != null)
                .collect(Collectors.groupingBy(
                        tt -> tt.getNonTrainingActivity().getActivityId(),
                        Collectors.summingDouble(tt -> {
                            double q1 = tt.getQ1Budget() != null ? tt.getQ1Budget() : 0;
                            double q2 = tt.getQ2Budget() != null ? tt.getQ2Budget() : 0;
                            double q3 = tt.getQ3Budget() != null ? tt.getQ3Budget() : 0;
                            double q4 = tt.getQ4Budget() != null ? tt.getQ4Budget() : 0;
                            return q1 + q2 + q3 + q4;
                        })
                ));

        // Build TrainingProgramDto list (unique per activity)
        List<TrainingProgramDto> trainingPrograms = activityIdToTotalTargetsMap.keySet().stream()
                .map(activityId -> {
                    Long achievedCount = Optional.ofNullable(activityCountByPrograms.get(activityId)).orElse(0L);
                    Double trainingExpenditure = Optional.ofNullable(expenditureByActivity.get(activityId)).orElse(0.0);

                    return trainingProgramMapper.trainingProgramDtoMapper(
                            Objects.requireNonNull(trainingTargetsList.stream()
                                    .filter(tt -> tt.getActivity() != null && tt.getActivity().getActivityId().equals(activityId))
                                    .findFirst()
                                    .orElse(null)), // representative TrainingTarget for meta info (agency, activity)
                            activityIdToTotalTargetsMap.get(activityId),
                            activityIdToTotalBudgetMap.get(activityId),
                            achievedCount,
                            trainingExpenditure
                    );
                })
                .toList();
*/
/*
        // Build NonTrainingProgramDto list (unique per non-training activity)
        List<NonTrainingProgramDto> nonTrainingPrograms = nonActivityIdToTotalTargetsMap.keySet().stream()
                .map(activityId -> {
                    Double nonTrainingExpenditure1 = Optional.ofNullable(nonTrainingExpenditureByActivity.get(activityId)).orElse(0.0);

                    return nonTrainingProgramMapper.nonTrainingProgramDtoMapper(
                            Objects.requireNonNull(nonTrainingTargets.stream()
                                    .filter(nt -> nt.getNonTrainingActivity() != null && nt.getNonTrainingActivity().getActivityId().equals(activityId))
                                    .findFirst()
                                    .orElse(null)), // representative NonTrainingTarget for meta info
                            nonActivityIdToTotalTargetsMap.get(activityId),
                            nonActivityIdToTotalBudgetMap.get(activityId),
                            nonTrainingExpenditure1
                    );
                })
                .toList();

*/
        return ProgressMonitoringDto.builder()
                .nonTrainingPrograms(null) //  replaced with null   actual variable. nonTrainingPrograms
                .trainingPrograms(null) //replaced with null   actual variable : trainingPrograms
                .build();
    }

}
