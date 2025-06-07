package com.metaverse.workflow.programattendance.service;

import com.metaverse.workflow.common.response.WorkflowResponse;

public interface ProgramAttendanceService {
    WorkflowResponse attendanceByProgramId(Long programId, int page, int size);
    WorkflowResponse updateProgramAttendance(ProgramAttendanceRequest request);
}
