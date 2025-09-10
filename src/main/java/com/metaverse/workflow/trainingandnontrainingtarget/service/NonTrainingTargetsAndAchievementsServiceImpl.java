package com.metaverse.workflow.trainingandnontrainingtarget.service;

import com.metaverse.workflow.model.NonTrainingAchievement;
import com.metaverse.workflow.model.NonTrainingTargets;
import com.metaverse.workflow.nontraining.repository.NonTrainingAchievementRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingExpenditureRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingResourceRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.TravelAndTransportRepository;
import com.metaverse.workflow.trainingandnontrainingtarget.dtos.NonTrainingTargetsAndAchievementsResponse;
import com.metaverse.workflow.trainingandnontrainingtarget.repository.NonTrainingTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NonTrainingTargetsAndAchievementsServiceImpl implements NonTrainingTargetsAndAchievementsService {

    private final NonTrainingTargetRepository nonTrainingTargetRepository;

    private final NonTrainingExpenditureRepository nonTrainingExpenditureRepository;

    private final NonTrainingAchievementRepository nonTrainingAchievementRepository;
    private final NonTrainingResourceRepository nonTrainingResourceRepository;
    private final TravelAndTransportRepository travelAndTransportRepository;


    @Override
    public List<NonTrainingTargetsAndAchievementsResponse> getTargetsAndAchievements(String financialYear, Long agencyId) {
        // Targets for this agency + FY
        List<NonTrainingTargets> targets = nonTrainingTargetRepository
                .findByNonTrainingSubActivity_NonTrainingActivity_Agency_AgencyIdAndFinancialYear(agencyId, financialYear);

        if (targets.isEmpty()) {
            throw new RuntimeException("No targets found for agency " + agencyId + " in FY " + financialYear);
        }

        return targets.stream().map(t -> {
            NonTrainingTargetsAndAchievementsResponse dto = new NonTrainingTargetsAndAchievementsResponse();
            dto.setActivityName(t.getNonTrainingSubActivity().getNonTrainingActivity().getActivityName());
            dto.setSubActivityName(t.getNonTrainingSubActivity().getSubActivityName());
            dto.setFinancialYear(t.getFinancialYear());

            dto.setTrainingTargetQ1(t.getQ1Target() != null ? t.getQ1Target() : 0);
            dto.setTrainingTargetQ2(t.getQ2Target() != null ? t.getQ2Target() : 0);
            dto.setTrainingTargetQ3(t.getQ3Target() != null ? t.getQ3Target() : 0);
            dto.setTrainingTargetQ4(t.getQ4Target() != null ? t.getQ4Target() : 0);

            dto.setFinancialTargetQ1(t.getQ1Budget() != null ? t.getQ1Budget() : 0);
            dto.setFinancialTargetQ2(t.getQ2Budget() != null ? t.getQ2Budget() : 0);
            dto.setFinancialTargetQ3(t.getQ3Budget() != null ? t.getQ3Budget() : 0);
            dto.setFinancialTargetQ4(t.getQ4Budget() != null ? t.getQ4Budget() : 0);

            dto.setPhysicalExpenditurePercentage("Not Applicable"); // Not applicable for Non-Training

            // Financial Achievements per quarter
            Date[] fyRange = getFinancialYearRange(financialYear);

            Double[] finQuarterly = quarterlyWiseExpenditure(agencyId, t.getNonTrainingSubActivity().getSubActivityId(), financialYear, t.getNonTrainingSubActivity().getNonTrainingActivity().getActivityName(), t.getNonTrainingSubActivity().getSubActivityName());
            Double finQ1 = finQuarterly[0];
            Double finQ2 = finQuarterly[1];
            Double finQ3 = finQuarterly[2];
            Double finQ4 = finQuarterly[3];

            dto.setFinancialAchievedQ1(finQ1 != null ? finQ1 : 0.0);
            dto.setFinancialAchievedQ2(finQ2 != null ? finQ2 : 0.0);
            dto.setFinancialAchievedQ3(finQ3 != null ? finQ3 : 0.0);
            dto.setFinancialAchievedQ4(finQ4 != null ? finQ4 : 0.0);

// Totals
            dto.setTotalFinancialTarget(
                    (int) ((t.getQ1Budget() != null ? t.getQ1Budget() : 0)
                            + (t.getQ2Budget() != null ? t.getQ2Budget() : 0)
                            + (t.getQ3Budget() != null ? t.getQ3Budget() : 0)
                            + (t.getQ4Budget() != null ? t.getQ4Budget() : 0))
            );

            dto.setTotalFinancialAchieved(
                    ((finQ1 != null ? finQ1 : 0.0) +
                            (finQ2 != null ? finQ2 : 0.0) +
                            (finQ3 != null ? finQ3 : 0.0) +
                            (finQ4 != null ? finQ4 : 0.0))
            );


            // Set totals
            dto.setAchievedQ1(t.getAchievements().stream().map(NonTrainingAchievement::getQ1Achievement).findFirst().orElse("Yet to Begin"));
            dto.setAchievedQ2(t.getAchievements().stream().map(NonTrainingAchievement::getQ2Achievement).findFirst().orElse("Yet to Begin"));
            dto.setAchievedQ3(t.getAchievements().stream().map(NonTrainingAchievement::getQ3Achievement).findFirst().orElse("Yet to Begin"));
            dto.setAchievedQ4(t.getAchievements().stream().map(NonTrainingAchievement::getQ4Achievement).findFirst().orElse("Yet to Begin"));
            dto.setTotalFinancialTarget(
                    (int) ((t.getQ1Budget() != null ? t.getQ1Budget() : 0)
                            + (t.getQ2Budget() != null ? t.getQ2Budget() : 0)
                            + (t.getQ3Budget() != null ? t.getQ3Budget() : 0)
                            + (t.getQ4Budget() != null ? t.getQ4Budget() : 0))
            );
            dto.setTotalTarget((t.getQ1Target() != null ? t.getQ1Target() : 0)
                    + (t.getQ2Target() != null ? t.getQ2Target() : 0)
                    + (t.getQ3Target() != null ? t.getQ3Target() : 0)
                    + (t.getQ4Target() != null ? t.getQ4Target() : 0));
            dto.setFinancialExpenditurePercentage(
                    dto.getTotalFinancialTarget() == 0 ? 0.0 :
                            Math.round((dto.getTotalFinancialAchieved() / dto.getTotalFinancialTarget()) * 100 * 100.0) / 100.0
            );
            if (finQ1 != null && finQ2 != null && finQ3 != null && finQ4 != null) {
                dto.setTotalFinancialAchieved(finQ1 + finQ2 + finQ3 + finQ4);
            }
            return dto;
        }).toList();
    }

    public static Date[] getFinancialYearRange(String financialYear) {
        // financialYear format: "YYYY-YYYY", e.g., "2025-2026"
        String[] parts = financialYear.split("-");


        LocalDate start = LocalDate.of(Integer.parseInt(parts[0]), 4, 1);   // FY starts April 1st
        LocalDate end = LocalDate.of(Integer.parseInt(parts[1]), 3, 31);    // FY ends March 31st

        return new Date[]{
                java.sql.Date.valueOf(start),
                java.sql.Date.valueOf(end)
        };
    }

    public Double[] quarterlyWiseExpenditure(Long agencyId, Long subActivityId, String financialYear, String activityName, String subActivityName) {
        switch (activityName.toLowerCase()) {  // coi completed,
            case "staff", "project team" -> {
                // Handle staff differently per sub-activity if needed
                switch (subActivityName.toLowerCase()) {
                    case "staff - ceo", "staff - designers", "staff - project manager", "interns for certifications",
                         "r&d", "staff" -> {
                        return getNonTrainingResourceExpenditureByQuarterlyWise(agencyId, subActivityId, financialYear, activityName, subActivityName);
                    }
                    default -> {
                        return getNonTrainingExpenditureByQuarterlyWise(agencyId, subActivityId, financialYear, activityName, subActivityName);
                    }
                }
            }

            case "contingency fund" -> {
                System.out.println("Fetching " + activityName + " expenditure from resources repository");
                return getNonTrainingResourceExpenditureByQuarterlyWise(agencyId, subActivityId, financialYear, activityName, subActivityName);
            }

            case "setting up call center services for validation" -> {
                // All treated as Staff
                System.out.println("Fetching Staff expenditure (" + activityName + " - " + subActivityName + ")");
                switch (subActivityName) {
                    case "staff", "technology firm" -> {
                        return getNonTrainingResourceExpenditureByQuarterlyWise(agencyId, subActivityId, financialYear, activityName, subActivityName);
                    }
                    default -> {
                        return getNonTrainingExpenditureByQuarterlyWise(agencyId, subActivityId, financialYear, activityName, subActivityName);
                    }
                }
            }

            case "hiring of technology platform" -> {
                // Map to Technology firm
                System.out.println("Fetching Technology firm expenditure");
                return getNonTrainingResourceExpenditureByQuarterlyWise(agencyId, subActivityId, financialYear, activityName, subActivityName);
            }

            case "travel & transport" -> {
                System.out.println("travel & transport");
                return getTravelAndTransportExpenditureByQuetrlyWise(agencyId, subActivityId, financialYear, activityName, subActivityName);
            }

            default -> {
                return getNonTrainingExpenditureByQuarterlyWise(agencyId, subActivityId, financialYear, activityName, subActivityName);
            }
        }
    }


    public Double[] getTravelAndTransportExpenditureByQuetrlyWise(Long agencyId, Long subActivityId, String financialYear, String activityName, String subActivityName) {
        System.out.println("Fetching Travel & Transport expenditure for " + subActivityName);
        Double finQ1 = travelAndTransportRepository.sumExpenditureByAgencyAndSubActivityAndDateRange(
                agencyId, subActivityId,
                getFinancialYearRange1(financialYear.split("-")[0],4,1,false),
                getFinancialYearRange1(financialYear.split("-")[0],6,30,false)
        );

        Double finQ2 = travelAndTransportRepository.sumExpenditureByAgencyAndSubActivityAndDateRange(
                agencyId, subActivityId,
                getFinancialYearRange1(financialYear.split("-")[0],7,1,false),
                getFinancialYearRange1(financialYear.split("-")[0],9,30,false)
        );

// Q3: Oct–Dec
        Double finQ3 = travelAndTransportRepository.sumExpenditureByAgencyAndSubActivityAndDateRange(
                agencyId, subActivityId,
                getFinancialYearRange1(financialYear.split("-")[0],10,1,false),
                getFinancialYearRange1(financialYear.split("-")[0],12,31,false)
        );

// Q4: Jan–Mar (next year)
        Double finQ4 = travelAndTransportRepository.sumExpenditureByAgencyAndSubActivityAndDateRange(
                agencyId, subActivityId,
                getFinancialYearRange1(financialYear.split("-")[1],1,1,false),
                getFinancialYearRange1(financialYear.split("-")[1],3,31,false)

        );
        return new Double[]{finQ1, finQ2, finQ3, finQ4};
    }


    public Double[] getNonTrainingExpenditureByQuarterlyWise(Long agencyId, Long subActivityId, String financialYear, String activityName, String subActivityName) {
        System.out.println("Using expenditure from nonTrainingExp map for " + activityName + " - " + subActivityName);
        // Q1: Apr–Jun
        Double finQ1 = nonTrainingExpenditureRepository.sumExpenditureByAgencyAndSubActivityAndDateRange(
                agencyId, subActivityId,
                getFinancialYearRange1(financialYear.split("-")[0],4,1,true),
                getFinancialYearRange1(financialYear.split("-")[0],6,30,true)
        );

// Q2: Jul–Sep
        Double finQ2 = nonTrainingExpenditureRepository.sumExpenditureByAgencyAndSubActivityAndDateRange(
                agencyId, subActivityId,
                getFinancialYearRange1(financialYear.split("-")[0],7,1,true),
                getFinancialYearRange1(financialYear.split("-")[0],9,30,true)
        );

// Q3: Oct–Dec
        Double finQ3 = nonTrainingExpenditureRepository.sumExpenditureByAgencyAndSubActivityAndDateRange(
                agencyId, subActivityId,
                getFinancialYearRange1(financialYear.split("-")[0],10,1,true),
                getFinancialYearRange1(financialYear.split("-")[0],12,31,true)
        );

// Q4: Jan–Mar (next year)
        Double finQ4 = nonTrainingExpenditureRepository.sumExpenditureByAgencyAndSubActivityAndDateRange(
                agencyId, subActivityId,
                getFinancialYearRange1(financialYear.split("-")[1],1,1,true),
                getFinancialYearRange1(financialYear.split("-")[1],3,31,true)
        );
        return new Double[]{finQ1, finQ2, finQ3, finQ4};
    }

    public Double[] getNonTrainingResourceExpenditureByQuarterlyWise(Long agencyId, Long subActivityId, String financialYear, String activityName, String subActivityName) {
        Double finQ1 = nonTrainingResourceRepository.sumExpenditureByAgencyAndSubActivityAndDateRange(
                agencyId, subActivityId,
                getFinancialYearRange1(financialYear.split("-")[0],4,1,true),
                getFinancialYearRange1(financialYear.split("-")[0],6,30,true)
        );


// Q2: Jul–Sep
        Double finQ2 = nonTrainingResourceRepository.sumExpenditureByAgencyAndSubActivityAndDateRange(
                agencyId, subActivityId,
                getFinancialYearRange1(financialYear.split("-")[0],7,1,true),
                getFinancialYearRange1(financialYear.split("-")[0],9,30,true)
        );

// Q3: Oct–Dec
        Double finQ3 = nonTrainingResourceRepository.sumExpenditureByAgencyAndSubActivityAndDateRange(
                agencyId, subActivityId,
                getFinancialYearRange1(financialYear.split("-")[0],10,1,true),
                getFinancialYearRange1(financialYear.split("-")[0],12,31,true)
        );

// Q4: Jan–Mar (next year)
        Double finQ4 = nonTrainingResourceRepository.sumExpenditureByAgencyAndSubActivityAndDateRange(
                agencyId, subActivityId,
                getFinancialYearRange1(financialYear.split("-")[1],1,1,true),
                getFinancialYearRange1(financialYear.split("-")[1],3,31,true)
        );

        return new Double[]{finQ1, finQ2, finQ3, finQ4};

    }

    public static <T> T getFinancialYearRange1(String financialYear, Integer month, Integer dayOfMonth, boolean check) {
        LocalDate start = LocalDate.of(Integer.parseInt(financialYear), month, dayOfMonth);
        if (check) {
            return (T) java.sql.Date.valueOf(start);
        } else {
            return (T) start;
        }
    }
}