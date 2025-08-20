package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.expenditure.repository.ProgramExpenditureRepository;
import com.metaverse.workflow.model.NonTrainingActivity;
import com.metaverse.workflow.nontraining.dto.NonTrainingProgramDto;
import com.metaverse.workflow.nontraining.dto.ProgressMonitoringDto;
import com.metaverse.workflow.nontraining.dto.TrainingProgramDto;
import com.metaverse.workflow.nontraining.repository.NonTrainingActivityRepository;
import com.metaverse.workflow.nontraining.repository.TrainingBudgetAllocatedRepository;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressMonitoringServiceImpl implements ProgressMonitoringService {

    private final NonTrainingActivityRepository nonTrainingActivityRepository;

    private final TrainingBudgetAllocatedRepository trainingBudgetAllocatedRepository;

    private final ParticipantRepository participantRepository;

    private final TrainingProgramMapper trainingProgramMapper;

    private final ProgramExpenditureRepository programExpenditureRepository;

    @Override
    public ProgressMonitoringDto getAllTrainingAndNonTrainings(Long agencyId) {
        if (agencyId == null) {
            return ProgressMonitoringDto.builder()
                    .nonTrainingPrograms(Collections.emptyList())
                    .trainingPrograms(Collections.emptyList())
                    .build();
        }

        List<Object[]> results = participantRepository.countParticipantsByAgencyGroupedByActivity(agencyId);

        List<Object[]> expenditure = programExpenditureRepository.sumExpenditureByAgencyGroupedByActivity(agencyId);


        Map<Long, Long> activityToCount = results.stream()
                .collect(Collectors.toMap(
                        r -> (Long) r[0],   // activityId
                        r -> (Long) r[1]    // count
                ));

        Map<Long, Double> expenditureByActivity = expenditure.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],   // activityId
                        row -> (Double) row[1]  // sum of cost
                ));


        List<NonTrainingActivity> nonTrainingActivities =
                Optional.ofNullable(nonTrainingActivityRepository.findByAgency_AgencyId(agencyId))
                        .orElse(Collections.emptyList());

        List<TrainingProgramDto> trainingPrograms =
                Optional.ofNullable(trainingBudgetAllocatedRepository.findByAgency_AgencyId(agencyId))
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(t -> trainingProgramMapper.trainingProgramDtoMapper(t, activityToCount, expenditureByActivity))
                        .toList();

        List<NonTrainingProgramDto> nonTrainingPrograms =
                nonTrainingActivities.stream()
                        .map(NonTrainingProgramMapper::trainingProgramDtoMapper)
                        .toList();

        return ProgressMonitoringDto.builder()
                .nonTrainingPrograms(nonTrainingPrograms)
                .trainingPrograms(trainingPrograms)
                .build();
    }

}
