package com.metaverse.workflow.model;

import com.metaverse.workflow.enums.BillRemarksStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "nimsme_central_data")
public class NIMSMECentralData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="central_data_id")
    private Long centralDataId;

    @ManyToOne
    @JoinColumn(name = "sub_activity_id")
    private NonTrainingSubActivity nonTrainingSubActivity;

    @Column(name = "no_of_files_uploaded")
    private Integer noOfFileUploaded;

    @Column(name = "cost")
    private Double cost;

    @Column(name = "bill_no")
    private String billNo;

    @Column(name = "bill_date")
    private Date billDate;

    @Column(name = "payee_name")
    private String payeeName;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "ifsc_code")
    private String ifscCode;

    @Column(name = "mode_of_payment")
    private String modeOfPayment;

    @Column(name = "transaction_id")
    private String transactionId;//for upi

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "status")
    private Boolean verified;

    @Column(name = "check_no")
    private String checkNo;

    @Column(name = "check_date")
    private Date checkDate;

    @Column(name = "bill_url")
    private String uploadBillUrl;

    @Column(name = "created_on", insertable = true, updatable = false)
    @CreationTimestamp
    private Date createdOn;

    @Column(name = "updated_on", insertable = false, updatable = true)
    @UpdateTimestamp
    private Date updatedOn;

    @Column(name = "bill_status")
    private BillRemarksStatus billStatus;

    @OneToMany(mappedBy = "nimsmeCentralData")
    private List<NonTrainingSpiuComments> spiuComments;

    @OneToMany(mappedBy = "nimsmeCentralData")
    private List<NonTrainingAgencyComments> agencyComments;
}
