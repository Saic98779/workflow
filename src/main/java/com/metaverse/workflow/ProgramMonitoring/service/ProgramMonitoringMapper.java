package com.metaverse.workflow.ProgramMonitoring.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProgramMonitoringMapper {

    public static ProgramMonitoring mapRequest(ProgramMonitoringRequest request) {
        ProgramMonitoring feedback = ProgramMonitoring.builder()
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

                .timingPunctuality(request.getTimingPunctuality())
                .sessionContinuity(request.getSessionContinuity())
                .participantInterestLevel(request.getParticipantInterestLevel())

                .attendanceSheet(request.getAttendanceSheet())
                .registrationForms(request.getRegistrationForms())
                .participantFeedBack(request.getParticipantFeedBack())
                .speakerFeedBack(request.getSpeakerFeedBack())

                .overallObservation(request.getOverallObservation())

                .participantKnowAboutProgram(request.getParticipantKnowAboutProgram())
                .build();

        // PreEventChecklist
        if (request.getPreEventChecklists() != null) {
            List<PreEventChecklistNew> checklistList = request.getPreEventChecklists().stream()
                    .map(item -> PreEventChecklistNew.builder()
                            .item(item.getItem())
                            .status(item.getStatus())
                            .remarks(item.getRemarks())
                            .programMonitoring(feedback)
                            .build())
                    .collect(Collectors.toList());
            feedback.setPreEventChecklists(checklistList);
        }

        // ProgramDeliveryDetails
        if (request.getProgramDeliveryDetails() != null) {
            List<ProgramDeliveryDetailsNew> deliveryList = request.getProgramDeliveryDetails().stream()
                    .map(d -> com.metaverse.workflow.model.ProgramDeliveryDetailsNew.builder()
                            .speakerName(d.getSpeakerName())
                            .topicDelivered(d.getTopicDelivered())
                            .timeTaken(d.getTimeTaken())
                            .audioVisualUsed(d.getAudioVisualUsed())
                            .relevance(d.getRelevance())
                            .speakerEffectiveness(d.getSpeakerEffectiveness())
                            .programMonitoring(feedback)
                            .build())
                    .collect(Collectors.toList());
            feedback.setProgramDeliveryDetails(deliveryList);
        }

        // LogisticsEvaluation
        if (request.getLogisticsEvaluations() != null) {
            List<LogisticsEvaluationNew> logisticsList = request.getLogisticsEvaluations().stream()
                    .map(l -> LogisticsEvaluationNew.builder()
                            .parameter(l.getParameter())
                            .rating(l.getRating())
                            .remarks(l.getRemarks())
                            .programMonitoring(feedback)
                            .build())
                    .collect(Collectors.toList());
            feedback.setLogisticsEvaluations(logisticsList);
        }

        return feedback;
    }

    public static ProgramMonitoringResponse mapResponse(ProgramMonitoring monitoringFeedBack) {
        return ProgramMonitoringResponse.builder()
                .monitorId(monitoringFeedBack.getProgramMonitoringId())
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

                .timingPunctuality(monitoringFeedBack.getTimingPunctuality())
                .sessionContinuity(monitoringFeedBack.getSessionContinuity())
                .participantInterestLevel(monitoringFeedBack.getParticipantInterestLevel())

                .attendanceSheet(monitoringFeedBack.getAttendanceSheet())
                .registrationForms(monitoringFeedBack.getRegistrationForms())
                .participantFeedBack(monitoringFeedBack.getParticipantFeedBack())
                .speakerFeedBack(monitoringFeedBack.getSpeakerFeedBack())

                .overallObservation(monitoringFeedBack.getOverallObservation())

                .participantKnowAboutProgram(monitoringFeedBack.getParticipantKnowAboutProgram())

                .preEventChecklists(
                        monitoringFeedBack.getPreEventChecklists().stream()
                                .map(pre -> ProgramMonitoringResponse.PreEventChecklist.builder()
                                        .item(pre.getItem())
                                        .status(pre.getStatus())
                                        .remarks(pre.getRemarks())
                                        .build()
                                ).toList()
                )
                .programDeliveryDetails(
                        monitoringFeedBack.getProgramDeliveryDetails().stream()
                                .map(detail -> ProgramMonitoringResponse.ProgramDeliveryDetails.builder()
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
                                .map(log -> ProgramMonitoringResponse.LogisticsEvaluation.builder()
                                        .parameter(log.getParameter())
                                        .rating(log.getRating())
                                        .remarks(log.getRemarks())
                                        .build()
                                ).toList()
                )
                .build();
    }

    public static void updateProgramMonitoring(ProgramMonitoring entity, ProgramMonitoringRequest request) throws DataException {
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
            }
            case 3 -> {
                List<PreEventChecklistNew> existingList = entity.getPreEventChecklists();

                Map<String, PreEventChecklistNew> existingMap = existingList.stream()
                        .collect(Collectors.toMap(
                                PreEventChecklistNew::getItem,
                                c -> c,
                                (existing, duplicate) -> existing // Handle duplicate keys gracefully
                        ));

                List<PreEventChecklistNew> updatedList = new ArrayList<>();

                for (ProgramMonitoringRequest.PreEventChecklist reqChecklist : request.getPreEventChecklists()) {
                    PreEventChecklistNew existing = existingMap.get(reqChecklist.getItem());

                    if (existing != null) {
                        // Update existing checklist
                        existing.setStatus(reqChecklist.getStatus());
                        existing.setRemarks(reqChecklist.getRemarks());
                        updatedList.add(existing);
                    } else {
                        // Create new checklist
                        PreEventChecklistNew newChecklist = PreEventChecklistNew.builder()
                                .item(reqChecklist.getItem())
                                .status(reqChecklist.getStatus())
                                .remarks(reqChecklist.getRemarks())
                                .programMonitoring(entity)
                                .build();
                        updatedList.add(newChecklist);
                    }
                }
                entity.setPreEventChecklists(updatedList);
                entity.setParticipantKnowAboutProgram(request.getParticipantKnowAboutProgram());
            }
            case 4 -> {
                if (request.getProgramDeliveryDetails() != null) {
                    List<ProgramDeliveryDetailsNew> existingList = entity.getProgramDeliveryDetails();
                    Map<Long, ProgramDeliveryDetailsNew> existingMap = existingList.stream()
                            .filter(p -> p.getProgramDeliveryDetailsId() != null)
                            .collect(Collectors.toMap(ProgramDeliveryDetailsNew::getProgramDeliveryDetailsId, p -> p));

                    List<ProgramDeliveryDetailsNew> updatedList = new ArrayList<>();

                    for (ProgramMonitoringRequest.ProgramDeliveryDetails req : request.getProgramDeliveryDetails()) {
                        if (req.getProgramDeliveryDetailsId() != null && existingMap.containsKey(req.getProgramDeliveryDetailsId())) {

                            ProgramDeliveryDetailsNew existing = existingMap.get(req.getProgramDeliveryDetailsId());
                            existing.setSpeakerName(req.getSpeakerName());
                            existing.setTopicDelivered(req.getTopicDelivered());
                            existing.setTimeTaken(req.getTimeTaken());
                            existing.setAudioVisualUsed(req.getAudioVisualUsed());
                            existing.setRelevance(req.getRelevance());
                            existing.setSpeakerEffectiveness(req.getSpeakerEffectiveness());
                            updatedList.add(existing);
                            existingMap.remove(req.getProgramDeliveryDetailsId());
                        } else {
                            ProgramDeliveryDetailsNew newDetail = ProgramDeliveryDetailsNew.builder()
                                    .speakerName(req.getSpeakerName())
                                    .topicDelivered(req.getTopicDelivered())
                                    .timeTaken(req.getTimeTaken())
                                    .audioVisualUsed(req.getAudioVisualUsed())
                                    .relevance(req.getRelevance())
                                    .speakerEffectiveness(req.getSpeakerEffectiveness())
                                    .programMonitoring(entity)
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
            }
            case 6 -> {
                Map<String, LogisticsEvaluationNew> existingMap = entity.getLogisticsEvaluations().stream()
                        .collect(Collectors.toMap(LogisticsEvaluationNew::getParameter, e -> e)); // Use unique field like `parameter`

                List<LogisticsEvaluationNew> updatedList = new ArrayList<>();

                for (ProgramMonitoringRequest.LogisticsEvaluation reqEval : request.getLogisticsEvaluations()) {
                    LogisticsEvaluationNew existing = existingMap.get(reqEval.getParameter());
                    if (existing != null) {
                        existing.setRating(reqEval.getRating());
                        existing.setRemarks(reqEval.getRemarks());
                        updatedList.add(existing); // Keep updated
                        existingMap.remove(reqEval.getParameter());
                    } else {
                        updatedList.add(LogisticsEvaluationNew.builder()
                                .parameter(reqEval.getParameter())
                                .rating(reqEval.getRating())
                                .remarks(reqEval.getRemarks())
                                .programMonitoring(entity)
                                .build());
                    }
                }
                entity.getLogisticsEvaluations().clear();
                entity.getLogisticsEvaluations().addAll(updatedList);
            }

            case 7-> {
                entity.setAttendanceSheet(request.getAttendanceSheet());
                entity.setRegistrationForms(request.getRegistrationForms());
                entity.setParticipantFeedBack(request.getParticipantFeedBack());
                entity.setSpeakerFeedBack(request.getSpeakerFeedBack());
            }
            case 8 -> {
                entity.setOverallObservation(request.getOverallObservation());
            }
            default -> {
                throw new DataException("Invalid step number: " + stepNumber, "INVALID_STEP_NUMBER", 406);
            }
        }
    }
}