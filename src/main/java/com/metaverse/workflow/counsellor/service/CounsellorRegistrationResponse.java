package com.metaverse.workflow.counsellor.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CounsellorRegistrationResponse {
    private Long counsellorRegistrationId;
    private String dateOfRegistration;
    private String nameOfCounsellor;
    private String dateOfBirth;
    private String gender;
    private String socialCategory;
    private String educationalQualification;
    private Integer districtId;
    private String districtName;
    private Integer mandalId;
    private String mandalName;
    private String village;
    private String houseNo;
    private String streetName;
    private Integer pincode;
    private String landmark;
    private Integer experience;
    private String designation;
    private String specialization;
    private Long contactNo;
    private Long altContactNo;
    private String emailId;
    private Integer allocatedDistrictId;
    private String allocatedDistrictName;
    private Integer allocatedMandalId;
    private String allocatedMandalName;
}
