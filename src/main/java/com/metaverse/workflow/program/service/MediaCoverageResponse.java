package com.metaverse.workflow.program.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MediaCoverageResponse {
    private Long mediaCoverageId;
    private String mediaCoverageType;
    private Long image1;
    private Long image2;
    private Long image3;
    private String mediaCoverageUrl;
    private String date;
}
