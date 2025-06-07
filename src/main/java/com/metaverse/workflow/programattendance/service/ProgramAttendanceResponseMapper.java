package com.metaverse.workflow.programattendance.service;

import com.metaverse.workflow.model.ProgramAttendance;

import java.util.ArrayList;
import java.util.List;

public class ProgramAttendanceResponseMapper {

    public static ProgramAttendanceResponse map(List<ProgramAttendance> attendanceList) {
        List<ProgramAttendanceResponse.ParticipantAttendance> list = new ArrayList<>();
        Long programId = null;
        for (ProgramAttendance attendance : attendanceList) {
            programId = attendance.getProgramAttendanceId().getProgramId();
            Character[] charArray = null;
            String attendances = attendance.getProgramAttendanceData();
            if (attendances != null && attendances.length() > 0) {
                charArray = new Character[attendances.length()];
                for (int i = 0; i < attendances.length(); i++) {
                    charArray[i] = attendances.charAt(i);
                }
            }
            list.add(ProgramAttendanceResponse.ParticipantAttendance
                    .builder()
                    .participantId(attendance.getProgramAttendanceId().getParticipantId())
                    .attendanceData(charArray)
                    .build());
        }
        return ProgramAttendanceResponse.builder().programId(programId).participantAttendanceList(list).build();
    }

}
