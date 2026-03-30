package com.metaverse.workflow.centralaRAMPData.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CentralRampRequestDto {
    @JsonProperty("StateRAMPDashbrdData")
    private List<StateRAMPDashbrdDataDto> stateRAMPDashbrdData;
}
