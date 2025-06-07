package com.metaverse.workflow.programattendance.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ProgramAttendanceRequest {

    private Long programId;
    private List<ParticipantAttendance> participantAttendanceList;

    @Builder
    @AllArgsConstructor
    @Getter
    @Setter
    public static class ParticipantAttendance {
        private Long participantId;
        private Character[] attendanceData;
    }

}
