package com.metaverse.workflow.model.outcomes;

import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "outcome_oem")
public class OEM {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "oem_id")
    private Long oemId;

    @Column(name = "oem_registration_date")
    private Date oemRegistrationDate;

    @Column(name = "oem_registration_number")
    private String oemRegistrationNumber;

    @Column(name = "oem_targeted")
    private String oemTargeted;

    @Column(name = "oem_vendor_code")
    private String oemVendorCode;

    @Column(name = "products_supplied")
    private String productsSupplied;

    @Column(name = "vendor_registration_date")
    private Date vendorRegistrationDate;

    @Column(name = "first_po_date")
    private Date firstPurchaseOrderDate;

    @Column(name = "first_po_value")
    private Double firstPOValue;

    @Column(name = "monthly_supply_value")
    private Double currentMonthlySupplyValue;

    @Column(name = "certification_status")
    private Boolean isCertificationStatus;//Doubt

    @Column(name = "machinery_up_gradation")
    private String machineryUpGradation;

    @Column(name = "oem_audit_score")
    private String oemAuditScore;

    @Column(name = "is_influenced")
    private Boolean isInfluenced;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
    @Column(name = "created_on", insertable = true, updatable = false)
    @CreationTimestamp
    private Date createdOn;
    @Column(name = "updated_on", insertable = false, updatable = true)
    @UpdateTimestamp
    private Date updatedOn;

}
