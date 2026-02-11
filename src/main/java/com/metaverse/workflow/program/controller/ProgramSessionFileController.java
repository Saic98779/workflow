package com.metaverse.workflow.program.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.model.DeleteFileResponse;
import com.metaverse.workflow.model.FileUrlResponse;
import com.metaverse.workflow.program.service.ProgramSessionFileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/program-session-files")
public class ProgramSessionFileController {
    @Autowired
    private ProgramSessionFileService fileService;
    @Autowired
    private ActivityLogService logService;

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteFileResponse> deleteProgramSessionFile(@PathVariable("id") Long id, Principal principal, HttpServletRequest servletRequest) {
        logService.logs(principal.getName(),"DELETE","deleted program session file successfully with id " + id,"program", servletRequest.getRequestURI());
        boolean deleted = fileService.deleteFileById(id);
        if (deleted) {
            return ResponseEntity.ok(new DeleteFileResponse("File deleted successfully.", id));
        } else {
            return ResponseEntity
                    .status(404)
                    .body(new DeleteFileResponse("File not found with ID: " + id, id));
        }
    }

    @GetMapping("/program/{programId}/file-url")
    public ResponseEntity<FileUrlResponse> getFileUrlByProgramIdAndType(@PathVariable("programId") Long programId,
                                                                        @RequestParam(name = "fileType") String fileType,
                                                                        Principal principal,
                                                                        HttpServletRequest servletRequest) {
        // log the access
        String path = servletRequest != null ? servletRequest.getRequestURI() + "?fileType=" + fileType : "/program-session-files/program/" + programId + "/file-url?fileType=" + fileType;
        String username = principal != null ? principal.getName() : "anonymous";
        logService.logs(username, "VIEW", "Viewed file URL for programId " + programId + " and fileType " + fileType, "program", path);

        Optional<String> filePathOpt = fileService.getFirstFilePathByProgramIdAndFileType(programId, fileType);
        if (filePathOpt.isPresent()) {
            return ResponseEntity.ok(new FileUrlResponse(programId, fileType, filePathOpt.get()));
        } else {
            return ResponseEntity.status(404).body(new FileUrlResponse(programId, fileType, null));
        }
    }
}
