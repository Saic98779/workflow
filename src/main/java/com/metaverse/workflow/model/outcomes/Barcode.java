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

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="outcome_barcode")
public class Barcode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long barcodeId;

    @Column(name = "industry")
    private String industry;

    @Column(name = "location")
    private String location;

    @Column(name = "bar_code_type")
    private String barCodeType;

    @Column(name = "gs1_registration_number")
    private String gs1RegistrationNumber;

    @Column(name = "bar_code_coverage")
    private String barCodeCoverage;

    @Column(name = "revenue_from_bar_code_integration")
    private Double revenueFromBarCodeIntegration;

    @Column(name = "online_market_registered")
    private String onlineMarketRegistered;

    @Column(name = "date_of_registration")
    private Date dateOfRegistration;

    @Column(name = "value_of_transaction")
    private Double valueOfTransaction;

    @Column(name = "date_of_export")
    private Date dateOfExport;

    @Column(name = "value_of_export")
    private Double valueOfExport;

    @Column(name = "country_exported")
    private String countryExported;

    @Column(name = "Influenced")
    Boolean isInfluenced;
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
