package com.metaverse.workflow.expenditure.service;

import com.metaverse.workflow.common.enums.PaymentType;
import jakarta.persistence.Column;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class BulkExpenditureRequest {
    private Long agencyId;
    private String itemName;
    private String purchaseDate;
    private Integer headOfExpenseId;
    private Integer purchasedQuantity;
    private Double unitCost;
    private String billNo;
    private String billDate;
    private String payeeName;
    private String bankName;
    private String ifscCode;
    private String transactionId;//for upi
    private PaymentType modeOfPayment;
    private String remarks;
    private String uploadBillUrl;
}
