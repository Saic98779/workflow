package com.metaverse.workflow.program.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProgramRequestDto {
    private String programTitle;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String spocName;
    private Long spocContactNo;
    private Long locationId;
    private String status;
}
