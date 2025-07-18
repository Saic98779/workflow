package com.metaverse.workflow.expenditure.service;

import com.metaverse.workflow.enums.BillRemarksStatus;
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
public class BulkTransactions {
    private Long bulkExpenditureTransactionId;
    private String itemName;
    private Integer purchasedQuantity;
    private Double unitCost;
    private Long bulkExpenditureId;
    private String purchaseDate;
    private Integer consumedQuantity;
    private Integer availableQuantity;
    private String expenditureType;
    private String headOfExpense;
    private Double allocatedCost;
    private String billNo;
    private Date billDate;
    private String payeeName;
    private String bankName;
    private String ifscCode;
    private String modeOfPayment;
    private String remarks;
    private String uploadBillUrl;
    private List<String> spiuComments;
    private List<String> agencyComments;
    private BillRemarksStatus status;
}

