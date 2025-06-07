package com.metaverse.workflow.program.service;

import com.metaverse.workflow.common.util.CommonUtil;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.participant.service.ParticipantResponse;
import com.metaverse.workflow.participant.service.ParticipantResponseMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProgramResponseMapper {

    public static ProgramResponse map(Program program) {
        return ProgramResponse.builder()
                .mandal(program.getLocation() != null ? program.getLocation().getMandal() :null)
                .district(program.getLocation() != null ? program.getLocation().getDistrict() :null)
                .status(program.getStatus())
                .programId(program.getProgramId())
                .activityId(program.getActivityId())
                .subActivityId(program.getSubActivityId())
                .activityName(CommonUtil.activityMap.get(program.getActivityId()))
                .subActivityName(CommonUtil.subActivityMap.get(program.getSubActivityId()))
                .programType(program.getProgramType())
                .programDetails(program.getProgramDetails())
                .programTitle(program.getProgramTitle())
                .agencyId(program.getAgency().getAgencyId())
                .agencyName(program.getAgency().getAgencyName())
                .startDate(DateUtil.dateToString(program.getStartDate(), "dd-MM-yyyy"))
                .endDate(DateUtil.dateToString(program.getEndDate(), "dd-MM-yyyy"))
                .startTime(program.getStartTime())
                .endTime(program.getEndTime())
                .spocName(program.getSpocName())
                .spocContactNo(program.getSpocContactNo())
                .programLocation(program.getLocation() != null ?program.getLocation().getLocationId() :null)
                .programLocationName(program.getLocation() != null ?program.getLocation().getLocationName() :null)
                .kpi(program.getKpi())
                .createdOn(program.getCreatedOn())
                .updatedOn(program.getUpdatedOn())
                .build();
    }

    public static ProgramSessionResponse mapSession(ProgramSession session, List<ProgramSessionFile> sessionFiles) {
        return ProgramSessionResponse.builder()
                .sessionId(session.getProgramSessionId())
                .sessionDate(DateUtil.dateToString(session.getSessionDate(), "dd-MM-yyyy"))
                .sessionDetails(session.getSessionDetails())
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .sessionTypeName(session.getSessionTypeName())
                .sessionTypeMethodology(session.getSessionTypeMethodology())
                .resourceId(session.getResource().getResourceId())
                .resourceName(session.getResource().getName())
                .programId(session.getProgram().getProgramId())
                .sessionStreamingUrl(session.getSessionStreamingUrl())
                .files(sessionFiles != null ? mapSessionFile(sessionFiles) : null)
                .image1(session.getImage1())
                .image2(session.getImage2())
                .image3(session.getImage3())
                .image4(session.getImage4())
                .image5(session.getImage5())
                .createdOn(session.getCreatedOn())
                .updatedOn(session.getUpdatedOn())
                .build();
    }

    public static ProgramResponse mapProgram(Program program) {
        return ProgramResponse.builder()
                .mandal(program.getLocation() != null ? program.getLocation().getMandal() :null)
                .district(program.getLocation() != null ? program.getLocation().getDistrict() :null)
                .status(program.getStatus())
                .programId(program.getProgramId())
                .activityId(program.getActivityId())
                .subActivityId(program.getSubActivityId())
                .activityName(CommonUtil.activityMap.get(program.getActivityId()))
                .subActivityName(CommonUtil.subActivityMap.get(program.getSubActivityId()))
                .programType(program.getProgramType())
                .programDetails(program.getProgramDetails())
                .programTitle(program.getProgramTitle())
                .agencyId(program.getAgency().getAgencyId())
                .agencyName(program.getAgency().getAgencyName())
                .startDate(DateUtil.dateToString(program.getStartDate(), "dd-MM-yyyy"))
                .endDate(DateUtil.dateToString(program.getEndDate(), "dd-MM-yyyy"))
                .startTime(program.getStartTime())
                .endTime(program.getEndTime())
                .spocName(program.getSpocName())
                .overdue(program.isOverdue())
                .spocContactNo(program.getSpocContactNo())
                .programLocation(program.getLocation().getLocationId())
                .programLocationName(program.getLocation().getLocationName())
                .kpi(program.getKpi())
                .createdOn(program.getCreatedOn())
                .updatedOn(program.getUpdatedOn())
                .programSessionList(program.getProgramSessionList().stream().map(session -> mapSession(session, session.getProgramSessionFileList())).collect(Collectors.toList()))
                .mediaCoverageList(program.getMediaCoverageList().stream().map(ProgramResponseMapper::mapMediaCoverage).collect(Collectors.toList()))
                .build();
    }

    public static List<ProgramSessionResponse.ProgramSessionFile> mapSessionFile(List<ProgramSessionFile> programSessionFileList) {
        List<ProgramSessionResponse.ProgramSessionFile> files = new ArrayList<>();
        return programSessionFileList != null ? programSessionFileList.stream().map(file ->
                        ProgramSessionResponse.ProgramSessionFile.builder()
                                .programSessionFileId(file.getProgramSessionFileId())
                                .fileType(file.getFileType())
                                .filePath(file.getFilePath())
                                .build())
                .collect(Collectors.toList()) : files;
    }

    private static List<ProgramResponse.ProgramSession> getProgramSessions(List<ProgramSession> sessions) {
        return sessions.stream().map(session -> ProgramResponse.ProgramSession.builder()
                        .sessionId(session.getProgramSessionId())
                        .sessionDate(DateUtil.dateToString(session.getSessionDate(), "dd-MM-yyyy"))
                        .startTime(session.getStartTime())
                        .endTime(session.getEndTime())
                        .sessionTypeName(session.getSessionTypeName())
                        .sessionTypeMethodology(session.getSessionTypeMethodology())
                        .sessionStreamingUrl(session.getSessionStreamingUrl())
                        .image1(session.getImage1())
                        .image2(session.getImage2())
                        .image3(session.getImage3())
                        .image4(session.getImage4())
                        .image5(session.getImage5())
                        .createdOn(session.getCreatedOn())
                        .updatedOn(session.getUpdatedOn())
                        .build())
                .collect(Collectors.toList());
    }

    public static List<ParticipantResponse> mapProgramParticipants(List<Participant> participantList) {
        return participantList != null ? participantList.stream().map(participant ->
                        ParticipantResponseMapper.map(participant))
                .collect(Collectors.toList()) : null;
    }

   /* public static List<ParticipantVerificationResponse> mapProgramParticipantVerification(List<Participant> participantList, List<CallCenterVerification> callCenterVerificationList) {
        List<ParticipantVerificationResponse> responseList = new ArrayList<>();
        final Map<Long, CallCenterVerification> verificationMap = callCenterVerificationList != null && callCenterVerificationList.size() > 0 ? callCenterVerificationList.stream().collect(Collectors.toMap(callCenterVerification -> callCenterVerification.getParticipantId(), callCenterVerification -> callCenterVerification)) : new HashMap<>();
        if(participantList != null && participantList.size() > 0) {
            responseList = participantList.stream().map(participant ->
                ParticipantVerificationResponse
                        .builder()
                        .participantId(participant.getParticipantId())
                        .email(participant.getEmail())
                        .ccVerificationStatusId(verificationMap.get(participant.getParticipantId()) != null ? verificationMap.get(participant.getParticipantId()).getCcVerificationStatus().getCcVerificationStatusId() : null)
                        .ccVerificationStatus(verificationMap.get(participant.getParticipantId()) != null ? verificationMap.get(participant.getParticipantId()).getCcVerificationStatus().getVerificationDetails() : null)
                        .verifiedBy(verificationMap.get(participant.getParticipantId()) != null ? verificationMap.get(participant.getParticipantId()).getVerifiedBy().getUserId() : null)
                        .aadharNo(participant.getAadharNo())
                        .mobileNo(participant.getMobileNo())
                        .participantName(participant.getParticipantName())
                        .organizationName(participant.getOrganization() != null ? participant.getOrganization().getOrganizationName() : null)
                        .gender(participant.getGender())
                        .designation(participant.getDesignation())
                        .disability(participant.getDisability())
                        .memberId(participant.getMemberId())
                        .questionAnswersList(verificationMap.get(participant.getParticipantId()) != null ? verificationMap.get(participant.getParticipantId()).getQuestionAnswers() : null)
                        .build()
            ).collect(Collectors.toList());

        }
        return responseList;
    }*/
    public static List<ParticipantVerificationResponse> mapProgramParticipantVerification(List<Participant> participantList, List<CallCenterVerification> callCenterVerificationList) {
        List<ParticipantVerificationResponse> responseList = new ArrayList<>();
        final Map<Long, CallCenterVerification> verificationMap = (callCenterVerificationList != null && !callCenterVerificationList.isEmpty())   ? callCenterVerificationList.stream().collect(Collectors.toMap(CallCenterVerification::getParticipantId, verification -> verification)) : new HashMap<>();
        if (participantList != null && !participantList.isEmpty()) {
            responseList = participantList.stream().map(participant -> {
                CallCenterVerification verification = verificationMap.get(participant.getParticipantId());
                List<ParticipantVerificationResponse.QuestionAnswers> mappedAnswers = null;
                if (verification != null && verification.getQuestionAnswers() != null) {
                    mappedAnswers = verification.getQuestionAnswers().stream().map(answer ->
                            ParticipantVerificationResponse.QuestionAnswers.builder()
                                    .question(answer.getQuestion())
                                    .answers(answer.getAnswers())
                                    .build()
                    ).collect(Collectors.toList());
                }

                return ParticipantVerificationResponse.builder()
                        .participantId(participant.getParticipantId())
                        .participantName(participant.getParticipantName())
                        .gender(participant.getGender())
                        .category(participant.getCategory()) // Assuming `getCategory()` exists
                        .disability(participant.getDisability())
                        .aadharNo(participant.getAadharNo())
                        .mobileNo(participant.getMobileNo())
                        .email(participant.getEmail())
                        .designation(participant.getDesignation())
                        .organizationName(participant.getOrganization() != null ? participant.getOrganization().getOrganizationName() : null)
                        .verifiedBy(verification != null && verification.getVerifiedBy() != null? verification.getVerifiedBy().getUserId() : null)
                        .ccVerificationStatusId(verification != null && verification.getCcVerificationStatus() != null ? verification.getCcVerificationStatus().getCcVerificationStatusId() : null)
                        .ccVerificationStatus(verification != null && verification.getCcVerificationStatus() != null ? verification.getCcVerificationStatus().getVerificationDetails() : null)
                        .memberId(participant.getMemberId())
                        .questionAnswersList(mappedAnswers)
                        .build();
            }).collect(Collectors.toList());
        }

        return responseList;
    }


    public static MediaCoverageResponse mapMediaCoverage(MediaCoverage mediaCoverage) {
        return MediaCoverageResponse
                .builder()
                .mediaCoverageType(mediaCoverage.getMediaCoverageType())
                .mediaCoverageId(mediaCoverage.getMediaCoverageId())
                .mediaCoverageUrl(mediaCoverage.getMediaCoverageUrl())
                .date(DateUtil.dateToString(mediaCoverage.getDate(), "dd-MM-yyyy"))
                .image1(mediaCoverage.getImage1())
                .image2(mediaCoverage.getImage2())
                .image3(mediaCoverage.getImage3())
                .build();
    }

}
