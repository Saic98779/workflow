package com.metaverse.workflow.nontraining.service;

import com.metaverse.workflow.model.TargetsBase;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class for ProgressMonitoringService helper methods.
 */
public class ProgressMonitoringUtils {

    /**
     * Converts a list of Object arrays to a map of Long -> Long.
     */
    public static Map<Long, Long> toLongMap(List<Object[]> rows) {
        return Optional.ofNullable(rows).orElse(List.of()).stream()
                .filter(r -> r[0] != null && r[1] != null)
                .collect(Collectors.toMap(
                        r -> (Long) r[0],
                        r -> (Long) r[1],
                        Long::sum
                ));
    }

    /**
     * Converts a list of Object arrays to a map of Long -> Double.
     */
    public static Map<Long, Double> toDoubleMap(List<Object[]> rows) {
        return Optional.ofNullable(rows).orElse(List.of()).stream()
                .filter(r -> r[0] != null && r[1] != null)
                .collect(Collectors.toMap(
                        r -> (Long) r[0],
                        r -> (Double) r[1],
                        Double::sum
                ));
    }

    /**
     * Builds a summary map from a list of targets.
     */
    public static <T extends TargetsBase> Map<Long, TargetSummary<T>> buildSummary(
            List<T> targets, Function<T, Long> idExtractor) {

        return Optional.ofNullable(targets).orElse(List.of()).stream()
                .map(t -> new AbstractMap.SimpleEntry<>(idExtractor.apply(t), t))
                .filter(e -> e.getKey() != null)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new TargetSummary<>(e.getValue()),
                        TargetSummary::merge
                ));
    }

    /**
     * Holds summary information for targets (total targets, total budget).
     */
    public static class TargetSummary<T extends TargetsBase> {
        public final T representative;
        public long totalTargets;
        public double totalBudget;

        public TargetSummary(T target) {
            this.representative = target;
            this.totalTargets = sumTargets(target);
            this.totalBudget = sumBudgets(target);
        }

        public static <T extends TargetsBase> TargetSummary<T> merge(TargetSummary<T> a, TargetSummary<T> b) {
            a.totalTargets += b.totalTargets;
            a.totalBudget += b.totalBudget;
            return a; // keep first representative
        }

        private static long sumTargets(TargetsBase t) {
            return Optional.ofNullable(t.getQ1Target()).orElse(0L)
                    + Optional.ofNullable(t.getQ2Target()).orElse(0L)
                    + Optional.ofNullable(t.getQ3Target()).orElse(0L)
                    + Optional.ofNullable(t.getQ4Target()).orElse(0L);
        }

        private static double sumBudgets(TargetsBase t) {
            return Optional.ofNullable(t.getQ1Budget()).orElse(0.0)
                    + Optional.ofNullable(t.getQ2Budget()).orElse(0.0)
                    + Optional.ofNullable(t.getQ3Budget()).orElse(0.0)
                    + Optional.ofNullable(t.getQ4Budget()).orElse(0.0);
        }
    }
}