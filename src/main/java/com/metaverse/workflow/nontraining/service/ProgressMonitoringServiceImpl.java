package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.ProgramMonitoring.service.SubActivityParticipantCountDTO;
import com.metaverse.workflow.activity.repository.ActivityRepository;
import com.metaverse.workflow.expenditure.repository.ProgramExpenditureRepository;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.nontraining.dto.NonTrainingProgramDto;
import com.metaverse.workflow.nontraining.dto.ProgressMonitoringDto;
import com.metaverse.workflow.nontraining.dto.TrainingProgramDto;
import com.metaverse.workflow.nontraining.repository.NonTrainingAchievementRepository;
import com.metaverse.workflow.nontraining.repository.NonTrainingActivityRepository;
import com.metaverse.workflow.nontrainingExpenditures.Dto.CorpusDebitFinancing;
import com.metaverse.workflow.nontrainingExpenditures.repository.ListingOnNSERepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingExpenditureRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingResourceRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.TravelAndTransportRepository;
import com.metaverse.workflow.nontrainingExpenditures.service.WeHubService;
import com.metaverse.workflow.trainingandnontrainingtarget.repository.NonTrainingTargetRepository;
import com.metaverse.workflow.trainingandnontrainingtarget.repository.TrainingTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProgressMonitoringServiceImpl implements ProgressMonitoringService {

    private final NonTrainingProgramMapper nonTrainingProgramMapper;
    private final ProgramExpenditureRepository programExpenditureRepository;
    private final NonTrainingExpenditureRepository nonTrainingExpenditureRepository;
    private final NonTrainingTargetRepository nonTrainingTargetRepository;
    private final TrainingTargetRepository trainingTargetRepository;
    private final NonTrainingResourceRepository nonTrainingResourcesRepository;
    private final ActivityRepository activityRepository;
    private final TravelAndTransportRepository travelAndTransportRepository;

    private final WeHubService service;

    private final NonTrainingActivityRepository nonTrainingActivityRepository;
    private final NonTrainingAchievementRepository nonTrainingAchievementRepository;
    private final ListingOnNSERepository listingOnNSERepository;

    /*
        --- Non-Training Programs ---
        1. Fetch all non-training targets for the given agency and group them by activity.
        2. Sum up the expenditures for each sub-activity under the agency.
        3. For each activity, calculate total targets, total budget, and total expenditure.
        4. Handle special case for "Contingency Fund" by fetching expenditure from resources repository.
        5. Combine all the data into NonTrainingProgramDto objects and return the summary.
     */

    @Override
    public ProgressMonitoringDto getAllNonTrainingsSummary(Long agencyId) {
        List<NonTrainingProgramDto> nonTrainingPrograms = new ArrayList<>();

        // --- Non-Training Programs ---
        Map<Long, Double> nonTrainingExp = ProgressMonitoringUtils.toDoubleMap(
                nonTrainingExpenditureRepository.sumExpenditureByAgencyGroupedBySubActivity(agencyId)
        );

        Map<Long, ProgressMonitoringUtils.TargetSummary<NonTrainingTargets>> nonTrainingSummary =
                ProgressMonitoringUtils.buildSummary(
                        nonTrainingTargetRepository
                                .findByNonTrainingSubActivity_NonTrainingActivity_Agency_AgencyId(agencyId),
                        t -> t.getNonTrainingSubActivity() != null
                                ? t.getNonTrainingSubActivity().getSubActivityId()
                                : null
                );

        nonTrainingSummary.forEach((key, summary) -> {
            NonTrainingTargets target = summary.activityName;
            double expenditure = nonTrainingExp.getOrDefault(key, 0.0);

            if (target.getNonTrainingSubActivity() != null &&
                    target.getNonTrainingSubActivity().getNonTrainingActivity() != null) {

                String activityName = target.getNonTrainingSubActivity()
                        .getNonTrainingActivity()
                        .getActivityName()
                        .trim();

                String subActivityName = target.getNonTrainingSubActivity()
                        .getSubActivityName()
                        .trim();

                // Normalize to lower case for safe comparison
                String activityKey = activityName.toLowerCase();
                String subActivityKey = subActivityName.toLowerCase();

                switch (activityKey) {
                    case "staff" -> {
                        // Handle staff differently per sub-activity if needed
                        switch (subActivityKey) {
                            case "staff - ceo", "staff - designers", "staff - project manager", "interns for certifications", "r&d" -> {
                                System.out.println("Fetching Staff expenditure (" + subActivityName + ") from resources repository");
                                Double resourceExp = nonTrainingResourcesRepository
                                        .sumExpenditureByActivityAndSubActivity(
                                                target.getNonTrainingSubActivity().getNonTrainingActivity().getActivityId(),
                                                target.getNonTrainingSubActivity().getSubActivityId()
                                        );
                                expenditure = resourceExp != null ? resourceExp : 0.0;
                            }
                            default -> {
                                System.out.println("Default Staff handling for: " + subActivityName);
                                expenditure = nonTrainingExp.getOrDefault(key, 0.0);
                            }
                        }
                    }

                    case "contingency fund" -> {
                        System.out.println("Fetching " + activityName + " expenditure from resources repository");
                        Double resourceExp = nonTrainingResourcesRepository
                                .sumExpenditureByActivityAndSubActivity(
                                        target.getNonTrainingSubActivity().getNonTrainingActivity().getActivityId(),
                                        target.getNonTrainingSubActivity().getSubActivityId()
                                );
                        expenditure = resourceExp != null ? resourceExp : 0.0;
                    }

                    case "project team", "setting up call center services for validation" -> {
                        // All treated as Staff
                        System.out.println("Fetching Staff expenditure (" + activityName + " - " + subActivityName + ")");
                        switch (subActivityKey) {
                            case "staff", "technology firm" -> {
                                Double resourceExp = nonTrainingResourcesRepository
                                        .sumExpenditureByActivityAndSubActivity(
                                                target.getNonTrainingSubActivity().getNonTrainingActivity().getActivityId(),
                                                target.getNonTrainingSubActivity().getSubActivityId()
                                        );
                                expenditure = resourceExp != null ? resourceExp : 0.0;
                            }
                        }
                    }

                    case "hiring of technology platform" -> {
                        // Map to Technology firm
                        System.out.println("Fetching Technology firm expenditure");
                        Double resourceExp = nonTrainingResourcesRepository
                                .sumExpenditureByActivityAndSubActivity(
                                        target.getNonTrainingSubActivity().getNonTrainingActivity().getActivityId(),
                                        target.getNonTrainingSubActivity().getSubActivityId()
                                );
                        expenditure = resourceExp != null ? resourceExp : 0.0;
                    }


                    case "travel & transport" -> {
                        System.out.println("Fetching Travel & Transport expenditure for " + subActivityName);
                        Double resourceExp = travelAndTransportRepository
                                .sumTravelAndTransportByActivityAndSubActivity(
                                        target.getNonTrainingSubActivity().getNonTrainingActivity().getActivityId(),
                                        target.getNonTrainingSubActivity().getSubActivityId()
                                );
                        expenditure = resourceExp != null ? resourceExp : 0.0;
                    }

                    default -> {
                        System.out.println("Using default expenditure from nonTrainingExp map for " + activityName + " - " + subActivityName);
                        expenditure = nonTrainingExp.getOrDefault(key, 0.0);
                    }
                }
            }

            nonTrainingPrograms.add(
                    nonTrainingProgramMapper.nonTrainingProgramDtoMapper(
                            target,
                            summary.totalTargets,
                            summary.totalBudget,
                            expenditure
                    )
            );
        });

        return ProgressMonitoringDto.builder()
                .nonTrainingPrograms(nonTrainingPrograms)
                .build();
    }




    /*
        --- Training Programs ---
        1. Fetch all activities for the given agency.
        2. For each activity, fetch its sub-activities and map their names.
        3. Fetch training targets for these sub-activities and sum them up.
        4. Fetch participant counts for these sub-activities.
        5. Fetch program expenditures for these sub-activities and sum them up.
        6. Combine all the data into TrainingProgramDto objects and return the list.
     */

    @Override
    public List<TrainingProgramDto> getAllTrainingProgressMonitoringProgress(Long agencyId) {

        List<TrainingProgramDto> trainingProgressData = new ArrayList<>();
        Map<Long, String> activityNames = new HashMap<>();
        Map<Long, String> subActivityNames = new HashMap<>();
        Map<Long, Long> target = new HashMap<>();
        Map<Long, Long> countOfParticipants = new HashMap<>();
        Map<Long,Double> programExp = new HashMap<>();

        List<Activity> byAgencyAgencyId = activityRepository.findByAgencyAgencyId(agencyId);
        String agencyName = byAgencyAgencyId.get(0).getAgency().getAgencyName();

        for (Activity a : byAgencyAgencyId) {
            List<SubActivity> subActivities = a.getSubActivities();
            for (SubActivity sb : subActivities) {
                activityNames.put(sb.getSubActivityId(), a.getActivityName());
                subActivityNames.put(sb.getSubActivityId(), sb.getSubActivityName());
            }
        }

        Set<Long> longs = subActivityNames.keySet();

        List<TrainingTargets> allById = trainingTargetRepository.findBySubActivity_SubActivityIdInAndAgency_AgencyId(longs,agencyId);
        for (TrainingTargets targets : allById) {
            Long subActivityId = targets.getSubActivity().getSubActivityId();
            long total = targets.getQ1Target() + targets.getQ2Target() + targets.getQ3Target() + targets.getQ4Target();
            target.merge(subActivityId, total, Long::sum);
        }
        List<SubActivityParticipantCountDTO> participantCountByAgencyId = activityRepository.findProgramCountByAgencyId(agencyId);

        for (SubActivityParticipantCountDTO dto : participantCountByAgencyId) {
            countOfParticipants.put(dto.getSubActivityId(), Long.valueOf(dto.getParticipantCount()));
        }

        Map<Long,Double> budgetAllocated = new HashMap<>();
        for (TrainingTargets targets : allById) {
            budgetAllocated.put(targets.getSubActivity().getSubActivityId(),(targets.getQ1Budget() + targets.getQ2Budget() + targets.getQ3Budget() + targets.getQ4Budget()));
        }

        Iterable<ProgramExpenditure> bySubActivitySubActivityId = programExpenditureRepository.findBySubActivity_SubActivityIdInAndAgency_AgencyId(subActivityNames.keySet(),agencyId);

        for (ProgramExpenditure   p : bySubActivitySubActivityId){
            addOrUpdate(programExp,p.getSubActivity().getSubActivityId(),p.getCost());
        }

        Set<Long> commonKeys = new HashSet<>(subActivityNames.keySet());
        for (Long key : commonKeys) {
            Double target1 =  Double.valueOf(target.getOrDefault(key, 0L));
            Long countOfParticipants1 = countOfParticipants.getOrDefault(key, 0L);

            Double bud = budgetAllocated.getOrDefault(key, 0.0);
            Double programExpO = programExp.getOrDefault(key, 0.0);

            trainingProgressData.add(TrainingProgramDto.builder().agency(agencyName)
                    .activity(activityNames.get(key))
                    .subActivityId(key)
                    .subActivity(subActivityNames.get(key))
                    .trainingTarget(target1)
                    .trainingAchievement(countOfParticipants1)
                    .trainingPercentage((target1 != 0 ? Math.round((countOfParticipants1 / target1) * 100 * 1000.0) / 1000.0 : 0.0))
                    .budgetAllocated(bud)
                    .expenditure(programExpO)
                    .expenditurePercentage((bud != 0.0) ? Math.round((programExpO / bud) * 100 * 1000.0) / 1000.0 : 0.0)
                    .build());
        }
        return trainingProgressData;
    }

    private static void addOrUpdate(Map<Long, Double> map, Long key, Double value) {
        map.merge(key, value, Double::sum);
    }


    public List<NonTrainingProgramDto> nonTrainingProgressMonitoring(Long agencyId) {

        List<NonTrainingProgramDto> nonTrainingProgramDtoList = new ArrayList<>();

        List<NonTrainingActivity> byAgencyAgencyId =
                nonTrainingActivityRepository.findByAgency_AgencyId(agencyId);

        for (NonTrainingActivity activity : byAgencyAgencyId) {
            System.out.println(activity.getActivityName());
            long activityId = activity.getActivityId();

            switch ((int) activityId) {
                case 13, 16, 17, 18, 22, 23, 24, 25 -> {
                    List<NonTrainingSubActivity> subActivities = activity.getSubActivities();

                    for (NonTrainingSubActivity subActivity : subActivities) {
                        NonTrainingProgramDto nonTrainingProgramDto = new NonTrainingProgramDto();
                        nonTrainingProgramDto.setNonTrainingActivity(activity.getActivityName());
                        nonTrainingProgramDto.setNonTrainingSubActivity(subActivity.getSubActivityName());

                        long subActivityId = subActivity.getSubActivityId();

                        List<NonTrainingTargets> nonTrainingSubActivity = nonTrainingTargetRepository.findByNonTrainingSubActivity_subActivityId(subActivityId);
                        long physicalTarget = nonTrainingSubActivity.stream().mapToLong(r -> r.getQ1Target() + r.getQ2Target() + r.getQ3Target() + r.getQ4Target()).sum();
                        double financialTarget = nonTrainingSubActivity.stream().mapToDouble(r -> r.getQ1Budget() + r.getQ2Budget() + r.getQ3Budget() + r.getQ4Budget()).sum();
                        nonTrainingProgramDto.setPhysicalTarget((int)physicalTarget);
                        nonTrainingProgramDto.setFinancialTarget(financialTarget);

                        Object[] objects = progressMonitoringSubActivityWiseAchievements(subActivityId);
                        nonTrainingProgramDto.setPhysicalAchievement((String) objects[0]);
                        nonTrainingProgramDto.setFinancialExpenditure((Double) objects[1]);

                        nonTrainingProgramDto.setPercentage((financialTarget != 0.0) ? Math.round(((Double) objects[1] / financialTarget) * 100 * 100.0) / 100.0 : 0.0);
                        try {
                            nonTrainingProgramDto.setPhysicalPercentage((physicalTarget != 0.0) ? Math.round(Long.valueOf((String) objects[0]) / physicalTarget * 100* 1000.0)/1000.0+"" : 0.0+"");
                        }catch (Exception ee){
                            nonTrainingProgramDto.setPhysicalPercentage((String) objects[0]);
                        }

                        nonTrainingProgramDtoList.add(nonTrainingProgramDto);
                    }
                }

            }
        }
        return nonTrainingProgramDtoList;
    }


    public Object[] progressMonitoringSubActivityWiseAchievements(Long subActivityId) {
        NonTrainingProgramDto nonTrainingProgramDto = new NonTrainingProgramDto();
        long subActivityId1 = subActivityId;
        switch ((int) subActivityId1) {
            // TIHCL sub activities
            case 67 -> { // 67  Corpus-Debt Financing
                List<CorpusDebitFinancing> list = service.corpusDebitFinancing();
                if (list.isEmpty()) {
                    nonTrainingProgramDto.setFinancialExpenditure(0.0);
                }
                Double financialAchieved = list.stream().mapToDouble(r -> r.getTotalDisbursedAmount()).sum();
                String.valueOf(list.size());
                Object[] objects = {String.valueOf(list.size()), financialAchieved};
                return objects;
            }
            case 76 -> { // 76 Corpus-Listing on NSE from corpusListingOnNSE
                Optional<List<ListingOnNSE>> corpusListingOnNSE = listingOnNSERepository.findByNonTrainingSubActivity_SubActivityId(subActivityId);
                if (corpusListingOnNSE.isPresent()) {
                    List<ListingOnNSE> listingOnNSES = corpusListingOnNSE.get();
                    Double financialAchieved = listingOnNSES.stream().mapToDouble(r -> r.getAmountOfLoanProvided()).sum();
                    Object[] objects = {String.valueOf(listingOnNSES.size()), financialAchieved};
                    return objects;
                } else {
                    Object[] objects = {String.valueOf(0), 0.0};
                    return objects;
                }
            }
            /*
            Resource table
             COI  ->  26 : Staff
             CODE ->  14 : Staff - CEO, 15 : Staff - Designers, 16 : Staff - Designers, 17 : Staff - Project Manager,
                      74 : Interns for certifications, 75 : R&D,
             */
            case 26,14,15,16,17,74 -> { //
                List<NonTrainingResource> nonTrainingSubActivity = nonTrainingResourcesRepository.findByNonTrainingSubActivity_subActivityId(subActivityId);
                if (nonTrainingSubActivity != null || !nonTrainingSubActivity.isEmpty()) {
                    Double financialAchieved = nonTrainingSubActivity.stream().map(
                            r -> r.getNonTrainingResourceExpenditures()).mapToDouble(
                            exp -> exp.stream().mapToDouble(
                                    resExp -> resExp.getAmount()).sum()).sum();
                    Object[] objects = {String.valueOf(nonTrainingSubActivity.size()), financialAchieved};
                    return objects;
                } else {
                    Object[] objects = {String.valueOf(0), 0.0};
                    return objects;
                }
            }

            /*
            Expenditure Table
                COI  ->  27 : Technology firm, 28 : Staff - Call center Agency
                CODE ->  73 : IT Infrastructure Setup
             */
            case 27,28,73 -> { //
                Optional<List<NonTrainingExpenditure>> nonTrainingSubActivity = nonTrainingExpenditureRepository.findByNonTrainingSubActivity_SubActivityId(subActivityId);
                if (nonTrainingSubActivity.isPresent()) {
                    Double financialAchieved = nonTrainingSubActivity.get().stream().mapToDouble(exp -> exp.getExpenditureAmount()).sum();
                    Object[] objects = {String.valueOf(nonTrainingSubActivity.get().size()), financialAchieved};
                    return objects;
                } else {
                    Object[] objects = {String.valueOf(0), 0.0};
                    return objects;
                }
            }
            /*
              CODE -> 19 : Travel - IITH
             */
            case 19 ->{
                List<TravelAndTransport> nonTrainingSubActivity = travelAndTransportRepository.findByNonTrainingSubActivity_SubActivityId(subActivityId);
                    Double financialAchieved = nonTrainingSubActivity.stream().mapToDouble(exp -> exp.getAmount()).sum();
                    Object[] targetAndFinancialAchieved = {String.valueOf(nonTrainingSubActivity.size()), financialAchieved};
                    return targetAndFinancialAchieved;
            }
        }
        return new Object[]{String.valueOf(0), 0.0};
    }
}
/*
18		Consumable : not done from code
 */