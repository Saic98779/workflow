package com.metaverse.workflow.handHolidingSupport.model;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "sector_advisory_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectorAdvisoryDetails extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handholding_support_id", nullable = false)
    private HandholdingSupport handholdingSupport;

    @Column(name = "advisory_date")
    private LocalDate advisoryDate;

    @Column(name = "advisory_details", length = 1000)
    private String advisoryDetails;
}
