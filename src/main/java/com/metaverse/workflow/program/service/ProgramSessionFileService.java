package com.metaverse.workflow.program.service;

import com.metaverse.workflow.model.ProgramSessionFile;
import com.metaverse.workflow.program.repository.ProgramSessionFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class ProgramSessionFileService {

    @Autowired
    private ProgramSessionFileRepository fileRepository;

    public boolean deleteFileById(Long fileId) {
        Optional<ProgramSessionFile> optionalFile = fileRepository.findById(fileId);
        if (optionalFile.isPresent()) {
            ProgramSessionFile file = optionalFile.get();

            File physicalFile = new File(file.getFilePath());
            if (physicalFile.exists()) {
                physicalFile.delete();
            }
            fileRepository.deleteById(fileId);
            return true;
        } else {
            return false;
        }
    }

    // New: return the first filePath found for a programId (if any)
    public Optional<String> getFirstFilePathByProgramId(Long programId) {
        List<ProgramSessionFile> files = fileRepository.findByProgramSession_Program_ProgramId(programId);
        if (files == null || files.isEmpty()) {
            return Optional.empty();
        }
        // return the first non-null filePath
        for (ProgramSessionFile f : files) {
            if (f != null && f.getFilePath() != null && !f.getFilePath().isEmpty()) {
                return Optional.of(f.getFilePath());
            }
        }
        return Optional.empty();
    }

    // New: return the first filePath found for a programId and matching fileType
    public Optional<String> getFirstFilePathByProgramIdAndFileType(Long programId, String fileType) {
        if (fileType == null) {
            return Optional.empty();
        }
        List<ProgramSessionFile>  files = fileRepository.findByProgramProgramIdAndFileTypeIgnoreCase(programId, fileType);
        if (files == null || files.isEmpty()) {
            return Optional.empty();
        }
        for (ProgramSessionFile f : files) {
            if (f != null && f.getFilePath() != null && !f.getFilePath().isEmpty()) {
                return Optional.of("https://metaverseedu.in/workflowfiles-temp" + f.getFilePath());
            }
        }
        return Optional.empty();
    }
}
