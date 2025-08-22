package com.metaverse.workflow.trainingtarget.service;

import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.TrainingTarget;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import com.metaverse.workflow.trainingtarget.dtos.TargetsAndAchievementsResponseDto;
import com.metaverse.workflow.trainingtarget.repository.TrainingTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TargetsAndAchievementsServiceImpl implements TargetsAndAchievementsService {

    private final TrainingTargetRepository trainingTargetRepository;
    private final ParticipantRepository participantRepository;

    public List<TargetsAndAchievementsResponseDto> getTargetsAndAchievements(String financialYear, Long agencyId) {

        Date[] range = getFinancialYearRange("2025-2026");

        List<Participant> participants =
                participantRepository.findAllByAgencyIdAndProgramCreatedOnBetween(
                        agencyId,
                        range[0], // 2025-04-01
                        range[1]  // 2026-03-31
                );    // 31st Mar 2026

        // Targets for this agency + FY
        List<TrainingTarget> targets = trainingTargetRepository
                .findByAgency_AgencyIdAndFinancialYear(agencyId, financialYear);

        if (targets.isEmpty()) {
            throw new RuntimeException("No targets found for agency " + agencyId + " in FY " + financialYear);
        }

        return targets.stream().map(t -> {
            TargetsAndAchievementsResponseDto dto = new TargetsAndAchievementsResponseDto();
            dto.setActivityName(t.getActivity().getActivityName());
            dto.setFinancialYear(t.getFinancialYear());
            dto.setTrainingTargetQ1(t.getQ1());
            dto.setTrainingTargetQ2(t.getQ2());
            dto.setTrainingTargetQ3(t.getQ3());
            dto.setTrainingTargetQ4(t.getQ4());

            // Achievements per quarter
            int achievedQ1 = (int) participants.stream()
                    .filter(p -> belongsToActivity(p, t))
                    .filter(p -> getFinancialQuarter(p.getCreatedOn()) == 1)
                    .count();

            int achievedQ2 = (int) participants.stream()
                    .filter(p -> belongsToActivity(p, t))
                    .filter(p -> getFinancialQuarter(p.getCreatedOn()) == 2)
                    .count();

            int achievedQ3 = (int) participants.stream()
                    .filter(p -> belongsToActivity(p, t))
                    .filter(p -> getFinancialQuarter(p.getCreatedOn()) == 3)
                    .count();

            int achievedQ4 = (int) participants.stream()
                    .filter(p -> belongsToActivity(p, t))
                    .filter(p -> getFinancialQuarter(p.getCreatedOn()) == 4)
                    .count();

            // Set totals
            dto.setAchievedQ1(achievedQ1);
            dto.setAchievedQ2(achievedQ2);
            dto.setAchievedQ3(achievedQ3);
            dto.setAchievedQ4(achievedQ4);
            dto.setTotalTarget(t.getQ1() + t.getQ2() + t.getQ3() + t.getQ4());
            dto.setTotalAchieved(achievedQ1 + achievedQ2 + achievedQ3 + achievedQ4);

            return dto;
        }).toList();
    }

    private boolean belongsToActivity(Participant p, TrainingTarget t) {
        return p.getPrograms().stream()
                .anyMatch(pr -> pr.getActivityId().equals(t.getActivity().getActivityId()));
    }

    /**
     * Convert createdOn -> Financial Quarter (Apr–Mar)
     */
    private int getFinancialQuarter(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1; // 1-12

        if (month >= 4 && month <= 6) return 1;   // Q1: Apr–Jun
        if (month >= 7 && month <= 9) return 2;   // Q2: Jul–Sep
        if (month >= 10 && month <= 12) return 3; // Q3: Oct–Dec
        return 4;                                 // Q4: Jan–Mar
    }

        public static Date[] getFinancialYearRange(String financialYear) {
            // financialYear format: "YYYY-YYYY", e.g., "2025-2026"
            String[] parts = financialYear.split("-");
            int startYear = Integer.parseInt(parts[0]);
            int endYear   = Integer.parseInt(parts[1]);

            LocalDate start = LocalDate.of(startYear, 4, 1);   // FY starts April 1st
            LocalDate end   = LocalDate.of(endYear, 3, 31);    // FY ends March 31st

            return new Date[] {
                    java.sql.Date.valueOf(start),
                    java.sql.Date.valueOf(end)
            };
        }
}

