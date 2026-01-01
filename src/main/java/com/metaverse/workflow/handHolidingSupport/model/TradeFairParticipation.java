package com.metaverse.workflow.handHolidingSupport.model;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "trade_fair_participation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TradeFairParticipation extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packaging_branding_support_id", nullable = false)
    private PackagingBrandingSupport packagingBrandingSupport;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private EventType eventType;

    @Column(name = "event_date")
    private LocalDate eventDate;

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

