package com.metaverse.workflow.programoutcome.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GeMTransactionRequest {

    public String procurementDate;
    public String productName;
    public String unitOfMeasurement;
    private String registeredAs; // Buyer or Seller
    public Integer quantity;
    public Double productValue;
    private Long participantId;
    private Long organizationId;
    private Long agencyId;


}
