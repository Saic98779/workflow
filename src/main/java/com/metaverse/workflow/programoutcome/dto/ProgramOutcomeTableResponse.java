package com.metaverse.workflow.programoutcome.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgramOutcomeTableResponse {
    private Integer outcomeTableId;
    private String outcomeTableName;
    private String outcomeTableDisplayName;
}
