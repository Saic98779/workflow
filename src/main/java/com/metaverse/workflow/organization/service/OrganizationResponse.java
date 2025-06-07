package com.metaverse.workflow.organization.service;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationResponse {

    private Long organizationId;
    private String organizationName;
    private String organizationCategory;
    private String organizationType;
    private String udyamregistrationNo;
    private String dateOfRegistration;
    private String startupCertificateNo;
    private String natureOfStartup;
    private String areasOfWorking;
    private String incorporationDate;
    private String dateOfIssue;
    private String validUpto;
    private String stateId;
    private String distId;
    private String mandal;
    private String town;
    private String streetNo;
    private String houseNo;
    private Double latitude;
    private Double longitude;
    private Long contactNo;
    private String email;
    private String website;
    private String ownerName;
    private Long ownerContactNo;
    private String ownerEmail;
    private String ownerAddress;
    private String nameOfTheSHG;
    private String nameOfTheVO;
    private String gramaPanchayat;
	private List<Sector> sectorList;

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
    public static class Sector {
		private Integer sectorId;
		private String sectorName;
    }

}


