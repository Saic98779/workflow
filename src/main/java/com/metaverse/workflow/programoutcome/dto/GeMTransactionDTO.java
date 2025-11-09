package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeMTransactionDTO {
    private Long id;
    private String gemRegistrationId;
    private Date procurementDate;
    private String productName;
    private String unitOfMeasurement;
    private String registeredAs; // Buyer or Seller
    private Integer quantity;
    private Double productValue;

    private String agencyName;
    private String participantName;
    private String organizationName;

    private Date createdOn;
    private Date updatedOn;
}

