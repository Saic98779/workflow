package com.metaverse.workflow.model;

import com.metaverse.workflow.common.enums.PaymentType;
import com.metaverse.workflow.enums.BillRemarksStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @OneToMany(mappedBy = "travelAndTransport",
            cascade = CascadeType.ALL,   // delete children automatically
            orphanRemoval = true)        // remove orphans
    private List<ProgramSessionFile> programSessionFiles = new ArrayList<>();

    @Column(name = "created_on", updatable = false)
    private Date createdOn;

    @Column(name = "updated_on", insertable = false, updatable = true)
    private Date updatedOn;

    @Column(name = "status")
    private BillRemarksStatus status;
    
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "travelAndTransport")
    private List<NonTrainingSpiuComments> spiuComments;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "travelAndTransport")
    private List<NonTrainingAgencyComments> agencyComments;
}
