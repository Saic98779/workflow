package com.metaverse.workflow.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CollageImageResponse {
    private Long programId;
    private Long fileId;
    private String filePath;
}