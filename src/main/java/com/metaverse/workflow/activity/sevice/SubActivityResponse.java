package com.metaverse.workflow.activity.sevice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubActivityResponse {

    private Long subActivityId;
    private String subActivityName;

    private List<String> Activities;

}
