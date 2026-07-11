package com.metaverse.workflow.richnontraining.service;

import java.util.List;

import com.metaverse.workflow.model.RichMilestone;
import com.metaverse.workflow.richnontraining.repository.RichMilestoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RichMilestoneService {

    @Autowired
    private RichMilestoneRepository richMilestoneRepository;


    public List<RichMilestone> getAllRichMilestones() {
        return richMilestoneRepository.findAll();
    }
}
