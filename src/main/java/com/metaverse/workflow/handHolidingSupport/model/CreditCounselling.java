package com.metaverse.workflow.handHolidingSupport.model;

import com.metaverse.workflow.model.BaseEntity;
import com.metaverse.workflow.model.CounsellorRegistration;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "credit_counselling")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditCounselling extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finance_support_id", nullable = false)
    private FinanceSupport financeSupport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counsellor_id")
    private CounsellorRegistration counsellor;

    @Column(name = "counselling_date")
    private LocalDate counsellingDate;

    @Column(name = "subject_delivered", length = 1000)
    private String subjectDelivered;
}

