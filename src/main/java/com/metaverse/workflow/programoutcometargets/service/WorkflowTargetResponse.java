package com.metaverse.workflow.programoutcometargets.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class WorkflowTargetResponse {
    protected Integer status;
    protected String message;
    protected List<Object> data;
}
