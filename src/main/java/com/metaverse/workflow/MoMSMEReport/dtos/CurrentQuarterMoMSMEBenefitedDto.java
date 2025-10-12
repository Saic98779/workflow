package com.metaverse.workflow.MoMSMEReport.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentQuarterMoMSMEBenefitedDto {
    private Integer total;
    private Integer women;
    private Integer sc;
    private Integer st;
    private Integer obc;
}


