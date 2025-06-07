package com.metaverse.workflow.organization.service;

import java.util.Date;
import java.util.List;

import com.metaverse.workflow.model.Sector;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrganizationRequest {

    private Long organizationId;
    private String organizationName;
    private String organizationCategory;
    private String organizationType;
    private String udyamRegistrationNo;
    private Date dateOfRegistration;
    private String startupCertificateNo;
    private String natureOfStartup;
    private String areasOfWorking;
    private Date incorporationDate;
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
    private List<Integer> sectorIds;
//    private String departmentName;
//    private String designation;


}
