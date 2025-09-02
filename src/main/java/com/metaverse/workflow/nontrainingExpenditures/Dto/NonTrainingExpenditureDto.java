package com.metaverse.workflow.nontrainingExpenditures.Dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NonTrainingExpenditureDto {
    //it infrastructure
    private Long id;
    private String nonTrainingSubActivityId;
    private String dateOfPurchase;
    private String category;
    private String itemDetails;
    private String  paymentDate;
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
}
