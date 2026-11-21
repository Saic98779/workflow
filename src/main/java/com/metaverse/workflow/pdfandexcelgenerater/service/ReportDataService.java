package com.metaverse.workflow.pdfandexcelgenerater.service;


import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.nontraining.service.ProgressMonitoringService;
import com.metaverse.workflow.trainingandnontrainingtarget.service.NonTrainingTargetsAndAchievementsService;
import com.metaverse.workflow.trainingandnontrainingtarget.service.TrainingTargetsAndAchievementsService;
import com.metaverse.workflow.trainingandnontrainingtarget.service.TrainingTargetsAndAchievementsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Prepares the rows of the progress report Excel for one agency.
 * <p>
 * Never throws for missing data: if a source has no records for the agency (or fails),
 * a single row with zero values is written for that agency / training type instead.
 */
@Service
@RequiredArgsConstructor
public class ReportDataService {

    private static final Logger log = LogManager.getLogger(ReportDataService.class);

    private static final String NO_DATA = "No data available";
    private static final String ERROR_REMARK = "Error while loading data - values shown as 0";

    private final ProgressMonitoringService progressMonitoringService;
    private final TrainingTargetsAndAchievementsService trainingTargetsAndAchievementsService;
    private final NonTrainingTargetsAndAchievementsService nonTrainingTargetsAndAchievementsService;
    private final AgencyRepository agencyRepository;

