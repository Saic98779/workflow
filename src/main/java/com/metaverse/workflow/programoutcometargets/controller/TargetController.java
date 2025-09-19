package com.metaverse.workflow.programoutcometargets.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.programoutcometargets.dto.FinancialTargetOverAllDTO;
import com.metaverse.workflow.programoutcometargets.dto.FinancialTargetSummaryDTO;
import com.metaverse.workflow.programoutcometargets.service.FinancialTargetRequest;
import com.metaverse.workflow.programoutcometargets.service.PhysicalTargetRequest;
import com.metaverse.workflow.programoutcometargets.service.TargetService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/targets")
public class TargetController {
    @Autowired
    private TargetService targetService;
    @Autowired
    private ActivityLogService logService;

    @PostMapping("/financial/save")
    public ResponseEntity<?> createFinancialTarget(@RequestBody FinancialTargetRequest request, Principal principal,
                                                   HttpServletRequest servletRequest) {
        try {
            WorkflowResponse response = targetService.saveFinancialTarget(request);
            logService.logs(principal.getName(), "SAVE",
                    "Financial target created successfully | Agency ID: " + request.getAgencyId(),
                    "FinancialTarget",
                    servletRequest.getRequestURI());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @PutMapping("/financial/update/{id}")
    public ResponseEntity<?> updateFinancialTarget(
            @PathVariable("id") Long id,Principal principal,
            @RequestBody FinancialTargetRequest request,
            HttpServletRequest servletRequest) {
        try {
            WorkflowResponse response = targetService.updateFinancialTarget(request, id);
            logService.logs(principal.getName(), "UPDATE",
                    "Financial target updated successfully | Target ID: " + id,
                    "FinancialTarget",
                    servletRequest.getRequestURI());
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/financial/agency/{agencyId}")
    public ResponseEntity<?> getAllFinancialTargets(@PathVariable("agencyId") Long agencyId) {
        try {
            WorkflowResponse response = targetService.getFinancialTargetsByAgencyId(agencyId);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }

    }

    @GetMapping("/financial/{id}")
    public ResponseEntity<?> getFinancialTargetById(@PathVariable("id") Long id) {
        try {
            WorkflowResponse response = targetService.getFinancialTargetsById(id);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
    @DeleteMapping("/financial/{id}")
    public ResponseEntity<?> deleteFinancialTargetById(@PathVariable("id") Long id,Principal principal,
                                                       HttpServletRequest servletRequest) {
        try {
            WorkflowResponse response = targetService.deleteFinancialTarget(id);
            logService.logs(principal.getName(), "DELETE",
                    "Financial target deleted successfully | Target ID: " + id,
                    "FinancialTarget",
                    servletRequest.getRequestURI());
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
    @PostMapping("/physical/save")
    public ResponseEntity<?> createPhysicalTarget(@RequestBody PhysicalTargetRequest request,Principal principal,
                                                  HttpServletRequest servletRequest) {
        try {
            WorkflowResponse response = targetService.savePhysicalTarget(request);
            logService.logs(principal.getName(), "SAVE",
                    "Physical target created successfully | Agency ID: " + request.getAgencyId(),
                    "PhysicalTarget",
                    servletRequest.getRequestURI());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
    @PutMapping("/physical/update/{id}")
    public ResponseEntity<?> updatePhysicalTarget(
            @PathVariable("id") Long id,
            @RequestBody PhysicalTargetRequest request,Principal  principal,
            HttpServletRequest servletRequest) {
        try {
            WorkflowResponse response = targetService.updatePhysicalTarget(request, id);
            logService.logs(principal.getName(), "UPDATE",
                    "Physical target updated successfully | Target ID: " + id,
                    "PhysicalTarget",
                    servletRequest.getRequestURI());
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
    @GetMapping("/physical/agency/{agencyId}")
    public ResponseEntity<?> getAllPhysicalTargets(@PathVariable("agencyId") Long agencyId) {
        try {
            WorkflowResponse response = targetService.getPhysicalTargetsByAgencyId(agencyId);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }

    }
    @GetMapping("/physical/{id}")
    public ResponseEntity<?> getPhysicalTargetById(@PathVariable("id") Long id) {
        try {
            WorkflowResponse response = targetService.getPhysicalTargetsById(id);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
    @DeleteMapping("/physical/{id}")
    public ResponseEntity<?> deletePhysicalTargetById(@PathVariable("id") Long id,Principal principal,HttpServletRequest servletRequest) {
        try {
            WorkflowResponse response = targetService.deletePhysicalTarget(id);
            logService.logs(principal.getName(), "DELETE",
                    "Physical target deleted successfully | Target ID: " + id,
                    "PhysicalTarget",
                    servletRequest.getRequestURI());
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("financial/year/summary")
    public FinancialTargetOverAllDTO getFinancialTargets(
            @RequestParam("agencyId") Long agencyId) {
        return targetService.getFinancialTargetSummary(agencyId);
    }
}
