package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.repository.CFCSupportRepository;
import com.metaverse.workflow.aleap_handholding.repository.MachineryIdentificationRepository;
import com.metaverse.workflow.aleap_handholding.repository.VendorConnectionRepository;
import com.metaverse.workflow.aleap_handholding.response_dto.AccessToTechnologyAndInfrastructureResponse;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessToTechnologyAndInfrastructureService {
    private final VendorConnectionRepository vendorConnectionRepository;
    private final MachineryIdentificationRepository machineryIdentificationRepository;
    private final CFCSupportRepository cfcSupportRepository;

    public WorkflowResponse getAccessToTechnologyAndInfrastructure(Long subActivityId) {

        List<AccessToTechnologyAndInfrastructureResponse> responses = new ArrayList<>();

        // -------- Vendor Connection --------
        vendorConnectionRepository
                .findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId)
                .forEach(v -> responses.add(
                        AccessToTechnologyAndInfrastructureResponse.builder()
                                .vendorConnectionId(v.getId())
                                .handholdingSupportId(v.getHandholdingSupport() != null ? v.getHandholdingSupport().getId() : null)
                                .nonTrainingActivityId(v.getHandholdingSupport() != null &&
                                        v.getHandholdingSupport().getNonTrainingSubActivity() != null
                                        ? v.getHandholdingSupport().getNonTrainingSubActivity().getNonTrainingActivity().getActivityId()
                                        : null)
                                .nonTrainingSubActivityId(v.getHandholdingSupport() != null &&
                                        v.getHandholdingSupport().getNonTrainingSubActivity() != null
                                        ? v.getHandholdingSupport().getNonTrainingSubActivity().getSubActivityId()
                                        : null)
                                .nonTrainingActivityName(v.getHandholdingSupport() != null &&
                                        v.getHandholdingSupport().getNonTrainingSubActivity() != null
                                        ? v.getHandholdingSupport().getNonTrainingSubActivity().getNonTrainingActivity().getActivityName()
                                        : null)
                                .nonTrainingSubActivityName(v.getHandholdingSupport() != null &&
                                        v.getHandholdingSupport().getNonTrainingSubActivity() != null
                                        ? v.getHandholdingSupport().getNonTrainingSubActivity().getSubActivityName()
                                        : null)
                                .vendorSuggested(v.getVendorSuggested())
                                .quotationDate(DateUtil.dateToString(v.getQuotationDate(), "dd-MM-yyyy"))
                                .details(v.getDetails())
                                .cost(v.getCost())
                                .build()
                ));

        // -------- Machinery Identification --------
        machineryIdentificationRepository
                .findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId)
                .forEach(m -> responses.add(
                        AccessToTechnologyAndInfrastructureResponse.builder()
                                .machineryIdentificationId(m.getId())
                                .handholdingSupportId(m.getHandholdingSupport() != null ? m.getHandholdingSupport().getId() : null)
                                .nonTrainingActivityId(m.getHandholdingSupport() != null &&
                                        m.getHandholdingSupport().getNonTrainingSubActivity() != null
                                        ? m.getHandholdingSupport().getNonTrainingSubActivity().getNonTrainingActivity().getActivityId()
                                        : null)
                                .nonTrainingSubActivityId(m.getHandholdingSupport() != null &&
                                        m.getHandholdingSupport().getNonTrainingSubActivity() != null
                                        ? m.getHandholdingSupport().getNonTrainingSubActivity().getSubActivityId()
                                        : null)
                                .nonTrainingActivityName(m.getHandholdingSupport() != null &&
                                        m.getHandholdingSupport().getNonTrainingSubActivity() != null
                                        ? m.getHandholdingSupport().getNonTrainingSubActivity().getNonTrainingActivity().getActivityName()
                                        : null)
                                .nonTrainingSubActivityName(m.getHandholdingSupport() != null &&
                                        m.getHandholdingSupport().getNonTrainingSubActivity() != null
                                        ? m.getHandholdingSupport().getNonTrainingSubActivity().getSubActivityName()
                                        : null)
                                .requirement(m.getRequirement())
                                .existingMachinery(m.getExistingMachinery())
                                .suggestedMachinery(m.getSuggestedMachinery())
                                .manufacturer(m.getManufacturer())
                                .groundingDate(DateUtil.dateToString(m.getGroundingDate(), "dd-MM-yyyy"))
                                .placeOfInstallation(m.getPlaceOfInstallation())
                                .costOfMachinery(m.getCostOfMachinery())
                                .build()
                ));

        // -------- CFC Support --------
        cfcSupportRepository
                .findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId)
                .forEach(c -> responses.add(
                        AccessToTechnologyAndInfrastructureResponse.builder()
                                .cfsSupportId(c.getId())
                                .handholdingSupportId(c.getHandholdingSupport() != null ? c.getHandholdingSupport().getId() : null)
                                .nonTrainingActivityId(c.getHandholdingSupport() != null &&
                                        c.getHandholdingSupport().getNonTrainingSubActivity() != null
                                        ? c.getHandholdingSupport().getNonTrainingSubActivity().getNonTrainingActivity().getActivityId()
                                        : null)
                                .nonTrainingSubActivityId(c.getHandholdingSupport() != null &&
                                        c.getHandholdingSupport().getNonTrainingSubActivity() != null
                                        ? c.getHandholdingSupport().getNonTrainingSubActivity().getSubActivityId()
                                        : null)
                                .nonTrainingActivityName(c.getHandholdingSupport() != null &&
                                        c.getHandholdingSupport().getNonTrainingSubActivity() != null
                                        ? c.getHandholdingSupport().getNonTrainingSubActivity().getNonTrainingActivity().getActivityName()
                                        : null)
                                .nonTrainingSubActivityName(c.getHandholdingSupport() != null &&
                                        c.getHandholdingSupport().getNonTrainingSubActivity() != null
                                        ? c.getHandholdingSupport().getNonTrainingSubActivity().getSubActivityName()
                                        : null)
                                .technologyDetails(c.getTechnologyDetails())
                                .vendorName(c.getVendorName())
                                .vendorContactNo(c.getVendorContactNo())
                                .vendorEmail(c.getVendorEmail())
                                .approxCost(c.getApproxCost())
                                .build()
                ));

        return WorkflowResponse.builder()
                .message("Fetched combined Access To Technology & Infrastructure data")
                .status(200)
                .data(responses)
                .build();
    }


}
