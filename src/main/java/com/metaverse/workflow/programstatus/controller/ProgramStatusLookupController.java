package com.metaverse.workflow.programstatus.controller;

import com.metaverse.workflow.programstatus.service.ProgramStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("programStatusLookupController")
@RequestMapping("/program-status")
@Slf4j
public class ProgramStatusLookupController {

    @Autowired
    ProgramStatusService programStatusService;

    @GetMapping
    public ResponseEntity<String> getProgramStatus() {
        return ResponseEntity.ok(programStatusService.getStatus().toString());
    }

}
