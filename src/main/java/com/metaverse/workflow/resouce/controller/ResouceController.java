package com.metaverse.workflow.resouce.controller;

import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.resouce.service.ResourceRequest;
import com.metaverse.workflow.resouce.service.ResourceResponse;
import com.metaverse.workflow.resouce.service.ResourceService;

@RestController
public class ResouceController {

    @Autowired
    private ResourceService service;

    @PostMapping("/resource/save")
    public ResponseEntity<WorkflowResponse> saveResource(@RequestBody ResourceRequest resourceRequest) {
        ResourceResponse saveResource = service.saveResource(resourceRequest);
        return ResponseEntity.ok(WorkflowResponse.builder().status(200).message("Created").data(saveResource).build());
    }

    @GetMapping("/resources")
    public ResponseEntity<WorkflowResponse> getResourses() {
        WorkflowResponse response = service.getResources();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/resource/update/{resourceId}")
    public ResponseEntity<?> updateResource(@PathVariable Long resourceId, @RequestBody ResourceRequest resourceRequest) {
        try {
            return ResponseEntity.ok(service.updateResources(resourceRequest, resourceId));
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
    @DeleteMapping("/resource/delete/{resourceId}")
    public ResponseEntity<?> deleteResource(@PathVariable Long resourceId) {
        try {
            return ResponseEntity.ok(service.deleteResources(resourceId));
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

}
