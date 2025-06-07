package com.metaverse.workflow.programattendance.service;

import com.metaverse.workflow.model.ProgramAttendance;
import com.metaverse.workflow.model.ProgramAttendanceId;

import java.util.ArrayList;
import java.util.List;


public class ProgramAttendanceRequestMapper {
    public static List<ProgramAttendance> map(ProgramAttendanceRequest request) {
        List<ProgramAttendance> list = new ArrayList<>();
        for(ProgramAttendanceRequest.ParticipantAttendance attendance : request.getParticipantAttendanceList()) {
            StringBuffer sb = new StringBuffer();
            for(Character ch : attendance.getAttendanceData()) {
                sb.append(ch+"");
            }
            list.add(ProgramAttendance
                    .builder()
                    .programAttendanceId(ProgramAttendanceId.builder().programId(request.getProgramId()).participantId(attendance.getParticipantId()).build())
                    .programAttendanceData(sb.toString())
                    .build());
        }
        return list;
    }
}
