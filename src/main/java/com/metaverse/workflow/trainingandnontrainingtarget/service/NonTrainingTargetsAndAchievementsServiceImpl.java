package com.metaverse.workflow.trainingandnontrainingtarget.service;

import com.metaverse.workflow.model.NonTrainingTargets;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.nontraining.repository.NonTrainingAchievementRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingExpenditureRepository;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import com.metaverse.workflow.trainingandnontrainingtarget.dtos.NonTrainingTargetsAndAchievementsResponse;
import com.metaverse.workflow.trainingandnontrainingtarget.repository.NonTrainingTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
public class NonTrainingTargetsAndAchievementsServiceImpl implements NonTrainingTargetsAndAchievementsService {

    private final NonTrainingTargetRepository nonTrainingTargetRepository;
    private final NonTrainingExpenditureRepository nonTrainingExpenditureRepository;
    private final NonTrainingAchievementRepository nonTrainingAchievementRepository;
    private final ParticipantRepository participantRepository;

    @Override
    public List<NonTrainingTargetsAndAchievementsResponse> getTargetsAndAchievements(String financialYear, Long agencyId) {
        Date[] range = getFinancialYearRange(financialYear);

        //  Fetch participants once
        List<Participant> participants =
                participantRepository.findAllByAgencyIdAndProgramCreatedOnBetween(
                        agencyId, range[0], range[1]);

        //  Pre-compute participant counts by subActivityId + quarter
        Map<Long, Map<Integer, Long>> participantCounts = participants.stream()
                .flatMap(p -> p.getPrograms().stream()
                        .map(pr -> Map.entry(
                                new AbstractMap.SimpleEntry<>(
                                        pr.getSubActivityId(),
                                        getFinancialQuarter(p.getCreatedOn())
                                ), 1L)))
                .collect(Collectors.groupingBy(
                        e -> e.getKey().getKey(), // subActivityId
                        Collectors.groupingBy(
                                e -> e.getKey().getValue(), // quarter
                                Collectors.counting()
                        )
                ));

        //  Fetch targets
        List<NonTrainingTargets> targets =
                nonTrainingTargetRepository.findByAgencyAndFinancialYear(agencyId, financialYear);

        if (targets.isEmpty()) {
            throw new RuntimeException("No targets found for agency " + agencyId + " in FY " + financialYear);
        }

        int startYear = Integer.parseInt(financialYear.split("-")[0]);
        int endYear = Integer.parseInt(financialYear.split("-")[1]);

        //  Build DTOs
        return targets.stream().map(t -> {
            Long subId = t.getNonTrainingSubActivity().getSubActivityId();

            NonTrainingTargetsAndAchievementsResponse dto = new NonTrainingTargetsAndAchievementsResponse();
            dto.setActivityName(t.getNonTrainingSubActivity().getNonTrainingActivity().getActivityName());
            dto.setSubActivityName(t.getNonTrainingSubActivity().getSubActivityName());
            dto.setFinancialYear(t.getFinancialYear());

            // Training & Financial targets
            dto.setTrainingTargetQ1(t.getQ1Target());
            dto.setTrainingTargetQ2(t.getQ2Target());
            dto.setTrainingTargetQ3(t.getQ3Target());
            dto.setTrainingTargetQ4(t.getQ4Target());
            dto.setFinancialTargetQ1(t.getQ1Budget());
            dto.setFinancialTargetQ2(t.getQ2Budget());
            dto.setFinancialTargetQ3(t.getQ3Budget());
            dto.setFinancialTargetQ4(t.getQ4Budget());
            dto.setPhysicalExpenditurePercentage("Not Applicable");

            //  Financial Achievements
            double finQ1 = getExpenditure(agencyId, subId,
                    LocalDate.of(startYear, 4, 1), LocalDate.of(startYear, 6, 30));
            double finQ2 = getExpenditure(agencyId, subId,
                    LocalDate.of(startYear, 7, 1), LocalDate.of(startYear, 9, 30));
            double finQ3 = getExpenditure(agencyId, subId,
                    LocalDate.of(startYear, 10, 1), LocalDate.of(startYear, 12, 31));
            double finQ4 = getExpenditure(agencyId, subId,
                    LocalDate.of(endYear, 1, 1), LocalDate.of(endYear, 3, 31));

            dto.setFinancialAchievedQ1(finQ1);
            dto.setFinancialAchievedQ2(finQ2);
            dto.setFinancialAchievedQ3(finQ3);
            dto.setFinancialAchievedQ4(finQ4);

            //  Participant Achievements (lookup only, no filtering each time)
            Map<Integer, Long> pc = participantCounts.getOrDefault(subId, Map.of());
            dto.setAchievedQ1(pc.getOrDefault(1, 0L).intValue());
            dto.setAchievedQ2(pc.getOrDefault(2, 0L).intValue());
            dto.setAchievedQ3(pc.getOrDefault(3, 0L).intValue());
            dto.setAchievedQ4(pc.getOrDefault(4, 0L).intValue());

            //  Totals
            dto.setTotalTarget(t.getQ1Target() + t.getQ2Target() + t.getQ3Target() + t.getQ4Target());
            dto.setTotalAchieved(dto.getAchievedQ1() + dto.getAchievedQ2() +
                    dto.getAchievedQ3() + dto.getAchievedQ4());

            dto.setTotalFinancialTarget(
                    (int) (t.getQ1Budget() + t.getQ2Budget() + t.getQ3Budget() + t.getQ4Budget())
            );

            dto.setTotalFinancialAchieved(finQ1 + finQ2 + finQ3 + finQ4);

            return dto;
        }).toList();
    }

