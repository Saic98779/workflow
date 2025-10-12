package com.metaverse.workflow.MoMSMEReport.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MSMEBenefitedDto {
    private Integer total;
    private Integer women;
    private Integer sc;
    private Integer st;
    private Integer obc;
}


