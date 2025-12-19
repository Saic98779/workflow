package com.metaverse.workflow.nontrainingExpenditures.service;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.List;

@Data
public class VisitDetailsRequest {
    private Long organizationId;
    private Long subActivityId;
    private String dateOfVisit;
    private String timeOfVisit;
    private List<Long> nonTrainingResourceIds;
    private String state;
    private String district;
    private String mandal;
    private String town;
    private String streetNo;
    private String houseNo;
    private Double latitude;
    private Double longitude;
    private Long contactNo;
    private String email;
    private Boolean withInHyderabad;

}