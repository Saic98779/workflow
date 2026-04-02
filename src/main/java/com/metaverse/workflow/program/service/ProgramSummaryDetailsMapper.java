package com.metaverse.workflow.program.service;


import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.model.ProgramSummaryDetails;

public class ProgramSummaryDetailsMapper {

    public static ProgramSummaryDetails toEntity(ProgramSummaryDetailsDto dto, Program program) {
        return ProgramSummaryDetails.builder()
                .program(program)
                .executiveSummary(dto.getExecutiveSummary())
                .collegeDetails(dto.getCollegeDetails())
                .build();
    }

    public static ProgramSummaryDetailsDto toDto(ProgramSummaryDetails entity) {
        return ProgramSummaryDetailsDto.builder()
                .id(entity.getId())
                .programId(entity.getProgram() != null ? entity.getProgram().getProgramId() : null)
                .executiveSummary(entity.getExecutiveSummary())
                .collegeDetails(entity.getCollegeDetails())
                .build();
    }
}
