package com.metaverse.workflow.counsellor.service;

import com.metaverse.workflow.model.CounsellorAllotment;
import com.metaverse.workflow.model.CounsellorRegistration;

public class CounsellorAllotmentMapper {

    public static CounsellorAllotment map(CounsellorRegistration counsellorRegistration)
    {
        return CounsellorAllotment.builder().counsellorRegistration(counsellorRegistration)
                .startDate(counsellorRegistration.getDateOfSelection())
                .build();
    }
}
