package com.metaverse.workflow.programoutcome.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.outcomes.ProgramOutcomeTable;
import com.metaverse.workflow.programoutcome.dto.ProgramOutcomeTableResponse;
import com.metaverse.workflow.programoutcome.service.OutcomeDetails;
import com.metaverse.workflow.programoutcome.service.ProgramOutcomeService;
import com.metaverse.workflow.programoutcome.service.UdyamService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Slf4j
public class ProgramOutcomeController {

    @Autowired
    private ProgramOutcomeService programOutcomeService;
    @Autowired
    private ActivityLogService logService;
    @Autowired
    private UdyamService udyamService;

    @GetMapping(value = "/program/outcome/tables")
    public ResponseEntity<WorkflowResponse> getProgramOutcomeTables() {
        List<ProgramOutcomeTable> outcomeTableList = programOutcomeService.getProgramOutcomeTables();
        //Map<String, String> outcomeTableMap = outcomeTableList.stream().collect(Collectors.toMap(table -> table.getOutcomeTableDisplayName(), table -> table.getOutcomeTableName()));
        List<ProgramOutcomeTableResponse> response = outcomeTableList.stream().map(table -> ProgramOutcomeTableResponse.builder().outcomeTableId(table.getOutcomeTableId()).outcomeTableDisplayName(table.getOutcomeTableDisplayName()).outcomeTableName(table.getOutcomeTableName()).build()).collect(Collectors.toList());
        return ResponseEntity.ok(WorkflowResponse.builder().status(200).message("Success").data(response).build());
    }

    @GetMapping(value = "/program/outcome/details/{participantId}/{outcome}")
    public ResponseEntity<WorkflowResponse> getProgramOutcomeDetails(@PathVariable("participantId") Long participantId,
                                                                     @PathVariable("outcome") String outcome,
                                                                     @RequestParam(value = "type", required = false) String type) {
        log.info("/program/outcome/details/");
        WorkflowResponse response = programOutcomeService.getOutcomeDetails(participantId, outcome, type);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/program/outcome/save/{outcome}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveOutcome(Principal principal, @PathVariable("outcome") String outcomeName, @RequestPart("data") String data) throws ParseException {
        log.info("Program outcome : {}", data);
        WorkflowResponse response = null;
        try {
            response = programOutcomeService.saveOutCome(outcomeName, data);
        } catch (DataException exception) {
            return RestControllerBase.error(exception);
        }
        logService.logs(principal.getName(), "SAVE",
                "Program outcome saved successfully | Outcome: " + outcomeName,
                "ProgramOutcome",
                "/program/outcome/save/" + outcomeName);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/program/getOutcomeByName/{outcomeName}")
    public ResponseEntity<WorkflowResponse> getProgramOutcomes(@PathVariable("outcome") String outcomeName) {
        WorkflowResponse response = programOutcomeService.getOutcomeDetailsByName(outcomeName);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/udyam/data/upload")
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file,Principal principal) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }
        try {
            udyamService.saveUdyamDataFromExcel(file);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
        logService.logs(principal.getName(), "SAVE",
                "Udyam Excel data uploaded successfully",
                "UdyamData",
                "/udyam/data/upload");
        return ResponseEntity.ok("Excel data saved successfully!");
    }

}
