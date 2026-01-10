package com.metaverse.workflow.tgtpc_handholding.request_dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawMaterialSupportRequest {
    private TGTPCHandholdingSupportRequest tgtpcHandholdingSupportRequest;
    private String rawMaterialDetails;
    private String firstDateOfSupply;
    private Double cost;
}
