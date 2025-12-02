package com.metaverse.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProgramFileResponse {
    private Long programId;
    private Long fileId;
    private String fileUrl;

}

