package com.metaverse.workflow.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class WorkflowResponse {
    protected Integer status;
    protected String message;
    protected Object data;
    private int totalPages;
    private long totalElements;
}
