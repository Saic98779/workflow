package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "TGTPC4_NT_Handholding")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TGTPC4NTHandholding extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_activity_id", nullable = false)
    private NonTrainingSubActivity nonTrainingSubActivity;

    // Name of the MSME
    @Column(name = "msme_name")
    private String msmeName;

    // Imported components analysed for substitution
    @Column(name = "imported_components", columnDefinition = "TEXT")
    private String importedComponents;

    // Domesti substitute design developled (Yes/No)
    @Column(name = "design_developed")
    private Boolean designDeveloped;

    // Date of adoption
    @Temporal(TemporalType.DATE)
    @Column(name = "adoption_date")
    private Date adoptionDate;

    // Name of the Domestic substitute products / components tested
    @Column(name = "domestic_products_tested", columnDefinition = "TEXT")
    private String domesticProductsTested;

    // Name of the accredited testing lab facilitated
    @Column(name = "testing_lab")
    private String testingLab;

    // Name of the test
    @Column(name = "test_name")
    private String testName;

    // Name of the quality certifications obtained
    @Column(name = "quality_certifications")
    private String qualityCertifications;

    // Name of the Domestic Buyer onboarded for substitute products
    @Column(name = "domestic_buyer")
    private String domesticBuyer;

    // Import substitution product catalogue
    @Column(name = "product_catalogue", columnDefinition = "TEXT")
    private String productCatalogue;

    // Name of the manufacturing lin planned for substituion
    @Column(name = "manufacturing_line")
    private String manufacturingLine;

    // Date of commencement of domestic production of substitute products
    @Column(name = "production_start_date")
    private Date productionStartDate;

    // Name of the Scheme converged
    @Column(name = "scheme_name")
    private String schemeName;

    // Value of Investment
    @Column(name = "investment_value")
    private Double investmentValue;

    // Subsidy through scheme
    @Column(name = "scheme_subsidy")
    private Double schemeSubsidy;

    // Date of release
    @Column(name = "release_date")
    private Date releaseDate;

    // Date of DPR submission
    @Column(name = "dpr_submission_date")
    private Date dprSubmissionDate;

    // Date of sanction
    @Column(name = "sanction_date")
    private Date sanctionDate;

    // Amount of sanction
    @Column(name = "sanction_amount")
    private Double sanctionAmount;

    // Name of the Bank / NBFC supported finance
    @Column(name = "bank_nbfc")
    private String bankNbfc;

    // Date of import substitute products launched
    @Column(name = "product_launch_date")
    private Date productLaunchDate;

    // Name of the import substitute products launched
    @Column(name = "launched_products")
    private String launchedProducts;

    // Name of the technology design mapped
    @Column(name = "technology_design")
    private String technologyDesign;

    // Name of the technical instituion engaged
    @Column(name = "technical_institution")
    private String technicalInstitution;

    // Name of the IPR adopted
    @Column(name = "ipr_name")
    private String iprName;

    // Date of IPR registered
    @Column(name = "ipr_registration_date")
    private Date iprRegistrationDate;
}

