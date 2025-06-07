package com.metaverse.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProgramUpdateResponse {
    private Long id;
    private String message;
    private boolean updated;
}
