package com.metaverse.workflow.trainingandnontrainingtarget.service;

import com.metaverse.workflow.expenditure.repository.ProgramExpenditureRepository;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.TrainingTargets;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import com.metaverse.workflow.trainingandnontrainingtarget.dtos.TrainingTargetsAndAchievementsResponse;
import com.metaverse.workflow.trainingandnontrainingtarget.repository.TrainingTargetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingTargetsAndAchievementsServiceImpl implements TrainingTargetsAndAchievementsService {

    private final TrainingTargetRepository trainingTargetRepository;
    private final ParticipantRepository participantRepository;
    private final ProgramExpenditureRepository programExpenditureRepository;

    public List<TrainingTargetsAndAchievementsResponse> getTargetsAndAchievements(String financialYear, Long agencyId) {

        log.info("Fetching Targets & Achievements for Agency: {} | FY: {}", agencyId, financialYear);

        Date[] range = getFinancialYearRange(financialYear);

        log.debug("Financial Year range: {} to {}", range[0], range[1]);

        List<Participant> participants =
                participantRepository.findAllByAgencyIdAndProgramCreatedOnBetween(
                        agencyId,
                        range[0], // FY start
                        range[1]  // FY end
                );

        log.info("Participants found: {}", participants.size());

        List<TrainingTargets> targets = trainingTargetRepository
                .findByAgency_AgencyIdAndFinancialYear(agencyId, financialYear);

        if (targets.isEmpty()) {
            log.warn("No targets found for agency {} in FY {}", agencyId, financialYear);
            throw new RuntimeException("No targets found for agency " + agencyId + " in FY " + financialYear);
        }

        return targets.stream().map(t -> {
            log.info("Processing Target ID: {} | SubActivity: {}",
                    t.getTrainingTargetId(), t.getSubActivity().getSubActivityName());

            TrainingTargetsAndAchievementsResponse dto = new TrainingTargetsAndAchievementsResponse();
            dto.setActivityName(t.getSubActivity().getActivity().getActivityName());
            dto.setSubActivityName(t.getSubActivity().getSubActivityName());
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
            Double finQ1 = programExpenditureRepository.sumExpenditureByAgencyAndSubActivityAndDateRange(
                    agencyId,
                    t.getSubActivity().getSubActivityId(),
                    java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(financialYear.split("-")[0]), 4, 1)),
                    java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(financialYear.split("-")[0]), 6, 30))
            );
            dto.setFinancialAchievedQ1(finQ1 != null ? finQ1 : 0.0);
            log.debug("Financial Q1 Achieved: {}", dto.getFinancialAchievedQ1());

            Double finQ2 = programExpenditureRepository.sumExpenditureByAgencyAndSubActivityAndDateRange(
                    agencyId, t.getSubActivity().getSubActivityId(),
                    java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(financialYear.split("-")[0]), 7, 1)),
                    java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(financialYear.split("-")[0]), 9, 30))
            );
            dto.setFinancialAchievedQ2(finQ2 != null ? finQ2 : 0.0);
            log.debug("Financial Q2 Achieved: {}", dto.getFinancialAchievedQ2());

            Double finQ3 = programExpenditureRepository.sumExpenditureByAgencyAndSubActivityAndDateRange(
                    agencyId, t.getSubActivity().getSubActivityId(),
                    java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(financialYear.split("-")[0]), 10, 1)),
                    java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(financialYear.split("-")[0]), 12, 31))
            );
            dto.setFinancialAchievedQ3(finQ3 != null ? finQ3 : 0.0);
            log.debug("Financial Q3 Achieved: {}", dto.getFinancialAchievedQ3());

            Double finQ4 = programExpenditureRepository.sumExpenditureByAgencyAndSubActivityAndDateRange(
                    agencyId, t.getSubActivity().getSubActivityId(),
                    java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(financialYear.split("-")[1]), 1, 1)),
                    java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(financialYear.split("-")[1]), 3, 31))
            );
            dto.setFinancialAchievedQ4(finQ4 != null ? finQ4 : 0.0);
            log.debug("Financial Q4 Achieved: {}", dto.getFinancialAchievedQ4());

            // Participant Achievements per quarter
            int achievedQ1 = (int) participants.stream()
                    .filter(p -> belongsToSubActivity(p, t))
                    .filter(p -> getFinancialQuarter(p.getCreatedOn()) == 1)
                    .count();
            dto.setAchievedQ1(achievedQ1);

            int achievedQ2 = (int) participants.stream()
                    .filter(p -> belongsToSubActivity(p, t))
                    .filter(p -> getFinancialQuarter(p.getCreatedOn()) == 2)
                    .count();
            dto.setAchievedQ2(achievedQ2);

            int achievedQ3 = (int) participants.stream()
                    .filter(p -> belongsToSubActivity(p, t))
                    .filter(p -> getFinancialQuarter(p.getCreatedOn()) == 3)
                    .count();
            dto.setAchievedQ3(achievedQ3);

            int achievedQ4 = (int) participants.stream()
                    .filter(p -> belongsToSubActivity(p, t))
                    .filter(p -> getFinancialQuarter(p.getCreatedOn()) == 4)
                    .count();
            dto.setAchievedQ4(achievedQ4);

            log.info("Achievements -> Q1: {}, Q2: {}, Q3: {}, Q4: {}",
                    achievedQ1, achievedQ2, achievedQ3, achievedQ4);

            // Totals
            dto.setTotalTarget(t.getQ1Target() + t.getQ2Target() + t.getQ3Target() + t.getQ4Target());
            dto.setTotalAchieved(achievedQ1 + achievedQ2 + achievedQ3 + achievedQ4);

            dto.setTotalFinancialTarget(
                    (int)(t.getQ1Budget() + t.getQ2Budget() + t.getQ3Budget() + t.getQ4Budget())
            );

            dto.setTotalFinancialAchieved(
                    (finQ1 != null ? finQ1 : 0.0) +
                            (finQ2 != null ? finQ2 : 0.0) +
                            (finQ3 != null ? finQ3 : 0.0) +
                            (finQ4 != null ? finQ4 : 0.0)
            );

            log.info("Final DTO for SubActivity {} -> Total Achieved: {}, Total Financial Achieved: {}",
                    t.getSubActivity().getSubActivityName(),
                    dto.getTotalAchieved(),
                    dto.getTotalFinancialAchieved());

            return dto;
        }).toList();
    }

    @Override
    public List<TargetResponse> getTrainingTargets(String year, Long agencyId) {

        log.info("Fetching ONLY Targets for Agency: {} | FY: {}", agencyId, year);

        List<TrainingTargets> targets =
                trainingTargetRepository.findByAgency_AgencyIdAndFinancialYear(agencyId, year);

        if (targets.isEmpty()) {
            log.warn("No targets found for agency {} in FY {}", agencyId, year);
            throw new RuntimeException("No targets found for agency " + agencyId + " in FY " + year);
        }

        return targets.stream().map(t -> {
            TargetResponse dto = new TargetResponse();
            dto.setTargetId(t.getTrainingTargetId());
            dto.setAgencyName(t.getAgency().getAgencyName());
            dto.setActivityName(t.getSubActivity().getActivity().getActivityName());
            dto.setSubActivityName(t.getSubActivity().getSubActivityName());
            dto.setFinancialYear(t.getFinancialYear());

            // Training targets
            dto.setPhysicalTargetQ1(t.getQ1Target());
            dto.setPhysicalTargetQ2(t.getQ2Target());
            dto.setPhysicalTargetQ3(t.getQ3Target());
            dto.setPhysicalTargetQ3(t.getQ4Target());
            dto.setTotalTrainingTarget(
                    t.getQ1Target() + t.getQ2Target() + t.getQ3Target() + t.getQ4Target()
            );

            // Financial targets
            dto.setFinancialTargetQ1(t.getQ1Budget());
            dto.setFinancialTargetQ2(t.getQ2Budget());
            dto.setFinancialTargetQ3(t.getQ3Budget());
            dto.setFinancialTargetQ4(t.getQ4Budget());
            dto.setTotalFinancialTarget(
                    t.getQ1Budget() + t.getQ2Budget() + t.getQ3Budget() + t.getQ4Budget()
            );

            return dto;
        }).toList();
    }


    private boolean belongsToSubActivity(Participant p, TrainingTargets t) {
        return p.getPrograms().stream()
                .anyMatch(pr -> pr.getSubActivityId()
                        .equals(t.getSubActivity().getSubActivityId()));
    }

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
        String[] parts = financialYear.split("-");
        int startYear = Integer.parseInt(parts[0]);
        int endYear   = Integer.parseInt(parts[1]);

        LocalDate start = LocalDate.of(startYear, 4, 1);
        LocalDate end   = LocalDate.of(endYear, 3, 31);

        return new Date[] {
                java.sql.Date.valueOf(start),
                java.sql.Date.valueOf(end)
        };
    }
}
