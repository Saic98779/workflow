package com.metaverse.workflow.program.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProgramListDto {
    private Long programId;
    private String programName;
}
