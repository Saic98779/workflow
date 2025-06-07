package com.metaverse.workflow.program.service;

import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.model.ProgramUpdateResponse;
import com.metaverse.workflow.program.repository.ProgramRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OverdueProgramUpdater {

    @Autowired
    private ProgramRepository programRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") //Every day 12
//    @Scheduled(cron = "0 * * * * ?") //min update
    public void updateOverduePrograms() {
        Date twoDaysAgo = java.sql.Date.valueOf(LocalDate.now().plusDays(2));
        List<Program> overdueProgramUpdate = new ArrayList<>();
        List<Program> overduePrograms = programRepository.findProgramsWithStartDateEqual(twoDaysAgo);

        for (Program program : overduePrograms) {
            System.out.println("Two days ago: " + twoDaysAgo);
            System.out.println("Found programs: " + overduePrograms.size());
            program.setOverdue(true);
            Long currentVersion = program.getVersion();
            if (currentVersion == null) {
                program.setVersion(1L);
            } else {
                program.setVersion(currentVersion + 1);
            }
            overdueProgramUpdate.add(program);
        }
        programRepository.saveAll(overdueProgramUpdate);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") //Every day 12
//    @Scheduled(cron = "0 * * * * ?") //min update
    public void updatePastDaysPrograms() {
        Date today = java.sql.Date.valueOf(LocalDate.now());

        List<Program> overduePrograms = programRepository.findByStartDateBefore(today);


        for (Program program : overduePrograms) {
            System.out.println("Found programs: " + overduePrograms.size());
            program.setOverdue(true);
            Long currentVersion = program.getVersion();
            if (currentVersion == null) {
                program.setVersion(1L);
            } else {
                program.setVersion(currentVersion + 1);
            }
        }

        programRepository.saveAll(overduePrograms);
    }


    public ProgramUpdateResponse updateOverdueStatusById(Long id) {
        Program program = programRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Program not found with ID: " + id));

        program.setOverdue(false);
        program.setVersion(program.getVersion() + 1);
        programRepository.save(program);
        return new ProgramUpdateResponse(program.getProgramId(), "Program marked as overdue.", true);
    }
}
