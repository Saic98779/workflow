package com.metaverse.workflow.model;

import com.metaverse.workflow.common.enums.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class TravelAndTransport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long travelTransportId;
    private LocalDate dateOfTravel;
    private String purposeOfTravel;
    private String modeOfTravel;
    private String destination;
    private Integer noOfPersonsTraveled;
    private Double amount;
    private String billNo;
    private LocalDate billDate;
    private PaymentType modeOfPayment;
    private String payeeName;
    private String transactionId;
    private String bank;
    private String ifscCode;
    private String purpose;
    private String billInvoicePath;

    @JoinColumn(name = "subActivityId")
    @ManyToOne(cascade = CascadeType.ALL)
    private NonTrainingSubActivity nonTrainingSubActivity;
}
