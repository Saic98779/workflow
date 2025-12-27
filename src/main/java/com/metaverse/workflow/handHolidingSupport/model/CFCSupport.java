package com.metaverse.workflow.handHolidingSupport.model;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "cfc_support")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CFCSupport extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technology_support_id", nullable = false)
    private TechnologySupport technologySupport;

    @Column(columnDefinition = "TEXT")
    private String technologyDetails;

    private String vendorName;

    private String vendorContactNo;

    private String vendorEmail;

    private Double approxCost;
}

