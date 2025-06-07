package com.metaverse.workflow.programrawmaterial.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.metaverse.workflow.programattendance.service.ProgramAttendanceResponse;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgramRawMaterialResponse {
    private Long programId;
    private List<ParticipantRawMaterial> participantRawMaterialList;

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ParticipantRawMaterial {
        private Long participantId;
        private String participantName;
        private String memberId;
        private String SHGName;
        private Long mobileNo;
        private String email;
        private Long aadharNo;
        private String designation;
        private Character[] rawMaterialData;
    }

}
