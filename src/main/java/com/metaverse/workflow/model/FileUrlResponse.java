package com.metaverse.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUrlResponse {
    private Long programId;
    private String fileType;
    private String fileUrl;
}
