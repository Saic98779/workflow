package com.metaverse.workflow.nontrainingExpenditures.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelAndTransportDto {

    private Long travelTransportId;
    private String dateOfTravel;
    private String purposeOfTravel;
    private String modeOfTravel;
    private String destination;
    private Integer noOfPersonsTraveled;
    private Double amount;
    private String billNo;
    private String billDate;
    private String modeOfPayment;
    private String payeeName;
    private String transactionId;
    private String bank;
    private String ifscCode;
    private String purpose;
    private String billInvoicePath;
    private Long nonTrainingSubActivityId;
    private String checkNo;
    private String checkDate;

}
