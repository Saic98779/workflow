package com.metaverse.workflow.activity.sevice;

import com.metaverse.workflow.activity.repository.ActivityRepository;
import com.metaverse.workflow.activity.repository.SubActivityRepository;
import com.metaverse.workflow.agency.service.AgencyService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.Activity;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.SubActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActivityServiceAdepter implements ActivityService {

    @Autowired
    ActivityRepository activityRepository;
    @Autowired
    SubActivityRepository subActivityRepository;

    @Autowired
    AgencyService agencyService;


    @Override
    public WorkflowResponse saveActivity(ActivityRequest activityRequest) {
        List<SubActivity> savedSubActivities = activityRequest.getSubActivities()
                .stream()
                .map(subActivityRepository::save)
                .toList();
        Agency agency = agencyService.getAgencyById(activityRequest.getAgencyId());
        if (agency == null) return WorkflowResponse.builder()
                .message("agency Not found")
                .status(400)
                .build();
        Activity activity = ActivityRequestMapper.map(activityRequest, agency);

        activityRepository.save(activity);
        return WorkflowResponse.builder()
                .message("Activity saved successfully")
                .status(200)
                .data(ActivityResponseMapper.map(activity))
                .build();
    }

    @Override
    public List<Activity> getActivityEntities() {
        return activityRepository.findAll();
    }

    @Override
    public List<SubActivity> getSubActivityEntities() {
        return subActivityRepository.findAll();
    }

    @Override
    public WorkflowResponse getSubActivityById(Long id) {
        Optional<SubActivity> subActivity = subActivityRepository.findById(id);
        if (!subActivity.isPresent()) return WorkflowResponse.builder()
                .message("SubActivity Not found")
                .status(400)
                .build();
        SubActivityResponse response = SubActivityResponseMapper.map(subActivity.get());
        return WorkflowResponse.builder()
                .message("Success")
                .status(200)
                .data(response)
                .build();
    }


    @Override
    public WorkflowResponse getActivityById(Long id) {
        Optional<Activity> activity = activityRepository.findById(id);
        if (!activity.isPresent()) return WorkflowResponse.builder()
                .message("Activity Not found")
                .status(400)
                .build();
        ActivityResponse response = ActivityResponseMapper.map(activity.get());
        return WorkflowResponse.builder()
                .message("Success")
                .status(200)
                .data(response)
                .build();
    }

    @Override
    public WorkflowResponse getActivityByAgencyId(Long id) {

        List<Activity> activityList = activityRepository.findByAgencyAgencyId(id);
        if (activityList.isEmpty()) return WorkflowResponse.builder()
                .message("Activity Not found on this agency")
                .status(400)
                .build();
        List<ActivityResponse> response = activityList.stream()
                .map(activity -> ActivityResponseMapper.map(activity))
                .collect(Collectors.toList());

        return WorkflowResponse.builder()
                .message("Success")
                .status(200)
                .data(response)
                .build();
    }


    @Override
    public WorkflowResponse getActivities() {
        List<Activity> activityList = activityRepository.findAll();
        if (activityList.isEmpty()) {
            return WorkflowResponse.builder()
                    .message("No activities found")
                    .status(404)
                    .data(Collections.emptyList())
                    .build();
        }
        List<ActivityResponse> response = activityList.stream()
                .map(activity -> ActivityResponseMapper.map(activity))
                .collect(Collectors.toList());
        return WorkflowResponse.builder()
                .message("Success")
                .status(200)
                .data(response)
                .build();
    }

}
