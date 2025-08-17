package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.nontraining.dto.TrainingAndNonTrainingDto;
import org.springframework.stereotype.Service;

@Service
public interface ProgramMonitoringService {

    TrainingAndNonTrainingDto getAllTrainingAndNonTrainings(Long agencyId);
}