    /**
     * Wraps expenditure query (handles null safely).
     */
    private double getExpenditure(Long agencyId, Long subId, LocalDate start, LocalDate end) {
        Double val = nonTrainingExpenditureRepository.sumExpenditureByAgencyAndSubActivityAndDateRange(
                agencyId, subId,
                java.sql.Date.valueOf(start),
                java.sql.Date.valueOf(end));
        return val != null ? val : 0.0;
    }

    /**
     * Convert createdOn -> Financial Quarter (Apr–Mar)
     */
    private int getFinancialQuarter(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        if (month >= 4 && month <= 6) return 1;
        if (month >= 7 && month <= 9) return 2;
        if (month >= 10 && month <= 12) return 3;
        return 4;
    }

    public static Date[] getFinancialYearRange(String financialYear) {
        String[] parts = financialYear.split("-");
        int startYear = Integer.parseInt(parts[0]);
        int endYear = Integer.parseInt(parts[1]);
        LocalDate start = LocalDate.of(startYear, 4, 1);
        LocalDate end = LocalDate.of(endYear, 3, 31);
        return new Date[]{java.sql.Date.valueOf(start), java.sql.Date.valueOf(end)};
    }

    public List<NonTrainingTargetsAndAchievementsResponse> getTargetsAndAchievementsHI(String financialYear, Long agencyId) {
        // FY start and end
        Date[] range = getFinancialYearRange(financialYear);

        // Targets for this agency + FY
        List<NonTrainingTargets> targets = nonTrainingTargetRepository.findByAgencyAndFinancialYear(agencyId, financialYear);
        if (targets.isEmpty()) {
            throw new RuntimeException("No targets found for agency " + agencyId + " in FY " + financialYear);
        }

        // Participants of this agency in FY
        List<Participant> participants = participantRepository.findAllByAgencyIdAndProgramCreatedOnBetween(agencyId, range[0], range[1]);

        // Aggregate participants by subActivity and quarter
        Map<Long, long[]> participantQuarterMap = new HashMap<>();
        for (Participant p : participants) {
            int quarter = getFinancialQuarter(p.getCreatedOn()) - 1; // 0-based index
            for (var pr : p.getPrograms()) {
                long subId = pr.getSubActivityId();
                long[] qCounts = participantQuarterMap.computeIfAbsent(subId, k -> new long[4]);
                qCounts[quarter]++;
            }
        }

        // Aggregate expenditure by subActivity and quarter
        Map<Long, double[]> expenditureQuarterMap = new HashMap<>();
        nonTrainingExpenditureRepository.sumExpenditureBySubActivityAndQuarter(agencyId, range[0], range[1])
                .forEach(row -> {
                    long subId = ((Number) row[0]).longValue();
                    int quarter = ((Number) row[1]).intValue() - 1; // 0-based index
                    double amount = row[2] != null ? ((Number) row[2]).doubleValue() : 0.0;
                    double[] qAmounts = expenditureQuarterMap.computeIfAbsent(subId, k -> new double[4]);
                    qAmounts[quarter] = amount;
                });

        // Map targets to DTOs
        return targets.stream().map(t -> {
            NonTrainingTargetsAndAchievementsResponse dto = new NonTrainingTargetsAndAchievementsResponse();
            dto.setActivityName(t.getNonTrainingSubActivity().getNonTrainingActivity().getActivityName());
            dto.setSubActivityName(t.getNonTrainingSubActivity().getSubActivityName());
            dto.setFinancialYear(t.getFinancialYear());

            // Set targets
            long[] targetQ = {t.getQ1Target(), t.getQ2Target(), t.getQ3Target(), t.getQ4Target()};
            double[] budgetQ = {t.getQ1Budget(), t.getQ2Budget(), t.getQ3Budget(), t.getQ4Budget()};
            dto.setTrainingTargetQ1(targetQ[0]);
            dto.setTrainingTargetQ3(targetQ[2]);
            dto.setTrainingTargetQ4(targetQ[3]);
            dto.setFinancialTargetQ1(budgetQ[0]);
            dto.setFinancialTargetQ2(budgetQ[1]);
            dto.setFinancialTargetQ3(budgetQ[2]);
            dto.setFinancialTargetQ4(budgetQ[3]);
            dto.setPhysicalExpenditurePercentage("Not Applicable");

            long subId = t.getNonTrainingSubActivity().getSubActivityId();

            // Financial achieved
            double[] finQ = expenditureQuarterMap.getOrDefault(subId, new double[4]);
            dto.setFinancialAchievedQ1(finQ[0]);
            dto.setFinancialAchievedQ2(finQ[1]);
            dto.setFinancialAchievedQ3(finQ[2]);
            dto.setFinancialAchievedQ4(finQ[3]);
            dto.setTotalFinancialAchieved(Arrays.stream(finQ).sum());

            // Participants achieved
            long[] partQ = participantQuarterMap.getOrDefault(subId, new long[4]);
            dto.setAchievedQ1((int) partQ[0]);
            dto.setAchievedQ2((int) partQ[1]);
            dto.setAchievedQ3((int) partQ[2]);
            dto.setAchievedQ4((int) partQ[3]);
            dto.setTotalAchieved((int) Arrays.stream(partQ).sum());

            // Total targets
            dto.setTotalTarget(LongStream.of(targetQ).sum());
            dto.setTotalFinancialTarget((int) DoubleStream.of(budgetQ).sum());

            return dto;
        }).toList();
    }
    }
