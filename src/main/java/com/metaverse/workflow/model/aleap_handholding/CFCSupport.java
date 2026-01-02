package com.metaverse.workflow.model.aleap_handholding;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
@Setter
@Entity
@Table(name = "cfc_support")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CFCSupport extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handholding_support_id", nullable = false)
    private HandholdingSupport handholdingSupport;

    @Column(columnDefinition = "TEXT")
    private String technologyDetails;

    @Column(name = "vendor_name")
    private String vendorName;

    @Column(name = "vendor_contact_no")
    private String vendorContactNo;

    @Column(name = "vendor_email")
    private String vendorEmail;

    @Column(name = "approx_cost")
    private Double approxCost;

}

