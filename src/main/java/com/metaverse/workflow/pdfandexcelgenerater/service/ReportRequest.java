package com.metaverse.workflow.pdfandexcelgenerater.service;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ReportRequest {

    private List<Long> agencyIds;
    private String loginName;
    private ReportType reportType;
    private TrainingType trainingType;
    private DateType dateType;
    private String financialYear;
    private String fromDate;
    private String toDate;
}

