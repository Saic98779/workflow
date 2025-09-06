package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.nontraining.dto.ProgressMonitoringDto;
import com.metaverse.workflow.nontraining.dto.TrainingProgramDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProgressMonitoringService {

    ProgressMonitoringDto getAllNonTrainingsSummary(Long agencyId);
    List<TrainingProgramDto> getAllTrainingProgressMonitoringProgress(Long agencyId);
}
