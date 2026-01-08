package com.metaverse.workflow.tgtpc_handholding.request_dto;
import com.metaverse.workflow.model.tgtpc_handholding.IECRegistrationCertification;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IECRegistrationCertificationRequest {
    private TGTPCHandholdingSupportRequest tgtpcHandholdingSupportRequest;
    private IECRegistrationCertification.SupportType supportType;
    // IEC Registration fields
    private String iecRegistrationNumber;
    private String registrationDate;
    // Certification fields
    private String certificationName;
    private String certificateNumber;
    private String certificateDate;

}

