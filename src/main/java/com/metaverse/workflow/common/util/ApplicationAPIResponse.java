package com.metaverse.workflow.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationAPIResponse<T> {

    private T data;
    private Object code;
    private String message;
    private boolean success;
    private LocalDateTime timestamp;



    public ApplicationAPIResponse(T data, boolean success) {
        this.data = data;
        this.success = success;
    }

    public LocalDateTime getTimestamp() {
        return LocalDateTime.now();
    }
}