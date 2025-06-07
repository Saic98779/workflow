package com.metaverse.workflow.programrawmaterial.service;

import com.metaverse.workflow.common.util.CommonUtil;
import com.metaverse.workflow.model.ProgramRawMaterial;

import java.util.ArrayList;
import java.util.List;

public class ProgramRawMaterialResponseMapper {
    public static ProgramRawMaterialResponse map(List<ProgramRawMaterial> rawMaterialList)
    {
        List<ProgramRawMaterialResponse.ParticipantRawMaterial> list=new ArrayList<>();
        Long programId = null;
        for(ProgramRawMaterial rawMaterial : rawMaterialList)
        {
            programId=rawMaterial.getProgramAttendanceId().getProgramId();
            Character [] charArray= null;
            String rawMaterials = rawMaterial.getProgramRawMaterialUsedData();
            if(rawMaterials != null && rawMaterials.length() > 0)
            {
                charArray =new Character[rawMaterials.length()];
                for(int i=0 ;i < rawMaterials.length(); i++)
                {
                    charArray[i]=rawMaterials.charAt(i);
                }
            }
            list.add(ProgramRawMaterialResponse.ParticipantRawMaterial
                    .builder()
                    .participantId(rawMaterial.getProgramAttendanceId().getParticipantId())
                    /*.participantName(CommonUtil.participantMap.get(rawMaterial.getProgramAttendanceId().getParticipantId()).getParticipantName())
                    .email(CommonUtil.participantMap.get(rawMaterial.getProgramAttendanceId().getParticipantId()).getEmail())
                    .aadharNo(CommonUtil.participantMap.get(rawMaterial.getProgramAttendanceId().getParticipantId()).getAadharNo())
                    .designation(CommonUtil.participantMap.get(rawMaterial.getProgramAttendanceId().getParticipantId()).getDesignation())
                    .SHGName(CommonUtil.participantMap.get(rawMaterial.getProgramAttendanceId().getParticipantId()).getOrganization().getNameOfTheSHG())
                    .mobileNo(CommonUtil.participantMap.get(rawMaterial.getProgramAttendanceId().getParticipantId()).getMobileNo())
                    .memberId(CommonUtil.participantMap.get(rawMaterial.getProgramAttendanceId().getParticipantId()).getMemberId())
                    */.rawMaterialData(charArray)
                    .build());
        }
        return ProgramRawMaterialResponse.builder().programId(programId).participantRawMaterialList(list).build();
    }
}
