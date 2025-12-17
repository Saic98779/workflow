package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.nontraining.dto.NonTrainingProgramDto;
import com.metaverse.workflow.nontraining.dto.ProgressMonitoringDto;
import com.metaverse.workflow.nontraining.dto.TrainingProgramDto;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface ProgressMonitoringService {

    ProgressMonitoringDto getAllNonTrainingsSummary(Long agencyId);
    List<TrainingProgramDto> getAllTrainingProgressMonitoringProgress(Long agencyId);
    List<NonTrainingProgramDto> nonTrainingProgressMonitoring(Long AgencyId);
    List<TrainingProgramDto> getAllTrainingProgress(Long agencyId);

     List<TrainingProgramDto> getAllTrainingProgressMonitoringProgress(
            Long agencyId,
            Date fromDate,
            Date toDate
    );

}
