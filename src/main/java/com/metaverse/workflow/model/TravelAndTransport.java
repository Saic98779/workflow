package com.metaverse.workflow.model;

import com.metaverse.workflow.common.enums.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

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
    private String checkNo;
    private Date checkDate;
    @JoinColumn(name = "subActivityId")
    @ManyToOne
    private NonTrainingSubActivity nonTrainingSubActivity;

    @Column(name = "created_on", updatable = false)
    private String createdOn;

    @Column(name = "updated_on", insertable = false, updatable = true)
    private String updatedOn;
}
