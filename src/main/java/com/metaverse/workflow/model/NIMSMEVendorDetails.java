package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "nimsme_vendor_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NIMSMEVendorDetails extends BaseEntity {

    @Column(name = "vendor_company_name")
    private String vendorCompanyName;

    @Column(name = "date_of_order")
    private Date dateOfOrder;

    @Column(name = "order_details", columnDefinition = "TEXT")
    private String orderDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_activity_id", nullable = false)
    private NonTrainingSubActivity nonTrainingSubActivity;
}