    public List<ExcelReportRow> prepareExcelRows(Long agencyId, ReportRequest request) {
        List<ExcelReportRow> rows = new ArrayList<>();
        if (agencyId == null || request == null) {
            return rows;
        }

        ReportType reportType = request.getReportType() != null ? request.getReportType() : ReportType.TARGET;
        TrainingType trainingType = request.getTrainingType() != null
                ? request.getTrainingType() : TrainingType.TRAINING_AND_NON_TRAINING;
        String fy = effectiveFinancialYear(request);

        boolean includeTraining = trainingType == TrainingType.TRAINING
                || trainingType == TrainingType.TRAINING_AND_NON_TRAINING;
        boolean includeNonTraining = trainingType == TrainingType.NON_TRAINING
                || trainingType == TrainingType.TRAINING_AND_NON_TRAINING;

        String training = TrainingType.TRAINING.name();
        String nonTraining = TrainingType.NON_TRAINING.name();

        /* ---------------- TARGET ---------------- */
        if (reportType == ReportType.TARGET) {

            if (includeTraining) {
                addRows(rows, agencyId, training, "training targets",
                        () -> trainingTargetsAndAchievementsService.getTrainingTargetsForExport(fy, agencyId),
                        t -> ExcelReportRow.builder()
                                .agency(t.getAgencyName())
                                .activity(t.getActivityName())
                                .subActivity(t.getSubActivityName())
                                .physicalTarget(toLong(t.getTotalTrainingTarget()))
                                .budgetAllocated(toDouble(t.getTotalFinancialTarget()))
                                .reportType(training)
                                .build());
            }

            if (includeNonTraining) {
                addRows(rows, agencyId, nonTraining, "non-training targets",
                        () -> nonTrainingTargetsAndAchievementsService.getNonTrainingTargetsForExport(fy, agencyId),
                        n -> ExcelReportRow.builder()
                                .agency(n.getAgencyName())
                                .activity(n.getActivityName())
                                .subActivity(n.getSubActivityName())
                                .physicalTarget(toLong(n.getTotalTrainingTarget()))
                                .budgetAllocated(toDouble(n.getTotalFinancialTarget()))
                                .reportType(nonTraining)
                                .build());
            }
        }

        /* ---------------- ACHIEVEMENT ---------------- */
        else if (reportType == ReportType.ACHIEVEMENT) {

            if (includeTraining) {
                Date[] range = resolveDateRange(request);
                addRows(rows, agencyId, training, "training achievements",
                        () -> progressMonitoringService.getAllTrainingProgressMonitoringProgress(
                                agencyId, range[0], range[1]),
                        t -> ExcelReportRow.builder()
                                .agency(t.getAgency())
                                .activity(t.getActivity())
                                .subActivity(t.getSubActivity())
                                .trainingAchievement(toLong(t.getTrainingAchievement()))
                                .expenditure(toDouble(t.getExpenditure()))
                                .reportType(training)
                                .build());
            }

            if (includeNonTraining) {
                addRows(rows, agencyId, nonTraining, "non-training achievements",
                        () -> nonTrainingTargetsAndAchievementsService.getTargetsAndAchievements(fy, agencyId),
                        n -> ExcelReportRow.builder()
                                .agency(n.getAgencyName())
                                .activity(n.getActivityName())
                                .subActivity(n.getSubActivityName())
                                .trainingAchievement(toLong(n.getTotalAchieved()))
                                .expenditure(toDouble(n.getTotalFinancialAchieved()))
                                .error(n.isDataError())
                                .reportType(nonTraining)
                                .build());
            }
        }

        /* -------- TARGET AND ACHIEVEMENT -------- */
        else {

            if (includeTraining) {
                addRows(rows, agencyId, training, "training targets & achievements",
                        () -> trainingTargetsAndAchievementsService.getTargetsAndAchievements(fy, agencyId),
                        t -> ExcelReportRow.builder()
                                .agency(t.getAgencyName())
                                .activity(t.getActivityName())
                                .subActivity(t.getSubActivityName())
                                .physicalTarget(toLong(t.getTotalTarget()))
                                .trainingAchievement(toLong(t.getTotalAchieved()))
                                .budgetAllocated(sum(t.getFinancialTargetQ1(), t.getFinancialTargetQ2(),
                                        t.getFinancialTargetQ3(), t.getFinancialTargetQ4()))
                                .expenditure(toDouble(t.getTotalFinancialAchieved()))
                                .error(t.isDataError())
                                .reportType(training)
                                .build());
            }

            if (includeNonTraining) {
                addRows(rows, agencyId, nonTraining, "non-training targets & achievements",
                        () -> nonTrainingTargetsAndAchievementsService.getTargetsAndAchievements(fy, agencyId),
                        n -> ExcelReportRow.builder()
                                .agency(n.getAgencyName())
                                .activity(n.getActivityName())
                                .subActivity(n.getSubActivityName())
                                .physicalTarget(toLong(n.getTotalTarget()))
                                .trainingAchievement(toLong(n.getTotalAchieved()))
                                .budgetAllocated(sum(n.getFinancialTargetQ1(), n.getFinancialTargetQ2(),
                                        n.getFinancialTargetQ3(), n.getFinancialTargetQ4()))
                                .expenditure(toDouble(n.getTotalFinancialAchieved()))
                                .error(n.isDataError())
                                .reportType(nonTraining)
                                .build());
            }
        }

        return rows;
    }

    /**
     * Fetches the records of one source and maps them to Excel rows.
     * <ul>
     *     <li>Record loaded fine → row with real values.</li>
     *     <li>Record failed (while calculating or mapping) → row kept, values 0, highlighted as error.</li>
     *     <li>Whole source failed → one highlighted error row with 0 values for the agency.</li>
     *     <li>Source has no records → one "No data available" row with 0 values.</li>
     * </ul>
     */
    private <T> void addRows(List<ExcelReportRow> rows, Long agencyId, String reportType, String label,
                             Supplier<? extends Collection<T>> fetch, Function<T, ExcelReportRow> mapper) {
        Collection<T> records;
        try {
            records = fetch.get();
        } catch (Exception ex) {
            if (isNoDataException(ex)) {
                log.info("No {} found for agencyId={}", label, agencyId);
                rows.add(zeroRow(agencyId, reportType, false, NO_DATA));
            } else {
                log.error("Could not load {} for agencyId={}", label, agencyId, ex);
                rows.add(zeroRow(agencyId, reportType, true, ERROR_REMARK));
            }
            return;
        }

        int added = 0;
        if (records != null) {
            for (T record : records) {
                if (record == null) {
                    continue;
                }
                ExcelReportRow row;
                try {
                    row = mapper.apply(record);
                } catch (Exception ex) {
                    log.error("Invalid {} record for agencyId={}", label, agencyId, ex);
                    row = zeroRow(agencyId, reportType, true, ERROR_REMARK);
                }
                if (row.isError() && row.getRemarks() == null) {
                    row.setRemarks(ERROR_REMARK);
                }
                rows.add(row);
                added++;
            }
        }

        if (added == 0) {
            log.info("No {} found for agencyId={}", label, agencyId);
            rows.add(zeroRow(agencyId, reportType, false, NO_DATA));
        }
    }

