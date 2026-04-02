package com.metaverse.workflow.program.service;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramSummaryDetailsDto {

    private Long id;
    private Long programId;
    private String executiveSummary;
    private String collegeDetails;
}