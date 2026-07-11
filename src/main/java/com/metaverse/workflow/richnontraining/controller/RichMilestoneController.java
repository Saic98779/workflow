package com.metaverse.workflow.richnontraining.controller;

import java.util.List;

import com.metaverse.workflow.model.RichMilestone;
import com.metaverse.workflow.richnontraining.service.RichMilestoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rich-milestones")
public class RichMilestoneController {

    @Autowired
    private RichMilestoneService richMilestoneService;

    @GetMapping
    public List<RichMilestone> getAllRichMilestones() {
        return richMilestoneService.getAllRichMilestones();
    }
}