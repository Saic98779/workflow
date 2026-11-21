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

    // true when this row's data could not be loaded; values are shown as 0 and the row is highlighted
    private boolean error;

    // Explanation shown in the Remarks column (error / no data)
    private String remarks;
}