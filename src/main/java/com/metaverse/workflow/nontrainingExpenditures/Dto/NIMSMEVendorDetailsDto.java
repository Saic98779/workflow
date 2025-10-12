package com.metaverse.workflow.nontrainingExpenditures.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NIMSMEVendorDetailsDto {

    private Long vendorId;
    private String vendorCompanyName;
    private String dateOfOrder;
    private String orderDetails;
    private String orderUpload;
    private Long subActivityId;
}
