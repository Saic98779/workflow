package com.metaverse.workflow.expenditure.service;

import com.metaverse.workflow.enums.BillRemarksStatus;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProgramExpenditureResponse {

    private Long programExpenditureId;
    private Long activityId;
    private Long subActivityId;
    private Long programId;
    private Long agencyId;
    private String activityName;
    private String subActivityName;
    private String programName;
    private String agencyName;
    private String expenditureType;
    private String headOfExpense;
    private Double cost;
    private String billNo;
    private String billDate;
    private String payeeName;
    private String bankName;
    private String ifscCode;
    private String transactionId;//for upi
    private String modeOfPayment;
    private String purpose;
    private String uploadBillUrl;
    private List<Long> fileIds;
    private String checkNo;
    private String checkDate;
    private List<String> spiuComments;
    private List<String> agencyComments;
    private BillRemarksStatus status;
}
