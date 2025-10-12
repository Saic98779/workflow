package com.metaverse.workflow.MoMSMEReport.service;

import com.metaverse.workflow.MoMSMEReport.dtos.CumulativeQuarterData;
import com.metaverse.workflow.model.MoMSMEQuarterlyReportTargets;
import com.metaverse.workflow.model.MoMSMEReport;
import com.metaverse.workflow.model.MoMSMEReportSubmitted;

import java.util.List;

public class MoMSMEReportSubmittedMapper {
    public static MoMSMEReportSubmittedDto toDTO(MoMSMEReportSubmitted entity) {
        if (entity == null) return null;

        return MoMSMEReportSubmittedDto.builder()
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

    public static MoMSMEReportSubmitted toEntity(MoMSMEReportSubmittedDto dto, MoMSMEReport moMSMEReport) {
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


    public static class MoMSMEReportMapper {

        public static MoMSMEReportDto toDTOReport(MoMSMEReport entity, String month, String financialYear) {
            if (entity == null) return null;

            MoMSMEReportSubmitted currentMonthSubmission = entity.getMoMSMEReportSubmitted().stream()
                    .filter(s -> s.getMonth().equals(month) && s.getFinancialYear().equals(financialYear))
                    .findFirst()
                    .orElse(null);

            String quarter = getQuarter(month);

            MoMSMEQuarterlyReportTargets currentQuarterTargets = entity.getMoMSMEQuarterlyReportTargets().stream()
                    .filter(t -> t.getQuarter().equals(quarter) && t.getFinancialYear().equals(financialYear))
                    .findFirst()
                    .orElse(null);

            List<String> quarterMonths = getQuarterMonths(quarter);
            int total = 0, women = 0, sc = 0, st = 0, obc = 0;
            double physicalAchievement = 0, financialAchievement = 0;

            for (MoMSMEReportSubmitted s : entity.getMoMSMEReportSubmitted()) {
                if (s.getFinancialYear().equals(financialYear) && quarterMonths.contains(s.getMonth())) {
                    total += s.getTotal() != null ? s.getTotal() : 0;
                    women += s.getWomen() != null ? s.getWomen() : 0;
                    sc += s.getSc() != null ? s.getSc() : 0;
                    st += s.getSt() != null ? s.getSt() : 0;
                    obc += s.getObc() != null ? s.getObc() : 0;
                    physicalAchievement += s.getPhysicalAchievement() != null ? s.getPhysicalAchievement() : 0;
                    financialAchievement += s.getFinancialAchievement() != null ? s.getFinancialAchievement() : 0;
                }
            }

            return MoMSMEReportDto.builder()
                    .moMSMEActivityId(entity.getMoMSMEActivityId())
                    .financialYear(financialYear)
                    .month(month)
                    .physicalTarget(currentQuarterTargets != null ? currentQuarterTargets.getPhysicalTarget() : 0)
                    .financialTarget(currentQuarterTargets != null ? currentQuarterTargets.getFinancialTarget() : 0)
                    .currentPhysicalAchievement(currentMonthSubmission != null ? currentMonthSubmission.getPhysicalAchievement() : 0)
                    .currentFinancialAchievement(currentMonthSubmission != null ? currentMonthSubmission.getFinancialAchievement() : 0)
                    .physicalAchievement(physicalAchievement)
                    .financialAchievement(financialAchievement)
                    .currentMonthMoMSMEBenefitedDto(currentMonthSubmission != null ?
                            CurrentMonthMoMSMEBenefitedDto.builder()
                                    .total(currentMonthSubmission.getTotal())
                                    .women(currentMonthSubmission.getWomen())
                                    .sc(currentMonthSubmission.getSc())
                                    .st(currentMonthSubmission.getSt())
                                    .obc(currentMonthSubmission.getObc())
                                    .build() : null)

                    .currentQuarterMoMSMEBenefitedDto(CurrentQuarterMoMSMEBenefitedDto.builder()
                            .total(total)
                            .women(women)
                            .sc(sc)
                            .st(st)
                            .obc(obc)
                            .build())

                    .intervention(entity.getIntervention())
                    .component(entity.getComponent())
                    .activity(entity.getActivity())
                    .build();
        }


        public static MoMSMEReportDto toDTOReportByQuarter(MoMSMEReport entity, String quarter, String financialYear) {
            if (entity == null) return null;

            // Get months in the quarter
            List<String> quarterMonths = getQuarterMonths(quarter);

            // Filter monthly submissions for this quarter
            List<MoMSMEReportSubmitted> quarterlySubmissions = entity.getMoMSMEReportSubmitted().stream()
                    .filter(s -> s.getFinancialYear().equals(financialYear) && quarterMonths.contains(s.getMonth().toLowerCase()))
                    .toList();

            // Calculate quarterly totals
            int totalQuarter = 0, womenQuarter = 0, scQuarter = 0, stQuarter = 0, obcQuarter = 0;
            double physicalAchievementQuarter = 0, financialAchievementQuarter = 0;

            for (MoMSMEReportSubmitted s : quarterlySubmissions) {
                totalQuarter += s.getTotal() != null ? s.getTotal() : 0;
                womenQuarter += s.getWomen() != null ? s.getWomen() : 0;
                scQuarter += s.getSc() != null ? s.getSc() : 0;
                stQuarter += s.getSt() != null ? s.getSt() : 0;
                obcQuarter += s.getObc() != null ? s.getObc() : 0;
                physicalAchievementQuarter += s.getPhysicalAchievement() != null ? s.getPhysicalAchievement() : 0;
                financialAchievementQuarter += s.getFinancialAchievement() != null ? s.getFinancialAchievement() : 0;
            }

            // Find quarterly target
            MoMSMEQuarterlyReportTargets quarterlyTarget = entity.getMoMSMEQuarterlyReportTargets().stream()
                    .filter(t -> t.getQuarter().equalsIgnoreCase(quarter) && t.getFinancialYear().equals(financialYear))
                    .findFirst()
                    .orElse(null);

            // Build cumulative FY data
            CumulativeQuarterData cumulativeDto = CumulativeQuarterData.builder()
                    .intervention(entity.getIntervention())
                    .component(entity.getComponent())
                    .activity(entity.getActivity())
                    .financialYear(financialYear)
                    .month("FY")
                    .physicalTarget(entity.getMoMSMEQuarterlyReportTargets().stream()
                            .filter(t -> t.getFinancialYear().equals(financialYear))
                            .mapToDouble(t -> t.getPhysicalTarget() != null ? t.getPhysicalTarget() : 0)
                            .sum())
                    .financialTarget(entity.getMoMSMEQuarterlyReportTargets().stream()
                            .filter(t -> t.getFinancialYear().equals(financialYear))
                            .mapToDouble(t -> t.getFinancialTarget() != null ? t.getFinancialTarget() : 0)
                            .sum())
                    .physicalAchievement(0.0)
                    .financialAchievement(0.0)
                    .total(0)
                    .women(0)
                    .sc(0)
                    .st(0)
                    .obc(0)
                    .build();

            // Sum cumulative achievements and counts
            entity.getMoMSMEReportSubmitted().stream()
                    .filter(s -> s.getFinancialYear().equals(financialYear))
                    .forEach(s -> {
                        cumulativeDto.setTotal(cumulativeDto.getTotal() + (s.getTotal() != null ? s.getTotal() : 0));
                        cumulativeDto.setWomen(cumulativeDto.getWomen() + (s.getWomen() != null ? s.getWomen() : 0));
                        cumulativeDto.setSc(cumulativeDto.getSc() + (s.getSc() != null ? s.getSc() : 0));
                        cumulativeDto.setSt(cumulativeDto.getSt() + (s.getSt() != null ? s.getSt() : 0));
                        cumulativeDto.setObc(cumulativeDto.getObc() + (s.getObc() != null ? s.getObc() : 0));
                        cumulativeDto.setPhysicalAchievement(cumulativeDto.getPhysicalAchievement() + (s.getPhysicalAchievement() != null ? s.getPhysicalAchievement() : 0));
                        cumulativeDto.setFinancialAchievement(cumulativeDto.getFinancialAchievement() + (s.getFinancialAchievement() != null ? s.getFinancialAchievement() : 0));
                    });

            // Build main DTO
            return MoMSMEReportDto.builder()
                    .moMSMEActivityId(entity.getMoMSMEActivityId())
                    .financialYear(financialYear)
                    .month(String.join(", ", quarterMonths))
                    .physicalTarget(quarterlyTarget != null ? quarterlyTarget.getPhysicalTarget() : 0)
                    .financialTarget(quarterlyTarget != null ? quarterlyTarget.getFinancialTarget() : 0)
                    .physicalAchievement(physicalAchievementQuarter)
                    .financialAchievement(financialAchievementQuarter)
                    .cumulativeMoMSMEBenefitedDto(cumulativeDto)
                    .currentMonthMoMSMEBenefitedDto(null) // optional
                    .currentQuarterMoMSMEBenefitedDto(CurrentQuarterMoMSMEBenefitedDto.builder()
                            .total(totalQuarter)
                            .women(womenQuarter)
                            .sc(scQuarter)
                            .st(stQuarter)
                            .obc(obcQuarter)
                            .build())
                    .intervention(entity.getIntervention())
                    .component(entity.getComponent())
                    .activity(entity.getActivity())
                    .build();
        }


        public static MoMSMEReportDto toDTOReportByCumulative(MoMSMEReport entity, String financialYear) {
            if (entity == null) return null;

            // ---- Filter submissions for the full FY ----
            List<MoMSMEReportSubmitted> fySubmissions = entity.getMoMSMEReportSubmitted().stream()
                    .filter(s -> s.getFinancialYear().equals(financialYear))
                    .toList();

            // ---- Calculate totals ----
            int total = 0, women = 0, sc = 0, st = 0, obc = 0;
            double physicalAchievement = 0.0, financialAchievement = 0.0;

            for (MoMSMEReportSubmitted s : fySubmissions) {
                total += s.getTotal() != null ? s.getTotal() : 0;
                women += s.getWomen() != null ? s.getWomen() : 0;
                sc += s.getSc() != null ? s.getSc() : 0;
                st += s.getSt() != null ? s.getSt() : 0;
                obc += s.getObc() != null ? s.getObc() : 0;
                physicalAchievement += s.getPhysicalAchievement() != null ? s.getPhysicalAchievement() : 0;
                financialAchievement += s.getFinancialAchievement() != null ? s.getFinancialAchievement() : 0;
            }

            // ---- Filter and sum targets ----
            List<MoMSMEQuarterlyReportTargets> fyTargets = entity.getMoMSMEQuarterlyReportTargets().stream()
                    .filter(t -> t.getFinancialYear().equals(financialYear))
                    .toList();

            double physicalTarget = fyTargets.stream()
                    .mapToDouble(t -> t.getPhysicalTarget() != null ? t.getPhysicalTarget() : 0.0)
                    .sum();

            double financialTarget = fyTargets.stream()
                    .mapToDouble(t -> t.getFinancialTarget() != null ? t.getFinancialTarget() : 0.0)
                    .sum();

            // ---- Build cumulative data DTO ----
            CumulativeQuarterData cumulativeDto = CumulativeQuarterData.builder()
                    .intervention(entity.getIntervention())
                    .component(entity.getComponent())
                    .activity(entity.getActivity())
                    .financialYear(financialYear)
                    .month("FY")
                    .physicalTarget(physicalTarget)
                    .financialTarget(financialTarget)
                    .physicalAchievement(physicalAchievement)
                    .financialAchievement(financialAchievement)
                    .total(total)
                    .women(women)
                    .sc(sc)
                    .st(st)
                    .obc(obc)
                    .build();

            // ---- Final DTO ----
            return MoMSMEReportDto.builder()
                    .moMSMEActivityId(entity.getMoMSMEActivityId())
                    .financialYear(financialYear)
                    .physicalTarget(physicalTarget)
                    .financialTarget(financialTarget)
                    .physicalAchievement(physicalAchievement)
                    .financialAchievement(financialAchievement)
                    .cumulativeMoMSMEBenefitedDto(cumulativeDto)
                    .currentMonthMoMSMEBenefitedDto(null)
                    .currentQuarterMoMSMEBenefitedDto(null)
                    .intervention(entity.getIntervention())
                    .component(entity.getComponent())
                    .activity(entity.getActivity())
                    .build();
        }



        // Helper class to return achievement totals
        private record AchievementData(double physical, double financial, int total, int women, int sc, int st, int obc) {}

        private static List<String> getQuarterMonths(String quarter) {
            return switch (quarter.toUpperCase()) {
                case "Q1" -> List.of("april", "may", "june");
                case "Q2" -> List.of("july", "august", "september");
                case "Q3" -> List.of("october", "november", "december");
                case "Q4" -> List.of("january", "february", "march");
                default -> throw new IllegalArgumentException("Invalid quarter: " + quarter);
            };
        }

        private static String getQuarter(String month) {
            return switch (month.toLowerCase()) {
                case "april", "may", "june" -> "Q1";
                case "july", "august", "september" -> "Q2";
                case "october", "november", "december" -> "Q3";
                case "january", "february", "march" -> "Q4";
                default -> throw new IllegalArgumentException("Invalid month: " + month);
            };
        }
    }
}
