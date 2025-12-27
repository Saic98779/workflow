package com.metaverse.workflow.handHolidingSupport.model;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "finance_support")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinanceSupport extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handholding_support_id", nullable = false)
    private HandholdingSupport handholdingSupport;

    @Enumerated(EnumType.STRING)
    @Column(name = "linking_with", nullable = false)
    private LinkingWith linkingWith;

    public enum LinkingWith {
        BANK_NBFC,
        GOVT_SCHEME,
        PREP_OF_LOAN_DOCS,
        CREDIT_COUNSELLING
    }
}


