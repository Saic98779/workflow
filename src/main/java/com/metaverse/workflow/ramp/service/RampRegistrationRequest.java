package com.metaverse.workflow.ramp.service;

import lombok.Data;

@Data
public class RampRegistrationRequest {
    private Long programId;
    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
    private String email;
    private String phoneNo;
    private String aadhaarNo;
    private String udhyamRegistrationNo;
    private String socialCategory;
    private Boolean minorities;
    private Boolean disability;
    private String designation;
    private String organization;
    private String sector;
    private String address;
}
