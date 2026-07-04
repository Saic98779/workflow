package com.metaverse.workflow.program.service;

import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.FileType;
import com.metaverse.workflow.model.ProgramFileResponse;

import java.util.List;

public interface FileService {
    List<ProgramFileResponse> getAllProgramFilePathsByStatus(FileType fileType, String userId) throws DataException;
}

