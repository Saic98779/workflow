package com.metaverse.workflow.counsellor.service;

import lombok.*;

@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CounsellorRegistrationRequest {
    private  String dateOfRegistration;
    private String nameOfCounsellor;
    private String dateOfBirth;
    private String gender;
    private String socialCategory;
    private String educationalQualification;
    private Integer districtId;
    private Integer mandalId;
    private String village;
    private String houseNo;
    private  String streetName;
    private  Integer pincode;
    private  String landmark;
    private Integer expriance;
    private String designation;
    private String specialzation;
    private  Long contactNo;
    private  Long altContactNo;
    private String emailId;
    private Integer allortedDistrictId;
    private Integer allortedMandalId;
    private String dateOfSelection;



}
