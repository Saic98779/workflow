package com.metaverse.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteFileResponse {
    private String message;
    private Long fileId;
}
