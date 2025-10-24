package com.metaverse.workflow.dto;

import lombok.Data;
import java.util.List;

@Data
public class RampDataDto {
    private Integer State;
    private List<StateRAMPDashbrdDataDto> StateRAMPDashbrdData;
}
