package com.metaverse.workflow.trainingandnontrainingtarget.service;

import com.metaverse.workflow.model.NonTrainingAchievement;
import com.metaverse.workflow.model.NonTrainingTargets;
import com.metaverse.workflow.nontraining.repository.NonTrainingAchievementRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingExpenditureRepository;
import com.metaverse.workflow.trainingandnontrainingtarget.dtos.NonTrainingTargetsAndAchievementsResponse;
import com.metaverse.workflow.trainingandnontrainingtarget.repository.NonTrainingTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NonTrainingTargetsAndAchievementsServiceImpl implements NonTrainingTargetsAndAchievementsService {

    private final NonTrainingTargetRepository nonTrainingTargetRepository;

    private final NonTrainingExpenditureRepository nonTrainingExpenditureRepository;

    private final NonTrainingAchievementRepository nonTrainingAchievementRepository;

    @Override
    public List<NonTrainingTargetsAndAchievementsResponse> getTargetsAndAchievements(String financialYear, Long agencyId) {
        Date[] range = getFinancialYearRange(financialYear);

        // Targets for this agency + FY
        List<NonTrainingTargets> targets = nonTrainingTargetRepository
                .findByNonTrainingActivity_Agency_AgencyIdAndFinancialYear(agencyId, financialYear);

        if (targets.isEmpty()) {
            throw new RuntimeException("No targets found for agency " + agencyId + " in FY " + financialYear);
        }

        return targets.stream().map(t -> {
            NonTrainingTargetsAndAchievementsResponse dto = new NonTrainingTargetsAndAchievementsResponse();
            dto.setActivityName(t.getNonTrainingActivity().getActivityName());
            dto.setFinancialYear(t.getFinancialYear());
            dto.setTrainingTargetQ1(t.getQ1Target());
            dto.setTrainingTargetQ2(t.getQ2Target());
            dto.setTrainingTargetQ3(t.getQ3Target());
            dto.setTrainingTargetQ4(t.getQ4Target());
            dto.setFinancialTargetQ1(t.getQ1Budget());
            dto.setFinancialTargetQ2(t.getQ2Budget());
            dto.setFinancialTargetQ3(t.getQ3Budget());
            dto.setFinancialTargetQ4(t.getQ4Budget());
            dto.setPhysicalExpenditurePercentage("Not Applicable"); // Not applicable for Non-Training

            // Financial Achievements per quarter
            Date[] fyRange = getFinancialYearRange(financialYear);

// Q1: Apr–Jun
            Double finQ1 = nonTrainingExpenditureRepository.sumExpenditureByAgencyAndActivityAndDateRange(
                    agencyId, t.getNonTrainingActivity().getActivityId(),
                    java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(financialYear.split("-")[0]), 4, 1)),
                    java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(financialYear.split("-")[0]), 6, 30))
            );
            dto.setFinancialAchievedQ1(finQ1 != null ? finQ1 : 0.0);

// Q2: Jul–Sep
            Double finQ2 = nonTrainingExpenditureRepository.sumExpenditureByAgencyAndActivityAndDateRange(
                    agencyId, t.getNonTrainingActivity().getActivityId(),
                    java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(financialYear.split("-")[0]), 7, 1)),
                    java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(financialYear.split("-")[0]), 9, 30))
            );
            dto.setFinancialAchievedQ2(finQ2 != null ? finQ2 : 0.0);

// Q3: Oct–Dec
            Double finQ3 = nonTrainingExpenditureRepository.sumExpenditureByAgencyAndActivityAndDateRange(
                    agencyId, t.getNonTrainingActivity().getActivityId(),
                    java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(financialYear.split("-")[0]), 10, 1)),
                    java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(financialYear.split("-")[0]), 12, 31))
            );
            dto.setFinancialAchievedQ3(finQ3 != null ? finQ3 : 0.0);

// Q4: Jan–Mar (next year)
            Double finQ4 = nonTrainingExpenditureRepository.sumExpenditureByAgencyAndActivityAndDateRange(
                    agencyId, t.getNonTrainingActivity().getActivityId(),
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


            // Set totals
            dto.setAchievedQ1(t.getAchievements().stream().map(NonTrainingAchievement::getQ1Achievement).findFirst().orElse("Yet to Begin"));
            dto.setAchievedQ2(t.getAchievements().stream().map(NonTrainingAchievement::getQ2Achievement).findFirst().orElse("Yet to Begin"));
            dto.setAchievedQ3(t.getAchievements().stream().map(NonTrainingAchievement::getQ3Achievement).findFirst().orElse("Yet to Begin"));
            dto.setAchievedQ4(t.getAchievements().stream().map(NonTrainingAchievement::getQ4Achievement).findFirst().orElse("Yet to Begin"));
            dto.setTotalFinancialTarget(
                    (int) (t.getQ1Budget() + t.getQ2Budget() + t.getQ3Budget() + t.getQ4Budget())
            );
            dto.setTotalTarget(t.getQ1Target() + t.getQ2Target() + t.getQ3Target() + t.getQ4Target());
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
