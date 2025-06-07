package com.metaverse.workflow.programrawmaterial.service;

import com.metaverse.workflow.programattendance.service.ProgramAttendanceRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ProgramRawMaterialRequest {

    private Long programId;
    private List<ParticipantRawMaterial> participantRawMaterialList;

    @Builder
    @AllArgsConstructor
    @Getter
    @Setter
    public static class ParticipantRawMaterial {
        private Long participantId;
        private Character[] rawMaterialData;
    }

}
