package com.metaverse.workflow.MoMSMEReport.service;

import com.metaverse.workflow.activity.repository.SubActivityRepository;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.expenditure.repository.BulkExpenditureTransactionRepository;
import com.metaverse.workflow.expenditure.repository.ProgramExpenditureRepository;
import com.metaverse.workflow.login.service.AuthRequest;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.nontraining.repository.NonTrainingActivityRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.ListingOnNSERepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingResourceRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import com.metaverse.workflow.program.repository.ProgramRepository;
import com.metaverse.workflow.trainingandnontrainingtarget.repository.NonTrainingTargetRepository;
import com.metaverse.workflow.trainingandnontrainingtarget.repository.TrainingTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TiHclMoMSMEReportService {

    private final TrainingTargetRepository trainingTargetRepository;
    private final ProgramRepository programRepository;
    private final ProgramExpenditureRepository programExpenditureRepository;
    private final BulkExpenditureTransactionRepository transactionRepository;
    private final SubActivityRepository subActivityRepository;
    private final NonTrainingSubActivityRepository nonTrainingSubActivityRepository;
    private final NonTrainingTargetRepository nonTrainingTargetRepository;
    private final ListingOnNSERepository listingOnNSERepository;


    @Value("${tihcl.username}")
    private String userName;

    @Value("${tihcl.password}")
    private String password;

    public TiHclMoMSMEReportDTO fetchMoMsmeReport() {

        RestTemplate restTemplate = new RestTemplate();

//        String url = "http://localhost:8080/tihcl/api/tihcl-momsme-report-dto";
//        String loginUrl = "http://localhost:8080/tihcl/api/auth/login";
        String url = "https://tihcl.com/tihcl/api/tihcl/tihcl-momsme-report-dto";
        String loginUrl = "https://tihcl.com/tihcl/api/auth/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AuthRequest> entity =
                new HttpEntity<>(new AuthRequest(userName, password), headers);

        try {
            // Login and get token
            ResponseEntity<Map> response = restTemplate.exchange(
                    loginUrl,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            String token = (String) response.getBody().get("token");

            // Set Authorization header
            HttpHeaders authHeaders = new HttpHeaders();
            authHeaders.setBearerAuth(token);

            HttpEntity<Void> requestEntity = new HttpEntity<>(authHeaders);

            // Fetch MOMSME report
            ResponseEntity<TiHclMoMSMEReportDTO> reportResponse =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            requestEntity,
                            new ParameterizedTypeReference<TiHclMoMSMEReportDTO>() {
                            }
                    );

            return reportResponse.getBody();

        } catch (RestClientException e) {
            System.err.println(e.getMessage());
            return new TiHclMoMSMEReportDTO();
        }
    }

    public MoMSMERowDTO getMoMSMERowData(Long subActivityId, String activityType) throws DataException {

        MoMSMERowDTO dto = new MoMSMERowDTO();
        LocalDate last25 = LocalDate.now().minusMonths(1).withDayOfMonth(25);
        Date startDate = java.sql.Date.valueOf(last25);
        Date endDate = new Date();

        switch (activityType) {

            case "Training": {

                SubActivity subActivity = subActivityRepository.findById(subActivityId)
                        .orElseThrow(() -> new DataException("sub activity not found", "NOT-FOUND", 400));

                Long activityId = subActivity.getActivity().getActivityId();
                Long agencyId = subActivity.getActivity().getAgency().getAgencyId();

                List<Program> programs = programRepository.findBySubActivityId(subActivityId);

                List<ProgramExpenditure> expList =
                        programExpenditureRepository.findBySubActivity_SubActivityIdAndAgency_AgencyId(subActivityId, agencyId);

                List<BulkExpenditureTransaction> bulkList =
                        transactionRepository.findByProgram_ProgramIdIn(
                                programs.stream().map(Program::getProgramId).toList()
                        );

                List<TrainingTargets> targetsList =
                        trainingTargetRepository.findBySubActivity_SubActivityId(subActivityId);

                // EXPENDITURE
                Double expUptoLast = expList.stream()
                        .filter(e -> e.getCreatedOn() != null && e.getCreatedOn().before(startDate))
                        .mapToDouble(ProgramExpenditure::getCost)
                        .sum();

                Double expDuringMonth = expList.stream()
                        .filter(e -> e.getCreatedOn() != null &&
                                !e.getCreatedOn().before(startDate) &&
                                !e.getCreatedOn().after(endDate))
                        .mapToDouble(ProgramExpenditure::getCost)
                        .sum();

                Double bulkUptoLast = bulkList.stream()
                        .filter(b -> b.getCreatedOn() != null && b.getCreatedOn().before(startDate))
                        .mapToDouble(BulkExpenditureTransaction::getAllocatedCost)
                        .sum();

                Double bulkDuringMonth = bulkList.stream()
                        .filter(b -> b.getCreatedOn() != null &&
                                !b.getCreatedOn().before(startDate) &&
                                !b.getCreatedOn().after(endDate))
                        .mapToDouble(BulkExpenditureTransaction::getAllocatedCost)
                        .sum();

                Double totalBulk = bulkList.stream()
                        .mapToDouble(BulkExpenditureTransaction::getAllocatedCost)
                        .sum();

                Double totalExp = expList.stream()
                        .mapToDouble(ProgramExpenditure::getCost)
                        .sum();

                // ACHIEVEMENT COUNTS
                int lastMonthAch = programRepository
                        .findProgramsUptoLastMonth25th(agencyId, subActivityId).size();

                int currMonthAch = programRepository
                        .findProgramsFromLastMonth25ToCurrent(agencyId, subActivityId).size();

                // MSME BENEFITED
                int msmeBenefited = (int) programs.stream()
                        .flatMap(p -> p.getParticipants().stream())
                        .filter(pa -> pa.getOrganization() != null)
                        .count();

                // TARGET & BUDGET
                long totalTarget = targetsList.stream()
                        .mapToLong(t -> t.getQ1Target() + t.getQ2Target() + t.getQ3Target() + t.getQ4Target())
                        .sum();

                Double approvedBudget = targetsList.stream()
                        .mapToDouble(t -> t.getQ1Budget() + t.getQ2Budget() + t.getQ3Budget() + t.getQ4Budget())
                        .sum();

                // PERCENTAGE FIXED (avoiding integer division)
                String percentTarget = totalTarget == 0 ? "0%" :
                        String.format("%.1f%%", (programs.size() * 100.0) / totalTarget);

                String percentBudget = approvedBudget == 0 ? "0%" :
                        String.format("%.1f%%", ((totalBulk + totalExp) * 100.0) / approvedBudget);

                dto.setActivityName(subActivity.getActivity().getActivityName());
                dto.setSubActivityName(subActivity.getSubActivityName());
                dto.setTotalTarget(totalTarget);
                dto.setAchievementLastMonth(lastMonthAch);
                dto.setAchievementDuringMonth(currMonthAch);
                dto.setCumulativeAchievement(programs.size());
                dto.setAchievementPercentage(percentTarget);
                dto.setMsmesBenefited(msmeBenefited);
                dto.setAmountApproved(approvedBudget);
                dto.setExpenditureLastMonth(expUptoLast + bulkUptoLast);
                dto.setExpenditureDuringMonth(expDuringMonth + bulkDuringMonth);
                dto.setCumulativeExpenditure(totalBulk + totalExp);
                dto.setExpenditurePercentage(percentBudget);
                return dto;
            }

            case "NonTraining": {

                TiHclMoMSMEReportDTO tiDto = fetchMoMsmeReport();

                NonTrainingSubActivity subActivity = nonTrainingSubActivityRepository.findById(subActivityId)
                        .orElseThrow(() ->
                                new DataException("non training sub activity not found", "NOT-FOUND", 400));

                List<NonTrainingTargets> targetsList =
                        nonTrainingTargetRepository.findByNonTrainingSubActivity_SubActivityId(subActivityId);

                long totalTarget = targetsList.stream()
                        .mapToLong(t -> t.getQ1Target() + t.getQ2Target() + t.getQ3Target() + t.getQ4Target())
                        .sum();

                Double approvedBudget = targetsList.stream()
                        .mapToDouble(t -> t.getQ1Budget() + t.getQ2Budget() + t.getQ3Budget() + t.getQ4Budget())
                        .sum();

                switch (subActivityId.toString()) {

                    // CASE 67
                    case "67": {

                        int val = Integer.parseInt(tiDto.getNoOfMsmesExtendedDebtFinance().toString());
                        Double disbursed = tiDto.getDisbursedAmount();

                        dto.setActivityName(subActivity.getNonTrainingActivity().getActivityName());
                        dto.setSubActivityName(subActivity.getSubActivityName());
                        dto.setTotalTarget(totalTarget);
                        dto.setAchievementLastMonth(val);
                        dto.setAchievementDuringMonth(0);
                        dto.setCumulativeAchievement(val);
                        dto.setAchievementPercentage(
                                totalTarget == 0 ? "0%" : String.format("%.1f%%", (val * 100.0) / totalTarget)
                        );
                        dto.setMsmesBenefited(val);
                        dto.setAmountApproved(approvedBudget);
                        dto.setExpenditureLastMonth(disbursed);
                        dto.setExpenditureDuringMonth(0.0);
                        dto.setCumulativeExpenditure(disbursed);
                        dto.setExpenditurePercentage(
                                approvedBudget == 0 ? "0%" : String.format("%.1f%%", (disbursed * 100.0) / approvedBudget)
                        );
                        return dto;
                    }

                    // CASE 76
                    case "76": {

                        List<ListingOnNSE> listingOnNSEList =
                                listingOnNSERepository.findByNonTrainingSubActivity_SubActivityId(subActivityId)
                                        .orElseThrow(() ->
                                                new DataException("ListingOnNSE not found for subActivityId: " + subActivityId,
                                                        "LISTING_ON_NSE_NOT_FOUND", 404));

                        Double expUptoLast = listingOnNSEList.stream()
                                .filter(e -> e.getDateOfListing() != null && e.getDateOfListing().before(startDate))
                                .mapToDouble(ListingOnNSE::getAmountOfLoanProvided)
                                .sum();

                        Double expDuringMonth = listingOnNSEList.stream()
                                .filter(e -> e.getCreatedOn() != null &&
                                        !e.getCreatedOn().before(startDate) &&
                                        !e.getCreatedOn().after(endDate))
                                .mapToDouble(ListingOnNSE::getAmountOfLoanProvided)
                                .sum();

                        Double totalAmount = listingOnNSEList.stream()
                                .mapToDouble(ListingOnNSE::getAmountOfLoanProvided)
                                .sum();

                        dto.setActivityName(subActivity.getNonTrainingActivity().getActivityName());
                        dto.setSubActivityName(subActivity.getSubActivityName());
                        dto.setTotalTarget(totalTarget);
                        dto.setAchievementLastMonth(listingOnNSEList.size());
                        dto.setAchievementDuringMonth(0);
                        dto.setCumulativeAchievement(listingOnNSEList.size());
                        dto.setAchievementPercentage(
                                totalTarget == 0 ? "0%" :
                                        String.format("%.1f%%", (listingOnNSEList.size() * 100.0) / totalTarget)
                        );
                        dto.setMsmesBenefited(listingOnNSEList.size());
                        dto.setAmountApproved(approvedBudget);
                        dto.setExpenditureLastMonth(expUptoLast);
                        dto.setExpenditureDuringMonth(expDuringMonth);
                        dto.setCumulativeExpenditure(totalAmount);
                        dto.setExpenditurePercentage(
                                approvedBudget == 0 ? "0%" :
                                        String.format("%.1f%%", (totalAmount * 100.0) / approvedBudget)
                        );
                        return dto;
                    }

                    // CASE 129
                    case "129": {

                        int val = Integer.parseInt(tiDto.getNoOfApplicationsReceivedFromMsmes().toString());

                        dto.setActivityName(subActivity.getNonTrainingActivity().getActivityName());
                        dto.setSubActivityName(subActivity.getSubActivityName());
                        dto.setTotalTarget(0L);
                        dto.setAchievementLastMonth(val);
                        dto.setAchievementDuringMonth(0);
                        dto.setCumulativeAchievement(val);
                        dto.setAchievementPercentage("0%");
                        dto.setMsmesBenefited(0);
                        dto.setAmountApproved(0.0);
                        dto.setExpenditureLastMonth(0.0);
                        dto.setExpenditureDuringMonth(0.0);
                        dto.setCumulativeExpenditure(0.0);
                        dto.setExpenditurePercentage("0%");
                        return dto;
                    }

                    // CASE 130
                    case "130": {

                        int val = Integer.parseInt(tiDto.getNoOfApplicationsShortlisted().toString());

                        dto.setActivityName(subActivity.getNonTrainingActivity().getActivityName());
                        dto.setSubActivityName(subActivity.getSubActivityName());
                        dto.setTotalTarget(0L);
                        dto.setAchievementLastMonth(val);
                        dto.setAchievementDuringMonth(0);
                        dto.setCumulativeAchievement(val);
                        dto.setAchievementPercentage("0%");
                        dto.setMsmesBenefited(0);
                        dto.setAmountApproved(0.0);
                        dto.setExpenditureLastMonth(0.0);
                        dto.setExpenditureDuringMonth(0.0);
                        dto.setCumulativeExpenditure(0.0);
                        dto.setExpenditurePercentage("0%");
                        return dto;
                    }

                    // CASE 131
                    case "131": {

                        int val = Integer.parseInt(tiDto.getNoOfUnitsVisited().toString());

                        dto.setActivityName(subActivity.getNonTrainingActivity().getActivityName());
                        dto.setSubActivityName(subActivity.getSubActivityName());
                        dto.setTotalTarget(0L);
                        dto.setAchievementLastMonth(val);
                        dto.setAchievementDuringMonth(0);
                        dto.setCumulativeAchievement(val);
                        dto.setAchievementPercentage("0%");
                        dto.setMsmesBenefited(0);
                        dto.setAmountApproved(0.0);
                        dto.setExpenditureLastMonth(0.0);
                        dto.setExpenditureDuringMonth(0.0);
                        dto.setCumulativeExpenditure(0.0);
                        dto.setExpenditurePercentage("0%");
                        return dto;
                    }
                }

                return dto;
            }
        }

        return dto;
    }


}
