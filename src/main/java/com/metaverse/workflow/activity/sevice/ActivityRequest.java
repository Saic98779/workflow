package com.metaverse.workflow.activity.sevice;

import com.metaverse.workflow.model.SubActivity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ActivityRequest {

    private String activityName;
    private Long agencyId;
    private List<SubActivity> subActivities;
}
