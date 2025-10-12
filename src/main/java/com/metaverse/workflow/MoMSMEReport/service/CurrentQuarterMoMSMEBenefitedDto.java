package com.metaverse.workflow.MoMSMEReport.service;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CurrentQuarterMoMSMEBenefitedDto {
    private Integer total;
    private Integer women;
    private Integer sc;
    private Integer st;
    private Integer obc;
}
