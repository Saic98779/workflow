package com.metaverse.workflow.trainingandnontrainingtarget.service;

import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.nontraining.repository.NonTrainingAchievementRepository;
import com.metaverse.workflow.nontrainingExpenditures.Dto.CorpusDebitFinancing;
import com.metaverse.workflow.nontrainingExpenditures.repository.*;
import com.metaverse.workflow.nontrainingExpenditures.service.WeHubService;
import com.metaverse.workflow.trainingandnontrainingtarget.dtos.NonTrainingTargetsAndAchievementsResponse;
import com.metaverse.workflow.trainingandnontrainingtarget.repository.NonTrainingTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class NonTrainingTargetsAndAchievementsServiceImpl implements NonTrainingTargetsAndAchievementsService {

    private final NonTrainingTargetRepository nonTrainingTargetRepository;

    private final NonTrainingExpenditureRepository nonTrainingExpenditureRepository;

    private final NonTrainingAchievementRepository nonTrainingAchievementRepository;
    private final NonTrainingResourceRepository nonTrainingResourceRepository;
    private final TravelAndTransportRepository travelAndTransportRepository;
    private final WeHubService service;
    private final AgencyRepository agencyRepository;
    private final NonTrainingSubActivityRepository  nonTrainingSubActivityRepository;



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
            dto.setAgencyName(t.getNonTrainingActivity().getAgency().getAgencyName());
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
                Long q2 = nonTrainingResourceRepository.countResourcesBySubActivityAndDateRange(subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[0], 7, 1, true),
                        getFinancialYearRange1(financialYear.split("-")[0], 9, 30, true)
                );
                Long q3 = nonTrainingResourceRepository.countResourcesBySubActivityAndDateRange(subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[0], 10, 1, true),
                        getFinancialYearRange1(financialYear.split("-")[0], 12, 31, true)
                );
                Long q4 = nonTrainingResourceRepository.countResourcesBySubActivityAndDateRange(subActivityId,
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
                List<CorpusDebitFinancing> list = service.corpusDebitFinancing();
                Object[] quarterlySummaryCorpusFinance = getQuarterlySummaryCorpusFinance(list, financialYear);
                return quarterlySummaryCorpusFinance;
            }
            case 76 -> { // Corpus-Listing On NSE
                Double  expQ1 = listingOnNSERepository.sumLoanAmountBySubActivityAndDateRange(subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[0], 4, 1, true),
                        getFinancialYearRange1(financialYear.split("-")[0], 6, 30, true));
                Double expQ2 = listingOnNSERepository.sumLoanAmountBySubActivityAndDateRange(
                        subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[0],7,1,true),
                        getFinancialYearRange1(financialYear.split("-")[0],9,30,true)
                );

                Double expQ3 = listingOnNSERepository.sumLoanAmountBySubActivityAndDateRange(
                         subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[0],10,1,true),
                        getFinancialYearRange1(financialYear.split("-")[0],12,31,true)
                );

                Double expQ4 = listingOnNSERepository.sumLoanAmountBySubActivityAndDateRange(
                         subActivityId,
                        getFinancialYearRange1(financialYear.split("-")[1],1,1,true),
                        getFinancialYearRange1(financialYear.split("-")[1],3,31,true)

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

    public static Object[] getQuarterlySummaryCorpusFinance(List<CorpusDebitFinancing> usages, String financialYear) {

        Long[] registrations = {0L, 0L, 0L, 0L};
        Double[] sanctionedAmounts = {0.0, 0.0, 0.0, 0.0};

        String[] parts = financialYear.split("-");
        int startYear = Integer.parseInt(parts[0]);
        int endYear = Integer.parseInt(parts[1]);

        LocalDate fyStart = LocalDate.of(startYear, 4, 1);
        LocalDate fyEnd   = LocalDate.of(endYear, 3, 31);

        for (CorpusDebitFinancing usage : usages) {
            LocalDate createdDate = usage.getCreatedOn();

            // skip if not in financial year
            if (createdDate.isBefore(fyStart) || createdDate.isAfter(fyEnd)) {
                continue;
            }

            int month = createdDate.getMonthValue();
            int year = createdDate.getYear();
            int quarter;

            if (year == startYear) {
                if (month >= 4 && month <= 6) quarter = 1;        // Apr–Jun
                else if (month >= 7 && month <= 9) quarter = 2;   // Jul–Sep
                else quarter = 3;                                 // Oct–Dec
            } else {
                quarter = 4;
            }

            registrations[quarter - 1] += 1;
            sanctionedAmounts[quarter - 1] += usage.getSanctionedAmount();
        }
        return new Object[]{registrations, sanctionedAmounts};
    }

    @Override
    public WorkflowResponse saveNonTrainingTarget(TargetRequest request) throws DataException {

        NonTrainingSubActivity subActivity = nonTrainingSubActivityRepository.findById(request.getSubActivityId())
                .orElseThrow(() -> new DataException("Sub Activity not found", "SUB_ACTIVITY_NOT_FOUND", 400));

        Optional<NonTrainingTargets> existingTargetOpt =
                nonTrainingTargetRepository.findByNonTrainingSubActivityAndFinancialYear(
                        subActivity, request.getFinancialYear()
                );

        NonTrainingTargets trainingTarget;
        String message;

        if (existingTargetOpt.isPresent()) {
            trainingTarget = existingTargetOpt.get();

            if (request.getQ1Target() != null) trainingTarget.setQ1Target(request.getQ1Target());
            if (request.getQ2Target() != null) trainingTarget.setQ2Target(request.getQ2Target());
            if (request.getQ3Target() != null) trainingTarget.setQ3Target(request.getQ3Target());
            if (request.getQ4Target() != null) trainingTarget.setQ4Target(request.getQ4Target());

            if (request.getQ1Budget() != null) trainingTarget.setQ1Budget(request.getQ1Budget());
            if (request.getQ2Budget() != null) trainingTarget.setQ2Budget(request.getQ2Budget());
            if (request.getQ3Budget() != null) trainingTarget.setQ3Budget(request.getQ3Budget());
            if (request.getQ4Budget() != null) trainingTarget.setQ4Budget(request.getQ4Budget());

            message = "Target updated successfully";

        } else {
            trainingTarget = NonTrainingTargetMapper.mapToTrainingTarget(request, subActivity);
            message = "Target saved successfully";
        }

        nonTrainingTargetRepository.save(trainingTarget);

        return WorkflowResponse.builder()
                .data(NonTrainingTargetMapper.mapToTrainingTargetResponse(trainingTarget))
                .message(message)   // 🔥 dynamic msg
                .status(200)
                .build();
    }


    @Override
    public List<TargetResponse> getNonTrainingTargets(String year, Long agencyId) {

        List<NonTrainingTargets> targets =
                nonTrainingTargetRepository
                        .findByNonTrainingSubActivity_NonTrainingActivity_Agency_AgencyIdAndFinancialYear(agencyId, year);

        if (targets.isEmpty()) {
            throw new RuntimeException("No targets found for agency " + agencyId + " in FY " + year);
        }

        return targets.stream().map(t -> {
            TargetResponse dto = new TargetResponse();

            dto.setTargetId(t.getId());
            dto.setActivityName(t.getNonTrainingSubActivity().getNonTrainingActivity().getActivityName());
            dto.setSubActivityName(t.getNonTrainingSubActivity().getSubActivityName());
            dto.setAgencyName(t.getNonTrainingSubActivity().getNonTrainingActivity().getAgency().getAgencyName());
            dto.setFinancialYear(t.getFinancialYear());

            // Physical targets
            dto.setPhysicalTargetQ1(Optional.ofNullable(t.getQ1Target()).orElse(0L));
            dto.setPhysicalTargetQ2(Optional.ofNullable(t.getQ2Target()).orElse(0L));
            dto.setPhysicalTargetQ3(Optional.ofNullable(t.getQ3Target()).orElse(0L));
            dto.setPhysicalTargetQ4(Optional.ofNullable(t.getQ4Target()).orElse(0L));
            dto.setTotalTrainingTarget(
                    dto.getPhysicalTargetQ1() + dto.getPhysicalTargetQ2() +
                            dto.getPhysicalTargetQ3() + dto.getPhysicalTargetQ4()
            );

            // Financial targets
            dto.setFinancialTargetQ1(Optional.ofNullable(t.getQ1Budget()).orElse(0.0));
            dto.setFinancialTargetQ2(Optional.ofNullable(t.getQ2Budget()).orElse(0.0));
            dto.setFinancialTargetQ3(Optional.ofNullable(t.getQ3Budget()).orElse(0.0));
            dto.setFinancialTargetQ4(Optional.ofNullable(t.getQ4Budget()).orElse(0.0));
            dto.setTotalFinancialTarget(
                    dto.getFinancialTargetQ1() + dto.getFinancialTargetQ2() +
                            dto.getFinancialTargetQ3() + dto.getFinancialTargetQ4()
            );

            return dto;
        }).toList();
    }



}