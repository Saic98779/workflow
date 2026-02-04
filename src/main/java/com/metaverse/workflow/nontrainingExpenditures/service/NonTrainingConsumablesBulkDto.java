package com.metaverse.workflow.nontrainingExpenditures.service;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NonTrainingConsumablesBulkDto {

    private Long Id;
    private Long agencyId;
    private Long subActivityId;

    private String itemName;
    private String purchaseDate;
    private Integer purchasedQuantity;
    private Double unitCost;
    private Integer consumedQuantity;
    private Integer availableQuantity;
    private Double totalCost;

    private String billNo;
    private String billDate;
    private String modeOfPayment;
    private String payeeName;
    private String bankName;
    private String ifscCode;
    private String transactionId;
    private String checkNo;
    private String checkDate;

    private String uploadBillUrl;
    private String purchaseOrderNo;
    private String nameOfTheVendor;
}
