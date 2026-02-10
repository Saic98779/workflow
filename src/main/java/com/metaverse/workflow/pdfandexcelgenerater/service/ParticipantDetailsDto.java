package com.metaverse.workflow.pdfandexcelgenerater.service;

import lombok.Data;

@Data
public class ParticipantDetailsDto {

    private String participantName;
    private String organizationName;
    private String organizationType;
    private String districtName;
    private String category;
    private Long mobileNumber;
}
