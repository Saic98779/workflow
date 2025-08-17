package com.metaverse.workflow.nontrainingactivity.service;

import lombok.Data;

import java.util.Date;
@Data
public class NonTrainingExpenditureDTO {
    private Long id;
    private Long agencyId;
    private Long activityId;
    private Date paymentDate;
    private Double expenditureAmount;
    private String billNo;
    private Date billDate;
    private String payeeName;
    private String accountNumber;
    private String bankName;
    private String ifscCode;
    private String modeOfPayment;
    private String transactionId;
    private String purpose;
    private String uploadBillUrl;
    private String createdOn;
    private String updatedOn;

}
