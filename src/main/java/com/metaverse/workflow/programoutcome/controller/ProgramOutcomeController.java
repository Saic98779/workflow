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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
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
                                                                     @PathParam("participantType") Boolean isInfluenced,
                                                                     @RequestParam(value = "type", required = false) String type) {
        WorkflowResponse response = programOutcomeService.getOutcomeDetails(participantId, outcome, type,isInfluenced);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/program/outcome/save/{outcome}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveOutcome(Principal principal, @PathVariable("outcome") String outcomeName,
                                         @RequestPart("data") String data,
                                         HttpServletRequest servletRequest) throws ParseException {
        WorkflowResponse response = null;
        try {
            response = programOutcomeService.saveOutCome(outcomeName, data);
        } catch (DataException exception) {
            return RestControllerBase.error(exception);
        }
        logService.logs(principal.getName(), "SAVE",
                "Program outcome saved successfully | Outcome: " + outcomeName,
                "ProgramOutcome",
                servletRequest.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/program/getOutcomeByName/{outcomeName}")
    public ResponseEntity<WorkflowResponse> getProgramOutcomes(@PathVariable("outcome") String outcomeName) {
        WorkflowResponse response = programOutcomeService.getOutcomeDetailsByName(outcomeName);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/udyam/data/upload")
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file,Principal principal,HttpServletRequest servletRequest) {
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
                servletRequest.getRequestURI());
        return ResponseEntity.ok("Excel data saved successfully!");
    }

    @GetMapping(path = "/get/outcome-targets/{agencyId}")
    public ResponseEntity<?> getApiForOutcomes(@PathVariable Long agencyId, @RequestParam(required = false) Long outcomeId){

        try {
           return ResponseEntity.ok(programOutcomeService.getApiForOutcomes(agencyId,outcomeId));
        }catch (Exception e) {
          return   ResponseEntity.status(400).body(WorkflowResponse.builder().status(400).message(e.getMessage()).build());
        }
    }

    @Operation(
            summary = "Fetch outcome data dynamically",
            description = """
        Retrieves paginated outcome data (like ONDC Registration or Transaction) 
        based on the given outcome name and agency ID.
        
        Supports pagination via 'page' and 'size' parameters.
        Returns dynamic headers and corresponding data fields.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Outcome data fetched successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkflowResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "404", description = "Outcome not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/fetch/outcome-data")
    public ResponseEntity<WorkflowResponse> getDynamicOutcome(
            @Parameter(description = "Name of the outcome type (e.g. ONDCTransaction, ONDCRegistration)", required = true)
            @RequestParam String outcomeName,

            @Parameter(description = "Agency ID for which data needs to be fetched", required = true)
            @RequestParam Long agencyId,

            @Parameter(description = "Page number (starts from 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size (number of records per page)", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        WorkflowResponse response = programOutcomeService.getOutcomeData(outcomeName, agencyId, page, size);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/fetch/outcome/by-id")
    public ResponseEntity<?> getDynamicOutcomeById(
            @Parameter(description = "Name of the outcome type (e.g. ONDCTransaction, ONDCRegistration)", required = true)
            @RequestParam String outcomeName,

            @Parameter(description = "Outcome ID for which data needs to be fetched", required = true)
            @RequestParam Long outcomeId){

        WorkflowResponse response = null;
        try {
            response = programOutcomeService.getOutcomeDataById(outcomeName,outcomeId);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
        return ResponseEntity.ok(response);
    }
    @PutMapping(value = "/program/outcome/update/{outcome}/{id}",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateOutcome(
            Principal principal,
            @PathVariable("outcome") String outcomeName,
            @PathVariable("id") Long id,
            @RequestPart("data") String data,
            HttpServletRequest servletRequest) throws ParseException {

        WorkflowResponse response;

        try {
            response = programOutcomeService.updateOutCome(outcomeName, data, id);
        } catch (DataException exception) {
            return RestControllerBase.error(exception);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new WorkflowResponse("ERROR", "Unexpected error: " + exception.getMessage(), 500)
            );
        }

        logService.logs(principal.getName(), "UPDATE",
                "Program outcome updated successfully | Outcome: " + outcomeName + " | ID: " + id,
                "ProgramOutcome",
                servletRequest.getRequestURI());

        return ResponseEntity.ok(response);
    }



}
