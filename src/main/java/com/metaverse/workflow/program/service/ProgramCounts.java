package com.metaverse.workflow.program.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramCounts {
    private String agencyName;
    private Integer scheduledOnDate;
    private Integer fullyUploaded;
    private Integer partiallyUploaded;
    private Integer inProcess;
    private Integer overDue;
    private Integer noIfProgramAsPerMpr;

    public ProgramCounts(String agencyName,
                         Long fullyUploaded,
                         Long overDue,
                         Long partiallyUploaded,
                         Long inProcess,
                         Long scheduledOnDate) {

        this.agencyName = agencyName;
        this.fullyUploaded = fullyUploaded != null ? fullyUploaded.intValue() : 0;
        this.overDue = overDue != null ? overDue.intValue() : 0;
        this.partiallyUploaded = partiallyUploaded != null ? partiallyUploaded.intValue() : 0;
        this.inProcess = inProcess != null ? inProcess.intValue() : 0;
        this.scheduledOnDate = scheduledOnDate != null ? scheduledOnDate.intValue() : 0;
    }
}
