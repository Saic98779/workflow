package com.metaverse.workflow.programattendance.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ParticipantAttendanceRequest {
    private Long programId;
    private Long participantId;
    private Character[] attendanceData;
}
