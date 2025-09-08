package com.metaverse.workflow.nontrainingExpenditures.Dto;

import lombok.Data;

@Data
public class CentralDataResponse {
    private Long id;
    private Long subActivityId;
    private String subActivityName;
    private Integer noOfFileUploaded;
    private Double cost;
    private String billNo;
    private String billDate;
    private String payeeName;
    private String bankName;
    private String ifscCode;
    private String modeOfPayment;
    private String transactionId;
    private String purpose;
    private Boolean verified;
    private String checkNo;
    private String checkDate;
    private String uploadBillUrl;
}
