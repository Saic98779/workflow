package com.metaverse.workflow.program.service;

import com.metaverse.workflow.model.ProgramType;

public class ProgramTypeResponseMapper {

    public static ProgramTypeResponse map(ProgramType programType) {
        return ProgramTypeResponse.builder()
                .programTypeId(programType.getProgramsId())
                .programType(programType.getProgramType())
                .agencyId(programType.getAgency().getAgencyId())
                .agencyName(programType.getAgency().getAgencyName())
                .build();
    }
}
