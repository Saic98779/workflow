package com.metaverse.workflow.ramp.service;

import lombok.Data;

@Data
public class RampEnrollmentRequest {
    private String rampWorkshop;
    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
    private String email;
    private String phone;
    private String aadhaarNo;
    private String udhyamRegistrationNo;
    private String socialCategory;
    private Boolean minorities;
    private Boolean disability;
    private String designation;
    private String organization;
    private String sector;
    private String address;
    private String district;
    private String mandal;
}

