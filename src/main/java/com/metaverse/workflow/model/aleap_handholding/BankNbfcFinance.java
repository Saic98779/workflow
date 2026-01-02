package com.metaverse.workflow.model.aleap_handholding;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Builder
@Entity
@Table(name = "bank_nbfc_finance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankNbfcFinance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handholding_support_id", nullable = false)
    private HandholdingSupport handholdingSupport;

    @Column(name = "institution_name")
    private String institutionName;

    @Column(name = "branch_name")
    private String branchName;

    @Column(name = "dpr_submission_date")
    private Date dprSubmissionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "application_status")
    private ApplicationStatus status;

    @Column(name = "sanction_date")
    private Date sanctionDate;

    @Column(name = "sanctioned_amount")
    private Double sanctionedAmount;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    public enum ApplicationStatus {
        SUBMITTED,
        UNDER_PROCESS,
        APPROVED,
        REJECTED
    }
}
