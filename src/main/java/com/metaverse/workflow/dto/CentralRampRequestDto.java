package com.metaverse.workflow.dto;

import lombok.Data;

@Data
public class CentralRampRequestDto {
    private String username;
    private String apikey;
    private RampDataDto data;
}
