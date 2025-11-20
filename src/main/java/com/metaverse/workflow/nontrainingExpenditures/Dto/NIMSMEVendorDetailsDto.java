package com.metaverse.workflow.nontrainingExpenditures.Dto;

import com.metaverse.workflow.enums.BillRemarksStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<String> spiuComments;
    private List<String> agencyComments;
    private BillRemarksStatus status;
}
