package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.enums.BillRemarksStatus;
import lombok.Data;

import java.util.List;

@Data
public class NonTrainingExpenditureDTO {
    private Long id;
    private Long agencyId;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String dateOfPurchase;
    private String category;
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
    private String checkNo;
    private String checkDate;
    private List<String> spiuComments;
    private List<String> agencyComments;
    private BillRemarksStatus status;


}
