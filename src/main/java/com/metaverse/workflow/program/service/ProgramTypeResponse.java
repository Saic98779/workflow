package com.metaverse.workflow.program.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ProgramTypeResponse {

    private Integer programTypeId;
    private String programType;
    private Long agencyId;
    private String agencyName;

}
