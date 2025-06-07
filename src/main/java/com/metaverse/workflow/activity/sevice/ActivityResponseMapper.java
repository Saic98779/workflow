package com.metaverse.workflow.activity.sevice;

import com.metaverse.workflow.model.Activity;

public class ActivityResponseMapper {

    public static ActivityResponse map(Activity activity)
    {
        ActivityResponse response = ActivityResponse.builder()
                .activityId(activity.getActivityId())
                .activityName(activity.getActivityName())
                .agencyId(activity.getAgency().getAgencyId())
                .agencyName(activity.getAgency().getAgencyName())
                .subActivities(activity.getSubActivities())
                .build();
        return response;
    }
}
