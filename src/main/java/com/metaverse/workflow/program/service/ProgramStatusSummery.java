package com.metaverse.workflow.program.service;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class ProgramStatusSummery {
    private Integer programScheduled;
    private Integer programInProcess;
    private Integer programsCompleted;
    private Integer programsCompletedDataPending;
    private Integer programYetBegin;
    private Integer programOverDue;
}
