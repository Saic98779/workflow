package com.metaverse.workflow.handHolidingSupport.model;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "vendor_connection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VendorConnection extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technology_support_id", nullable = false)
    private TechnologySupport technologySupport;

    private String vendorSuggested;

    private LocalDate quotationDate;

    @Column(columnDefinition = "TEXT")
    private String details;

    private Double cost;
}
