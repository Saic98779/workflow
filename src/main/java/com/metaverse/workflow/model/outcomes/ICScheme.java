package com.metaverse.workflow.model.outcomes;

import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "outcome_ic_scheme")
public class ICScheme {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ic_scheme_id")
    private Long icSchemeId;

    @Column(name = "industry_name")
    private String industryName;

    @Column(name = "location")
    private String location;

    @Column(name = "type_of_msme")
    private String typeOfMsme;

    @Column(name = "annual_turnover")
    private Double annualTurnover;

    @Column(name = "domestic_sales")
    private Double domesticSales;

    @Column(name = "export_market_amount")
    private Double exportMarket;

    @Column(name = "employment_direct")
    private Integer employmentDirect;

    @Column(name = "employment_indirect")
    private Integer employmentIndirect;

    @Temporal(TemporalType.DATE)
    private Date dateOfExport;

    @Column(name = "value_of_export")
    private Double valueOfExport;

    @Column(name = "export_market_details")
    private String exportMarketDetails;

    @Column(name="Influenced")
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
