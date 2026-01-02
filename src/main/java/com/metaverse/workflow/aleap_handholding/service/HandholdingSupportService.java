package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.repository.HandholdingSupportRepository;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.aleap_handholding.HandholdingSupport;
import com.metaverse.workflow.nontraining.repository.NonTrainingActivityRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HandholdingSupportService {

    private final HandholdingSupportRepository supportRepo;
    private final NonTrainingActivityRepository activityRepo;
    private final NonTrainingSubActivityRepository subActivityRepo;


    public HandholdingSupport getOrCreateSupport(
            Long supportId,
            Long activityId,
            Long subActivityId,
            String handholdingType
    ) throws DataException {

        if (supportId != null) {
            return supportRepo.findById(supportId)
                    .orElseThrow(() ->
                            new DataException(
                                    "HandholdingSupport not found with id " + supportId,
                                    "HANDHOLDING_SUPPORT_NOT_FOUND",
                                    400
                            ));
        }

        HandholdingSupport support = HandholdingSupport.builder()
                .nonTrainingActivity(
                        activityRepo.findById(activityId)
                                .orElseThrow(() ->
                                        new DataException(
                                                "Activity not found with id " + activityId,
                                                "ACTIVITY_NOT_FOUND",
                                                400
                                        ))
                )
                .nonTrainingSubActivity(
                        subActivityRepo.findById(subActivityId)
                                .orElseThrow(() ->
                                        new DataException("SubActivity not found with id " + subActivityId,
                                                "SUB_ACTIVITY_NOT_FOUND",
                                                400
                                        ))
                )
                .handholdingSupportType(handholdingType)
                .build();

        return supportRepo.save(support);
    }
}
