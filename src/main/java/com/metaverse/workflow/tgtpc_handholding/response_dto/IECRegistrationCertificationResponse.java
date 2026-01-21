package com.metaverse.workflow.tgtpc_handholding.response_dto;

import com.metaverse.workflow.aleap_handholding.service.Participants;
import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IECRegistrationCertificationResponse {
    private Long id;
    private String supportType;
    private String iecRegistrationNumber;
    private String registrationDate;
    private String certificationName;
    private String certificateNumber;
    private String certificateDate;

    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String nonTrainingActivityName;
    private String nonTrainingSubActivityName;
    private String handholdingSupportBy;
    private Long organizationId;
    private String organizationName;
    private List<Participants> participants;
    private String handholdingDate;
    private String handholdingTime;
}
