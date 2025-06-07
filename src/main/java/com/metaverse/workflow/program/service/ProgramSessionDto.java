package com.metaverse.workflow.dto;

import com.metaverse.workflow.model.ProgramSession;
import com.metaverse.workflow.program.service.ProgramSessionResponse;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgramSessionDto {
    private Long programId;
    private String programTitle;
    private String programType;
    private String programDetails;
    private String locationName;
    private String activityName;
    private Long agencyId;
    private String subActivityName;
    private String kpi;
    private Date startDate;
    private Date endDate;
    private String startTime;
    private String endTime;
    private String spocName;
    private Long spocContactNo;
    private String status;
    private Date createdOn;
    private Date updatedOn;

    private String programLocationName;
    private String agencyName;

    private List<ProgramSession> programSessions;
    private int currentPage;
    private int totalPages;
    private long totalSessions;
}
