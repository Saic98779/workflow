package com.metaverse.workflow.nontrainingExpenditures.service;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class VisitDetailsResponseDTO {

    private Long visitDetailsId;

    private Long organizationId;
    private String organizationName;

    private Long subActivityId;
    private String subActivityName;

    private Date dateOfVisit;
    private String timeOfVisit;

    private List<String> resourceNames;

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
    private String districtName;
    private String mandalName;
}
