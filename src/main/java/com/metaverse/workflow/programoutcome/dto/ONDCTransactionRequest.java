package com.metaverse.workflow.programoutcome.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ONDCTransactionRequest {
    public String productName;
    public String transactionDate;
    public Integer productQuantity;
    public String productUnits;
    public String transactionType;
    public Double transactionValue;
    private Long participantId;
    private Long organizationId;
    private Long agencyId;

}
