package com.metaverse.workflow.trainingandnontrainingtarget.service;

import com.metaverse.workflow.model.NonTrainingAchievement;
import com.metaverse.workflow.model.NonTrainingTargets;
import com.metaverse.workflow.nontraining.repository.NonTrainingAchievementRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.ListingOnNSERepository;
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

    private final ListingOnNSERepository listingOnNSERepository;


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

            dto.setTrainingTargetQ1(t.getQ1Target() != null ? t.getQ1Target() : 0L);
            dto.setTrainingTargetQ2(t.getQ2Target() != null ? t.getQ2Target() : 0L);
            dto.setTrainingTargetQ3(t.getQ3Target() != null ? t.getQ3Target() : 0L);
            dto.setTrainingTargetQ4(t.getQ4Target() != null ? t.getQ4Target() : 0L);

            dto.setFinancialTargetQ1(t.getQ1Budget() != null ? t.getQ1Budget() : 0.0);
            dto.setFinancialTargetQ2(t.getQ2Budget() != null ? t.getQ2Budget() : 0.0);
            dto.setFinancialTargetQ3(t.getQ3Budget() != null ? t.getQ3Budget() : 0.0);
            dto.setFinancialTargetQ4(t.getQ4Budget() != null ? t.getQ4Budget() : 0.0);

            dto.setPhysicalExpenditurePercentage("Not Applicable"); // Not applicable for Non-Training

            // Financial Achievements per quarter
            Date[] fyRange = getFinancialYearRange(financialYear);

            Object[] physicalFinancialAchieved = quarterlyWiseExpenditure(agencyId, t.getNonTrainingSubActivity().getSubActivityId(), financialYear, t.getNonTrainingSubActivity().getNonTrainingActivity().getActivityName(), t.getNonTrainingSubActivity().getSubActivityName());
            Long[] physicalAchieved = (Long[]) physicalFinancialAchieved[0];
            Double[] financialAchieved = (Double[]) physicalFinancialAchieved[1];


            Double finQ1 = financialAchieved[0];
            Double finQ2 = financialAchieved[1];
            Double finQ3 = financialAchieved[2];
            Double finQ4 = financialAchieved[3];


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


            // Set totals  yet to begin
            dto.setAchievedQ1(String.valueOf(physicalAchieved[0]));
            dto.setAchievedQ2(String.valueOf(physicalAchieved[1]));
            dto.setAchievedQ3(String.valueOf(physicalAchieved[2]));
            dto.setAchievedQ4(String.valueOf(physicalAchieved[3]));

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

    public Object[] quarterlyWiseExpenditure(Long agencyId, Long subActivityId, String financialYear, String activityName, String subActivityName) {
        long subActivityId1 = subActivityId;
        switch ((int) subActivityId1) {
            case 26 -> {
                Long q1 = nonTrainingResourceRepository.countResourcesBySubActivityAndDateRange(subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[0], 4, 1, true),
                        getFinancialYearRange1(financialYear.split("-")[0], 6, 30, true)
                );
                Long q2 = nonTrainingExpenditureRepository.countRegistrationsBySubActivityAndDateRange(subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[0], 7, 1, true),
                        getFinancialYearRange1(financialYear.split("-")[0], 9, 30, true)
                );
                Long q3 = nonTrainingResourceRepository.countResourcesBySubActivityAndDateRange(subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[0], 10, 1, true),
                        getFinancialYearRange1(financialYear.split("-")[0], 12, 31, true)
                );
                Long q4 = nonTrainingExpenditureRepository.countRegistrationsBySubActivityAndDateRange(subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[1], 1, 1, true),
                        getFinancialYearRange1(financialYear.split("-")[1], 3, 31, true)
                );

                Double[] nonTrainingResourceExpenditure = getNonTrainingResourceExpenditureByQuarterlyWise(agencyId, subActivityId, financialYear, activityName, subActivityName);

                return new Object[]{new Long[]{q1, q2, q3, q4},
                        nonTrainingResourceExpenditure};
            }
            case 27, 28 -> {
                Long q1 = nonTrainingExpenditureRepository.countRegistrationsBySubActivityAndDateRange(subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[0], 4, 1, true),
                        getFinancialYearRange1(financialYear.split("-")[0], 6, 30, true)
                );
                Long q2 = nonTrainingExpenditureRepository.countRegistrationsBySubActivityAndDateRange(subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[0], 7, 1, true),
                        getFinancialYearRange1(financialYear.split("-")[0], 9, 30, true)
                );
                Long q3 = nonTrainingExpenditureRepository.countRegistrationsBySubActivityAndDateRange(subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[0], 10, 1, true),
                        getFinancialYearRange1(financialYear.split("-")[0], 12, 31, true)
                );
                Long q4 = nonTrainingExpenditureRepository.countRegistrationsBySubActivityAndDateRange(subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[1], 1, 1, true),
                        getFinancialYearRange1(financialYear.split("-")[1], 3, 31, true)
                );

                Double[] nonTrainingExpenditure = getNonTrainingExpenditureByQuarterlyWise(agencyId, subActivityId, financialYear, activityName, subActivityName);
                return new Object[]{new Long[]{q1, q2, q3, q4},
                                    nonTrainingExpenditure};

            }
            case 67 -> { //Corpus-Debt Financing

            }
            case 76 -> { // Corpus-Listing On NSE
                // financial Achieved
                Double  expQ1 = listingOnNSERepository.sumLoanAmountBySubActivityAndDateRange(subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[0], 4, 1, false),
                        getFinancialYearRange1(financialYear.split("-")[0], 6, 30, false));
                Double expQ2 = listingOnNSERepository.sumLoanAmountBySubActivityAndDateRange(
                        subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[0],7,1,false),
                        getFinancialYearRange1(financialYear.split("-")[0],9,30,false)
                );

                Double expQ3 = listingOnNSERepository.sumLoanAmountBySubActivityAndDateRange(
                         subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[0],10,1,false),
                        getFinancialYearRange1(financialYear.split("-")[0],12,31,false)
                );

                Double expQ4 = listingOnNSERepository.sumLoanAmountBySubActivityAndDateRange(
                         subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[1],1,1,false),
                        getFinancialYearRange1(financialYear.split("-")[1],3,31,false)

                );

              //  Training achievement
                Long trainingAchievement1 = listingOnNSERepository.countListingsBySubActivityAndDateRange(subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[0], 4, 1, true),
                        getFinancialYearRange1(financialYear.split("-")[0], 6, 30, true)
                );
                Long trainingAchievement2 = listingOnNSERepository.countListingsBySubActivityAndDateRange(subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[0], 7, 1, true),
                        getFinancialYearRange1(financialYear.split("-")[0], 9, 30, true)
                );
                Long trainingAchievement3 = listingOnNSERepository.countListingsBySubActivityAndDateRange(subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[0], 10, 1, true),
                        getFinancialYearRange1(financialYear.split("-")[0], 12, 31, true)
                );
                Long trainingAchievement4 = listingOnNSERepository.countListingsBySubActivityAndDateRange(subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[1], 1, 1, true),
                        getFinancialYearRange1(financialYear.split("-")[1], 3, 31, true));

                return new Object[]{new Long[]{trainingAchievement1, trainingAchievement2, trainingAchievement3, trainingAchievement4},
                                    new Double[]{expQ1, expQ2, expQ3, expQ4}};
            }
        }
        return  new Double[]{0.0, 0.0, 0.0, 0.0};
    }


    public Double[] getTravelAndTransportExpenditureByQuetrlyWise(Long agencyId, Long subActivityId, String financialYear, String activityName, String subActivityName) {
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