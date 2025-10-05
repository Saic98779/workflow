package com.metaverse.workflow.dto;

import lombok.Data;
import java.util.List;

@Data
public class CentralRampDataDto {
    private String statelgdCode;
    private List<StateRAMPDashbrdDataDto> stateRAMPDashbrdData;
}