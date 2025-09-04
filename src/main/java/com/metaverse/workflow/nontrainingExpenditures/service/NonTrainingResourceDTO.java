package com.metaverse.workflow.nontrainingExpenditures.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NonTrainingResourceDTO {
    private Long resourceId;
    private String name;
    private String designation;
    private Double relevantExperience;
    private String educationalQualification;
    private String dateOfJoining;
    private Double monthlySal;
    private String bankName;
    private String ifscCode;
    private String accountNo;
    private List<NonTrainingResourceExpenditureDTO> expenditures;
    private Long nonTrainingSubActivityId;
    private Long nonTrainingActivityId;
}
