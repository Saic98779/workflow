package com.metaverse.workflow.expenditure.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Builder
@Getter
@Setter
public class CombinedExpenditure {

    private String expenditureType;
    private String headOfExpense;
    private Double cost;
    private String billNo;
    private String billDate;
    private String payeeName;
    private String bankName;
    private String ifscCode;
    private String modeOfPayment;


}
