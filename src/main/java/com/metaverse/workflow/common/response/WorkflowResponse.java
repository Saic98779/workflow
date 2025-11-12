package com.metaverse.workflow.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WorkflowResponse {
    private Integer status;
    private String message;
    private Object data;
    private int totalPages;
    private long totalElements;

    public WorkflowResponse(String error, String message, int i) {
    }

    public static WorkflowResponse success(String message, Object data) {
        return WorkflowResponse.builder()
                .status(200)
                .message(message)
                .data(data)
                .build();
    }

    public static WorkflowResponse success(String message) {
        return WorkflowResponse.builder()
                .status(200)
                .message(message)
                .build();
    }

    public static WorkflowResponse success(String message, Object data, int totalPages, long totalElements) {
        return WorkflowResponse.builder()
                .status(200)
                .message(message)
                .data(data)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .build();
    }

    public static WorkflowResponse error(String message) {
        return WorkflowResponse.builder()
                .status(400)
                .message(message)
                .build();
    }

    public static WorkflowResponse serverError(String message) {
        return WorkflowResponse.builder()
                .status(500)
                .message(message)
                .build();
    }
}
