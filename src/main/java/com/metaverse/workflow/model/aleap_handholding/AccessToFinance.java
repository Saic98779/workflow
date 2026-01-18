package com.metaverse.workflow.model.aleap_handholding;

import com.metaverse.workflow.model.BaseEntity;
import com.metaverse.workflow.model.InfluencedParticipant;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "access_to_finance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessToFinance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handholding_support_id", nullable = false)
    private HandholdingSupport handholdingSupport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(name = "access_to_finance_type")
    private String accessToFinanceType;

    @Column(name = "scheme_name")
    private String schemeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "govt_application_status")
    private ApplicationStatus govtApplicationStatus;

    @Column(name = "govt_sanction_date")
    private Date govtSanctionDate;

    @Column(name = "govt_sanctioned_amount")
    private Double govtSanctionedAmount;

    @Column(name = "govt_details", columnDefinition = "TEXT")
    private String govtDetails;

    @Column(name = "institution_name")
    private String institutionName;

    @Column(name = "branch_name")
    private String branchName;

    @Column(name = "dpr_submission_date")
    private Date dprSubmissionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "bank_application_status")
    private ApplicationStatus bankApplicationStatus;

    @Column(name = "bank_sanction_date")
    private Date bankSanctionDate;

    @Column(name = "bank_sanctioned_amount")
    private Double bankSanctionedAmount;

    @Column(name = "bank_details", columnDefinition = "TEXT")
    private String bankDetails;

    @Column(name = "counselled_by")
    private String counselledBy;

    @Column(name = "counselling_date")
    private Date counsellingDate;

    @Column(name = "subject_delivered", columnDefinition = "TEXT")
    private String subjectDelivered;

    public enum ApplicationStatus {
        SUBMITTED,
        UNDER_PROCESS,
        APPROVED,
        REJECTED
    }

    @Column(name = "loan_document_details", columnDefinition = "TEXT")
    private String loanDocumentDetails;

    @ManyToMany
    @JoinTable(
            name = "access_to_finance_participants",
            joinColumns = @JoinColumn(name = "access_to_finance_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    private List<Participant> participants;

    @ManyToMany
    @JoinTable(
            name = "access_to_finance_influenced_participants",
            joinColumns = @JoinColumn(name = "access_to_finance_id"),
            inverseJoinColumns = @JoinColumn(name = "influenced_participant_id")
    )
    private List<InfluencedParticipant> influencedParticipants;

}
