package com.metaverse.workflow.visitorcount.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.visitorcount.service.VisitorCountRequest;
import com.metaverse.workflow.visitorcount.service.VisitorCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/visitor-count")
public class VisitorCountController {

    @Autowired
    private VisitorCountService visitorCountService;

    @Autowired
    private ActivityLogService logService;

    /**
     * Get current visitor count
     * @return response with current visitor count
     */
    @GetMapping("/get")
    public ResponseEntity<WorkflowResponse> getVisitorCount() {
        WorkflowResponse response = visitorCountService.getVisitorCount();
        return ResponseEntity.ok(response);
    }

    /**
     * Update visitor count - fetches existing count and increments by 1
     * @param principal the authenticated user
     * @return response with updated visitor count
     */
    @PutMapping("/update")
    public ResponseEntity<WorkflowResponse> updateVisitorCount(Principal principal) {
        WorkflowResponse response = visitorCountService.updateVisitorCount(null);
        if (principal != null) {
            logService.logs(principal.getName(), "UPDATE",
                    "Visitor count incremented by 1",
                    "VisitorCount",
                    "/visitor-count/update");
        }
        return ResponseEntity.ok(response);
    }
}

