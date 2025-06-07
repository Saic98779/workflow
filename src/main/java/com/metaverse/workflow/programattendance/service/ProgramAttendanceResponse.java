package com.metaverse.workflow.programattendance.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgramAttendanceResponse {

    private Long programId;
    private List<ParticipantAttendance> participantAttendanceList;

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ParticipantAttendance {
        private Long participantId;
        private String participantName;
        private String memberId;
        private String SHGName;
        private Long mobileNo;
        private String email;
        private Long aadharNo;
        private String designation;
        private Character[] attendanceData;
    }


}

