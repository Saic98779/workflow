package com.metaverse.workflow.ProgramMonitoring.service;

import com.metaverse.workflow.common.util.CommonUtil;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.*;

import java.util.Optional;

public class ProgramMonitoringMapper {

    public static ProgramMonitoring mapRequest(ProgramMonitoringRequest request, User user) {
        int screen1Score = 0;
        if (request.getProgramAgendaCirculated()) screen1Score += 2;
        if (request.getProgramAsPerSchedule()) screen1Score += 2;
        if (request.getTrainingMaterialSupplied()) screen1Score += 2;
        if (request.getSeatingArrangementsMade()) screen1Score += 2;
        if (request.getAvProjectorAvailable()) screen1Score += 2;
        String source = request.getHowDidYouKnowAboutProgram();

        if (source != null && !source.isBlank()) {
            if (source.equals("Officials") || source.equals("Others")) {
                screen1Score += 1;
            } else if (source.equals("IAs") || source.equals("News paper / TV") || source.equals("Social media")) {
                screen1Score += 2;
            }
        }
        return ProgramMonitoring.builder()
                .agencyId(request.getAgencyId())
                .district(request.getDistrict())
                .programId(request.getProgramId())
                .user(user)
                .stepNumber(request.getStepNumber())
                .programAgendaCirculated(request.getProgramAgendaCirculated())
                .programAsPerSchedule(request.getProgramAsPerSchedule())
                .trainingMaterialSupplied(request.getTrainingMaterialSupplied())
                .seatingArrangementsMade(request.getSeatingArrangementsMade())
                .avProjectorAvailable(request.getAvProjectorAvailable())
                .howDidYouKnowAboutProgram(request.getHowDidYouKnowAboutProgram())
                .participantsMale(request.getParticipantsMale())
                .participantsFemale(request.getParticipantsFemale())
                .participantsTransgender(request.getParticipantsTransgender())
                .dicRegistrationParticipated(request.getDicRegistrationParticipated())
                .shgRegistrationParticipated(request.getShgRegistrationParticipated())
                .msmeRegistrationParticipated(request.getMsmeRegistrationParticipated())
                .startupsRegistrationParticipated(request.getStartupsRegistrationParticipated())
                .noIAsParticipated(request.getNoIAsParticipated())
                .speaker1Name(request.getSpeaker1Name())
                .topicAsPerSessionPlan1(request.getTopicAsPerSessionPlan1())
                .timeTaken1(request.getTimeTaken1())
                .audioVisualAidUsed1(request.getAudioVisualAidUsed1())
                .relevance1(request.getRelevance1())
                .sessionContinuity1(request.getSessionContinuity1())
                .participantInteraction1(request.getParticipantInteraction1())
                .speaker2Name(request.getSpeaker2Name())
                .topicAsPerSessionPlan2(request.getTopicAsPerSessionPlan2())
                .timeTaken2(request.getTimeTaken2())
                .audioVisualAidUsed2(request.getAudioVisualAidUsed2())
                .relevance2(request.getRelevance2())
                .sessionContinuity2(request.getSessionContinuity2())
                .participantInteraction2(request.getParticipantInteraction2())
                .venueQuality(request.getVenueQuality())
                .accessibility(request.getAccessibility())
                .teaSnacks(request.getTeaSnacks())
                .lunch(request.getLunch())
                .cannedWater(request.getCannedWater())
                .toiletHygiene(request.getToiletHygiene())
                .avEquipment(request.getAvEquipment())
                .stationary(request.getStationary())
                .relevant(request.getRelevant())
                .enthusiast(request.getEnthusiast())
                .feltUseful(request.getFeltUseful())
                .futureWillingToEngage(request.getFutureWillingToEngage())
                .qualified(request.getQualified())
                .experienced(request.getExperienced())
                .certified(request.getCertified())
                .deliveryMethodologyGood(request.getDeliveryMethodologyGood())
                .relevantExperience(request.getRelevantExperience())
                .overallObservation(request.getOverallObservation())
                .screen1Score(screen1Score)
                .bestPracticesIdentified(request.getBestPracticesIdentified())
                .build();
    }


