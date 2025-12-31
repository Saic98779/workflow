package com.metaverse.workflow.model.aleap_handholding;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "trade_fair_participation")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeFairParticipation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handholding_support_id", nullable = false)
    private HandholdingSupport handholdingSupport;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private EventType eventType;

    @Column(name = "event_date")
    private Date eventDate;

    @Column(name = "event_location")
    private String eventLocation;

    @Column(name = "organized_by")
    private String organizedBy;

    public enum EventType {
        TRADE_FAIR,
        EXHIBITION,
        BUYER_SELLER_MEET
    }
}

