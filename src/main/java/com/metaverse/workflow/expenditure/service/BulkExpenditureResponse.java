package com.metaverse.workflow.expenditure.service;

import com.metaverse.workflow.common.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class BulkExpenditureResponse {


    private Long bulkExpenditureId;
    private Long agencyId;
    private String agencyName;
    private String itemName;
    private String purchaseDate;
    private String headOfExpense;
    private Integer purchasedQuantity;
    private Double unitCost;
    private Double totalCost;
    private String billNo;
    private String billDate;
    private String payeeName;
    private String bankName;
    private String ifscCode;
    private String transactionId;//for upi
    private String modeOfPayment;
    private String remarks;
    private String uploadBillUrl;
    private List<Long> fileIds;
}
