package com.metaverse.workflow.model.aleap_handholding;

import com.metaverse.workflow.model.BaseEntity;
import com.metaverse.workflow.model.Organization;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "access_to_technology_infrastructure")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
    public class AccessToTechnologyAndInfrastructure extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handholding_support_id", nullable = false)
    private HandholdingSupport handholdingSupport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(name = "access_to_technology_type")
    private String accessToTechnologyType;

    @Column(name = "vendor_suggested")
    private String vendorSuggested;

    @Column(name = "quotation_date")
    private Date quotationDate;

    @Column(name = "vendor_details", columnDefinition = "TEXT")
    private String details;

    @Column(name = "vendor_cost")
    private Double cost;

    @Column(columnDefinition = "TEXT")
    private String requirement;

    @Column(name = "existing_machinery")
    private String existingMachinery;

    @Column(name = "suggested_machinery")
    private String suggestedMachinery;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "grounding_date")
    private Date groundingDate;

    @Column(name = "place_of_installation")
    private String placeOfInstallation;

    @Column(name = "cost_of_machinery")
    private Double costOfMachinery;

    @Column(name = "technology_details", columnDefinition = "TEXT")
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
