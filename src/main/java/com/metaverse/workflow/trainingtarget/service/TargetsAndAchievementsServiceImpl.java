package com.metaverse.workflow.trainingtarget.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.TrainingTarget;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import com.metaverse.workflow.trainingtarget.dtos.TargetsAndAchievementsResponseDto;
import com.metaverse.workflow.trainingtarget.repository.TrainingTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TargetsAndAchievementsServiceImpl implements TargetsAndAchievementsService {

    private final TrainingTargetRepository trainingTargetRepository;

    private final ParticipantRepository participantRepository;

    public List<TargetsAndAchievementsResponseDto> getTargetsAndAchievements(Long agencyId) {
        List<TrainingTarget> targets = trainingTargetRepository.findByAgencyId_AgencyId(agencyId);
        if (targets.isEmpty()) {
            throw new RuntimeException("No targets found for the specified agency");
        }

        // Fetch all participants for the agency
        List<Participant> participants = participantRepository.findAllByAgencyId(agencyId);

        return targets.stream().map(t -> {
            TargetsAndAchievementsResponseDto dto = new TargetsAndAchievementsResponseDto();
            dto.setActivityName(t.getActivityId().getActivityName());
            dto.setFinancialYear(t.getFinancialYear());
            dto.setTrainingTargetQ1(t.getQ1());
            dto.setTrainingTargetQ2(t.getQ2());
            dto.setTrainingTargetQ3(t.getQ3());
            dto.setTrainingTargetQ4(t.getQ4());

            // Count achievements per quarter
            int achievedQ1 = (int) participants.stream()
                    .filter(p -> belongsToActivity(p, t))
                    .filter(p -> getQuarter(p.getCreatedOn()) == 1)
                    .count();

            int achievedQ2 = (int) participants.stream()
                    .filter(p -> belongsToActivity(p, t))
                    .filter(p -> getQuarter(p.getCreatedOn()) == 2)
                    .count();

            int achievedQ3 = (int) participants.stream()
                    .filter(p -> belongsToActivity(p, t))
                    .filter(p -> getQuarter(p.getCreatedOn()) == 3)
                    .count();

            int achievedQ4 = (int) participants.stream()
                    .filter(p -> belongsToActivity(p, t))
                    .filter(p -> getQuarter(p.getCreatedOn()) == 4)
                    .count();

            // Set achieved values
            dto.setAchievedQ1(achievedQ1);
            dto.setAchievedQ2(achievedQ2);
            dto.setAchievedQ3(achievedQ3);
            dto.setAchievedQ4(achievedQ4);

            dto.setTotalTarget(t.getQ1() + t.getQ2() + t.getQ3() + t.getQ4());
            dto.setTotalAchieved(achievedQ1 + achievedQ2 + achievedQ3 + achievedQ4);

            return dto;
        }).toList();
    }

    /**
     * Check if participant belongs to the given activity
     */
    private boolean belongsToActivity(Participant p, TrainingTarget t) {
        return p.getPrograms().stream()
                .anyMatch(pr -> pr.getActivityId().equals(t.getActivityId().getActivityId()));
    }

    /**
     * Get quarter from Date
     */
    private int getQuarter(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1; // 1-12
        if (month >= 1 && month <= 3) return 1;
        if (month >= 4 && month <= 6) return 2;
        if (month >= 7 && month <= 9) return 3;
        return 4; // 10-12
    }

}
