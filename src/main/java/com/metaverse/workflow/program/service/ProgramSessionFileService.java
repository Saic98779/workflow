package com.metaverse.workflow.program.service;

import com.metaverse.workflow.model.ProgramSessionFile;
import com.metaverse.workflow.program.repository.ProgramSessionFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
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
}

