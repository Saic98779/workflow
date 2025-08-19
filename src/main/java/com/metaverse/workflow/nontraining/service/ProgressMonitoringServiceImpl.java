package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.model.NonTrainingActivity;
import com.metaverse.workflow.nontraining.dto.NonTrainingProgramDto;
import com.metaverse.workflow.nontraining.dto.ProgressMonitoringDto;
import com.metaverse.workflow.nontraining.dto.TrainingProgramDto;
import com.metaverse.workflow.nontraining.repository.NonTrainingActivityRepository;
import com.metaverse.workflow.nontraining.repository.TrainingBudgetAllocatedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgressMonitoringServiceImpl implements ProgressMonitoringService {

    private final NonTrainingActivityRepository nonTrainingActivityRepository;

    private final TrainingBudgetAllocatedRepository trainingBudgetAllocatedRepository;

    @Override
    public ProgressMonitoringDto getAllTrainingAndNonTrainings(Long agencyId) {
        if (agencyId == null) {
            return ProgressMonitoringDto.builder()
                    .nonTrainingPrograms(Collections.emptyList())
                    .trainingPrograms(Collections.emptyList())
                    .build();
        }

        List<NonTrainingActivity> nonTrainingActivities =
                Optional.ofNullable(nonTrainingActivityRepository.findByAgency_AgencyId(agencyId))
                        .orElse(Collections.emptyList());

        List<TrainingProgramDto> trainingPrograms =
                Optional.ofNullable(trainingBudgetAllocatedRepository.findByAgency_AgencyId(agencyId))
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(TrainingProgramMapper::trainingProgramDtoMapper)
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
