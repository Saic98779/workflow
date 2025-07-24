package com.metaverse.workflow.ProgramMonitoring.service;

import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.*;

public class ProgramMonitoringMapper {

    public static ProgramMonitoring mapRequest(ProgramMonitoringRequest request,User user) {
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
                .build();
    }


    public static ProgramMonitoringResponse mapResponse(ProgramMonitoring monitoringFeedBack) {
        return ProgramMonitoringResponse.builder()
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
                .submittedBy(monitoringFeedBack.getUser() != null ?monitoringFeedBack.getUser().getFirstName()+monitoringFeedBack.getUser().getLastName() : null)
                .totalScore(monitoringFeedBack.getTotalScore())
                .build();
    }


    public static void updateProgramMonitoring(ProgramMonitoring entity, ProgramMonitoringRequest request) throws DataException {
        int stepNumber = request.getStepNumber();
        entity.setStepNumber(stepNumber);

        switch (stepNumber) {
            case 1 -> {
                entity.setProgramAgendaCirculated(request.getProgramAgendaCirculated());
                entity.setProgramAsPerSchedule(request.getProgramAsPerSchedule());
                entity.setTrainingMaterialSupplied(request.getTrainingMaterialSupplied());
                entity.setSeatingArrangementsMade(request.getSeatingArrangementsMade());
                entity.setAvProjectorAvailable(request.getAvProjectorAvailable());
                entity.setHowDidYouKnowAboutProgram(request.getHowDidYouKnowAboutProgram());

            }
            case 2 -> {
                entity.setParticipantsMale(request.getParticipantsMale());
                entity.setParticipantsFemale(request.getParticipantsFemale());
                entity.setParticipantsTransgender(request.getParticipantsTransgender());
                entity.setDicRegistrationParticipated(request.getDicRegistrationParticipated());
                entity.setShgRegistrationParticipated(request.getShgRegistrationParticipated());
                entity.setMsmeRegistrationParticipated(request.getMsmeRegistrationParticipated());
                entity.setStartupsRegistrationParticipated(request.getStartupsRegistrationParticipated());
                entity.setNoIAsParticipated(request.getNoIAsParticipated());

            }
            case 3 -> {
                entity.setSpeaker1Name(request.getSpeaker1Name());
                entity.setTopicAsPerSessionPlan1(request.getTopicAsPerSessionPlan1());
                entity.setTimeTaken1(request.getTimeTaken1());
                entity.setAudioVisualAidUsed1(request.getAudioVisualAidUsed1());
                entity.setRelevance1(request.getRelevance1());
                entity.setSessionContinuity1(request.getSessionContinuity1());
                entity.setParticipantInteraction1(request.getParticipantInteraction1());

            }
            case 4 -> {
                entity.setSpeaker2Name(request.getSpeaker2Name());
                entity.setTopicAsPerSessionPlan2(request.getTopicAsPerSessionPlan2());
                entity.setTimeTaken2(request.getTimeTaken2());
                entity.setAudioVisualAidUsed2(request.getAudioVisualAidUsed2());
                entity.setRelevance2(request.getRelevance2());
                entity.setSessionContinuity2(request.getSessionContinuity2());
                entity.setParticipantInteraction2(request.getParticipantInteraction2());


            }
            case 5 -> {
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
                entity.setRelevant(request.getRelevant());
                entity.setEnthusiast(request.getEnthusiast());
                entity.setFeltUseful(request.getFeltUseful());
                entity.setFutureWillingToEngage(request.getFutureWillingToEngage());

            }

            case 7 -> {
                entity.setQualified(request.getQualified());
                entity.setExperienced(request.getExperienced());
                entity.setCertified(request.getCertified());
                entity.setDeliveryMethodologyGood(request.getDeliveryMethodologyGood());
                entity.setRelevantExperience(request.getRelevantExperience());

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