package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.ProgramMonitoring.service.SubActivityParticipantCountDTO;
import com.metaverse.workflow.activity.repository.ActivityRepository;
import com.metaverse.workflow.enums.BillRemarksStatus;
import com.metaverse.workflow.expenditure.repository.ProgramExpenditureRepository;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.nontraining.dto.NonTrainingProgramDto;
import com.metaverse.workflow.nontraining.dto.ProgressMonitoringDto;
import com.metaverse.workflow.nontraining.dto.TrainingProgramDto;
import com.metaverse.workflow.nontraining.repository.NonTrainingAchievementRepository;
import com.metaverse.workflow.nontraining.repository.NonTrainingActivityRepository;
import com.metaverse.workflow.nontrainingExpenditures.Dto.CorpusDebitFinancing;
import com.metaverse.workflow.nontrainingExpenditures.repository.*;
import com.metaverse.workflow.nontrainingExpenditures.service.WeHubService;
import com.metaverse.workflow.trainingandnontrainingtarget.repository.NonTrainingTargetRepository;
import com.metaverse.workflow.trainingandnontrainingtarget.repository.TrainingTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    private final NIMSMEVendorDetailsRepository nimsmeVendorDetailsRepository;
    private final NIMSMEContentDetailsRepository nimsmeContentDetailsRepository;

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
                            case "staff - ceo", "staff - designers", "staff - project manager",
                                 "interns for certifications", "r&d" -> {
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
        Map<Long, Double> programExpApproved = new HashMap<>();
        Map<Long, Double> programExpPending = new HashMap<>();

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

        List<TrainingTargets> allById = trainingTargetRepository.findBySubActivity_SubActivityIdInAndAgency_AgencyId(longs, agencyId);
        for (TrainingTargets targets : allById) {
            Long subActivityId = targets.getSubActivity().getSubActivityId();
            long total = targets.getQ1Target() + targets.getQ2Target() + targets.getQ3Target() + targets.getQ4Target();
            target.merge(subActivityId, total, Long::sum);
        }
        List<SubActivityParticipantCountDTO> participantCountByAgencyId = activityRepository.findProgramCountByAgencyId(agencyId);

        for (SubActivityParticipantCountDTO dto : participantCountByAgencyId) {
            countOfParticipants.put(dto.getSubActivityId(), Long.valueOf(dto.getParticipantCount()));
        }

        Map<Long, Double> budgetAllocated = new HashMap<>();
        for (TrainingTargets targets : allById) {
            Double ifTargetPresent = budgetAllocated.getOrDefault(targets.getSubActivity().getSubActivityId(), 0.0);
            budgetAllocated.put(targets.getSubActivity().getSubActivityId(), (targets.getQ1Budget() + targets.getQ2Budget() + targets.getQ3Budget() + targets.getQ4Budget()) + ifTargetPresent);
        }

        Iterable<ProgramExpenditure> bySubActivitySubActivityId = programExpenditureRepository.findBySubActivity_SubActivityIdInAndAgency_AgencyId(subActivityNames.keySet(), agencyId);

        for (ProgramExpenditure p : bySubActivitySubActivityId) {
            if(p.getStatus() != null){
                if(BillRemarksStatus.APPROVED.equals(p.getStatus()))
                    addOrUpdate(programExpApproved, p.getSubActivity().getSubActivityId(), p.getCost());
                else
                    addOrUpdate(programExpPending, p.getSubActivity().getSubActivityId(), p.getCost());
            }else {
                addOrUpdate(programExpPending, p.getSubActivity().getSubActivityId(), p.getCost());
            }
        }

        Set<Long> commonKeys = new HashSet<>(subActivityNames.keySet());
        for (Long key : commonKeys) {
            Double target1 = Double.valueOf(target.getOrDefault(key, 0L));
            Long countOfParticipants1 = countOfParticipants.getOrDefault(key, 0L);

            Double bud = budgetAllocated.getOrDefault(key, 0.0);
            Double programExpApproved0 = programExpApproved.getOrDefault(key, 0.0);
            Double programExpPending0 = programExpPending.getOrDefault(key, 0.0);

            trainingProgressData.add(TrainingProgramDto.builder().agency(agencyName)
                    .activity(activityNames.get(key))
                    .subActivityId(key)
                    .subActivity(subActivityNames.get(key))
                    .trainingTarget(target1)
                    .trainingAchievement(countOfParticipants1)
                    .trainingPercentage((target1 != 0 ? Math.round((countOfParticipants1 / target1) * 100 * 1000.0) / 1000.0 : 0.0))
                    .budgetAllocated(bud)
                    .approvedExpenditure(Math.floor(programExpApproved0))
                    .pendingExpenditure(Math.floor(programExpPending0))
                    .expenditure(Math.floor(programExpApproved0+programExpPending0))
                    .expenditurePercentage((bud != 0.0) ? Math.round(((programExpApproved0+programExpPending0) / bud) * 100 * 1000.0) / 1000.0 : 0.0)
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
            long activityId = activity.getActivityId();

            switch ((int) activityId) {
                case 13, 16, 17, 18, 22, 23, 24, 25, 27, 30, 31, 6, 8, 9, 11, 12, 20, 21, 106, 1, 2, 3, 4, 5, 32, 33,
                     34, 35, 36, 37, 28, 29, 107 -> {
                    List<NonTrainingSubActivity> subActivities = activity.getSubActivities();

                    for (NonTrainingSubActivity subActivity : subActivities) {
                        NonTrainingProgramDto nonTrainingProgramDto = new NonTrainingProgramDto();
                        nonTrainingProgramDto.setNonTrainingActivity(activity.getActivityName());
                        nonTrainingProgramDto.setNonTrainingSubActivity(subActivity.getSubActivityName());

                        long subActivityId = subActivity.getSubActivityId();

                        List<NonTrainingTargets> nonTrainingSubActivity = nonTrainingTargetRepository.findByNonTrainingSubActivity_subActivityId(subActivityId);
                        long physicalTarget = nonTrainingSubActivity.stream().mapToLong(r -> r.getQ1Target() + r.getQ2Target() + r.getQ3Target() + r.getQ4Target()).sum();
                        double financialTarget = nonTrainingSubActivity.stream().mapToDouble(r -> r.getQ1Budget() + r.getQ2Budget() + r.getQ3Budget() + r.getQ4Budget()).sum();
                        nonTrainingProgramDto.setPhysicalTarget((int) physicalTarget);
                        nonTrainingProgramDto.setFinancialTarget(financialTarget);

                        Object[] objects = progressMonitoringSubActivityWiseAchievements(subActivityId);
                        nonTrainingProgramDto.setPhysicalAchievement((String) objects[0]);
                        double approved = (double) objects[1];
                        double pending = (double) objects[2];
                        nonTrainingProgramDto.setFinancialExpenditure(approved+pending);
                        nonTrainingProgramDto.setFinancialExpenditurePending(pending);
                        nonTrainingProgramDto.setFinancialExpenditureApproved(approved);
                        nonTrainingProgramDto.setPercentage((financialTarget != 0.0) ? Math.round(((approved+pending) / financialTarget) * 100 * 100.0) / 100.0 : 0.0);
                        try {
                            nonTrainingProgramDto.setPhysicalPercentage(String.valueOf(Math.round(((physicalTarget != 0.0) ? (Double.parseDouble((String) objects[0]) / physicalTarget) * 100 : 0.0) * 100.0) / 100.0));
                        } catch (Exception ee) {
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
                Object[] objects = {String.valueOf(list.size()), financialAchieved,0.0};
                return objects;
            }
            case 76 -> { // 76 Corpus-Listing on NSE from corpusListingOnNSE
                Optional<List<ListingOnNSE>> corpusListingOnNSE = listingOnNSERepository.findByNonTrainingSubActivity_SubActivityId(subActivityId);
                if (corpusListingOnNSE.isPresent()) {
                    List<ListingOnNSE> listingOnNSES = corpusListingOnNSE.get();
                    Double financialAchieved = listingOnNSES.stream().mapToDouble(r -> r.getAmountOfLoanProvided()).sum();
                    Object[] objects = {String.valueOf(listingOnNSES.size()), financialAchieved,0.0};
                    return objects;
                } else {
                    Object[] objects = {String.valueOf(0), 0.0,0.0};
                    return objects;
                }
            }
            /*
            Resource table
             COI     ->  26  : Staff
             CODE    ->  14  : Staff - CEO, 15 : Staff - Designers, 16 : Staff - Designers, 17 : Staff - Project Manager,
                         74  : Interns for certifications, 75 : R&D,
             RICH_6B ->  125 : Conducting study & dashboards
             CITD    ->  6   : RAMP Team Salaries
             NIMSME  ->  82  : Staff (EDC Managers), 84 : Training on Thematic Areas - EDC Asst Managers
             ALEAP   ->  72  : Human Resource

             */
            case 26, 14, 15, 16, 17, 74, 125, 6, 62, 82, 84, 72 -> { //
                List<NonTrainingResource> nonTrainingSubActivity =
                        nonTrainingResourcesRepository.findByNonTrainingSubActivity_subActivityId(subActivityId);

                if (nonTrainingSubActivity != null && !nonTrainingSubActivity.isEmpty()) {
                    Map<String, Double> result = nonTrainingSubActivity.stream()
                            .flatMap(r -> r.getNonTrainingResourceExpenditures().stream())
                            .collect(Collectors.groupingBy(
                                    exp -> exp.getStatus() == BillRemarksStatus.APPROVED ? "APPROVED" : "OTHER",
                                    Collectors.summingDouble(NonTrainingResourceExpenditure::getAmount)
                            ));

                    double approved = result.getOrDefault("APPROVED", 0.0);
                    double pending = result.getOrDefault("OTHER", 0.0);

                    String physicalAchievement = nonTrainingAchievementRepository.findByNonTrainingSubActivity_SubActivityId(subActivityId).map(d -> d.getPhysicalTargetAchievement()).orElse("0");
                    Object[] objects = {physicalAchievement, approved, pending};
                    return objects;
                } else {
                    Object[] objects = {"0", 0.0, 0.0, 0.0};
                    return objects;
                }
            }
            /*
            WEHUB  -> 63  : Single Window Platform
            NIMSME -> 79  : LMS, 80 : CMS, 81 : Website Development / Virtual EDC / Smart Search
            ALEAP  -> 69  : Dashboard/ Central Management System
            */
            case 63, 79, 80, 81, 69 -> { //
                List<NIMSMEVendorDetails> nonTrainingSubActivity =
                        nimsmeVendorDetailsRepository.findByNonTrainingSubActivity_subActivityId(subActivityId);

                if (nonTrainingSubActivity != null && !nonTrainingSubActivity.isEmpty()) {

                    Optional<List<NonTrainingExpenditure>> byNonTrainingSubActivity =
                            nonTrainingExpenditureRepository.findByNonTrainingSubActivity_SubActivityId(subActivityId);
                    double approved = 0.0;
                    double pending = 0.0;

                    if (byNonTrainingSubActivity.isPresent()) {

                        List<NonTrainingExpenditure> expenditureList = byNonTrainingSubActivity.get();
                        // Group APPROVED vs OTHER
                        Map<String, Double> result = expenditureList.stream()
                                .collect(Collectors.groupingBy(
                                        exp -> exp.getStatus() == BillRemarksStatus.APPROVED ? "APPROVED" : "OTHER",
                                        Collectors.summingDouble(NonTrainingExpenditure::getExpenditureAmount)
                                ));

                        approved = result.getOrDefault("APPROVED", 0.0);
                        pending = result.getOrDefault("OTHER", 0.0);
                    }
                    String physicalAchievement = nonTrainingAchievementRepository.findByNonTrainingSubActivity_SubActivityId(subActivityId).map(d -> d.getPhysicalTargetAchievement()).orElse("0");
                    Object[] objects = {physicalAchievement, approved, pending};                    return objects;
                } else {
                    Object[] objects = {"0", 0.0, 0.0, 0.0};
                    return objects;
                }
            }
            /*
             NIMSME -> 77 : Video, 78 : Documents
            */
            case 77, 78 -> { //
                List<NIMSMEContentDetails> nonTrainingSubActivity =
                        nimsmeContentDetailsRepository.findByNonTrainingSubActivity_subActivityId(subActivityId);

                if (nonTrainingSubActivity != null && !nonTrainingSubActivity.isEmpty()) {

                    Optional<List<NonTrainingExpenditure>> byNonTrainingSubActivity =
                            nonTrainingExpenditureRepository.findByNonTrainingSubActivity_SubActivityId(subActivityId);

                    double approved = 0.0;
                    double pending = 0.0;

                    if (byNonTrainingSubActivity.isPresent()) {

                        List<NonTrainingExpenditure> expenditureList = byNonTrainingSubActivity.get();
                        Map<String, Double> result = expenditureList.stream()
                                .collect(Collectors.groupingBy(
                                        exp -> exp.getStatus() == BillRemarksStatus.APPROVED ? "APPROVED" : "OTHER",
                                        Collectors.summingDouble(NonTrainingExpenditure::getExpenditureAmount)
                                ));

                        approved = result.getOrDefault("APPROVED", 0.0);
                        pending = result.getOrDefault("OTHER", 0.0);
                    }

                    String physicalAchievement = nonTrainingAchievementRepository.findByNonTrainingSubActivity_SubActivityId(subActivityId).map(d -> d.getPhysicalTargetAchievement()).orElse("0");
                    Object[] objects = {physicalAchievement, approved, pending};                    return objects;
                } else {

                    Object[] objects = {"0",0.0, 0.0};
                    return objects;
                }
            }

            /*
            Expenditure Table
                COI     ->  27  : Technology firm, 28 : Staff - Call center Agency, 132,133
                CODE    ->  73  : IT Infrastructure Setup
                CITD    ->  4   : Administration Exp,  90 : Operating Expenses
                CIPET   ->  12  : Other Administrative / Operating Expenses
                WEHUB   ->  38  : Training Kit for ToT,	39 : Training Manuals, 	40 : Curriculum and Course Material, 64	: RFPs - Tendering Processes & Miscellaneous Expn
                            37  : Admin Cost
                ALEAP   ->  128 : Miscellaneous
                TGTPC-4 ->  110 : Admin Cost , Logistic
                TGTPC-10->  124 : Admn Cost including logistics etc.
            */
            case 27, 28, 73, 90, 12, 38, 39, 40, 64, 37, 110, 124 ,132, 133-> { //
                Optional<List<NonTrainingExpenditure>> nonTrainingSubActivity = nonTrainingExpenditureRepository.findByNonTrainingSubActivity_SubActivityId(subActivityId);
                if (nonTrainingSubActivity.isPresent()) {
                    Map<String, Double> result =
                            nonTrainingSubActivity.get().stream()
                                    .collect(Collectors.groupingBy(
                                            p -> p.getStatus() == BillRemarksStatus.APPROVED ? "APPROVED" : "OTHER",
                                            Collectors.summingDouble(NonTrainingExpenditure::getExpenditureAmount)
                                    ));
                         Double approved = result.getOrDefault("APPROVED",0.0);
                         Double pending = result.getOrDefault("OTHER",0.0);

                    String physicalAchievement = nonTrainingAchievementRepository.findByNonTrainingSubActivity_SubActivityId(subActivityId).map(d -> d.getPhysicalTargetAchievement()).orElse("0");
                    Object[] objects = {physicalAchievement, approved, pending};
                    return objects;
                } else {
                    Object[] objects = {String.valueOf(0), 0.0,0.0};
                    return objects;
                }
            }
            /*
              CODE -> 19 : Travel - IITH
             */
            case 19 -> {
                List<TravelAndTransport> nonTrainingSubActivity = travelAndTransportRepository.findByNonTrainingSubActivity_SubActivityId(subActivityId);
                if (!nonTrainingSubActivity.isEmpty() && nonTrainingSubActivity != null) {
                    Map<String, Double> result =
                            nonTrainingSubActivity.stream()
                                    .collect(Collectors.groupingBy(
                                            p -> p.getStatus() == BillRemarksStatus.APPROVED ? "APPROVED" : "OTHER",
                                            Collectors.summingDouble(TravelAndTransport::getAmount)
                                    ));
                    Double approved = result.getOrDefault("APPROVED",0.0);
                    Double pending = result.getOrDefault("OTHER",0.0);

                    String physicalAchievement = nonTrainingAchievementRepository.findByNonTrainingSubActivity_SubActivityId(subActivityId).map(d -> d.getPhysicalTargetAchievement()).orElse("0");
                    Object[] objects = {physicalAchievement, approved, pending};
                    return objects;
                } else {
                    Object[] objects = {String.valueOf(0), 0.0,0.0};
                    return objects;
                }
            }
            default -> {
                return new Object[]{String.valueOf(0), 0.0,0.0};
            }
        }
    }


    @Override
    public List<TrainingProgramDto> getAllTrainingProgress(Long agencyId) {

        List<TrainingProgramDto> trainingProgressData = new ArrayList<>();

        // Maps for names
        Map<Long, String> activityNames = new HashMap<>();
        Map<Long, String> subActivityNames = new HashMap<>();
        Map<Long, Long> subActivityToAgency = new HashMap<>();
        Map<Long, String> agencyNames = new HashMap<>();

        // Maps for data (subActivity-wise)
        Map<Long, Long> target = new HashMap<>();
        Map<Long, Long> countOfParticipants = new HashMap<>();
        Map<Long, Double> programExp = new HashMap<>();
        Map<Long, Double> budgetAllocated = new HashMap<>();

        // Fetch activities
        List<Activity> activities;
        if (agencyId == -1) {
            activities = activityRepository.findAll();
        } else {
            activities = activityRepository.findByAgencyAgencyId(agencyId);
        }

        if (activities.isEmpty()) {
            return trainingProgressData;
        }

        // Build base maps
        for (Activity activity : activities) {
            String agencyName = activity.getAgency().getAgencyName();
            Long agencyIdVal = activity.getAgency().getAgencyId();

            for (SubActivity sb : activity.getSubActivities()) {
                subActivityNames.put(sb.getSubActivityId(), sb.getSubActivityName());
                activityNames.put(sb.getSubActivityId(), activity.getActivityName());
                subActivityToAgency.put(sb.getSubActivityId(), agencyIdVal);
                agencyNames.put(agencyIdVal, agencyName);
            }
        }

        Set<Long> subActivityIds = subActivityNames.keySet();
        if (subActivityIds.isEmpty()) {
            return trainingProgressData;
        }

        // Training targets
        List<TrainingTargets> allTargets;
        if (agencyId == -1) {
            allTargets = trainingTargetRepository.findBySubActivity_SubActivityIdIn(subActivityIds);
        } else {
            allTargets = trainingTargetRepository.findBySubActivity_SubActivityIdInAndAgency_AgencyId(subActivityIds, agencyId);
        }

        for (TrainingTargets t : allTargets) {
            Long subActivityId = t.getSubActivity().getSubActivityId();
            long total = t.getQ1Target() + t.getQ2Target() + t.getQ3Target() + t.getQ4Target();
            target.merge(subActivityId, total, Long::sum);

            double totalBudget = t.getQ1Budget() + t.getQ2Budget() + t.getQ3Budget() + t.getQ4Budget();
            budgetAllocated.merge(subActivityId, totalBudget, Double::sum);
        }

        // Participant counts
        List<SubActivityParticipantCountDTO> participantCounts;
        if (agencyId == -1) {
            participantCounts = activityRepository.findProgramCountForAllAgencies();
        } else {
            participantCounts = activityRepository.findProgramCountByAgencyId(agencyId);
        }

        for (SubActivityParticipantCountDTO dto : participantCounts) {
            countOfParticipants.put(dto.getSubActivityId(), (long) dto.getParticipantCount());
        }

        // Expenditure
        Iterable<ProgramExpenditure> expenditures;
        if (agencyId == -1) {
            expenditures = programExpenditureRepository.findBySubActivity_SubActivityIdIn(subActivityIds);
        } else {
            expenditures = programExpenditureRepository.findBySubActivity_SubActivityIdInAndAgency_AgencyId(subActivityIds, agencyId);
        }

        for (ProgramExpenditure p : expenditures) {
            programExp.merge(p.getSubActivity().getSubActivityId(), p.getCost(), Double::sum);
        }

        // 🔹 Aggregate all subActivity-level data into activity+agency-level
        Map<String, AggregatedActivityData> activitySummary = new HashMap<>();

        for (Long subActId : subActivityIds) {
            String activityName = activityNames.get(subActId);
            Long agId = subActivityToAgency.get(subActId);
            String agencyName = agencyNames.get(agId);

            if (activityName == null || agencyName == null) continue;

            // Use "agencyName|activityName" as unique key
            String key = agencyName + "|" + activityName;
            AggregatedActivityData data = activitySummary.computeIfAbsent(key, k -> new AggregatedActivityData(agencyName, activityName));

            data.totalTarget += target.getOrDefault(subActId, 0L);
            data.totalAchievement += countOfParticipants.getOrDefault(subActId, 0L);
            data.totalBudget += budgetAllocated.getOrDefault(subActId, 0.0);
            data.totalExpenditure += programExp.getOrDefault(subActId, 0.0);
        }

        // Build final DTO list (activity + agency wise)
        for (AggregatedActivityData data : activitySummary.values()) {
            double targetVal = data.totalTarget;
            double achievementVal = data.totalAchievement;

            double trainingPercentage = (targetVal != 0)
                    ? Math.round((achievementVal / targetVal) * 100 * 1000.0) / 1000.0
                    : 0.0;

            double expPercentage = (data.totalBudget != 0)
                    ? Math.round((data.totalExpenditure / data.totalBudget) * 100 * 1000.0) / 1000.0
                    : 0.0;

            trainingProgressData.add(TrainingProgramDto.builder()
                    .agency(data.agencyName)
                    .activity(data.activityName)
                    .trainingTarget(targetVal)
                    .trainingAchievement((long) achievementVal)
                    .trainingPercentage(trainingPercentage)
                    .budgetAllocated(data.totalBudget)
                    .expenditure(Math.floor(data.totalExpenditure))
                    .expenditurePercentage(expPercentage)
                    .build());
        }

        return trainingProgressData;
    }

    // Helper inner class for aggregation
    static class AggregatedActivityData {
        String agencyName;
        String activityName;
        long totalTarget = 0L;
        long totalAchievement = 0L;
        double totalBudget = 0.0;
        double totalExpenditure = 0.0;

        public AggregatedActivityData(String agencyName, String activityName) {
            this.agencyName = agencyName;
            this.activityName = activityName;
        }
    }


}
/*
18		Consumable : not done from code
Not Done SubActivities
CITD : Final Report Submission,Visit to MSMEs of respective Clusters,
       Benchmark Study of latest Technology
CIPET :
    10		Visit to MSMEs of respective Clusters
    11		RAMPExclusive Team Hiring Expenses
    13		Benchmark Study of latest Technology
    92		Final Report Submission
    93		TA / DA Charges for Manpower visiting for Units
WEHUB
	61		Online MooCs sessions- Online MooCs sessions
 */