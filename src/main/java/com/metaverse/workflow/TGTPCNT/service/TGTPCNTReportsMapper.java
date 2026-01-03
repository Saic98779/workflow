package com.metaverse.workflow.TGTPCNT.service;


import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.TGTPCNTReports;

public class TGTPCNTReportsMapper {

    public static TGTPCNTReports mapToEntity(TGTPCNTReportsRequest request, NonTrainingSubActivity subActivity) {
        return TGTPCNTReports.builder()
                .sectorName(request.getSectorName())
                .productName(request.getProductName())
                .reportSubmissionDate(DateUtil.covertStringToDate(request.getReportSubmissionDate()))
                .approvalDate(DateUtil.covertStringToDate(request.getApprovalDate()))
                .nonTrainingSubActivity(subActivity)
                .build();
    }

    public static void updateEntity(TGTPCNTReports entity, TGTPCNTReportsRequest request) {
        entity.setSectorName(request.getSectorName());
        entity.setProductName(request.getProductName());
        entity.setReportSubmissionDate(DateUtil.covertStringToDate(request.getReportSubmissionDate()));
        entity.setApprovalDate(DateUtil.covertStringToDate(request.getApprovalDate()));
    }

    public static TGTPCNTReportsResponse mapToResponse(TGTPCNTReports entity) {
        return TGTPCNTReportsResponse.builder()
                .reportId(entity.getId())
                .sectorName(entity.getSectorName())
                .productName(entity.getProductName())
                .reportSubmissionDate(DateUtil.dateToString(entity.getReportSubmissionDate(),"dd-MM-yyyy"))
                .approvalDate(DateUtil.dateToString(entity.getApprovalDate(),"dd-MM-yyyy"))
                .nonTrainingActivityId(entity.getNonTrainingSubActivity().getNonTrainingActivity().getActivityId())
                .nonTrainingActivityName(entity.getNonTrainingSubActivity().getNonTrainingActivity().getActivityName())
                .nonTrainingSubActivityId(entity.getNonTrainingSubActivity().getSubActivityId())
                .nonTrainingSubActivityName(entity.getNonTrainingSubActivity().getSubActivityName())
                .build();
    }
}
