package com.metaverse.workflow.handHolidingSupport.model;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "loan_document_preparation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanDocumentPreparation extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finance_support_id", nullable = false)
    private FinanceSupport financeSupport;

    @Column(name = "details", length = 1000)
    private String details;
}

