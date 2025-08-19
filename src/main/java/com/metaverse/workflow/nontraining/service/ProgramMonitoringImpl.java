package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.model.NonTrainingActivity;
import com.metaverse.workflow.nontraining.dto.ProgressMonitoringDto;
import com.metaverse.workflow.nontraining.repository.NonTrainingActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramMonitoringImpl implements  ProgramMonitoringService{

    private final NonTrainingActivityRepository nonTrainingActivityRepository;

    @Override
    public ProgressMonitoringDto getAllTrainingAndNonTrainings(Long agencyId) {
        List<NonTrainingActivity> byAgencyAgencyId = nonTrainingActivityRepository.findByAgency_AgencyId(agencyId);
        return ProgressMonitoringDto.builder()
                        .nonTrainingPrograms(byAgencyAgencyId.stream()
                        .map(NonTrainingProgramMapper::trainingProgramDtoMapper)
                        .toList()).build();
    }
}
