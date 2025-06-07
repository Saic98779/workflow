package com.metaverse.workflow.program.service;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MediaCoverageRequest {
    private Long programId;
    private Long mediaCoverageId;
    private String mediaCoverageType;
    private Long image1;
    private Long image2;
    private Long image3;
    private String mediaCoverageUrl;
    private String date;
}
