package com.metaverse.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.file.Path;

@Data
@AllArgsConstructor
public class ProgramFilePathInfo {

    private Long programId;
    private Path filePath;
}
