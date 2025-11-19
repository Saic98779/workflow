package com.metaverse.workflow.nontrainingExpenditures.Dto;

import com.metaverse.workflow.enums.BillRemarksStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;

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
    private BillRemarksStatus billStatus;
    private List<String> spiuComments;
    private List<String> agencyComments;
}
