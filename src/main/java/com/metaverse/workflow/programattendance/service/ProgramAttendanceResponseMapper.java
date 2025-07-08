package com.metaverse.workflow.programattendance.service;

import com.metaverse.workflow.model.ProgramAttendance;
import com.metaverse.workflow.programattendance.util.AttendanceUtil;

import java.util.ArrayList;
import java.util.List;

public class ProgramAttendanceResponseMapper {

    public static ProgramAttendanceResponse map(List<ProgramAttendance> attendanceList) {
        if (attendanceList == null || attendanceList.isEmpty()) {
            return ProgramAttendanceResponse.builder().build();
        }

        List<ProgramAttendanceResponse.ParticipantAttendance> list = new ArrayList<>();
        Long programId = null;

        for (ProgramAttendance attendance : attendanceList) {
            programId = attendance.getProgramAttendanceId().getProgramId();

            list.add(ProgramAttendanceResponse.ParticipantAttendance
                    .builder()
                    .participantId(attendance.getProgramAttendanceId().getParticipantId())
                    .attendanceData(AttendanceUtil.stringToCharacterArray(attendance.getProgramAttendanceData()))
                    .build());
        }

        return ProgramAttendanceResponse.builder()
                .programId(programId)
                .participantAttendanceList(list)
                .build();
    }
}
