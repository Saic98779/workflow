package com.metaverse.workflow.tgtpc_handholding.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.tgtpc_handholding.request_dto.TGTPCHandholdingSupportRequest;
import com.metaverse.workflow.tgtpc_handholding.service.HandholdingResponseMapper;
import com.metaverse.workflow.tgtpc_handholding.service.TGTPCHandholdingMasterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/tgtpc/handholding-support")
@Tag(name = "Handholding Support", description = "TGTPC Handholding Support APIs")
@RequiredArgsConstructor
public class TGTPCHandholdingController {

    private final TGTPCHandholdingMasterService service;
    private final ActivityLogService logService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(Principal principal,
                                  @RequestBody TGTPCHandholdingSupportRequest request,
                                  HttpServletRequest servletRequest) {

        try {
            WorkflowResponse response = WorkflowResponse.builder()
                    .status(200)
                    .message("TGTPC Handholding Support updated successfully")
                    .data(HandholdingResponseMapper.mapToTGTPCHandholdingSupportResponse(service.save(request)))
                    .build();

            logService.logs(
                    principal.getName(),
                    "SAVE",
                    "TGTPC Handholding Support saved successfully",
                    "TGTPCHandholding",
                    servletRequest.getRequestURI()
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(Principal principal,
                                    @PathVariable Long id,
                                    @RequestBody TGTPCHandholdingSupportRequest request,
                                    HttpServletRequest servletRequest) {

        try {
            WorkflowResponse response = WorkflowResponse.builder()
                    .status(200)
                    .message("TGTPC Handholding Support updated successfully")
                    .data(HandholdingResponseMapper.mapToTGTPCHandholdingSupportResponse(service.update(id, request)))
                    .build();

            logService.logs(
                    principal.getName(),
                    "UPDATE",
                    "TGTPC Handholding Support updated successfully",
                    "TGTPCHandholding",
                    servletRequest.getRequestURI()
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getById(id));
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/sub-activity/{subActivityId}")
    public ResponseEntity<?> getBySubActivity(@PathVariable Long subActivityId) {
        try {
            return ResponseEntity.ok(service.getBySubActivityId(subActivityId));
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(Principal principal, @PathVariable Long id, HttpServletRequest servletRequest) {
        try {
            WorkflowResponse response = service.delete(id);
            logService.logs(
                    principal.getName(),
                    "DELETE",
                    "TGTPC Handholding Support deleted successfully",
                    "TGTPCHandholding",
                    servletRequest.getRequestURI()
            );

            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
}
