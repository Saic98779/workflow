package com.metaverse.workflow.handHolidingSupport.model;

import com.metaverse.workflow.model.BaseEntity;
import com.metaverse.workflow.model.aleap_handholding.HandholdingSupport;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "govt_scheme_application")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GovtSchemeApplication extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handholding_support_id", nullable = false)
    private HandholdingSupport handholdingSupport;

    @Column(name = "application_no")
    private String applicationNo;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(name = "application_date")
    private LocalDate applicationDate;

    @Column(name = "sanction_details", length = 1000)
    private String sanctionDetails;

    @Column(name = "sanction_date")
    private LocalDate sanctionDate;

    @Column(name = "sanction_amount")
    private Double sanctionedAmount;

    public enum ApplicationStatus {
        SUBMITTED,
        UNDER_PROCESS,
        APPROVED,
        REJECTED
    }
}

