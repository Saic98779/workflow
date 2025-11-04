package com.metaverse.workflow.MoMSMEReport.service;

import com.metaverse.workflow.MoMSMEReport.dtos.CumulativeQuarterData;
import com.metaverse.workflow.model.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.poi.sl.usermodel.PlaceholderDetails.PlaceholderSize.quarter;

public class MoMSMEReportSubmittedMapper {
    public static MoMSMEReportSubmittedDto toDTO(MoMSMEReportSubmitted entity) {
        if (entity == null) return null;

        String months = null;
        if (entity.getMonthlyReports() != null && !entity.getMonthlyReports().isEmpty()) {
            months = entity.getMonthlyReports().stream()
                    .map(m -> m.getMonth())
                    .sorted()
                    .collect(Collectors.joining(", "));
        }

        return MoMSMEReportSubmittedDto.builder()
                .submittedId(entity.getSubmittedId())
                .financialYear(entity.getFinancialYear())
                .month(months)
                .physicalAchievement(entity.getQuarterlyReport().getPhysicalAchievement())
                .financialAchievement(entity.getQuarterlyReport().getFinancialAchievement())
                .total(entity.getQuarterlyReport().getTotal())
                .women(entity.getQuarterlyReport().getWomen())
                .sc(entity.getQuarterlyReport().getSc())
                .st(entity.getQuarterlyReport().getSt())
                .obc(entity.getQuarterlyReport().getObc())
                .intervention(
                        entity.getMoMSMEReport() != null ? entity.getMoMSMEReport().getIntervention() : null
                )
                .component(
                        entity.getMoMSMEReport() != null ? entity.getMoMSMEReport().getComponent() : null
                )
                .activity(
                        entity.getMoMSMEReport() != null ? entity.getMoMSMEReport().getActivity() : null
                )
                .build();
    }


    public static class MoMSMEReportMapper {

