package com.metaverse.workflow.programattendance.service;

import com.metaverse.workflow.model.ProgramAttendance;
import com.metaverse.workflow.model.ProgramAttendanceId;
import com.metaverse.workflow.programattendance.util.AttendanceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProgramAttendanceRequestMapper {

    public static List<ProgramAttendance> map(ProgramAttendanceRequest request) {
        List<ProgramAttendance> list = new ArrayList<>();
        for(ProgramAttendanceRequest.ParticipantAttendance attendance : request.getParticipantAttendanceList()) {
            list.add(createProgramAttendance(
                    request.getProgramId(), 
                    attendance.getParticipantId(), 
                    attendance.getAttendanceData()));
        }
        return list;
    }

    public static ProgramAttendance mapSingleParticipant(ParticipantAttendanceRequest request) {
        return createProgramAttendance(
                request.getProgramId(), 
                request.getParticipantId(), 
                request.getAttendanceData());
    }

    private static ProgramAttendance createProgramAttendance(Long programId, Long participantId, Character[] attendanceData) {
        return ProgramAttendance.builder()
                .programAttendanceId(ProgramAttendanceId.builder()
                        .programId(programId)
                        .participantId(participantId)
                        .build())
                .programAttendanceData(AttendanceUtil.characterArrayToString(attendanceData))
                .build();
    }
}