    /** The target services throw "No targets found ..." when an agency simply has no data. */
    private static boolean isNoDataException(Exception ex) {
        return ex.getMessage() != null && ex.getMessage().startsWith("No targets found");
    }

    private ExcelReportRow zeroRow(Long agencyId, String reportType, boolean error, String remarks) {
        return ExcelReportRow.builder()
                .agency(agencyName(agencyId))
                .activity("-")
                .subActivity("-")
                .physicalTarget(0L)
                .budgetAllocated(0.0)
                .trainingAchievement(0L)
                .expenditure(0.0)
                .reportType(reportType)
                .error(error)
                .remarks(remarks)
                .build();
    }

    private String agencyName(Long agencyId) {
        try {
            return agencyRepository.findById(agencyId)
                    .map(Agency::getAgencyName)
                    .orElse("Agency " + agencyId);
        } catch (Exception ex) {
            log.warn("Could not resolve agency name for agencyId={} : {}", agencyId, ex.getMessage());
            return "Agency " + agencyId;
        }
    }

    /** Uses fromDate / toDate when given, otherwise the financial year boundaries. */
    private Date[] resolveDateRange(ReportRequest request) {
        Date from = null;
        Date to = null;
        try {
            from = DateUtil.covertStringToDate(request.getFromDate());
            to = DateUtil.covertStringToDate(request.getToDate());
        } catch (Exception ex) {
            log.warn("Invalid from/to date in report request : {}", ex.getMessage());
        }
        if ((from == null || to == null) && isValidFinancialYear(request.getFinancialYear())) {
            Date[] fyRange = TrainingTargetsAndAchievementsServiceImpl.getFinancialYearRange(request.getFinancialYear());
            if (from == null) from = fyRange[0];
            if (to == null) to = fyRange[1];
        }
        return new Date[]{from, to};
    }

    /**
     * Financial year to use for target lookups: the request's financial year when valid,
     * otherwise the financial year (Apr–Mar) containing fromDate, e.g. 15-05-2026 → "2026-2027".
     */
    private String effectiveFinancialYear(ReportRequest request) {
        String fy = request.getFinancialYear();
        if (isValidFinancialYear(fy)) {
            return fy;
        }
        try {
            Date from = DateUtil.covertStringToDate(request.getFromDate());
            if (from != null) {
                java.time.LocalDate d = new java.sql.Date(from.getTime()).toLocalDate();
                int startYear = d.getMonthValue() >= 4 ? d.getYear() : d.getYear() - 1;
                return startYear + "-" + (startYear + 1);
            }
        } catch (Exception ex) {
            log.warn("Could not derive financial year from fromDate={} : {}", request.getFromDate(), ex.getMessage());
        }
        return fy;
    }

    private static Double sum(Double... values) {
        double total = 0;
        for (Double v : values) {
            total += v != null ? v : 0.0;
        }
        return total;
    }

    private static boolean isValidFinancialYear(String fy) {
        return fy != null && fy.matches("\\d{4}-\\d{4}");
    }

    private static Long toLong(Number value) {
        return value != null ? value.longValue() : 0L;
    }

    private static Double toDouble(Number value) {
        return value != null ? value.doubleValue() : 0.0;
    }
}
