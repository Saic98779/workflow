package com.metaverse.workflow.program.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;

import java.io.*;

import com.metaverse.workflow.model.*;
import com.metaverse.workflow.program.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class ProgramController {

    @Autowired
    ProgramService programService;

    @Autowired
    OverdueProgramUpdater overdueProgramUpdater;

    @Operation(summary = "Create program", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = WorkflowResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping(value = "/program/create", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<WorkflowResponse> createProgram(@RequestBody ProgramRequest request) {
        log.info("Program controller, title : {}", request.getProgramTitle());
        WorkflowResponse response = programService.createProgram(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create session", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = WorkflowResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping(value = "/program/session/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkflowResponse> createSession(@RequestPart("data") String data, @RequestPart(value = "files", required = false) List<MultipartFile> files) throws ParseException {
        log.info("Program controller, title : {}", data);
        JSONParser parser = new JSONParser();
        ProgramSessionRequest request = parser.parse(data, ProgramSessionRequest.class);
        WorkflowResponse response = programService.createProgramSession(request, files);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/program/session/delete")
    public ResponseEntity<String> deleteSession(@RequestParam("sessionId") Long sessionId) throws ParseException {
        String response = programService.deleteProgramSession(sessionId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/program/participants/{programId}")
    public ResponseEntity<WorkflowResponse> getParticipantsByProgramId(@PathVariable("programId") Long programId,
                                                                       @RequestParam(value = "agencyId", required = false) Long agencyId,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {
        WorkflowResponse response = programService.getProgramParticipants(programId,agencyId, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/program/{programId}")
    public ResponseEntity<WorkflowResponse> getProgramById(@PathVariable("programId") Long programId) {
        WorkflowResponse response = programService.getProgramById(programId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/programs")
    public ResponseEntity<WorkflowResponse> getPrograms() {
        WorkflowResponse response = programService.getPrograms();
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/updateProgram", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<WorkflowResponse> updateProgram(@RequestBody ProgramRequest request) {
        log.info("Updating Program with ID: {}", request.getProgramId());
        WorkflowResponse response = programService.updateProgram(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/save/program/type")
    public ResponseEntity<WorkflowResponse> saveProgramTypes(@RequestBody ProgramTypeRequest request) {
        WorkflowResponse response = programService.saveProgramType(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/program/types")
    public ResponseEntity<WorkflowResponse> getAllProgramTypes() {
        WorkflowResponse response = programService.getAllProgramTypes();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/program/types/agency/id/{agencyId}")
    public ResponseEntity<WorkflowResponse> getAllProgramTypesByAgencyId(@PathVariable Long agencyId) {
        WorkflowResponse response = programService.getAllProgramTypeByAgencyId(agencyId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/program/participant-verification/{programId}")
    public ResponseEntity<WorkflowResponse> getParticipantAndVerificationByProgramId(@PathVariable("programId") Long programId) {
        WorkflowResponse response = programService.getProgramParticipantAndVerifications(programId);
        return ResponseEntity.ok(response);
    }


    @PostMapping(value = "/program/session/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkflowResponse> editProgramSession(@RequestPart("data") String data, @RequestPart(value = "files", required = false) List<MultipartFile> files) throws ParseException {
        log.info("Program controller, title : {}", data);
        JSONParser parser = new JSONParser();
        ProgramSessionRequest request = parser.parse(data, ProgramSessionRequest.class);
        WorkflowResponse response = programService.editProgramSession(request, files);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/program/execution/images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkflowResponse> saveSessionImages(@RequestPart("data") String data,
                                                              @RequestPart(value = "image1", required = false) MultipartFile image1,
                                                              @RequestPart(value = "image2", required = false) MultipartFile image2,
                                                              @RequestPart(value = "image3", required = false) MultipartFile image3,
                                                              @RequestPart(value = "image4", required = false) MultipartFile image4,
                                                              @RequestPart(value = "image5", required = false) MultipartFile image5) throws ParseException, java.text.ParseException {
        log.info("Program controller save session images, data : {}", data);
        JSONParser parser = new JSONParser();
        ProgramSessionRequest request = parser.parse(data, ProgramSessionRequest.class);
        WorkflowResponse response = programService.saveSessionImages(request, image1, image2, image3, image4, image5);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/program/collage/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WorkflowResponse> saveCollageImages(@RequestParam("programId") Long programId,
                                                              @RequestPart(value = "image", required = false) MultipartFile image) {
        WorkflowResponse response = programService.saveCollageImages(programId, image);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/program/execution/media-coverage", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkflowResponse> saveMediaCoverage(@RequestPart("data") String data,
                                                              @RequestPart(value = "image1", required = false) MultipartFile image1,
                                                              @RequestPart(value = "image2", required = false) MultipartFile image2,
                                                              @RequestPart(value = "image3", required = false) MultipartFile image3) throws ParseException {
        log.info("Program controller save program media, data : {}", data);
        JSONParser parser = new JSONParser();
        MediaCoverageRequest request = parser.parse(data, MediaCoverageRequest.class);
        WorkflowResponse response = programService.saveMediaCoverage(request, image1, image2, image3);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/program/file/download/{fileId}")
    public ResponseEntity<InputStreamResource> getProgramFile(@PathVariable("fileId") Long fileId) throws FileNotFoundException {
        Path path = programService.getProgramFile(fileId);
        if (path == null) {
            return ResponseEntity.noContent().build();
        } else {
            File file = new File(path.toAbsolutePath().toString());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path.getFileName().toString());
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }
    }

    @GetMapping("/program/file/paths/{programId}")
    public ResponseEntity<List<String>> getAllProgramFilePaths(@PathVariable("programId") Long programId) {
        List<Path> paths = programService.getAllProgramFile(programId);

        if (paths == null || paths.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        String basePrefix = "/home/metaverseedu/public_html/";
        String urlPrefix = "https://metaverseedu.in/";

        List<String> fileUrls = paths.stream()
                .map(path -> {
                    String fullPath = path.toAbsolutePath().toString();
                    if (fullPath.startsWith(basePrefix)) {
                        return urlPrefix + fullPath.substring(basePrefix.length());
                    } else {
                        return fullPath;
                    }
                })
                .toList();

        return ResponseEntity.ok(fileUrls);
    }

    @GetMapping("/program/file/paths/status")
    public ResponseEntity<List<ProgramFileResponse>> getAllProgramFilePaths(
            @RequestParam(value = "fileType", required = false) FileType fileType) {

        if (fileType == null) {
            return ResponseEntity.badRequest().build();
        }

        List<ProgramFilePathInfo> paths = programService.getProgramFileByType(fileType);

        if (paths.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        String basePrefix = "/home/metaverseedu/public_html/";
        String urlPrefix = "https://metaverseedu.in/";

        List<ProgramFileResponse> fileResponses = paths.stream()
                .map(info -> {
                    String fullPath = info.getFilePath().toAbsolutePath().toString();
                    String url = fullPath.startsWith(basePrefix)
                            ? urlPrefix + fullPath.substring(basePrefix.length())
                            : fullPath;
                    return new ProgramFileResponse(info.getProgramId(), url);
                })
                .toList();

        return ResponseEntity.ok(fileResponses);
    }




    @GetMapping("/program/summary/{programId}")
    public ResponseEntity<?> getProgramSummeryById(@PathVariable("programId") Long programId) {
        try {
            return ResponseEntity.ok(programService.getProgramSummaryByProgramId(programId));
        } catch (DataException exception) {
            return RestControllerBase.error(exception);
        }

    }

    @GetMapping("/program/participants/dropdown/{programId}")
    public ResponseEntity<WorkflowResponse> getParticipantsByProgramIdDropDown(@PathVariable("programId") Long programId) {
        WorkflowResponse response = programService.getProgramParticipantsDropDown(programId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/program/feedback/save")
    public ResponseEntity<WorkflowResponse> saveFeedback(@RequestBody ProgramMonitoringFeedBackRequest request)
    {
        return ResponseEntity.ok(programService.saveFeedback(request));
    }
    @PostMapping("/program/feedback/update/{monitorId}")
    public ResponseEntity<?> updateFeedback(@PathVariable Long monitorId, @RequestBody ProgramMonitoringFeedBackRequest request
    ) {
        WorkflowResponse response ;
        try {
            response = programService.updateFeedback(monitorId, request);
        } catch (DataException exception) {
            return RestControllerBase.error(exception);
        }
        return ResponseEntity.ok(response);
    }
    @GetMapping("/program/feedback/{programId}")
    public ResponseEntity<?> getFeedbackByProgramId(@PathVariable Long programId) {
        WorkflowResponse response ;
        try {
            response = programService.getFeedBackByProgramId(programId);
        } catch (DataException exception) {
            return RestControllerBase.error(exception);
        }
        return ResponseEntity.ok(response);
    }
    @GetMapping("/program/feedback/id/{feedBackId}")
    public ResponseEntity<?> getFeedbackById(@PathVariable Long feedBackId) {

        return ResponseEntity.ok(programService.getFeedBackById(feedBackId));
    }

    @GetMapping("/program/details/for/feedback/{programId}")
    public ResponseEntity<?> getProgramDetailsFroFeedBack(@PathVariable Long programId) {
        WorkflowResponse response ;
        try {
            response = programService.getProgramDetailsFroFeedBack(programId);
        } catch (DataException exception) {
            return RestControllerBase.error(exception);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/program/import", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<WorkflowResponse> importPrograms(@RequestPart("file") MultipartFile file) {
        WorkflowResponse response = programService.importProgramsFromExcel(file);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("program/delete/{programId}")
    public ResponseEntity<WorkflowResponse> deleteProgram(@PathVariable Long programId) {

        return ResponseEntity.ok(programService.deleteProgramAndDependencies(programId));
    }

    @PutMapping("/{id}/update-overdue")
    public ResponseEntity<ProgramUpdateResponse> updateOverdueById(@PathVariable Long id) {
        try {
            ProgramUpdateResponse response = overdueProgramUpdater.updateOverdueStatusById(id);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
