package com.metaverse.workflow.model.outcomes;

import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "outcome_pms")
public class PMS {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pms_id")
    private Long pmsId;

    @Column(name = "business_turnover")
    private Double businessTurnover;

    @Column(name = "loan_number")
    private String loanNumber;//first loan

    @Column(name = "purpose_of_loan")
    private String purposeOfLoan;

    @Column(name = "loan_amount_released")
    private Double amountOfLoanReleased;

    @Column(name = "loan_release_date")
    private Date dateOfLoanReleased;

    @Column(name = "employment_created_direct")
    private Integer employmentCreatedDirect;


    @Column(name = "employment_created_in_direct")
    private Integer employmentCreatedInDirect;

    @Column(name = "repayment_amount")
    private Double repaymentAmount;

    @Column(name = "repayment_date")
    private Date dateOfRepayment;

    @Column(name = "upi_or_qr_code")
    private Boolean upiOrQrCode;

    @Column(name = "online_platform_used")
    private String onlinePlatformUsed;

    @Column(name="Influenced")
    Boolean isInfluenced;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
    @Column(name = "created_on", insertable = true, updatable = false)
    @CreationTimestamp
    private Date createdOn;
    @Column(name = "updated_on", insertable = false, updatable = true)
    @UpdateTimestamp
    private Date updatedOn;
}
