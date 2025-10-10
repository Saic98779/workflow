package com.metaverse.workflow.nontrainingExpenditures.Dto;

import lombok.Data;

import java.util.Date;

@Data
public class NIMSMEContentDetailsDto {

    private Long id;
    private String contentType;
    private String contentName;
    private Integer durationOrPages;
    private String topic;
    private String status;
    private Date dateOfUpload;
    private String url;
    private Long subActivityId;
}
