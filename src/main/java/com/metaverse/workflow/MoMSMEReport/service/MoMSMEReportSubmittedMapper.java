package com.metaverse.workflow.MoMSMEReport.service;

import com.metaverse.workflow.model.MoMSMEReport;
import com.metaverse.workflow.model.MoMSMEReportSubmitted;

public class MoMSMEReportSubmittedMapper {
    public static MoMSMEReportSubmittedDTO toDTO(MoMSMEReportSubmitted entity) {
        if (entity == null) return null;

        return MoMSMEReportSubmittedDTO.builder()
                .submittedId(entity.getSubmittedId())
                .financialYear(entity.getFinancialYear())
                .month(entity.getMonth())
                .physicalAchievement(entity.getPhysicalAchievement())
                .financialAchievement(entity.getFinancialAchievement())
                .total(entity.getTotal())
                .women(entity.getWomen())
                .sc(entity.getSc())
                .st(entity.getSt())
                .obc(entity.getObc())
                .intervention(entity.getMoMSMEReport().getIntervention())
                .component(entity.getMoMSMEReport().getComponent())
                .activity(entity.getMoMSMEReport().getActivity())
                .build();
    }

    public static MoMSMEReportSubmitted toEntity(MoMSMEReportSubmittedDTO dto, MoMSMEReport moMSMEReport) {
        if (dto == null) return null;

        return MoMSMEReportSubmitted.builder()
                .submittedId(dto.getSubmittedId())
                .financialYear(dto.getFinancialYear())
                .month(dto.getMonth())
                .physicalAchievement(dto.getPhysicalAchievement())
                .financialAchievement(dto.getFinancialAchievement())
                .total(dto.getTotal())
                .women(dto.getWomen())
                .sc(dto.getSc())
                .st(dto.getSt())
                .obc(dto.getObc())
                .moMSMEReport(moMSMEReport)
                .build();
    }
}
