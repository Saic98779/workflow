package com.metaverse.workflow.program.service;

import com.metaverse.workflow.common.constants.ProgramStatusConstants;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProgramRequestMapper {
    public static Program map(ProgramRequest programRequest, Agency agency, Location location) {
        return Program.builder()
                .activityId(programRequest.getActivityId())
                .subActivityId(programRequest.getSubActivityId())
                .programType(programRequest.getProgramType())
                .programDetails(programRequest.getProgramDetails())
                .programTitle(programRequest.getProgramTitle())
                .startDate(DateUtil.stringToDate(programRequest.getStartDate(), "dd-MM-yyyy"))
                .endDate(DateUtil.stringToDate(programRequest.getEndDate(), "dd-MM-yyyy"))
                .startTime(programRequest.getStartTime())
                .endTime(programRequest.getEndTime())
                .spocName(programRequest.getSpocName())
                .spocContactNo(programRequest.getSpocContactNo())
                .location(location)
                .kpi(programRequest.getKpi())
                .agency(agency)
                .status(ProgramStatusConstants.PROGRAM_SCHEDULED)
                .build();
    }

    public static ProgramSession mapSession(ProgramSessionRequest session, Resource resource, Program program) {
        return ProgramSession.builder()
                .programSessionId(session.getProgramSessionId())
                .sessionDate(DateUtil.stringToDate(session.getSessionDate(), "dd-MM-yyyy"))
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .sessionTypeName(session.getSessionTypeName())
                .sessionTypeMethodology(session.getSessionTypeMethodology())
                .resource(resource)
                .program(program)
                .sessionDetails(session.getSessionDetails())
                .sessionStreamingUrl(session.getSessionStreamingUrl())
                .build();
    }

    public static ProgramSession mapSession(ProgramSessionRequest request, ProgramSession session) {
        session.setSessionDate(DateUtil.stringToDate(request.getSessionDate(), "dd-MM-yyyy"));
        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());
        session.setSessionTypeName(request.getSessionTypeName());
        session.setSessionTypeMethodology(request.getSessionTypeMethodology());
        session.setSessionDetails(request.getSessionDetails());
        session.setSessionStreamingUrl(request.getSessionStreamingUrl());
        return session;
    }


    public static List<ProgramSessionFile> mapProgramFiles(List<String> files){
        List<ProgramSessionFile> programSessionFileList = new ArrayList<>();
        if (files != null && files.size() > 0)
            files.stream().forEach(file -> programSessionFileList.add(ProgramSessionFile.builder().fileType("files").filePath(file).build()));
        return programSessionFileList;
    }

    /*private static List<MediaCoverage> getMediaCoverages(List<ProgramRequest.MediaCoverage> mediaCoverages) {
        return mediaCoverages.stream().map(mediaCoverage -> MediaCoverage.builder()
                        .mediaCoverageType(mediaCoverage.getMediaCoverageType())
                        .mediaCoverageUrl(mediaCoverage.getMediaCoverageUrl())
                        .image1(mediaCoverage.getImage1())
                        .build())
                .collect(Collectors.toList());
    }*/

    public static Program mapUpdate(ProgramRequest programRequest, Agency agency, Location location, Program exisitingProgram) {

        Program program = exisitingProgram;
        program.setActivityId(programRequest.getActivityId());
        program.setSubActivityId(programRequest.getSubActivityId());
        program.setProgramType(programRequest.getProgramType());
        program.setProgramDetails(programRequest.getProgramDetails());
        program.setProgramTitle(programRequest.getProgramTitle());
        program.setStartTime(programRequest.getStartTime());
        program.setEndTime(programRequest.getEndTime());
        program.setSpocName(programRequest.getSpocName());
        program.setSpocContactNo(programRequest.getSpocContactNo());
        program.setKpi(programRequest.getKpi());
        program.setStartDate(DateUtil.stringToDate(programRequest.getStartDate(), "dd-MM-yyyy"));
        program.setEndDate(DateUtil.stringToDate(programRequest.getEndDate(), "dd-MM-yyyy"));
        program.setAgency(agency);
        program.setLocation(location);
        return program;
    }


}
