package com.metaverse.workflow.program.service;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProgramSessionRequest {
    private Long programId;
    private Long programSessionId;
    private String sessionDate;
    private String startTime;
    private String endTime;
    private String sessionTypeName;
    private String sessionTypeMethodology;
    private String sessionDetails;
    private Long resourceId;
    private List<String> videoUrls;
    private Long image1;
    private Long image2;
    private Long image3;
    private Long image4;
    private Long image5;
    private String sessionStreamingUrl;
}
