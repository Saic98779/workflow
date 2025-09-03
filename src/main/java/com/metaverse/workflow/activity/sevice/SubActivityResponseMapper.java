package com.metaverse.workflow.activity.sevice;

import com.metaverse.workflow.model.Activity;
import com.metaverse.workflow.model.SubActivity;

public class SubActivityResponseMapper {

    public static SubActivityResponse map(SubActivity subActivity)
    {
        SubActivityResponse response = SubActivityResponse.builder()
                .subActivityId(subActivity.getSubActivityId())
                .subActivityName(subActivity.getSubActivityName())
                .activityName(subActivity.getActivity().getActivityName())
                .build();
        return response;
    }
}
