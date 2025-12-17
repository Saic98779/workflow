package com.metaverse.workflow.pdfandexcelgenerater.service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExcelReportRow {

    private String agency;
    private String activity;
    private String subActivity;

    // Target
    private Long physicalTarget;
    private Long trainingAchievement;

    // Achievement
    private Double budgetAllocated;
    private Double expenditure;

    //Report Type
    private String reportType;
}