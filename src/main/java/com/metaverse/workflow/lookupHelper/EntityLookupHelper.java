package com.metaverse.workflow.lookupHelper;

import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.program.repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityLookupHelper {

    @Autowired
    private ProgramRepository programRepository;

    /**
     * Get the full Program object by programId.
     * Returns null if not found.
     */
    public Program getProgramById(Long programId) {
        if (programId == null) {
            return null;
        }
        return programRepository.findById(programId).orElse(null);
    }
}

