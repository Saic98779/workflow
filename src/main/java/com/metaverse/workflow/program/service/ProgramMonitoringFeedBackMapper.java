package com.metaverse.workflow.program.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.ProgramDeliveryDetails;
import com.metaverse.workflow.model.ProgramMonitoringFeedBack;
import com.metaverse.workflow.model.PreEventChecklist;
import com.metaverse.workflow.model.LogisticsEvaluation;
import org.apache.poi.ss.formula.functions.Replace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProgramMonitoringFeedBackMapper {


    public static ProgramMonitoringFeedBack mapRequest(ProgramMonitoringFeedBackRequest request) {
        ProgramMonitoringFeedBack feedback = ProgramMonitoringFeedBack.builder()
                .programId(request.getProgramId())
                .stepNumber(request.getStepNumber())
                .state(request.getState())
                .district(request.getDistrict())
                .startDate(DateUtil.stringToDate(request.getStartDate(), "dd-MM-yyy"))
                .agencyName(request.getAgencyName())
                .programType(request.getProgramType())
                .programName(request.getProgramName())
                .venueName(request.getVenueName())
                .hostingAgencyName(request.getHostingAgencyName())
                .spocName(request.getSpocName())
                .spocContact(request.getSpocContact())
                .inTime(request.getInTime())
                .outTime(request.getOutTime())

                .maleParticipants(request.getMaleParticipants())
                .femaleParticipants(request.getFemaleParticipants())
                .transGenderParticipants(request.getTransGenderParticipants())
                .totalParticipants(request.getTotalParticipants())
                .noOfSHG(request.getNoOfSHG())
                .noOfMSME(request.getNoOfMSME())
                .noOfStartup(request.getNoOfStartup())
                .noOfDIC(request.getNoOfDIC())
                .noOfIAs(request.getNoOfIAs())
                .noOfOthers(request.getNoOfOthers())
                .noOfSC(request.getNoOfSC())
                .noOfST(request.getNoOfST())
                .noOfOC(request.getNoOfOC())
                .noOfBC(request.getNoOfBC())
                .noOfMinority(request.getNoOfMinority())

                .timingPunctuality(request.getTimingPunctuality())
                .sessionContinuity(request.getSessionContinuity())
                .participantInterestLevel(request.getParticipantInterestLevel())
                .participantQueries(request.getParticipantQueries())
                .overallEnergyEngagement(request.getOverallEnergyEngagement())
                .unforeseenIssues(request.getUnforeseenIssues())

                .participantsFeedbackCollected(request.getParticipantsFeedbackCollected())
                .resourceFeedbackCollected(request.getResourceFeedbackCollected())
                .prePostTestsConducted(request.getPrePostTestsConducted())
                .learningHighlights(request.getLearningHighlights())

                .attendanceSheet(request.getAttendanceSheet())
                .registrationForms(request.getRegistrationForms())
                .participantFeedBack(request.getParticipantFeedBack())
                .speakerFeedBack(request.getSpeakerFeedBack())
                .photographsAttached(request.getPhotographsAttached())
                .summaryReportPrepared(request.getSummaryReportPrepared())

                .observedPractices(request.getObservedPractices())
                .challengesFaced(request.getChallengesFaced())
                .issuesAndCorrections(request.getIssuesAndCorrections())

                .overallProgramOrganization(request.getOverallProgramOrganization())
                .qualityOfSessions(request.getQualityOfSessions())
                .participantsSatisfaction(request.getParticipantsSatisfaction())
                .impactPotential(request.getImpactPotential())

                .additionalRemarks(request.getAdditionalRemarks())
                .build();

        // PreEventChecklist
        if (request.getPreEventChecklists() != null) {
            List<PreEventChecklist> checklistList = request.getPreEventChecklists().stream()
                    .map(item -> PreEventChecklist.builder()
                            .item(item.getItem())
                            .status(item.getStatus())
                            .remarks(item.getRemarks())
                            .programMonitoringFeedBack(feedback)
                            .build())
                    .collect(Collectors.toList());
            feedback.setPreEventChecklists(checklistList);
        }

        // ProgramDeliveryDetails
        if (request.getProgramDeliveryDetails() != null) {
            List<ProgramDeliveryDetails> deliveryList = request.getProgramDeliveryDetails().stream()
                    .map(d -> com.metaverse.workflow.model.ProgramDeliveryDetails.builder()
                            .speakerName(d.getSpeakerName())
                            .topicDelivered(d.getTopicDelivered())
                            .timeTaken(d.getTimeTaken())
                            .audioVisualUsed(d.getAudioVisualUsed())
                            .relevance(d.getRelevance())
                            .speakerEffectiveness(d.getSpeakerEffectiveness())
                            .programMonitoringFeedBack(feedback)
                            .build())
                    .collect(Collectors.toList());
            feedback.setProgramDeliveryDetails(deliveryList);
        }

        // LogisticsEvaluation
        if (request.getLogisticsEvaluations() != null) {
            List<LogisticsEvaluation> logisticsList = request.getLogisticsEvaluations().stream()
                    .map(l -> LogisticsEvaluation.builder()
                            .parameter(l.getParameter())
                            .rating(l.getRating())
                            .remarks(l.getRemarks())
                            .programMonitoringFeedBack(feedback)
                            .build())
                    .collect(Collectors.toList());
            feedback.setLogisticsEvaluations(logisticsList);
        }

        return feedback;
    }

    public static ProgramMonitoringFeedBackResponse mapResponse(ProgramMonitoringFeedBack monitoringFeedBack) {
        return ProgramMonitoringFeedBackResponse.builder()
                .monitorId(monitoringFeedBack.getMonitoringFeedBackId())
                .stepNumber(monitoringFeedBack.getStepNumber())
                .programId(monitoringFeedBack.getProgramId())
                .state(monitoringFeedBack.getState())
                .district(monitoringFeedBack.getDistrict())
                .startDate(DateUtil.dateToString(monitoringFeedBack.getStartDate(), "dd-MM-yyyy"))
                .agencyName(monitoringFeedBack.getAgencyName())
                .programType(monitoringFeedBack.getProgramType())
                .programName(monitoringFeedBack.getProgramName())
                .venueName(monitoringFeedBack.getVenueName())
                .hostingAgencyName(monitoringFeedBack.getHostingAgencyName())
                .spocName(monitoringFeedBack.getSpocName())
                .spocContact(monitoringFeedBack.getSpocContact())
                .inTime(monitoringFeedBack.getInTime())
                .outTime(monitoringFeedBack.getOutTime())

                .maleParticipants(monitoringFeedBack.getMaleParticipants())
                .femaleParticipants(monitoringFeedBack.getFemaleParticipants())
                .transGenderParticipants(monitoringFeedBack.getTransGenderParticipants())
                .totalParticipants(monitoringFeedBack.getTotalParticipants())
                .noOfSHG(monitoringFeedBack.getNoOfSHG())
                .noOfMSME(monitoringFeedBack.getNoOfMSME())
                .noOfStartup(monitoringFeedBack.getNoOfStartup())
                .noOfDIC(monitoringFeedBack.getNoOfDIC())
                .noOfIAs(monitoringFeedBack.getNoOfIAs())
                .noOfOthers(monitoringFeedBack.getNoOfOthers())
                .noOfSC(monitoringFeedBack.getNoOfSC())
                .noOfST(monitoringFeedBack.getNoOfST())
                .noOfOC(monitoringFeedBack.getNoOfOC())
                .noOfBC(monitoringFeedBack.getNoOfBC())
                .noOfMinority(monitoringFeedBack.getNoOfMinority())

                .timingPunctuality(monitoringFeedBack.getTimingPunctuality())
                .sessionContinuity(monitoringFeedBack.getSessionContinuity())
                .participantInterestLevel(monitoringFeedBack.getParticipantInterestLevel())
                .participantQueries(monitoringFeedBack.getParticipantQueries())
                .overallEnergyEngagement(monitoringFeedBack.getOverallEnergyEngagement())
                .unforeseenIssues(monitoringFeedBack.getUnforeseenIssues())

                .participantsFeedbackCollected(monitoringFeedBack.getParticipantsFeedbackCollected())
                .resourceFeedbackCollected(monitoringFeedBack.getResourceFeedbackCollected())
                .prePostTestsConducted(monitoringFeedBack.getPrePostTestsConducted())
                .learningHighlights(monitoringFeedBack.getLearningHighlights())

                .attendanceSheet(monitoringFeedBack.getAttendanceSheet())
                .registrationForms(monitoringFeedBack.getRegistrationForms())
                .participantFeedBack(monitoringFeedBack.getParticipantFeedBack())
                .speakerFeedBack(monitoringFeedBack.getSpeakerFeedBack())
                .photographsAttached(monitoringFeedBack.getPhotographsAttached())
                .summaryReportPrepared(monitoringFeedBack.getSummaryReportPrepared())

                .observedPractices(monitoringFeedBack.getObservedPractices())
                .challengesFaced(monitoringFeedBack.getChallengesFaced())
                .issuesAndCorrections(monitoringFeedBack.getIssuesAndCorrections())

                .overallProgramOrganization(monitoringFeedBack.getOverallProgramOrganization())
                .qualityOfSessions(monitoringFeedBack.getQualityOfSessions())
                .participantsSatisfaction(monitoringFeedBack.getParticipantsSatisfaction())
                .impactPotential(monitoringFeedBack.getImpactPotential())

                .additionalRemarks(monitoringFeedBack.getAdditionalRemarks())

                .preEventChecklists(
                        monitoringFeedBack.getPreEventChecklists().stream()
                                .map(pre -> ProgramMonitoringFeedBackResponse.PreEventChecklist.builder()
                                        .item(pre.getItem())
                                        .status(pre.getStatus())
                                        .remarks(pre.getRemarks())
                                        .build()
                                ).toList()
                )
                .programDeliveryDetails(
                        monitoringFeedBack.getProgramDeliveryDetails().stream()
                                .map(detail -> ProgramMonitoringFeedBackResponse.ProgramDeliveryDetails.builder()
                                        .programDeliveryDetailsId(detail.getProgramDeliveryDetailsId())
                                        .speakerName(detail.getSpeakerName())
                                        .topicDelivered(detail.getTopicDelivered())
                                        .timeTaken(detail.getTimeTaken())
                                        .audioVisualUsed(detail.getAudioVisualUsed())
                                        .relevance(detail.getRelevance())
                                        .speakerEffectiveness(detail.getSpeakerEffectiveness())
                                        .build()
                                ).toList()
                )
                .logisticsEvaluations(
                        monitoringFeedBack.getLogisticsEvaluations().stream()
                                .map(log -> ProgramMonitoringFeedBackResponse.LogisticsEvaluation.builder()
                                        .parameter(log.getParameter())
                                        .rating(log.getRating())
                                        .remarks(log.getRemarks())
                                        .build()
                                ).toList()
                )

                .build();
    }

    public static void updateProgramMonitoringFeedBack(ProgramMonitoringFeedBack entity, ProgramMonitoringFeedBackRequest request) throws DataException {
        int stepNumber = request.getStepNumber();
        entity.setStepNumber(stepNumber);

        switch (stepNumber) {
            case 1 -> {
                entity.setProgramId(request.getProgramId());
                entity.setState(request.getState());
                entity.setDistrict(request.getDistrict());
                entity.setStartDate(DateUtil.stringToDate(request.getStartDate(), "dd-MM-yyyy"));
                entity.setAgencyName(request.getAgencyName());
                entity.setProgramType(request.getProgramType());
                entity.setProgramName(request.getProgramName());
                entity.setVenueName(request.getVenueName());
                entity.setHostingAgencyName(request.getHostingAgencyName());
                entity.setSpocName(request.getSpocName());
                entity.setSpocContact(request.getSpocContact());
                entity.setInTime(request.getInTime());
                entity.setOutTime(request.getOutTime());
            }
            case 2 -> {
                entity.setMaleParticipants(request.getMaleParticipants());
                entity.setFemaleParticipants(request.getFemaleParticipants());
                entity.setTransGenderParticipants(request.getTransGenderParticipants());
                entity.setTotalParticipants(request.getTotalParticipants());
                entity.setNoOfSHG(request.getNoOfSHG());
                entity.setNoOfMSME(request.getNoOfMSME());
                entity.setNoOfStartup(request.getNoOfStartup());
                entity.setNoOfDIC(request.getNoOfDIC());
                entity.setNoOfIAs(request.getNoOfIAs());
                entity.setNoOfOthers(request.getNoOfOthers());
                entity.setNoOfSC(request.getNoOfSC());
                entity.setNoOfST(request.getNoOfST());
                entity.setNoOfOC(request.getNoOfOC());
                entity.setNoOfBC(request.getNoOfBC());
                entity.setNoOfMinority(request.getNoOfMinority());
            }
            case 3 -> {
                List<PreEventChecklist> existingList = entity.getPreEventChecklists();

                Map<String, PreEventChecklist> existingMap = existingList.stream()
                        .collect(Collectors.toMap(
                                PreEventChecklist::getItem,
                                c -> c,
                                (existing, duplicate) -> existing // Handle duplicate keys gracefully
                        ));

                List<PreEventChecklist> updatedList = new ArrayList<>();

                for (ProgramMonitoringFeedBackRequest.PreEventChecklist reqChecklist : request.getPreEventChecklists()) {
                    PreEventChecklist existing = existingMap.get(reqChecklist.getItem());

                    if (existing != null) {
                        // Update existing checklist
                        existing.setStatus(reqChecklist.getStatus());
                        existing.setRemarks(reqChecklist.getRemarks());
                        updatedList.add(existing);
                    } else {
                        // Create new checklist
                        PreEventChecklist newChecklist = PreEventChecklist.builder()
                                .item(reqChecklist.getItem())
                                .status(reqChecklist.getStatus())
                                .remarks(reqChecklist.getRemarks())
                                .programMonitoringFeedBack(entity)
                                .build();
                        updatedList.add(newChecklist);
                    }
                }


                entity.setPreEventChecklists(updatedList);



            }
            case 4 -> {
                if (request.getProgramDeliveryDetails() != null) {
                    List<ProgramDeliveryDetails> existingList = entity.getProgramDeliveryDetails();
                    Map<Long, ProgramDeliveryDetails> existingMap = existingList.stream()
                            .filter(p -> p.getProgramDeliveryDetailsId() != null)
                            .collect(Collectors.toMap(ProgramDeliveryDetails::getProgramDeliveryDetailsId, p -> p));

                    List<ProgramDeliveryDetails> updatedList = new ArrayList<>();

                    for (ProgramMonitoringFeedBackRequest.ProgramDeliveryDetails req : request.getProgramDeliveryDetails()) {
                        if (req.getProgramDeliveryDetailsId() != null && existingMap.containsKey(req.getProgramDeliveryDetailsId())) {

                            ProgramDeliveryDetails existing = existingMap.get(req.getProgramDeliveryDetailsId());
                            existing.setSpeakerName(req.getSpeakerName());
                            existing.setTopicDelivered(req.getTopicDelivered());
                            existing.setTimeTaken(req.getTimeTaken());
                            existing.setAudioVisualUsed(req.getAudioVisualUsed());
                            existing.setRelevance(req.getRelevance());
                            existing.setSpeakerEffectiveness(req.getSpeakerEffectiveness());
                            updatedList.add(existing);
                            existingMap.remove(req.getProgramDeliveryDetailsId());
                        } else {
                            ProgramDeliveryDetails newDetail = ProgramDeliveryDetails.builder()
                                    .speakerName(req.getSpeakerName())
                                    .topicDelivered(req.getTopicDelivered())
                                    .timeTaken(req.getTimeTaken())
                                    .audioVisualUsed(req.getAudioVisualUsed())
                                    .relevance(req.getRelevance())
                                    .speakerEffectiveness(req.getSpeakerEffectiveness())
                                    .programMonitoringFeedBack(entity)
                                    .build();
                            updatedList.add(newDetail);
                        }
                    }
                    existingList.clear();
                    existingList.addAll(updatedList);
                }



            }
            case 5 -> {
                entity.setTimingPunctuality(request.getTimingPunctuality());
                entity.setSessionContinuity(request.getSessionContinuity());
                entity.setParticipantInterestLevel(request.getParticipantInterestLevel());
                entity.setParticipantQueries(request.getParticipantQueries());
                entity.setOverallEnergyEngagement(request.getOverallEnergyEngagement());
                entity.setUnforeseenIssues(request.getUnforeseenIssues());


            }
            case 6 -> {
                Map<String, LogisticsEvaluation> existingMap = entity.getLogisticsEvaluations().stream()
                        .collect(Collectors.toMap(LogisticsEvaluation::getParameter, e -> e)); // Use unique field like `parameter`

                List<LogisticsEvaluation> updatedList = new ArrayList<>();

                for (ProgramMonitoringFeedBackRequest.LogisticsEvaluation reqEval : request.getLogisticsEvaluations()) {
                    LogisticsEvaluation existing = existingMap.get(reqEval.getParameter());
                    if (existing != null) {
                        existing.setRating(reqEval.getRating());
                        existing.setRemarks(reqEval.getRemarks());
                        updatedList.add(existing); // Keep updated
                        existingMap.remove(reqEval.getParameter());
                    } else {
                        updatedList.add(LogisticsEvaluation.builder()
                                .parameter(reqEval.getParameter())
                                .rating(reqEval.getRating())
                                .remarks(reqEval.getRemarks())
                                .programMonitoringFeedBack(entity)
                                .build());
                    }
                }


                entity.getLogisticsEvaluations().clear();
                entity.getLogisticsEvaluations().addAll(updatedList);

            }
            case 7 -> {
                entity.setParticipantsFeedbackCollected(request.getParticipantsFeedbackCollected());
                entity.setResourceFeedbackCollected(request.getResourceFeedbackCollected());
                entity.setPrePostTestsConducted(request.getPrePostTestsConducted());
                entity.setLearningHighlights(request.getLearningHighlights());

            }
            case 8 -> {
                entity.setAttendanceSheet(request.getAttendanceSheet());
                entity.setRegistrationForms(request.getRegistrationForms());
                entity.setParticipantFeedBack(request.getParticipantFeedBack());
                entity.setSpeakerFeedBack(request.getSpeakerFeedBack());
                entity.setPhotographsAttached(request.getPhotographsAttached());
                entity.setSummaryReportPrepared(request.getSummaryReportPrepared());

            }
            case 9 -> {
                entity.setObservedPractices(request.getObservedPractices());
                entity.setChallengesFaced(request.getChallengesFaced());
                entity.setIssuesAndCorrections(request.getIssuesAndCorrections());
            }
            case 10 -> {
                entity.setOverallProgramOrganization(request.getOverallProgramOrganization());
                entity.setQualityOfSessions(request.getQualityOfSessions());
                entity.setParticipantsSatisfaction(request.getParticipantsSatisfaction());
                entity.setImpactPotential(request.getImpactPotential());
            }
            case 11 -> {
                entity.setAdditionalRemarks(request.getAdditionalRemarks());
            }
            default -> {
                throw new DataException("Invalid step number: " + stepNumber, "INVALID_STEP_NUMBER", 406);
            }
        }
    }

}
