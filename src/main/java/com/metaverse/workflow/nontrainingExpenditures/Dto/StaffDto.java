package com.metaverse.workflow.nontrainingExpenditures.Dto;

import com.metaverse.workflow.nontrainingExpenditures.service.NonTrainingResourceExpenditureDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StaffDto {

    private Long id;
    private String nameOfTheStaff;
    private Long resourceId;
    private String designation;
    private Double relevantExperience;
    private String educationalQualification;
    private String dateOfJoining;
    private Double monthlySal;
    private String bankName;
    private String ifscCode;
    private String accountNo;
    private List<NonTrainingResourceExpenditureDTO> expenditures;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;

}
