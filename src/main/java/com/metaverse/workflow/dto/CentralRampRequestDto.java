package com.metaverse.workflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CentralRampRequestDto {
    @JsonProperty("StateRAMPDashbrdData")
    private List<StateRAMPDashbrdDataDto> stateRAMPDashbrdData;
}
