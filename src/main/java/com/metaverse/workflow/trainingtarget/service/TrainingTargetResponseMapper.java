package com.metaverse.workflow.trainingtarget.service;

import com.metaverse.workflow.model.TrainingTarget;

import java.util.List;

public class TrainingTargetResponseMapper {

    public static TrainingTargetResponse mapTrainingTarget(TrainingTarget request) {
        return TrainingTargetResponse.builder()
                .trainingTargetId(request.getTrainingTargetId())
                .financialYear(request.getFinancialYear())
                .q1(request.getQ1())
                .q2(request.getQ2())
                .q3(request.getQ3())
                .q4(request.getQ4())
                .yearlyTarget(request.getQ1() + request.getQ2() + request.getQ3() + request.getQ4())
                .agencyName(request.getAgency().getAgencyName())
                .activityName(request.getActivity().getActivityName())
                .build();
    }

    public static TrainingTargetSummaryResponse buildResponse(List<TrainingTarget> targets) {
        List<TrainingTargetResponse> responses = targets.stream()
                .map(TrainingTargetResponseMapper::mapTrainingTarget)
                .toList();

        List<String> headers = responses.stream()
                .map(TrainingTargetResponse::getFinancialYear)
                .map(year -> {
                    if (year == null || year.isBlank()) {
                        return "N/A"; // fallback
                    }

                    try {
                        if (year.contains("-")) {
                            // e.g. "2025-26" or "2025-2026"
                            String[] parts = year.split("-");
                            int start = Integer.parseInt(parts[0].trim());

                            // Handle "2025-26" → "2025-2026"
                            int end = parts[1].trim().length() == 2
                                    ? Integer.parseInt(parts[0].substring(0, 2) + parts[1].trim()) // "25" → "2025"
                                    : Integer.parseInt(parts[1].trim());

                            return start + "-" + end;
                        } else {
                            // Just "2025" → "2025-2026"
                            int start = Integer.parseInt(year.trim());
                            return start + "-" + (start + 1);
                        }
                    } catch (NumberFormatException e) {
                        return year; // fallback if parsing fails
                    }
                })
                .distinct()
                .toList();

        Double overallTarget = responses.stream()
                .mapToDouble(TrainingTargetResponse::getYearlyTarget)
                .sum();

        return TrainingTargetSummaryResponse.builder()
                .financialYearHeaders(headers)
                .overallTarget(overallTarget)
                .financialYear(responses)
                .build();
    }



}