        public static MoMSMEReportDto toDTOReport(MoMSMEReport entity, String month, String financialYear) {
            if (entity == null) return null;
            String quarter = getQuarter(month); // compute quarter early

            List<MoMSMEReportSubmittedMonthly> quarterMonthsRecords = entity.getMoMSMEReportSubmitted().stream()
                    .filter(s -> s.getFinancialYear().equals(financialYear))
                    .flatMap(s -> s.getMonthlyReports().stream())
                    .filter(m -> getQuarterMonths(quarter).contains(m.getMonth().toLowerCase())) // ✅ use quarter here
                    .toList();

            MoMSMEReportSubmittedMonthly monthRecord = entity.getMoMSMEReportSubmitted().stream()
                    .filter(s -> s.getFinancialYear().equals(financialYear))
                    .flatMap(s -> s.getMonthlyReports().stream())
                    .filter(m -> m.getMonth().equalsIgnoreCase(month))
                    .findFirst()
                    .orElse(null);

            MoMSMEReportSubmittedQuarterly quarterlyRecord = entity.getMoMSMEReportSubmitted().stream()
                    .filter(s -> s.getFinancialYear().equals(financialYear))
                    .map(MoMSMEReportSubmitted::getQuarterlyReport)
                    .filter(q -> q != null && q.getQuarter().equalsIgnoreCase(String.valueOf(quarter)))
                    .findFirst()
                    .orElse(null);


            MoMSMEQuarterlyReportTargets currentQuarterTargets = entity.getMoMSMEQuarterlyReportTargets().stream()
                    .filter(t -> t.getQuarter().equals(quarter) && t.getFinancialYear().equals(financialYear))
                    .findFirst()
                    .orElse(null);


            return MoMSMEReportDto.builder()
                    .moMSMEActivityId(entity.getMoMSMEActivityId())
                    .financialYear(financialYear)
                    .month(month)
                    .physicalTarget(currentQuarterTargets != null ? currentQuarterTargets.getPhysicalTarget() : 0)
                    .financialTarget(currentQuarterTargets != null ? currentQuarterTargets.getFinancialTarget() : 0)
                    .currentPhysicalAchievement(monthRecord != null ? monthRecord.getPhysicalAchievement() : 0)
                    .currentFinancialAchievement(monthRecord != null ? monthRecord.getFinancialAchievement() : 0)
                    .physicalAchievement(quarterlyRecord != null ? quarterlyRecord.getPhysicalAchievement() : 0)
                    .financialAchievement(quarterlyRecord != null ? quarterlyRecord.getFinancialAchievement() : 0)
                    .currentMonthMoMSMEBenefitedDto(!quarterMonthsRecords.isEmpty() ?
                            CurrentMonthMoMSMEBenefitedDto.builder()
                                    .total(monthRecord != null ? monthRecord.getTotal() : 0)
                                    .women(monthRecord != null ? monthRecord.getWomen() : 0)
                                    .sc(monthRecord != null ? monthRecord.getSc() : 0)
                                    .st(monthRecord != null ? monthRecord.getSt() : 0)
                                    .obc(monthRecord != null ? monthRecord.getObc() : 0)
                                    .build() : null)

                    .currentQuarterMoMSMEBenefitedDto(CurrentQuarterMoMSMEBenefitedDto.builder()
                            .total(quarterlyRecord != null ? quarterlyRecord.getTotal() : 0)
                            .women(quarterlyRecord != null ? quarterlyRecord.getWomen() : 0)
                            .sc(quarterlyRecord != null ? quarterlyRecord.getSc() : 0)
                            .st(quarterlyRecord != null ? quarterlyRecord.getSt() : 0)
                            .obc(quarterlyRecord != null ? quarterlyRecord.getObc() : 0)
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

            // Get all monthly records for this quarter
            List<MoMSMEReportSubmittedMonthly> quarterlyMonthRecords = entity.getMoMSMEReportSubmitted().stream()
                    .filter(s -> s.getFinancialYear().equals(financialYear))      // filter by financial year
                    .flatMap(s -> s.getMonthlyReports().stream())                  // flatten monthly reports
                    .filter(m -> quarterMonths.contains(m.getMonth().toLowerCase())) // filter by quarter months
                    .toList();

            // Compute quarterly totals
            int totalQuarter = 0, womenQuarter = 0, scQuarter = 0, stQuarter = 0, obcQuarter = 0;
            double physicalAchievementQuarter = 0.0, financialAchievementQuarter = 0.0;

            for (MoMSMEReportSubmittedMonthly m : quarterlyMonthRecords) {
                totalQuarter += m.getTotal() != null ? m.getTotal() : 0;
                womenQuarter += m.getWomen() != null ? m.getWomen() : 0;
                scQuarter += m.getSc() != null ? m.getSc() : 0;
                stQuarter += m.getSt() != null ? m.getSt() : 0;
                obcQuarter += m.getObc() != null ? m.getObc() : 0;
                physicalAchievementQuarter += m.getPhysicalAchievement() != null ? m.getPhysicalAchievement() : 0;
                financialAchievementQuarter += m.getFinancialAchievement() != null ? m.getFinancialAchievement() : 0;
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
                    .physicalAchievement(entity.getMoMSMEReportSubmitted().stream()
                            .filter(s -> s.getFinancialYear().equals(financialYear))
                            .flatMap(s -> s.getMonthlyReports().stream())
                            .mapToDouble(m -> m.getPhysicalAchievement() != null ? m.getPhysicalAchievement() : 0)
                            .sum())
                    .financialAchievement(entity.getMoMSMEReportSubmitted().stream()
                            .filter(s -> s.getFinancialYear().equals(financialYear))
                            .flatMap(s -> s.getMonthlyReports().stream())
                            .mapToDouble(m -> m.getFinancialAchievement() != null ? m.getFinancialAchievement() : 0)
                            .sum())
                    .total(entity.getMoMSMEReportSubmitted().stream()
                            .filter(s -> s.getFinancialYear().equals(financialYear))
                            .flatMap(s -> s.getMonthlyReports().stream())
                            .mapToInt(m -> m.getTotal() != null ? m.getTotal() : 0)
                            .sum())
                    .women(entity.getMoMSMEReportSubmitted().stream()
                            .filter(s -> s.getFinancialYear().equals(financialYear))
                            .flatMap(s -> s.getMonthlyReports().stream())
                            .mapToInt(m -> m.getWomen() != null ? m.getWomen() : 0)
                            .sum())
                    .sc(entity.getMoMSMEReportSubmitted().stream()
                            .filter(s -> s.getFinancialYear().equals(financialYear))
                            .flatMap(s -> s.getMonthlyReports().stream())
                            .mapToInt(m -> m.getSc() != null ? m.getSc() : 0)
                            .sum())
                    .st(entity.getMoMSMEReportSubmitted().stream()
                            .filter(s -> s.getFinancialYear().equals(financialYear))
                            .flatMap(s -> s.getMonthlyReports().stream())
                            .mapToInt(m -> m.getSt() != null ? m.getSt() : 0)
                            .sum())
                    .obc(entity.getMoMSMEReportSubmitted().stream()
                            .filter(s -> s.getFinancialYear().equals(financialYear))
                            .flatMap(s -> s.getMonthlyReports().stream())
                            .mapToInt(m -> m.getObc() != null ? m.getObc() : 0)
                            .sum())
                    .build();

            // Build main DTO
            return MoMSMEReportDto.builder()
                    .moMSMEActivityId(entity.getMoMSMEActivityId())
                    .financialYear(financialYear)
                    .month(String.join(", ", quarterMonths)) // comma-separated months
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

            // ---- Flatten all monthly records for the FY ----
            List<MoMSMEReportSubmittedMonthly> fyMonthlyRecords = entity.getMoMSMEReportSubmitted().stream()
                    .filter(s -> s.getFinancialYear().equals(financialYear))
                    .flatMap(s -> s.getMonthlyReports().stream())
                    .toList();

            // ---- Calculate totals ----
            int total = 0, women = 0, sc = 0, st = 0, obc = 0;
            double physicalAchievement = 0.0, financialAchievement = 0.0;

            for (MoMSMEReportSubmittedMonthly m : fyMonthlyRecords) {
                total += m.getTotal() != null ? m.getTotal() : 0;
                women += m.getWomen() != null ? m.getWomen() : 0;
                sc += m.getSc() != null ? m.getSc() : 0;
                st += m.getSt() != null ? m.getSt() : 0;
                obc += m.getObc() != null ? m.getObc() : 0;
                physicalAchievement += m.getPhysicalAchievement() != null ? m.getPhysicalAchievement() : 0;
                financialAchievement += m.getFinancialAchievement() != null ? m.getFinancialAchievement() : 0;
            }

            // ---- Sum quarterly targets for FY ----
            double physicalTarget = entity.getMoMSMEQuarterlyReportTargets().stream()
                    .filter(t -> t.getFinancialYear().equals(financialYear))
                    .mapToDouble(t -> t.getPhysicalTarget() != null ? t.getPhysicalTarget() : 0.0)
                    .sum();

            double financialTarget = entity.getMoMSMEQuarterlyReportTargets().stream()
                    .filter(t -> t.getFinancialYear().equals(financialYear))
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

            // ---- Build main DTO ----
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
