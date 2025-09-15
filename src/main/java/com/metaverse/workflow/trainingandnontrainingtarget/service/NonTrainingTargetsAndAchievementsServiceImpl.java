package com.metaverse.workflow.trainingandnontrainingtarget.service;

import com.metaverse.workflow.model.NonTrainingTargets;
import com.metaverse.workflow.nontraining.repository.NonTrainingAchievementRepository;
import com.metaverse.workflow.nontrainingExpenditures.Dto.CorpusDebitFinancing;
import com.metaverse.workflow.nontrainingExpenditures.repository.ListingOnNSERepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingExpenditureRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingResourceRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.TravelAndTransportRepository;
import com.metaverse.workflow.trainingandnontrainingtarget.dtos.NonTrainingTargetsAndAchievementsResponse;
import com.metaverse.workflow.trainingandnontrainingtarget.repository.NonTrainingTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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


            dto.setTrainingTargetQ1(Optional.ofNullable(t.getQ1Target()).orElse(0L));
            dto.setTrainingTargetQ2(Optional.ofNullable(t.getQ2Target()).orElse(0L));
            dto.setTrainingTargetQ3(Optional.ofNullable(t.getQ3Target()).orElse(0L));
            dto.setTrainingTargetQ4(Optional.ofNullable(t.getQ4Target()).orElse(0L));


            dto.setFinancialTargetQ1(Optional.ofNullable(t.getQ1Budget()).orElse(0.0));
            dto.setFinancialTargetQ2(Optional.ofNullable(t.getQ2Budget()).orElse(0.0));
            dto.setFinancialTargetQ3(Optional.ofNullable(t.getQ3Budget()).orElse(0.0));
            dto.setFinancialTargetQ4(Optional.ofNullable(t.getQ4Budget()).orElse(0.0));


            Object[] physicalFinancialAchieved = quarterlyWiseExpenditure(
                    agencyId,
                    t.getNonTrainingSubActivity().getSubActivityId(),
                    financialYear,
                    t.getNonTrainingSubActivity().getNonTrainingActivity().getActivityName(),
                    t.getNonTrainingSubActivity().getSubActivityName()
            );


            Long[] physicalAchieved = Arrays.stream((Object[]) physicalFinancialAchieved[0])
                    .map(o -> (o instanceof Number) ? ((Number) o).longValue() : 0L)
                    .toArray(Long[]::new);

            Double[] financialAchieved = Arrays.stream((Object[]) physicalFinancialAchieved[1])
                    .map(o -> (o instanceof Number) ? ((Number) o).doubleValue() : 0.0)
                    .toArray(Double[]::new);


            Double finQ1 = financialAchieved.length > 0 ? financialAchieved[0] : 0.0;
            Double finQ2 = financialAchieved.length > 1 ? financialAchieved[1] : 0.0;
            Double finQ3 = financialAchieved.length > 2 ? financialAchieved[2] : 0.0;
            Double finQ4 = financialAchieved.length > 3 ? financialAchieved[3] : 0.0;

            dto.setFinancialAchievedQ1(finQ1);
            dto.setFinancialAchievedQ2(finQ2);
            dto.setFinancialAchievedQ3(finQ3);
            dto.setFinancialAchievedQ4(finQ4);

            // Totals
            dto.setAchievedQ1(String.valueOf(physicalAchieved[0]));
            dto.setAchievedQ2(String.valueOf(physicalAchieved[1]));
            dto.setAchievedQ3(String.valueOf(physicalAchieved[2]));
            dto.setAchievedQ4(String.valueOf(physicalAchieved[3]));
            dto.setTotalAchieved(physicalAchieved[0]+physicalAchieved[1]+physicalAchieved[2]+physicalAchieved[3]);

            long totalAchieved = Arrays.stream(physicalAchieved).mapToLong(Long::longValue).sum();
            dto.setTotalTarget(dto.getTrainingTargetQ1() + dto.getTrainingTargetQ2()
                    + dto.getTrainingTargetQ3() + dto.getTrainingTargetQ4());

            dto.setTotalFinancialTarget((int) (dto.getFinancialTargetQ1()
                                + dto.getFinancialTargetQ2()
                                + dto.getFinancialTargetQ3()
                                + dto.getFinancialTargetQ4()));

            double totalFinancialAchieved = finQ1 + finQ2 + finQ3 + finQ4;
            dto.setTotalFinancialAchieved(totalFinancialAchieved);


            dto.setPhysicalExpenditurePercentage(
                    dto.getTotalTarget() == 0 ? "0.0"
                            : String.valueOf(Math.round ((((double) totalAchieved / dto.getTotalTarget()) * 100)*100.0)/ 100.0)
            );

            dto.setFinancialExpenditurePercentage(
                    dto.getTotalFinancialTarget() == 0 ? 0.0
                            : Math.round(((totalFinancialAchieved / dto.getTotalFinancialTarget()) * 100) * 100.0)/100.0
            );
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
            case 26, 14, 15, 16, 17, 74, 75 -> { //Resource : 14 : Staff - CEO, 15 : Staff - Designers, 16 : Staff - Designers ,17 : Staff - Project Manager,74 : Interns for certifications, 75 : R&D
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
            case 27, 28, 73 -> {  // Expenditure   73 : IT Infrastructure Setup
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

                return  new Object[][]{{0L,0L,0L,0L},{0.0, 0.0, 0.0, 0.0}};
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
            }case  19 -> { // 19 : Travel - IITH
                Long q1 = travelAndTransportRepository.countTravelBySubActivityAndDateRange(subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[0], 4, 1, false),
                        getFinancialYearRange1(financialYear.split("-")[0], 6, 30, false)
                );
                Long q2 = travelAndTransportRepository.countTravelBySubActivityAndDateRange(subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[0], 7, 1, false),
                        getFinancialYearRange1(financialYear.split("-")[0], 9, 30, false)
                );
                Long q3 = travelAndTransportRepository.countTravelBySubActivityAndDateRange(subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[0], 10, 1, false),
                        getFinancialYearRange1(financialYear.split("-")[0], 12, 31, false)
                );
                Long q4 = travelAndTransportRepository.countTravelBySubActivityAndDateRange(subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[1], 1, 1, false),
                        getFinancialYearRange1(financialYear.split("-")[1], 3, 31, false)
                );

                Double[] nonTrainingResourceExpenditure = getTravelAndTransportExpenditureByQuarterlyWise(agencyId, subActivityId, financialYear, activityName, subActivityName);

                return new Object[]{new Long[]{q1, q2, q3, q4},
                        nonTrainingResourceExpenditure};

            }
        }
        return  new Object[][]{{0L,0L,0L,0L},{0.0, 0.0, 0.0, 0.0}};
    }


    public Double[] getTravelAndTransportExpenditureByQuarterlyWise(Long agencyId, Long subActivityId, String financialYear, String activityName, String subActivityName) {
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