    public static ProgramMonitoringResponse mapResponse(ProgramMonitoring monitoringFeedBack) {
        return ProgramMonitoringResponse.builder()
                .programName(CommonUtil.programMap.get(monitoringFeedBack.getProgramId()))
                .agencyName(CommonUtil.agencyMap.get(monitoringFeedBack.getAgencyId()))
                .userId(monitoringFeedBack.getUser().getUserId())
                .userName(monitoringFeedBack.getUser().getFirstName()+" "+monitoringFeedBack.getUser().getLastName())
                .monitoringDate(DateUtil.dateToString(monitoringFeedBack.getMoniteringDate(),"dd-MM-yyyy"))

                .programMonitoringId(monitoringFeedBack.getProgramMonitoringId())
                .agencyId(monitoringFeedBack.getAgencyId())
                .district(monitoringFeedBack.getDistrict())
                .programId(monitoringFeedBack.getProgramId())
                .stepNumber(monitoringFeedBack.getStepNumber())
                .programAgendaCirculated(monitoringFeedBack.getProgramAgendaCirculated())
                .programAsPerSchedule(monitoringFeedBack.getProgramAsPerSchedule())
                .trainingMaterialSupplied(monitoringFeedBack.getTrainingMaterialSupplied())
                .seatingArrangementsMade(monitoringFeedBack.getSeatingArrangementsMade())
                .avProjectorAvailable(monitoringFeedBack.getAvProjectorAvailable())
                .howDidYouKnowAboutProgram(monitoringFeedBack.getHowDidYouKnowAboutProgram())
                .participantsMale(monitoringFeedBack.getParticipantsMale())
                .participantsFemale(monitoringFeedBack.getParticipantsFemale())
                .participantsTransgender(monitoringFeedBack.getParticipantsTransgender())
                .dicRegistrationParticipated(monitoringFeedBack.getDicRegistrationParticipated())
                .shgRegistrationParticipated(monitoringFeedBack.getShgRegistrationParticipated())
                .msmeRegistrationParticipated(monitoringFeedBack.getMsmeRegistrationParticipated())
                .startupsRegistrationParticipated(monitoringFeedBack.getStartupsRegistrationParticipated())
                .noIAsParticipated(monitoringFeedBack.getNoIAsParticipated())
                .speaker1Name(monitoringFeedBack.getSpeaker1Name())
                .topicAsPerSessionPlan1(monitoringFeedBack.getTopicAsPerSessionPlan1())
                .timeTaken1(monitoringFeedBack.getTimeTaken1())
                .audioVisualAidUsed1(monitoringFeedBack.getAudioVisualAidUsed1())
                .relevance1(monitoringFeedBack.getRelevance1())
                .sessionContinuity1(monitoringFeedBack.getSessionContinuity1())
                .participantInteraction1(monitoringFeedBack.getParticipantInteraction1())
                .speaker2Name(monitoringFeedBack.getSpeaker2Name())
                .topicAsPerSessionPlan2(monitoringFeedBack.getTopicAsPerSessionPlan2())
                .timeTaken2(monitoringFeedBack.getTimeTaken2())
                .audioVisualAidUsed2(monitoringFeedBack.getAudioVisualAidUsed2())
                .relevance2(monitoringFeedBack.getRelevance2())
                .sessionContinuity2(monitoringFeedBack.getSessionContinuity2())
                .participantInteraction2(monitoringFeedBack.getParticipantInteraction2())
                .venueQuality(monitoringFeedBack.getVenueQuality())
                .accessibility(monitoringFeedBack.getAccessibility())
                .teaSnacks(monitoringFeedBack.getTeaSnacks())
                .lunch(monitoringFeedBack.getLunch())
                .cannedWater(monitoringFeedBack.getCannedWater())
                .toiletHygiene(monitoringFeedBack.getToiletHygiene())
                .avEquipment(monitoringFeedBack.getAvEquipment())
                .stationary(monitoringFeedBack.getStationary())
                .relevant(monitoringFeedBack.getRelevant())
                .enthusiast(monitoringFeedBack.getEnthusiast())
                .feltUseful(monitoringFeedBack.getFeltUseful())
                .futureWillingToEngage(monitoringFeedBack.getFutureWillingToEngage())
                .qualified(monitoringFeedBack.getQualified())
                .experienced(monitoringFeedBack.getExperienced())
                .certified(monitoringFeedBack.getCertified())
                .deliveryMethodologyGood(monitoringFeedBack.getDeliveryMethodologyGood())
                .relevantExperience(monitoringFeedBack.getRelevantExperience())
                .overallObservation(monitoringFeedBack.getOverallObservation())
                .submittedBy(monitoringFeedBack.getUser() != null ? monitoringFeedBack.getUser().getFirstName() + monitoringFeedBack.getUser().getLastName() : null)
                .totalScore(monitoringFeedBack.getTotalScore())
                .bestPracticesIdentified(monitoringFeedBack.getBestPracticesIdentified())
                .build();
    }


