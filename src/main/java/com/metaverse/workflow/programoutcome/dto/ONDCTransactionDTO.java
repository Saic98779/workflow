package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ONDCTransactionDTO {
    private Long id;
    private String productName;
    private Date transactionDate;
    private Integer productQuantity;
    private String productUnits;
    private String transactionType;
    private Double transactionValue;
    private String ondcRegistrationNo;
    private Date createdOn;
    private Date updatedOn;
}
