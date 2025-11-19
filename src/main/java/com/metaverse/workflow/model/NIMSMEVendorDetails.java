package com.metaverse.workflow.model;

import com.metaverse.workflow.enums.BillRemarksStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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

    @Column(name = "order_upload")
    private String orderUpload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_activity_id", nullable = false)
    private NonTrainingSubActivity nonTrainingSubActivity;

    @Column(name = "status")
    private BillRemarksStatus status;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "nimsmeVendorDetails")
    private List<NonTrainingSpiuComments> spiuComments;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "nimsmeVendorDetails")
    private List<NonTrainingAgencyComments> agencyComments;

}
