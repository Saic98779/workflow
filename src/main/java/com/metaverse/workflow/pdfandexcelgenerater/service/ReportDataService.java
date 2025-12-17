package com.metaverse.workflow.pdfandexcelgenerater.service;


import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.nontraining.service.ProgressMonitoringService;
import com.metaverse.workflow.trainingandnontrainingtarget.service.NonTrainingTargetsAndAchievementsService;
import com.metaverse.workflow.trainingandnontrainingtarget.service.TrainingTargetsAndAchievementsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportDataService {

    private final ProgressMonitoringService progressMonitoringService;
    private final TrainingTargetsAndAchievementsService trainingTargetsAndAchievementsService;
    private final NonTrainingTargetsAndAchievementsService nonTrainingTargetsAndAchievementsService;

    public List<ExcelReportRow> prepareExcelRows(
            Long agencyId,
            ReportRequest request) {
        ReportType reportType = request.getReportType();
        TrainingType trainingType = request.getTrainingType();

        List<ExcelReportRow> rows = new ArrayList<>();

        boolean includeTraining =
                trainingType == TrainingType.TRAINING
                        || trainingType == TrainingType.TRAINING_AND_NON_TRAINING;

        boolean includeNonTraining =
                trainingType == TrainingType.NON_TRAINING
                        || trainingType == TrainingType.TRAINING_AND_NON_TRAINING;

        /* ---------------- TARGET ---------------- */
        if (reportType == ReportType.TARGET) {

            if (includeTraining) {
                trainingTargetsAndAchievementsService
                        .getTrainingTargets(request.getFinancialYear(), agencyId)
                        .forEach(t ->
                                rows.add(
                                        ExcelReportRow.builder()
                                                .agency(t.getAgencyName())
                                                .activity(t.getActivityName())
                                                .subActivity(t.getSubActivityName())
                                                .physicalTarget(t.getTotalTrainingTarget())
                                                .budgetAllocated(t.getTotalFinancialTarget())
                                                .reportType(TrainingType.TRAINING.name())
                                                .build()
                                )
                        );
            }


            if (includeNonTraining) {
                nonTrainingTargetsAndAchievementsService.getNonTrainingTargets(request.getFinancialYear(), agencyId)
                        .forEach(n ->
                                rows.add(
                                        ExcelReportRow.builder()
                                                .agency(n.getAgencyName())
                                                .activity(n.getActivityName())
                                                .subActivity(n.getSubActivityName())
                                                .physicalTarget(n.getTotalTrainingTarget())
                                                .budgetAllocated(n.getTotalFinancialTarget())
                                                .reportType(TrainingType.NON_TRAINING.name())
                                                .build()
                                )
                        );
            }
        }

        /* ---------------- ACHIEVEMENT ---------------- */
        else if (reportType == ReportType.ACHIEVEMENT) {

            if (includeTraining) {
                progressMonitoringService.getAllTrainingProgressMonitoringProgress(agencyId, DateUtil.covertStringToDate(request.getFromDate()),DateUtil.covertStringToDate(request.getToDate()))
                        .forEach(t ->
                                rows.add(
                                        ExcelReportRow.builder()
                                                .agency(t.getAgency())
                                                .activity(t.getActivity())
                                                .subActivity(t.getSubActivity())
                                                .trainingAchievement(t.getTrainingAchievement())
                                                .expenditure(t.getExpenditure())
                                                .reportType(TrainingType.TRAINING.name())
                                                .build()
                                )
                        );
            }
            if (includeNonTraining) {
                nonTrainingTargetsAndAchievementsService.getTargetsAndAchievements(request.getFinancialYear(), agencyId)
                        .forEach(t ->
                                rows.add(
                                        ExcelReportRow.builder()
                                                .agency(t.getAgencyName())
                                                .activity(t.getActivityName())
                                                .subActivity(t.getSubActivityName())
                                                .trainingAchievement(t.getTotalAchieved())
                                                .expenditure(t.getTotalFinancialAchieved())
                                                .reportType(TrainingType.NON_TRAINING.name())
                                                .build()
                                )
                        );
            }
        }

        /* -------- TARGET AND ACHIEVEMENT -------- */
        else {

            if (includeTraining) {
                trainingTargetsAndAchievementsService.getTargetsAndAchievements(request.getFinancialYear(), agencyId)
                        .forEach(t ->
                                rows.add(
                                        ExcelReportRow.builder()
                                                .agency(t.getAgencyName())
                                                .activity(t.getActivityName())
                                                .subActivity(t.getSubActivityName())
                                                .physicalTarget(t.getTotalTarget())
                                                .trainingAchievement(Long.valueOf(t.getTotalAchieved()))
                                                .budgetAllocated(Double.valueOf(t.getTotalFinancialTarget()))
                                                .expenditure(t.getTotalFinancialAchieved())
                                                .reportType(TrainingType.TRAINING.name())
                                                .build()
                                )
                        );
            }

            if (includeNonTraining) {
                nonTrainingTargetsAndAchievementsService.getTargetsAndAchievements(request.getFinancialYear(), agencyId)
                        .forEach(n ->
                                rows.add(
                                        ExcelReportRow.builder()
                                                .agency(n.getAgencyName())
                                                .activity(n.getActivityName())
                                                .subActivity(n.getSubActivityName())
                                                .physicalTarget(n.getTotalTarget())
                                                .trainingAchievement(n.getTotalAchieved())
                                                .budgetAllocated(Double.valueOf(n.getTotalFinancialTarget()))
                                                .expenditure(n.getTotalFinancialAchieved())
                                                .reportType(TrainingType.NON_TRAINING.name())
                                                .build()
                                )
                        );
            }
        }

        return rows;
    }
}
