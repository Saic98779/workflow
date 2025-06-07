package com.metaverse.workflow.callcenter.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubActivityQuestionsRequest {

    private Long subActivityId;
    private List<Integer> questions;
}
