package com.metaverse.workflow.nontrainingExpenditures.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NonTrainingExpenditureRemarksDTO {
    private String userId;

    private String spiuComments;

    private String agencyComments;

    private Long nonTrainingExpenditureId;

}
