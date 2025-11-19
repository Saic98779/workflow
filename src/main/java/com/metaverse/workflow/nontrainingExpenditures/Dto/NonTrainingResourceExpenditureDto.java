package com.metaverse.workflow.nontrainingExpenditures.Dto;

import com.metaverse.workflow.enums.BillRemarksStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<String> spiuComments;
    private List<String> agencyComments;
    private BillRemarksStatus status;
}
