package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
@Data
@Entity
@Table(name = "benchmarking_study")
public class BenchmarkingStudy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "benchmarking_study_id")
    private Long benchmarkingStudyId;

    @Column(name = "thrust_sector_name")
    private String thrustSectorName;

    @Column(name = "name_of_the_cluster_mapped")
    private String nameOfTheClusterMapped ;

    @Column(name = "name_of_the_MSME_visited")
    private String nameOfTheMSMEVisited ;

    @Column(name = "date_of_visit")
    private Date dateOfVisit;

    @Column(name = "name_of_the_best_performing_state")
    private String nameOfTheBestPerformingState;

    @Column(name = "report_submission_date")
    private Date reportSubmissionDate;

    @Column(name = "mode_of_travel")
    private String modeOfTravel;

    @Column(name = "no_of_persons_traveled")
    private Integer noOfPersonsTraveled;

    @Column(name = "payment_date")
    private Date paymentDate;

    @Column(name = "expenditure_amount")
    private Double expenditureAmount;

    @Column(name = "bill_no")
    private String billNo;

    @Column(name = "bill_date")
    private Date billDate;

    @Column(name = "payee_name")
    private String payeeName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "ifsc_code")
    private String ifscCode;

    @Column(name = "mode_of_payment")
    private String modeOfPayment;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "bill_url")
    private String uploadBillUrl;

    @Column(name = "created_on", updatable = false)
    private Date createdOn;

    @Column(name = "updated_on", insertable = false, updatable = true)
    private Date updatedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_activity_id")
    private NonTrainingSubActivity nonTrainingSubActivity;

    @Column(name = "check_no")
    private String checkNo;

    @Column(name = "check_date")
    private Date checkDate;
}
