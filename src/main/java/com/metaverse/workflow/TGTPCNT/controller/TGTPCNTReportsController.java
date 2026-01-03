package com.metaverse.workflow.TGTPCNT.controller;

import com.metaverse.workflow.TGTPCNT.service.TGTPCNTReportsRequest;
import com.metaverse.workflow.TGTPCNT.service.TGTPCNTReportsService;
import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/tgtpc-nt-reports")
@RequiredArgsConstructor
public class TGTPCNTReportsController {

    private final TGTPCNTReportsService service;
    private final ActivityLogService logService;

    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(Principal principal, @RequestBody TGTPCNTReportsRequest request) {

        try {
            WorkflowResponse response = service.save(request);

            logService.logs(
                    principal.getName(),
                    "SAVE",
                    "TGTPC NT Report created successfully",
                    "TGTPCNTReports",
                    "/tgtpc-nt-reports/save"
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @PutMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(Principal principal, @PathVariable Long id, @RequestBody TGTPCNTReportsRequest request) {

        try {
            WorkflowResponse response = service.update(id, request);
            logService.logs(
                    principal.getName(),
                    "UPDATE",
                    "TGTPC NT Report updated successfully",
                    "TGTPCNTReports",
                    "/tgtpc-nt-reports/update/" + id
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getById(
            Principal principal,
            @PathVariable Long id) {

        try {
            WorkflowResponse response = service.getById(id);

            logService.logs(
                    principal.getName(),
                    "VIEW",
                    "TGTPC NT Report viewed",
                    "TGTPCNTReports",
                    "/tgtpc-nt-reports/get/" + id
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/get-by-sub-activity/{subActivityId}")
    public ResponseEntity<?> getBySubActivity(
            Principal principal,
            @PathVariable Long subActivityId) {

        WorkflowResponse response = service.getBySubActivityId(subActivityId);

        logService.logs(
                principal.getName(),
                "VIEW",
                "TGTPC NT Reports viewed by sub activity",
                "TGTPCNTReports",
                "/tgtpc-nt-reports/get-by-sub-activity/" + subActivityId
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
            Principal principal,
            @PathVariable Long id) {

        try {
            WorkflowResponse response = service.delete(id);

            logService.logs(
                    principal.getName(),
                    "DELETE",
                    "TGTPC NT Report deleted successfully",
                    "TGTPCNTReports",
                    "/tgtpc-nt-reports/delete/" + id
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
}

