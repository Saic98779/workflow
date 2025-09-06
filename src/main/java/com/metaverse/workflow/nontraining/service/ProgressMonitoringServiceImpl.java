package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.ProgramMonitoring.service.SubActivityParticipantCountDTO;
import com.metaverse.workflow.activity.repository.ActivityRepository;
import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.expenditure.repository.ProgramExpenditureRepository;
import com.metaverse.workflow.model.*;
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
    private final NonTrainingResourceRepository nonTrainingResourcesRepository;
    private final ActivityRepository activityRepository;

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
                        (double) summary.totalTargets,
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

    @Override
    public List<TrainingProgramDto> getAllTrainingProgressMonitoringProgress(Long agencyId) {

        List<TrainingProgramDto> trainingProgressData = new ArrayList<>();
        Map<Long, String> activityNames = new HashMap<>();
        Map<Long, String> subActivityNames = new HashMap<>();
        Map<Long, Long> target = new HashMap<>();
        Map<Long, Long> countOfParticipants = new HashMap<>();
        Map<Long,Double> programExp = new HashMap<>();

        List<Activity> byAgencyAgencyId = activityRepository.findByAgencyAgencyId(agencyId);
        String agencyName = byAgencyAgencyId.get(0).getAgency().getAgencyName();

        for (Activity a : byAgencyAgencyId) {
            List<SubActivity> subActivities = a.getSubActivities();
            for (SubActivity sb : subActivities) {
                activityNames.put(sb.getSubActivityId(), a.getActivityName());
                subActivityNames.put(sb.getSubActivityId(), sb.getSubActivityName());
            }
        }

        Set<Long> longs = subActivityNames.keySet();

        List<TrainingTargets> allById = trainingTargetRepository.findBySubActivity_SubActivityIdInAndAgency_AgencyId(longs,agencyId);
        for (TrainingTargets targets : allById) {
            target.put(targets.getSubActivity().getSubActivityId(),  (targets.getQ1Target() + targets.getQ2Target() + targets.getQ3Target() + targets.getQ4Target()));
        }
        List<SubActivityParticipantCountDTO> participantCountByAgencyId = activityRepository.findProgramCountByAgencyId(agencyId);

        for (SubActivityParticipantCountDTO dto : participantCountByAgencyId) {
            countOfParticipants.put(dto.getSubActivityId(), Long.valueOf(dto.getParticipantCount()));
        }

        Map<Long,Double> budgetAllocated = new HashMap<>();
        for (TrainingTargets targets : allById) {
            budgetAllocated.put(targets.getSubActivity().getSubActivityId(),(targets.getQ1Budget() + targets.getQ2Budget() + targets.getQ3Budget() + targets.getQ4Budget()));
        }

        Iterable<ProgramExpenditure> bySubActivitySubActivityId = programExpenditureRepository.findBySubActivity_SubActivityIdInAndAgency_AgencyId(subActivityNames.keySet(),agencyId);

        for (ProgramExpenditure   p : bySubActivitySubActivityId){
            addOrUpdate(programExp,p.getSubActivity().getSubActivityId(),p.getCost());
        }

        Set<Long> commonKeys = new HashSet<>(subActivityNames.keySet());
        for (Long key : commonKeys) {
            Double target1 =  Double.valueOf(target.getOrDefault(key, 0L));
            Long countOfParticipants1 = countOfParticipants.getOrDefault(key, 0L);

            Double bud = budgetAllocated.getOrDefault(key, 0.0);
            Double programExpO = programExp.getOrDefault(key, 0.0);

            trainingProgressData.add( TrainingProgramDto.builder().agency(agencyName)
                    .activity(activityNames.get(key))
                    .subActivityId(key)
                    .subActivity(subActivityNames.get(key))
                    .trainingTarget(target1)
                    .trainingAchievement(countOfParticipants1)
                    .trainingPercentage((target1!=0 ? Math.round((countOfParticipants1/target1)* 100 * 1000.0)/1000.0 : 0.0))
                    .budgetAllocated(bud)
                    .expenditure(programExpO)
                    .expenditurePercentage((bud!=0.0) ? Math.round((programExpO/bud)* 100 * 1000.0)/1000.0 : 0.0)
                    .build());
        }
        return trainingProgressData;
    }

    private static void addOrUpdate(Map<Long, Double> map, Long key, Double value) {
        map.merge(key, value, Double::sum);
    }
}