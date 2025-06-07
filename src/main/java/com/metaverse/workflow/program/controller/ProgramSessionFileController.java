package com.metaverse.workflow.program.controller;

import com.metaverse.workflow.model.DeleteFileResponse;
import com.metaverse.workflow.program.service.ProgramSessionFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/program-session-files")
public class ProgramSessionFileController {
    @Autowired
    private ProgramSessionFileService fileService;

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteFileResponse> deleteProgramSessionFile(@PathVariable("id") Long id) {
        boolean deleted = fileService.deleteFileById(id);
        if (deleted) {
            return ResponseEntity.ok(new DeleteFileResponse("File deleted successfully.", id));
        } else {
            return ResponseEntity
                    .status(404)
                    .body(new DeleteFileResponse("File not found with ID: " + id, id));
        }
    }
}
