package com.metaverse.workflow.visitorcount.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VisitorCountResponse {
    private Long id;
    private Long totalCount;
    private LocalDateTime createdOn;
    private LocalDateTime lastModified;
}

