package com.metaverse.workflow.program.service;

import com.metaverse.workflow.common.constants.Constants;
import com.metaverse.workflow.common.constants.ProgramStatusConstants;
import com.metaverse.workflow.common.util.CommonUtil;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.program.repository.ProgramRescheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
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
                .status(
                        CommonUtil.activityMap.get(programRequest.getActivityId()).equals(Constants.OTHER_THAN_TRAINING)
                                ? ProgramStatusConstants.PROGRAM_EXECUTION_UPDATED
                                : ProgramStatusConstants.PROGRAM_SCHEDULED
                )
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


    public static List<ProgramSessionFile> mapProgramFiles(List<String> files) {
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

    public static Program mapUpdate(
            ProgramRequest programRequest,
            Agency agency,
            Location location,
            Program existingProgram,
            ProgramRescheduleRepository programRescheduleRepository
    ) {

        Date newStartDate = DateUtil.stringToDate(programRequest.getStartDate(), "dd-MM-yyyy");
        Date oldEndDate = DateUtil.stringToDate(programRequest.getEndDate(), "dd-MM-yyyy");

        // Compare old vs new start date
        if ((newStartDate != null || oldEndDate != null) &&
                (!existingProgram.getStartDate().equals(newStartDate) || !existingProgram.getEndDate().equals(oldEndDate))) {
            ProgramReschedule reschedule = ProgramReschedule.builder()
                    .program(existingProgram)
                    .oldStartDate(existingProgram.getStartDate())
                    .newStartDate(newStartDate)
                    .oldEndDate(existingProgram.getEndDate())
                    .newEndDate(oldEndDate)
                    .build();

            programRescheduleRepository.save(reschedule);
        }

        // Update program fields
        existingProgram.setActivityId(programRequest.getActivityId());
        existingProgram.setSubActivityId(programRequest.getSubActivityId());
        existingProgram.setProgramType(programRequest.getProgramType());
        existingProgram.setProgramDetails(programRequest.getProgramDetails());
        existingProgram.setProgramTitle(programRequest.getProgramTitle());
        existingProgram.setStartTime(programRequest.getStartTime());
        existingProgram.setEndTime(programRequest.getEndTime());
        existingProgram.setSpocName(programRequest.getSpocName());
        existingProgram.setSpocContactNo(programRequest.getSpocContactNo());
        existingProgram.setKpi(programRequest.getKpi());
        existingProgram.setStartDate(newStartDate);
        existingProgram.setEndDate(DateUtil.stringToDate(programRequest.getEndDate(), "dd-MM-yyyy"));
        existingProgram.setAgency(agency);
        existingProgram.setLocation(location);

        return existingProgram;
    }

}
