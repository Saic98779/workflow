package com.metaverse.workflow.program.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class  ProgramResponse {

    private String mandal;
    private String district;
    private Long programId;
    private String locationName;
    private Long activityId;
    private Long subActivityId;
    private String programName;
    private String activityName;
    private String subActivityName;
    private String agencyName;
    private String programType;
    private String programDetails;
    private String programTitle;
    private Long agencyId;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String spocName;
    private Long spocContactNo;
    private Long programLocation;
    private String programLocationName;
    private String kpi;
    private Date createdOn;
    private Date updatedOn;
    private String status;
    private boolean overdue;
    private List<ProgramSessionResponse> programSessionList;
    private List<MediaCoverageResponse> mediaCoverageList;

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ProgramSession {
        private Long sessionId;
        private String sessionDate;
        private String startTime;
        private String endTime;
        private String sessionTypeName;
        private String sessionTypeMethodology;
        private String sessionDetails;
        private Long resourceId;
        private String resourceName;
        private Long image1;
        private Long image2;
        private Long image3;
        private Long image4;
        private Long image5;
        private String sessionStreamingUrl;
        private Date createdOn;
        private Date updatedOn;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MediaCoverage {
        private Long mediaCoverageId;
        private String mediaCoverageType;
        private String image1;
        private String image2;
        private String image3;
        private String mediaCoverageUrl;
        private String date;
        private Date createdOn;
        private Date updatedOn;
    }

}
