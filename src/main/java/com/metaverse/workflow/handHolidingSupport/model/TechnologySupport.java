package com.metaverse.workflow.handHolidingSupport.model;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "technology_support")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnologySupport extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handholding_support_id", nullable = false)
    private HandholdingSupport handholdingSupport;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    public enum ServiceType {
        MACHINERY_IDENTIFICATION,
        VENDOR_CONNECTION,
        CFC_SUPPORT,
        TECHNOLOGY_CONSULTATION
    }
}

