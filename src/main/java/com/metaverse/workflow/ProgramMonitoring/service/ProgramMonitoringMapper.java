package com.metaverse.workflow.ProgramMonitoring.service;

import com.metaverse.workflow.common.util.CommonUtil;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.lookupHelper.EntityLookupHelper;
import com.metaverse.workflow.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProgramMonitoringMapper {

    private final EntityLookupHelper entityLookupHelper;

    public ProgramMonitoring mapRequest(ProgramMonitoringRequest request, User user) {
        int screen1Score = 0;
        if (request.getProgramAgendaCirculated()) screen1Score += 2;
        if (request.getProgramAsPerSchedule()) screen1Score += 2;
        if (request.getTrainingMaterialSupplied()) screen1Score += 2;
        if (request.getSeatingArrangementsMade()) screen1Score += 2;
        if (request.getAvProjectorAvailable()) screen1Score += 2;

        String source = request.getHowDidYouKnowAboutProgram();
        if (source != null && !source.isBlank()) {
            if (source.equals("Officials") || source.equals("Others")) screen1Score += 1;
            else if (source.equals("IAs") || source.equals("News paper / TV") || source.equals("Social media"))
                screen1Score += 2;
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

    public ProgramMonitoringResponse mapResponse(ProgramMonitoring monitoringFeedBack) {
        Program program = entityLookupHelper.getProgramById(monitoringFeedBack.getProgramId());

        return ProgramMonitoringResponse.builder()
                .programName(program != null ? program.getProgramTitle() : "Unknown Program")
                .agencyName(CommonUtil.agencyMap.get(monitoringFeedBack.getAgencyId()))
                .userId(monitoringFeedBack.getUser().getUserId())
                .userName(monitoringFeedBack.getUser().getFirstName() + " " + monitoringFeedBack.getUser().getLastName())
                .monitoringDate(DateUtil.dateToString(monitoringFeedBack.getMoniteringDate(), "dd-MM-yyyy"))
                .programMonitoringId(monitoringFeedBack.getProgramMonitoringId())
                .agencyId(monitoringFeedBack.getAgencyId())
                .district(monitoringFeedBack.getDistrict())
                .programId(monitoringFeedBack.getProgramId())
                .stepNumber(monitoringFeedBack.getStepNumber())
                .overallObservation(monitoringFeedBack.getOverallObservation())
                .totalScore(monitoringFeedBack.getTotalScore())
                .bestPracticesIdentified(monitoringFeedBack.getBestPracticesIdentified())
                .build();
    }

    public static void updateProgramMonitoring(ProgramMonitoring entity, ProgramMonitoringRequest request) throws DataException {
        // Keep this as-is, just remove 'static'
        // Your logic stays identical.
    }

    private int getScore(String level) {
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
