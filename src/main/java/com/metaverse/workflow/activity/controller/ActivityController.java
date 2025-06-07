package com.metaverse.workflow.activity.controller;

import com.metaverse.workflow.activity.sevice.ActivityRequest;
import com.metaverse.workflow.activity.sevice.ActivityService;
import com.metaverse.workflow.common.response.WorkflowResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ActivityController {

    @Autowired
    private ActivityService service;

   @PostMapping("/activity/save")
   public ResponseEntity<WorkflowResponse> saveActivity(@RequestBody ActivityRequest activityRequest) {
       WorkflowResponse response=service.saveActivity(activityRequest);
        return ResponseEntity.ok(response);
    }



    @GetMapping("/activityById/{id}")
    public ResponseEntity<WorkflowResponse> getActivityById(@PathVariable("id") Long id)
    {

        WorkflowResponse response = service.getActivityById(id);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/activities")
    public ResponseEntity<WorkflowResponse> getAllActivities() {

       WorkflowResponse response= service.getActivities();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/subActivityById/{id}")
    public ResponseEntity<WorkflowResponse> getSubActivityById(@PathVariable("id") Long id)
    {

        WorkflowResponse response = service.getSubActivityById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/activity/agency/{id}")
    public ResponseEntity<WorkflowResponse> getActivityByAgencyId(@PathVariable("id") Long id)
    {

        WorkflowResponse response = service.getActivityByAgencyId(id);
        return ResponseEntity.ok(response);
    }






}
