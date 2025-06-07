package com.metaverse.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileMetadataDTO {
    private String fileName;
    private String downloadUrl;
}
