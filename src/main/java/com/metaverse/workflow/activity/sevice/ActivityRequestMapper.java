package com.metaverse.workflow.activity.sevice;

import com.metaverse.workflow.model.Activity;
import com.metaverse.workflow.model.Agency;

public class ActivityRequestMapper {

    public static Activity map(ActivityRequest activityRequest, Agency agency)
    {
        return Activity.builder()
                .agency(agency)
                .activityName(activityRequest.getActivityName())
                .subActivities(activityRequest.getSubActivities())
                .build();
    }
}
