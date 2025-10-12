package com.metaverse.workflow.MoMSMEReport.controller;

import com.metaverse.workflow.MoMSMEReport.service.MoMSMEReportSubmittedDTO;
import com.metaverse.workflow.MoMSMEReport.service.MoMSMEReportSubmittedService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/momsme")
public class MoMSMEReportSubmittedController {
    private final MoMSMEReportSubmittedService service;

    @PostMapping("/submitted-save")
    public ResponseEntity<?> save(@RequestBody MoMSMEReportSubmittedDTO dto) {
        try {
            WorkflowResponse response = service.saveReport(dto);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/submitted-get/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            WorkflowResponse response = service.getById(id);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
}