package com.metaverse.workflow.nontrainingactivity.service;

import lombok.Data;

import java.util.List;
@Data
public class NonTrainingResourceDTO {
    private Long resourceId;
    private String name;
    private String designation;
    private Double relevantExperience;
    private String educationalQualifications;
    private String dateOfJoining;
    private Double monthlySal;
    private String bankName;
    private String ifscCode;
    private String accountNo;
    private List<NonTrainingResourceExpenditureDTO> expenditures;
}
