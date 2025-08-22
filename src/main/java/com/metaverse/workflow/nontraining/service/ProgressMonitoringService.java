package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.nontraining.dto.ProgressMonitoringDto;
import org.springframework.stereotype.Service;

@Service
public interface ProgressMonitoringService {

    ProgressMonitoringDto getAllTrainingAndNonTrainings(Long agencyId);
}