    public static void updateProgramMonitoring(ProgramMonitoring entity, ProgramMonitoringRequest request) throws DataException {
        int stepNumber = request.getStepNumber();
        entity.setStepNumber(stepNumber);

        switch (stepNumber) {
            case 1 -> {
                int screen1Score = 0;
                if (Boolean.TRUE.equals(request.getProgramAgendaCirculated())) screen1Score += 2;
                if (Boolean.TRUE.equals(request.getProgramAsPerSchedule())) screen1Score += 2;
                if (Boolean.TRUE.equals(request.getTrainingMaterialSupplied())) screen1Score += 2;
                if (Boolean.TRUE.equals(request.getSeatingArrangementsMade())) screen1Score += 2;
                if (Boolean.TRUE.equals(request.getAvProjectorAvailable())) screen1Score += 2;
                String source = request.getHowDidYouKnowAboutProgram();

                if (source != null && !source.isBlank()) {
                    if (source.equals("Officials") || source.equals("Others")) {
                        screen1Score += 1;
                    } else if (source.equals("IAs") || source.equals("News paper / TV") || source.equals("Social media")) {
                        screen1Score += 2;
                    }
                }
                entity.setProgramAgendaCirculated(request.getProgramAgendaCirculated());
                entity.setProgramAsPerSchedule(request.getProgramAsPerSchedule());
                entity.setTrainingMaterialSupplied(request.getTrainingMaterialSupplied());
                entity.setSeatingArrangementsMade(request.getSeatingArrangementsMade());
                entity.setAvProjectorAvailable(request.getAvProjectorAvailable());
                entity.setHowDidYouKnowAboutProgram(request.getHowDidYouKnowAboutProgram());
                entity.setScreen1Score(screen1Score);

            }
            case 2 -> {
                int screen2Score = 0;
                if (Boolean.TRUE.equals(request.getParticipantsMale())) screen2Score += 2;
                if (Boolean.TRUE.equals(request.getParticipantsFemale())) screen2Score += 3;
                if (Boolean.TRUE.equals(request.getParticipantsTransgender())) screen2Score += 2;
                if (Boolean.TRUE.equals(request.getDicRegistrationParticipated())) screen2Score += 1;

                int noIAs = request.getNoIAsParticipated().size();
                if (noIAs == 0) screen2Score += 0;
                else if (noIAs <= 2) screen2Score += 1;
                else if (noIAs <= 4) screen2Score += 2;
                else if (noIAs <= 6) screen2Score += 3;
                else screen2Score += 4;

                entity.setParticipantsMale(request.getParticipantsMale());
                entity.setParticipantsFemale(request.getParticipantsFemale());
                entity.setParticipantsTransgender(request.getParticipantsTransgender());
                entity.setDicRegistrationParticipated(request.getDicRegistrationParticipated());
                entity.setShgRegistrationParticipated(request.getShgRegistrationParticipated());
                entity.setMsmeRegistrationParticipated(request.getMsmeRegistrationParticipated());
                entity.setStartupsRegistrationParticipated(request.getStartupsRegistrationParticipated());
                entity.setNoIAsParticipated(request.getNoIAsParticipated());
                entity.setScreen2Score(screen2Score);

            }
            case 3 -> {
                int screen3Score = 0;
                if (Boolean.TRUE.equals(request.getTopicAsPerSessionPlan1())) screen3Score += 2;
                if (Boolean.TRUE.equals(request.getAudioVisualAidUsed1())) screen3Score += 2;
                if (Boolean.TRUE.equals(request.getSessionContinuity1())) screen3Score += 2;
                if (Boolean.TRUE.equals(request.getParticipantInteraction1())) screen3Score += 3;
                int minutes = request.getTimeTaken1();
                if (minutes < 30 || minutes > 90) {
                    screen3Score += 1;
                } else if (minutes > 45 && minutes <= 90) {
                    screen3Score += 2;
                } else if (minutes >= 30 && minutes <= 45) {
                    screen3Score += 3;
                }
                screen3Score += getScore(request.getRelevance1());

                entity.setSpeaker1Name(request.getSpeaker1Name());
                entity.setTopicAsPerSessionPlan1(request.getTopicAsPerSessionPlan1());
                entity.setTimeTaken1(request.getTimeTaken1());
                entity.setAudioVisualAidUsed1(request.getAudioVisualAidUsed1());
                entity.setRelevance1(request.getRelevance1());
                entity.setSessionContinuity1(request.getSessionContinuity1());
                entity.setParticipantInteraction1(request.getParticipantInteraction1());
                entity.setScreen3Score(screen3Score);


            }
            case 4 -> {
                int screen4Score = 0;
                if (Boolean.TRUE.equals(request.getTopicAsPerSessionPlan2())) screen4Score += 2;
                if (Boolean.TRUE.equals(request.getAudioVisualAidUsed2())) screen4Score += 2;
                if (Boolean.TRUE.equals(request.getSessionContinuity2())) screen4Score += 2;
                if (Boolean.TRUE.equals(request.getParticipantInteraction2())) screen4Score += 3;
                int minutes = request.getTimeTaken2();
                if (minutes < 30 || minutes > 90) {
                    screen4Score += 1;
                } else if (minutes > 45 && minutes <= 90) {
                    screen4Score += 2;
                } else if (minutes >= 30 && minutes <= 45) {
                    screen4Score += 3;
                }
                screen4Score += getScore(request.getRelevance2());

                entity.setSpeaker2Name(request.getSpeaker2Name());
                entity.setTopicAsPerSessionPlan2(request.getTopicAsPerSessionPlan2());
                entity.setTimeTaken2(request.getTimeTaken2());
                entity.setAudioVisualAidUsed2(request.getAudioVisualAidUsed2());
                entity.setRelevance2(request.getRelevance2());
                entity.setSessionContinuity2(request.getSessionContinuity2());
                entity.setParticipantInteraction2(request.getParticipantInteraction2());
                entity.setScreen4Score(screen4Score);


            }
            case 5 -> {
                int screen5Score = 0;
                if (Boolean.TRUE.equals(request.getVenueQuality())) screen5Score += 2;
                if (Boolean.TRUE.equals(request.getAccessibility())) screen5Score += 2;
                if (Boolean.TRUE.equals(request.getTeaSnacks())) screen5Score += 2;
                if (Boolean.TRUE.equals(request.getLunch())) screen5Score += 2;
                if (Boolean.TRUE.equals(request.getCannedWater())) screen5Score += 2;
                if (Boolean.TRUE.equals(request.getToiletHygiene())) screen5Score += 2;
                if (Boolean.TRUE.equals(request.getAvEquipment())) screen5Score += 2;
                if (Boolean.TRUE.equals(request.getStationary())) screen5Score += 2;

                entity.setScreen5Score(screen5Score);
                entity.setVenueQuality(request.getVenueQuality());
                entity.setAccessibility(request.getAccessibility());
                entity.setTeaSnacks(request.getTeaSnacks());
                entity.setLunch(request.getLunch());
                entity.setCannedWater(request.getCannedWater());
                entity.setToiletHygiene(request.getToiletHygiene());
                entity.setAvEquipment(request.getAvEquipment());
                entity.setStationary(request.getStationary());

            }
            case 6 -> {
                int screen6Score = 0;
                if (Boolean.TRUE.equals(request.getRelevant())) screen6Score += 2;
                if (Boolean.TRUE.equals(request.getEnthusiast())) screen6Score += 2;
                if (Boolean.TRUE.equals(request.getFeltUseful())) screen6Score += 2;
                if (Boolean.TRUE.equals(request.getFutureWillingToEngage())) screen6Score += 2;

                entity.setRelevant(request.getRelevant());
                entity.setEnthusiast(request.getEnthusiast());
                entity.setFeltUseful(request.getFeltUseful());
                entity.setFutureWillingToEngage(request.getFutureWillingToEngage());
                entity.setScreen6Score(screen6Score);

            }

            case 7 -> {
                int screen7Score = 0;
                if (Boolean.TRUE.equals(request.getQualified())) screen7Score += 2;
                if (Boolean.TRUE.equals(request.getExperienced())) screen7Score += 2;
                if (Boolean.TRUE.equals(request.getCertified())) screen7Score += 2;
                if (Boolean.TRUE.equals(request.getDeliveryMethodologyGood())) screen7Score += 4;
                if (Boolean.TRUE.equals(request.getRelevantExperience())) screen7Score += 4;
                entity.setScreen7Score(screen7Score);
                entity.setQualified(request.getQualified());
                entity.setExperienced(request.getExperienced());
                entity.setCertified(request.getCertified());
                entity.setDeliveryMethodologyGood(request.getDeliveryMethodologyGood());
                entity.setRelevantExperience(request.getRelevantExperience());

            }
            case 8 -> {
                entity.setOverallObservation(request.getOverallObservation());
                entity.setBestPracticesIdentified(request.getBestPracticesIdentified());
            }
            default -> {
                throw new DataException("Invalid step number: " + stepNumber, "INVALID_STEP_NUMBER", 406);
            }

        }
        int totalScore = 0;
        totalScore += Optional.ofNullable(entity.getScreen1Score()).orElse(0);
        totalScore += Optional.ofNullable(entity.getScreen2Score()).orElse(0);
        totalScore += Optional.ofNullable(entity.getScreen3Score()).orElse(0);
        totalScore += Optional.ofNullable(entity.getScreen4Score()).orElse(0);
        totalScore += Optional.ofNullable(entity.getScreen5Score()).orElse(0);
        totalScore += Optional.ofNullable(entity.getScreen6Score()).orElse(0);
        totalScore += Optional.ofNullable(entity.getScreen7Score()).orElse(0);

        entity.setTotalScore(totalScore);
    }

    private static int getScore(String level) {
        if (level == null) return 0;
        return switch (level.toLowerCase()) {
            case "high" -> 3;
            case "medium" -> 2;
            case "poor" -> 1;
            case "very poor" -> 0;
            default -> 0;
        };
    }

}