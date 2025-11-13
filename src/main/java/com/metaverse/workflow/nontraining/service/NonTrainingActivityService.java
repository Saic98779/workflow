package com.metaverse.workflow.nontraining.service;



import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.enums.BillRemarksStatus;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.expenditure.service.ExpenditureRemarksDTO;
import com.metaverse.workflow.nontraining.dto.NonTrainingActivityDto;
import com.metaverse.workflow.nontraining.dto.NonTrainingSubActivityDto;
import com.metaverse.workflow.nontrainingExpenditures.service.NonTrainingExpenditureRemarksDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NonTrainingActivityService {
    List<NonTrainingActivityDto> getAllActivitiesByAgency(Long agencyId);
    List<NonTrainingSubActivityDto> getAllSubActivitiesList(Long agencyId);

}