package com.metaverse.workflow.unified.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.unified.service.UnifiedTgtpcHandholdingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/tgtpc-handholding")
@RequiredArgsConstructor
@Tag(name = "Unified", description = "TGTPC Handholding")
public class UnifiedTgtpcHandholdingController {

    private final UnifiedTgtpcHandholdingService unifiedHandholdingService;
    private final ActivityLogService logService;

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(
            Principal principal,
            @RequestPart("type") String type,
            @RequestPart("data") String data,
            @RequestPart(value = "file", required = false) MultipartFile file,
            HttpServletRequest servletRequest
    ) {
        try {
            WorkflowResponse response = unifiedHandholdingService.save(type, data, file);

            logService.logs(principal.getName(), "SAVE", type + " saved successfully",
                    type, servletRequest.getRequestURI());
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> get(
            Principal principal,
            @RequestParam("type") String type,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "subActivityId", required = false) Long subActivityId,
            HttpServletRequest servletRequest
    ) {
        try {
            WorkflowResponse response =
                    unifiedHandholdingService.get(type, id, subActivityId);

            String user = principal != null ? principal.getName() : "anonymous";
            logService.logs(user, "FETCH", type + " fetched successfully",
                    type, servletRequest.getRequestURI());

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(
            @PathVariable Long id,
            Principal principal,
            @RequestPart("type") String type,
            @RequestPart("data") String data,
            @RequestPart(value = "file", required = false) MultipartFile file,
            HttpServletRequest servletRequest
    ) {
        try {
            WorkflowResponse response =
                    unifiedHandholdingService.update(id, type, data, file);

            String user = principal != null ? principal.getName() : "anonymous";
            logService.logs(user, "UPDATE", type + " updated successfully",
                    type, servletRequest.getRequestURI());

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    // ---------- DELETE ----------
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id,
            Principal principal,
            @RequestParam("type") String type,
            HttpServletRequest servletRequest
    ) {
        try {
            WorkflowResponse response =
                    unifiedHandholdingService.delete(type, id);

            String user = principal != null ? principal.getName() : "anonymous";
            logService.logs(user, "DELETE", type + " deleted successfully",
                    type, servletRequest.getRequestURI());

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
}
