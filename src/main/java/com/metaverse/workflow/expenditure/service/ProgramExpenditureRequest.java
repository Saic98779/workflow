package com.metaverse.workflow.expenditure.service;

import com.metaverse.workflow.common.enums.ExpenditureType;
import com.metaverse.workflow.common.enums.PaymentType;
import com.metaverse.workflow.model.HeadOfExpense;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProgramExpenditureRequest {


    private Long activityId;
    private Long subActivityId;
    private Long programId;
    private Long agencyId;
    private ExpenditureType expenditureType;
    private Integer headOfExpenseId;
    private Double cost;
    private String billNo;
    private String billDate;
    private String payeeName;
    private String bankName;
    private String ifscCode;
    private String transactionId;//for upi
    private PaymentType modeOfPayment;
    private String purpose;
    private String uploadBillUrl;

}
