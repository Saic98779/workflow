package com.metaverse.workflow.model.aleap_handholding;

import com.metaverse.workflow.model.BaseEntity;
import com.metaverse.workflow.model.CounsellorRegistration;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "credit_counselling")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCounselling extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handholding_support_id", nullable = false)
    private HandholdingSupport handholdingSupport;

    @Column(name ="counselled_by")
    private String counselledBy;

    @Column(name = "counselling_date")
    private Date counsellingDate;

    @Column(name = "subject_delivered", columnDefinition = "TEXT")
    private String subjectDelivered;
}

