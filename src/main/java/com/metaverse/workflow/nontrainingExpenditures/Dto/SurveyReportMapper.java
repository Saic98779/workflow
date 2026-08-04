package com.metaverse.workflow.nontrainingExpenditures.Dto;


import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.NonTrainingActivity;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.aleap_handholding.SurveyReport;

public class SurveyReportMapper {

    private SurveyReportMapper() {
    }

    public static SurveyReport toEntity(SurveyReportDTO dto,
                                        NonTrainingActivity activity,
                                        NonTrainingSubActivity subActivity) {

        if (dto == null) {
            return null;
        }

        return SurveyReport.builder()
                .nonTrainingActivity(activity)
                .nonTrainingSubActivity(subActivity)
                .reportSubmissionDate(
                        dto.getReportSubmissionDate() != null
                                ? DateUtil.stringToDate(dto.getReportSubmissionDate(), "dd-MM-yyyy")
                                : null)
                .approvalDate(
                        dto.getApprovalDate() != null
                                ? DateUtil.stringToDate(dto.getApprovalDate(), "dd-MM-yyyy")
                                : null)
                .reportFile(dto.getReportFile())
                .build();
    }

    public static SurveyReportDTO toDTO(SurveyReport entity) {

        if (entity == null) {
            return null;
        }

        return SurveyReportDTO.builder()
                .surveyReportId(entity.getId())
                .nonTrainingActivityId(
                        entity.getNonTrainingActivity() != null
                                ? entity.getNonTrainingActivity().getActivityId()
                                : null)
                .nonTrainingSubActivityId(
                        entity.getNonTrainingSubActivity() != null
                                ? entity.getNonTrainingSubActivity().getSubActivityId()
                                : null)
                .reportSubmissionDate(
                        entity.getReportSubmissionDate() != null
                                ? DateUtil.dateToString(entity.getReportSubmissionDate(), "dd-MM-yyyy")
                                : null)
                .approvalDate(
                        entity.getApprovalDate() != null
                                ? DateUtil.dateToString(entity.getApprovalDate(), "dd-MM-yyyy")
                                : null)
                .reportFile(entity.getReportFile())
                .build();
    }
}