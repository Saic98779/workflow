package com.metaverse.workflow.activity.sevice;

import com.metaverse.workflow.model.Activity;
import com.metaverse.workflow.model.SubActivity;

public class SubActivityResponseMapper {

    public static SubActivityResponse map(SubActivity subActivity)
    {
        SubActivityResponse response = SubActivityResponse.builder()
                .subActivityId(subActivity.getSubActivityId())
                .subActivityName(subActivity.getSubActivityName())

                .Activities(subActivity.getActivities().stream().map(Activity::getActivityName).toList())
                .build();
        return response;
    }
}
