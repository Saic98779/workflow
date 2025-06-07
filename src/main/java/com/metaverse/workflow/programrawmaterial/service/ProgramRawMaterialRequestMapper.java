package com.metaverse.workflow.programrawmaterial.service;

import com.metaverse.workflow.model.ProgramAttendanceId;
import com.metaverse.workflow.model.ProgramRawMaterial;

import java.util.ArrayList;
import java.util.List;

public class ProgramRawMaterialRequestMapper {

    public static List<ProgramRawMaterial> map(ProgramRawMaterialRequest request) {
        List<ProgramRawMaterial> list = new ArrayList<>();
        for (ProgramRawMaterialRequest.ParticipantRawMaterial rawMaterial : request.getParticipantRawMaterialList()) {
            StringBuffer sb = new StringBuffer();
            for (Character ch : rawMaterial.getRawMaterialData()) {
                sb.append(ch + "");
            }
            list.add(ProgramRawMaterial
                    .builder()
                    .programAttendanceId(ProgramAttendanceId.builder().programId(request.getProgramId()).participantId(rawMaterial.getParticipantId()).build())
                    .programRawMaterialUsedData(sb.toString())
                    .build());

        }
        return list;
    }

}
