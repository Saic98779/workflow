package com.metaverse.workflow.trainingandnontrainingtarget.service;

import com.metaverse.workflow.expenditure.repository.ProgramExpenditureRepository;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.model.TrainingTargets;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import com.metaverse.workflow.program.repository.ProgramRepository;
import com.metaverse.workflow.trainingandnontrainingtarget.dtos.TrainingTargetsAndAchievementsResponse;
import com.metaverse.workflow.trainingandnontrainingtarget.repository.TrainingTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingTargetsAndAchievementsServiceImpl implements TrainingTargetsAndAchievementsService {

    private final TrainingTargetRepository trainingTargetRepository;
    private final ParticipantRepository participantRepository;
    private final ProgramExpenditureRepository programExpenditureRepository;
    private final ProgramRepository programRepository;

    public List<TrainingTargetsAndAchievementsResponse> getTargetsAndAchievements(String financialYear, Long agencyId) {

        Date[] range = getFinancialYearRange(financialYear);

        List<Program> programs =
                programRepository.findProgramsWithParticipantsByAgencyAndDateRange(
                        agencyId,
                        range[0], // 2025-04-01
                        range[1]  // 2026-03-31
                );    // 31st Mar 2026

        // Targets for this agency + FY
        List<TrainingTargets> targets = trainingTargetRepository
                .findByAgency_AgencyIdAndFinancialYear(agencyId, financialYear);

        if (targets.isEmpty()) {
            throw new RuntimeException("No targets found for agency " + agencyId + " in FY " + financialYear);
        }

        return targets.stream().map(t -> {
            TrainingTargetsAndAchievementsResponse dto = new TrainingTargetsAndAchievementsResponse();
            dto.setActivityName(t.getActivity().getActivityName());
            dto.setFinancialYear(t.getFinancialYear());
            dto.setTrainingTargetQ1(t.getQ1Target());
            dto.setTrainingTargetQ2(t.getQ2Target());
            dto.setTrainingTargetQ3(t.getQ3Target());
            dto.setTrainingTargetQ4(t.getQ4Target());
            dto.setFinancialTargetQ1(t.getQ1Budget());
            dto.setFinancialTargetQ2(t.getQ2Budget());
            dto.setFinancialTargetQ3(t.getQ3Budget());
            dto.setFinancialTargetQ4(t.getQ4Budget());

            // Financial Achievements per quarter
            Date[] fyRange = getFinancialYearRange(financialYear);

// Q1: Apr–Jun
            Double finQ1 = programExpenditureRepository.sumExpenditureByAgencyAndActivityAndDateRange(
                    agencyId, t.getActivity().getActivityId(),
                    java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(financialYear.split("-")[0]), 4, 1)),
                    java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(financialYear.split("-")[0]), 6, 30))
            );
            dto.setFinancialAchievedQ1(finQ1 != null ? finQ1 : 0.0);

// Q2: Jul–Sep
            Double finQ2 = programExpenditureRepository.sumExpenditureByAgencyAndActivityAndDateRange(
                    agencyId, t.getActivity().getActivityId(),
                    java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(financialYear.split("-")[0]), 7, 1)),
                    java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(financialYear.split("-")[0]), 9, 30))
            );
            dto.setFinancialAchievedQ2(finQ2 != null ? finQ2 : 0.0);

// Q3: Oct–Dec
            Double finQ3 = programExpenditureRepository.sumExpenditureByAgencyAndActivityAndDateRange(
                    agencyId, t.getActivity().getActivityId(),
                    java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(financialYear.split("-")[0]), 10, 1)),
                    java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(financialYear.split("-")[0]), 12, 31))
            );
            dto.setFinancialAchievedQ3(finQ3 != null ? finQ3 : 0.0);

// Q4: Jan–Mar (next year)
            Double finQ4 = programExpenditureRepository.sumExpenditureByAgencyAndActivityAndDateRange(
                    agencyId, t.getActivity().getActivityId(),
                    java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(financialYear.split("-")[1]), 1, 1)),
                    java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(financialYear.split("-")[1]), 3, 31))
            );
            dto.setFinancialAchievedQ4(finQ4 != null ? finQ4 : 0.0);

// Totals
            dto.setTotalFinancialTarget(
                    (int) (t.getQ1Budget() + t.getQ2Budget() + t.getQ3Budget() + t.getQ4Budget())
            );

            dto.setTotalFinancialAchieved(
                    ((finQ1 != null ? finQ1 : 0.0) +
                            (finQ2 != null ? finQ2 : 0.0) +
                            (finQ3 != null ? finQ3 : 0.0) +
                            (finQ4 != null ? finQ4 : 0.0))
            );


            Map<Integer, Long> achievedPerQuarter = programs.stream()
                    .filter(pr -> pr.getActivityId().equals(t.getActivity().getActivityId()))
                    .collect(Collectors.groupingBy(
                            pr -> getFinancialQuarter(pr.getStartDate()), // or pr.getEndDate()
                            Collectors.mapping(Program::getProgramId, Collectors.toSet()) // unique programs
                    ))
                    .entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> (long) e.getValue().size() // count unique programIds
                    ));
            // Retrieve counts safely (defaulting to 0 if missing)
            int achievedQ1 = achievedPerQuarter.getOrDefault(1, 0L).intValue();
            int achievedQ2 = achievedPerQuarter.getOrDefault(2, 0L).intValue();
            int achievedQ3 = achievedPerQuarter.getOrDefault(3, 0L).intValue();
            int achievedQ4 = achievedPerQuarter.getOrDefault(4, 0L).intValue();
            // Set totals
            dto.setAchievedQ1(achievedQ1);
            dto.setAchievedQ2(achievedQ2);
            dto.setAchievedQ3(achievedQ3);
            dto.setAchievedQ4(achievedQ4);
            dto.setTotalTarget(t.getQ1Target() + t.getQ2Target() + t.getQ3Target() + t.getQ4Target());
            dto.setTotalAchieved(achievedQ1 + achievedQ2 + achievedQ3 + achievedQ4);
            dto.setTotalFinancialTarget(
                    (int) (t.getQ1Budget() + t.getQ2Budget() + t.getQ3Budget() + t.getQ4Budget())
            );
            if (finQ1 != null && finQ2 != null && finQ3 != null && finQ4 != null) {
                dto.setTotalFinancialAchieved(finQ1 + finQ2 + finQ3 + finQ4);
            }
            return dto;
        }).toList();
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
        int endYear = Integer.parseInt(parts[1]);

        LocalDate start = LocalDate.of(startYear, 4, 1);   // FY starts April 1st
        LocalDate end = LocalDate.of(endYear, 3, 31);    // FY ends March 31st

        return new Date[]{
                java.sql.Date.valueOf(start),
                java.sql.Date.valueOf(end)
        };
    }
}

