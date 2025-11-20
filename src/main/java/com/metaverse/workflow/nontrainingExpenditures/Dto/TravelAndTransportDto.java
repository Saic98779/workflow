package com.metaverse.workflow.nontrainingExpenditures.Dto;

import com.metaverse.workflow.enums.BillRemarksStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
    private List<String> spiuComments;
    private List<String> agencyComments;
    private BillRemarksStatus status;

}
