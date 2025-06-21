package com.metaverse.workflow.programattendance.service;

import com.metaverse.workflow.model.ProgramAttendance;
import com.metaverse.workflow.programattendance.util.AttendanceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper class for converting domain objects to response objects
 */
public class ProgramAttendanceResponseMapper {

    /**
     * Maps a list of ProgramAttendance objects to a ProgramAttendanceResponse
     * 
     * @param attendanceList List of ProgramAttendance objects
     * @return ProgramAttendanceResponse containing the mapped data
     */
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
