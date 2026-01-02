package com.metaverse.workflow.model.aleap_handholding;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "vendor_connection")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorConnection extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handholding_support_id", nullable = false)
    private HandholdingSupport handholdingSupport;

    @Column(name= "vendor_suggested")
    private String vendorSuggested;

    @Column(name= "quotation_date")
    private Date quotationDate;

    @Column(name="details",columnDefinition = "TEXT")
    private String details;

    @Column(name="cost")
    private Double cost;
}
