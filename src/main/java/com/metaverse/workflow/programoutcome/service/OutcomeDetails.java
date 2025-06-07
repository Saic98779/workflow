package com.metaverse.workflow.programoutcome.service;



import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OutcomeDetails {

    private List<OutcomeDataSet> outcomeForm;

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OutcomeDataSet {
        private String fieldName;
        private String fieldDisplayName;
        private String fieldType;
        private String fieldValue;
        private List<String> fieldOptions;
    }
}


