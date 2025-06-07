package com.metaverse.workflow.program.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ProgramTypeRequest {
    private Long agencyId;
    private String programType;
}
