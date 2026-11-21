package com.metaverse.workflow.trainingandnontrainingtarget.service;

import com.metaverse.workflow.expenditure.repository.ProgramExpenditureRepository;
import com.metaverse.workflow.model.TrainingTargets;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import com.metaverse.workflow.trainingandnontrainingtarget.dtos.TrainingTargetsAndAchievementsResponse;
import com.metaverse.workflow.trainingandnontrainingtarget.repository.TrainingTargetRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TrainingTargetsAndAchievementsServiceImpl implements TrainingTargetsAndAchievementsService {

    private static long nz(Long value) {
        return value != null ? value : 0L;
    }

    private static double nz(Double value) {
        return value != null ? value : 0.0;
    }

    private final TrainingTargetRepository trainingTargetRepository;
    private final ParticipantRepository participantRepository;
    private final ProgramExpenditureRepository programExpenditureRepository;

    private static final Logger log =
            LogManager.getLogger(TrainingTargetsAndAchievementsServiceImpl.class);

    public List<TrainingTargetsAndAchievementsResponse> getTargetsAndAchievements(String financialYear, Long agencyId) {

        log.info("Fetching Targets & Achievements for Agency: {} | FY: {}", agencyId, financialYear);

        List<TrainingTargets> targets = trainingTargetRepository
                .findByAgency_AgencyIdAndFinancialYear(agencyId, financialYear);

        if (targets.isEmpty()) {
            log.warn("No targets found for agency {} in FY {}", agencyId, financialYear);
            throw new RuntimeException("No targets found for agency " + agencyId + " in FY " + financialYear);
        }

        AchievementData loaded;
        boolean achievementsFailed = false;
        try {
            loaded = loadAchievementData(agencyId, financialYear);
        } catch (Exception ex) {
            log.error("Failed to load achievements for agency {} in FY {}", agencyId, financialYear, ex);
            loaded = new AchievementData(new HashMap<>(), new HashMap<>());
            achievementsFailed = true;
        }
        AchievementData data = loaded;
        boolean markAllAsError = achievementsFailed;

        return targets.stream()
                .map(t -> {
                    try {
                        TrainingTargetsAndAchievementsResponse dto = buildTargetsAndAchievements(t, data);
                        dto.setDataError(markAllAsError);
                        return dto;
                    } catch (Exception ex) {
                        log.error("Failed to build targets & achievements for Target ID: {}",
                                t.getTrainingTargetId(), ex);
                        return errorRow(t);
                    }
                })
                .toList();
    }

    /** Row for a target whose data could not be calculated: names kept, all values 0, flagged as error. */
    private TrainingTargetsAndAchievementsResponse errorRow(TrainingTargets t) {
        TrainingTargetsAndAchievementsResponse dto = new TrainingTargetsAndAchievementsResponse();
        try {
            if (t.getAgency() != null) {
                dto.setAgencyName(t.getAgency().getAgencyName());
            }
            if (t.getSubActivity() != null) {
                dto.setSubActivityName(t.getSubActivity().getSubActivityName());
                if (t.getSubActivity().getActivity() != null) {
                    dto.setActivityName(t.getSubActivity().getActivity().getActivityName());
                }
            }
            dto.setFinancialYear(t.getFinancialYear());
        } catch (Exception ignored) {
            // keep whatever names were resolved
        }
        dto.setTotalTarget(0L);
        dto.setTotalAchieved(0);
        dto.setTotalFinancialTarget(0);
        dto.setTotalFinancialAchieved(0.0);
        dto.setDataError(true);
        return dto;
    }

    @Override
    public List<TrainingTargetsAndAchievementsResponse> getTargetsAndAchievementsForExport(String year, Long agencyId) {

        log.info("Fetching Targets & Achievements for Excel export | Agency: {} | FY: {}", agencyId, year);

        boolean hasYear = year != null && !year.isBlank();
        boolean hasAgency = agencyId != null;

        List<TrainingTargets> targets;
        if (hasYear && hasAgency) {
            targets = trainingTargetRepository.findByAgency_AgencyIdAndFinancialYear(agencyId, year);
        } else if (hasAgency) {
            targets = trainingTargetRepository.findByAgency_AgencyId(agencyId);
        } else if (hasYear) {
            targets = trainingTargetRepository.findByFinancialYear(year);
        } else {
            targets = trainingTargetRepository.findAll();
        }

        if (targets.isEmpty()) {
            String agencyPart = hasAgency ? "agency " + agencyId : "any agency";
            String yearPart = hasYear ? "FY " + year : "any FY";
            log.warn("No targets found for {} in {}", agencyPart, yearPart);
            throw new RuntimeException("No targets found for " + agencyPart + " in " + yearPart);
        }

        // Achievement data is loaded once per (agency, financial year) with a few aggregate queries
        // and reused across all targets of that agency and year.
        Map<String, AchievementData> dataCache = new HashMap<>();

        return targets.stream()
                .filter(t -> {
                    if (!isValidFinancialYear(t.getFinancialYear())) {
                        log.warn("Skipping Target ID: {} due to invalid financial year: {}",
                                t.getTrainingTargetId(), t.getFinancialYear());
                        return false;
                    }
                    return true;
                })
                .map(t -> {
                    Long targetAgencyId = t.getAgency().getAgencyId();
                    String targetYear = t.getFinancialYear();
                    AchievementData data = dataCache.computeIfAbsent(
                            targetAgencyId + "|" + targetYear,
                            key -> loadAchievementData(targetAgencyId, targetYear));
                    return buildTargetsAndAchievements(t, data);
                })
                .sorted(Comparator
                        .comparing(TrainingTargetsAndAchievementsResponse::getAgencyName,
                                Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER))
                        .thenComparing(TrainingTargetsAndAchievementsResponse::getActivityName,
                                Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER))
                        .thenComparing(TrainingTargetsAndAchievementsResponse::getSubActivityName,
                                Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER))
                        .thenComparing(TrainingTargetsAndAchievementsResponse::getFinancialYear,
                                Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }

    @Override
    public List<TargetResponse> getTrainingTargets(String year, Long agencyId) {
        return buildTrainingTargets(year, agencyId, true);
    }

    @Override
    public List<TargetResponse> getTrainingTargetsForExport(String year, Long agencyId) {
        return buildTrainingTargets(year, agencyId, false);
    }

    private List<TargetResponse> buildTrainingTargets(String year, Long agencyId, boolean failIfEmpty) {

        log.info("Fetching ONLY Targets for Agency: {} | FY: {}", agencyId, year);

        boolean hasYear = year != null && !year.isBlank();
        boolean hasAgency = agencyId != null;

        List<TrainingTargets> targets;
        if (hasYear && hasAgency) {
            targets = trainingTargetRepository.findByAgency_AgencyIdAndFinancialYear(agencyId, year);
        } else if (hasAgency) {
            targets = trainingTargetRepository.findByAgency_AgencyId(agencyId);
        } else if (hasYear) {
            targets = trainingTargetRepository.findByFinancialYear(year);
        } else {
            targets = trainingTargetRepository.findAll();
        }

        if (failIfEmpty && targets.isEmpty()) {
            String agencyPart = hasAgency ? "agency " + agencyId : "any agency";
            String yearPart = hasYear ? "FY " + year : "any FY";
            log.warn("No targets found for {} in {}", agencyPart, yearPart);
            throw new RuntimeException("No targets found for " + agencyPart + " in " + yearPart);
        }

        return targets.stream().map(t -> {
            TargetResponse dto = new TargetResponse();
            dto.setTargetId(t.getTrainingTargetId());
            dto.setAgencyName(t.getAgency() != null ? t.getAgency().getAgencyName() : null);
            if (t.getSubActivity() != null) {
                dto.setActivityName(t.getSubActivity().getActivity() != null
                        ? t.getSubActivity().getActivity().getActivityName() : null);
                dto.setSubActivityName(t.getSubActivity().getSubActivityName());
            }
            dto.setFinancialYear(t.getFinancialYear());

            // Training targets
            dto.setPhysicalTargetQ1(t.getQ1Target());
            dto.setPhysicalTargetQ2(t.getQ2Target());
            dto.setPhysicalTargetQ3(t.getQ3Target());
            dto.setPhysicalTargetQ4(t.getQ4Target());
            dto.setTotalTrainingTarget(
                    nz(t.getQ1Target()) + nz(t.getQ2Target()) + nz(t.getQ3Target()) + nz(t.getQ4Target())
            );

            // Financial targets
            dto.setFinancialTargetQ1(t.getQ1Budget());
            dto.setFinancialTargetQ2(t.getQ2Budget());
            dto.setFinancialTargetQ3(t.getQ3Budget());
            dto.setFinancialTargetQ4(t.getQ4Budget());
            dto.setTotalFinancialTarget(
                    nz(t.getQ1Budget()) + nz(t.getQ2Budget()) + nz(t.getQ3Budget()) + nz(t.getQ4Budget())
            );

            return dto;
        }).toList();
    }

    /**
     * Pre-aggregated achievements for one (agency, financial year), keyed by sub-activity id.
     * Array index 0..3 = Q1..Q4.
     */
    private record AchievementData(Map<Long, int[]> participantCounts, Map<Long, double[]> expenditures) {
        int[] counts(Long subActivityId) {
            return participantCounts.getOrDefault(subActivityId, new int[4]);
        }

        double[] spend(Long subActivityId) {
            return expenditures.getOrDefault(subActivityId, new double[4]);
        }
    }

    /**
     * Loads all participant and expenditure achievements for an agency and financial year using
     * 1 participant projection query + 4 grouped expenditure queries, instead of loading every
     * Participant entity (and lazily each one's programs) plus 4 SUM queries per target.
     */
    private AchievementData loadAchievementData(Long agencyId, String financialYear) {
        long startMs = System.currentTimeMillis();
        Date[] range = getFinancialYearRange(financialYear);

        // Participant counts: a participant counts towards a sub-activity if any of their programs
        // belongs to it; the quarter comes from the participant's createdOn.
        Map<Long, int[]> participantCounts = new HashMap<>();
        List<Object[]> rows = participantRepository.findParticipantSubActivityRowsByAgencyIdAndProgramCreatedOnBetween(
                agencyId, range[0], range[1]);
        for (Object[] row : rows) {
            Long subActivityId = (Long) row[1];
            Date createdOn = (Date) row[2];
            if (subActivityId == null || createdOn == null) {
                continue;
            }
            participantCounts.computeIfAbsent(subActivityId, k -> new int[4])[getFinancialQuarter(createdOn) - 1]++;
        }

        // Expenditure sums per quarter, same date boundaries as before.
        int startYear = Integer.parseInt(financialYear.split("-")[0]);
        int endYear = Integer.parseInt(financialYear.split("-")[1]);
        LocalDate[][] quarters = {
                {LocalDate.of(startYear, 4, 1), LocalDate.of(startYear, 6, 30)},
                {LocalDate.of(startYear, 7, 1), LocalDate.of(startYear, 9, 30)},
                {LocalDate.of(startYear, 10, 1), LocalDate.of(startYear, 12, 31)},
                {LocalDate.of(endYear, 1, 1), LocalDate.of(endYear, 3, 31)}
        };
        Map<Long, double[]> expenditures = new HashMap<>();
        for (int q = 0; q < 4; q++) {
            List<Object[]> sums = programExpenditureRepository.sumExpenditureByAgencyAndDateRangeGroupBySubActivity(
                    agencyId, java.sql.Date.valueOf(quarters[q][0]), java.sql.Date.valueOf(quarters[q][1]));
            for (Object[] row : sums) {
                Long subActivityId = (Long) row[0];
                Number sum = (Number) row[1];
                if (subActivityId != null && sum != null) {
                    expenditures.computeIfAbsent(subActivityId, k -> new double[4])[q] = sum.doubleValue();
                }
            }
        }

        log.info("Loaded achievement data | Agency: {} | FY: {} | participant rows: {} | {} ms",
                agencyId, financialYear, rows.size(), System.currentTimeMillis() - startMs);
        return new AchievementData(participantCounts, expenditures);
    }

    /**
     * Builds the targets & achievements row for a single training target.
     * Shared by the JSON API and the Excel export so both use identical calculations.
     */
    private TrainingTargetsAndAchievementsResponse buildTargetsAndAchievements(TrainingTargets t, AchievementData data) {

        Long subActivityId = t.getSubActivity().getSubActivityId();

        TrainingTargetsAndAchievementsResponse dto = new TrainingTargetsAndAchievementsResponse();
        dto.setAgencyName(t.getSubActivity().getActivity().getAgency().getAgencyName());
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
        double[] spend = data.spend(subActivityId);
        dto.setFinancialAchievedQ1(spend[0]);
        dto.setFinancialAchievedQ2(spend[1]);
        dto.setFinancialAchievedQ3(spend[2]);
        dto.setFinancialAchievedQ4(spend[3]);

        // Participant Achievements per quarter
        int[] counts = data.counts(subActivityId);
        dto.setAchievedQ1(counts[0]);
        dto.setAchievedQ2(counts[1]);
        dto.setAchievedQ3(counts[2]);
        dto.setAchievedQ4(counts[3]);

        // Totals
        dto.setTotalTarget(nz(t.getQ1Target()) + nz(t.getQ2Target()) + nz(t.getQ3Target()) + nz(t.getQ4Target()));
        dto.setTotalAchieved(counts[0] + counts[1] + counts[2] + counts[3]);

        dto.setTotalFinancialTarget(
                (int) (nz(t.getQ1Budget()) + nz(t.getQ2Budget()) + nz(t.getQ3Budget()) + nz(t.getQ4Budget()))
        );
        dto.setTotalFinancialAchieved(spend[0] + spend[1] + spend[2] + spend[3]);

        log.debug("Target ID: {} | SubActivity: {} -> Achieved Q1-Q4: {}/{}/{}/{} | Financial Achieved: {}",
                t.getTrainingTargetId(), t.getSubActivity().getSubActivityName(),
                counts[0], counts[1], counts[2], counts[3], dto.getTotalFinancialAchieved());

        return dto;
    }

    private boolean isValidFinancialYear(String financialYear) {
        return financialYear != null && financialYear.matches("\\d{4}-\\d{4}");
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
