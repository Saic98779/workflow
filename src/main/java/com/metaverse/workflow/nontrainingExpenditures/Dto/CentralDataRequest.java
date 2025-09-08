package com.metaverse.workflow.nontrainingExpenditures.Dto;

import lombok.Data;

@Data
public class CentralDataRequest {
    private Long subActivityId;   // FK to NonTrainingSubActivity
    private Integer noOfFileUploaded;
    private Double cost;
    private String billNo;
    private String billDate;
    private String payeeName;
    private String bankName;
    private String ifscCode;
    private String modeOfPayment;
    private String transactionId; // for UPI
    private String purpose;
    private Boolean verified;
    private String checkNo;
    private String checkDate;

}
