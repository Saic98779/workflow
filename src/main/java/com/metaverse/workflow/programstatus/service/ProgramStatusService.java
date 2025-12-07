package com.metaverse.workflow.programstatus.service;

import com.metaverse.workflow.programstatus.entity.ProgramStatus;
import com.metaverse.workflow.programstatus.repository.ProgramStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramStatusService {

    private final ProgramStatusRepository repository;

    public List<String> getStatus() {
        return repository.findAll()
                .stream().map(ProgramStatus::getStatus).toList();
    }
}

