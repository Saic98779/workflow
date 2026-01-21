package com.metaverse.workflow.tgtpc_handholding.response_dto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TGTPC4NTHandholdingResponse {

    private Long id;
    private Long nonTrainingSubActivityId;
    private Long organizationId;
    private String organizationName;
    private String nameOfTheSector;
    private String nameOfTheDomesticSupplier;
    private String nameOfTheRawMaterial;
    private String importedComponents;
    private Boolean designDeveloped;
    private String adoptionDate;
    private String domesticProductsTested;
    private String testingLab;
    private String testName;
    private String qualityCertifications;
    private String domesticBuyer;
    private String productCatalogue;
    private String manufacturingLine;
    private String productionStartDate;
    private String schemeName;
    private Double investmentValue;
    private Double schemeSubsidy;
    private String releaseDate;
    private String dprSubmissionDate;
    private String sanctionDate;
    private Double sanctionAmount;
    private String bankNbfc;
    private String productLaunchDate;
    private String launchedProducts;
    private String technologyDesign;
    private String technicalInstitution;
    private String iprName;
    private String iprRegistrationDate;
}
