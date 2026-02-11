package com.metaverse.workflow.pdfandexcelgenerater.service;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ParticipantExcelDto {

    private Long sno;
    private String agencyName;
    private String programName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String district;
    private String isAspirant;

    private String organizationName;
    private String organizationType;

    private String participantName;
    private String gender;
    private String category;
    private String disability;
    private String aadharNo;
    private String participantMobileNo;
    private String participantEmail;
    private String participantDesignation;

    private String udyamRegistrationNo;
    private String sectors;

    private String organizationMobileNo;
    private String organizationEmail;
    private String ownerName;
    private String ownerMobileNo;
    private String ownerEmail;
}
