package com.metaverse.workflow.model.aleap_handholding;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "govt_scheme_finance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GovtSchemeFinance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handholding_support_id", nullable = false)
    private HandholdingSupport handholdingSupport;

    @Column(name = "scheme_name")
    private String schemeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "application_status")
    private ApplicationStatus status;

    @Column(name = "sanction_date")
    private Date sanctionDate;

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

