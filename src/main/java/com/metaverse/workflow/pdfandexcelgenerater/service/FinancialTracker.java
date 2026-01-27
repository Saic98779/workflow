package com.metaverse.workflow.pdfandexcelgenerater.service;

import lombok.Data;

@Data
public class FinancialTracker {
    private String agencyName;
    private Double totalBillsUploaded;
    private Double approved;
    private Double rejected;
    private Double needClarification;
    private Double totalBillsVerified;
}
