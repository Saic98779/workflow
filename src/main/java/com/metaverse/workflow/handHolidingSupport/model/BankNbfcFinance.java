package com.metaverse.workflow.handHolidingSupport.model;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "bank_nbfc_finance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankNbfcFinance extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finance_support_id", nullable = false)
    private FinanceSupport financeSupport;

    @Column(name = "institution_name")
    private String institutionName;

    @Column(name = "branch_name")
    private String branchName;

    @Column(name = "dpr_submission_date")
    private LocalDate dprSubmissionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "application_status")
    private ApplicationStatus status;

    @Column(name = "sanction_date")
    private LocalDate sanctionDate;

    @Column(name = "sanctioned_amount")
    private Double sanctionedAmount;

    @Column(name = "details", length = 1000)
    private String details;

    public enum ApplicationStatus {
        SUBMITTED,
        UNDER_PROCESS,
        APPROVED,
        REJECTED
    }
}
