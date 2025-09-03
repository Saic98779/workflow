package com.metaverse.workflow.nontraining.service;



import com.metaverse.workflow.nontraining.dto.NonTrainingActivityDto;
import com.metaverse.workflow.nontraining.dto.NonTrainingSubActivityDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NonTrainingActivityService {
    List<NonTrainingActivityDto> getAllActivitiesByAgency(Long agencyId);
    List<NonTrainingSubActivityDto> getAllSubActivitiesList(Long agencyId);
}