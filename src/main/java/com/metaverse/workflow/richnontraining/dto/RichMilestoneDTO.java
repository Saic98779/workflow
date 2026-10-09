package com.metaverse.workflow.richnontraining.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RichMilestoneDTO {
    private Long id;
    private String paymentIteration;
    private String paymentMilestone;
    private Double paymentPercentage;
    private Double amount;
    private Double consumedAmount;
    private Double availableAmount;
}
