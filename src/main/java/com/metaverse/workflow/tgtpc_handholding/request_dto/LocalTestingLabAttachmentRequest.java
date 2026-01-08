package com.metaverse.workflow.tgtpc_handholding.request_dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocalTestingLabAttachmentRequest {
    private TGTPCHandholdingSupportRequest tgtpcHandholdingSupportRequest;
    private String labOrCfcName;
    private String purposeOfAttachment;
    private String dateOfAttachment;
}
