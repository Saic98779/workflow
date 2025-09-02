package com.metaverse.workflow.nontrainingExpenditures.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NonTrainingResourceExpenditureDto {
    private Long nonTrainingResourceExpenditureId;
    private Long resourceId;
    private Double amount;
    private String paymentForMonth;
    private String dateOfPayment;
}
