package com.metaverse.workflow.nontrainingExpenditures.Dto;

import com.metaverse.workflow.enums.BillRemarksStatus;
import lombok.Data;

import java.util.List;

@Data
public class BenchmarkingStudyResponse {
    private Long thrustSectorVisitId;
    private String thrustSectorName;
    private String nameOfTheClusterMapped;
    private String nameOfTheMSMEVisited;
    private String dateOfVisit;
    private String paymentDate;
    private Double expenditureAmount;
    private String billNo;
    private String billDate;
    private String payeeName;
    private String accountNumber;
    private String bankName;
    private String ifscCode;
    private String modeOfPayment;
    private String transactionId;
    private String purpose;
    private String uploadBillUrl;
    private String nameOfTheBestPerformingState;
    private String reportSubmissionDate;
    private String modeOfTravel;
    private String checkNo;
    private String checkDate;
    private BillRemarksStatus status;
    private List<String> spiuComments;
    private List<String> agencyComments;
}
