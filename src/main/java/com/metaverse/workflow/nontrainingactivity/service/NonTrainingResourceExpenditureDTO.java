package com.metaverse.workflow.nontrainingactivity.service;

import lombok.Data;
@Data
public class NonTrainingResourceExpenditureDTO {
    private Long nonTrainingResourceExpenditureId;
    private Double amount;
    private String paymentForMonth;
    private String dateOfPayment;
    private Long resourceId;
}
