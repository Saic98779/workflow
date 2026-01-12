package com.metaverse.workflow.unified.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.aleap_handholding.request_dto.*;
import com.metaverse.workflow.aleap_handholding.service.*;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.unified.service.UnifiedHandholdingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/unified-handholding")
@RequiredArgsConstructor
@Tag(name = "Unified", description = "Aleap Handholding")
public class UnifiedHandHoldingController extends RestControllerBase {

    private static final Logger log = LoggerFactory.getLogger(UnifiedHandHoldingController.class);
    private final BusinessPlanDetailsService businessPlanService;
    private final CounsellingService counsellingService;
    private final GovtSchemeApplicationService govtSchemeApplicationService;
    private final MarketStudyService marketStudyService;
    private final SectorAdvisoryService sectorAdvisoryService;
    private final ActivityLogService logService;
    private final FormalisationComplianceService formalisationComplianceService;
    private final AccessToFinanceService accessToFinanceService;
    private final AccessToTechnologyAndInfrastructureService infrastructureService;
    private final UnifiedHandholdingService unifiedHandholdingService;
    private final AccessToPackagingLabellingAndBrandingService accessToPackagingLabellingAndBrandingService;


    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(
            Principal principal,
            @RequestPart("type") String type,
            @RequestPart("data") String data,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "image1", required = false) MultipartFile image1,
            @RequestPart(value = "image2", required = false) MultipartFile image2,
            @RequestPart(value = "image3", required = false) MultipartFile image3,
            HttpServletRequest servletRequest) {
        try {
            WorkflowResponse response = unifiedHandholdingService.save(type, data, file, image1, image2, image3);
            String user = principal != null ? principal.getName() : "anonymous";
            logService.logs(user, "FETCH", type + " fetched successfully", type, servletRequest.getRequestURI());
            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return RestControllerBase.error(e);
        }

    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(
            Principal principal,
            @PathVariable Long id,
            @RequestPart("type") String type,
            @RequestPart("data") String data,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "image1", required = false) MultipartFile image1,
            @RequestPart(value = "image2", required = false) MultipartFile image2,
            @RequestPart(value = "image3", required = false) MultipartFile image3,
            HttpServletRequest servletRequest) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            WorkflowResponse response;

            String lower = type == null ? "" : type.trim().toLowerCase();

            switch (lower) {
                case "businessplan":
                case "business-plan":
                case "businessplandetails":
                    BusinessPlanRequest bpReq = mapper.readValue(data, BusinessPlanRequest.class);
                    response = businessPlanService.update(id, bpReq, file);
                    break;

                case "counselling":
                    CounsellingRequest counsellingReq = mapper.readValue(data, CounsellingRequest.class);
                    response = counsellingService.update(id, counsellingReq);
                    break;

                case "govtschemeapplication":
                case "govt-scheme-application":
                    GovtSchemeApplicationRequest govAppReq = mapper.readValue(data, GovtSchemeApplicationRequest.class);
                    response = govtSchemeApplicationService.update(id, govAppReq);
                    break;

                case "marketstudy":
                case "market-study":
                    MarketStudyRequest marketReq = mapper.readValue(data, MarketStudyRequest.class);
                    response = marketStudyService.update(id, marketReq);
                    break;

                case "sectoradvisory":
                case "sector-advisory":
                    SectorAdvisoryRequest sectorReq = mapper.readValue(data, SectorAdvisoryRequest.class);
                    response = sectorAdvisoryService.update(id, sectorReq);
                    break;

                case "formalisationcompliance":
                case "formalisation-compliance":
                    FormalisationComplianceRequest request = mapper.readValue(data, FormalisationComplianceRequest.class);
                    response = formalisationComplianceService.update(id, request, file);
                    break;
                case "accesstofinance":
                case "access-to-finance":
                    AccessToFinanceRequest accessToFinanceRequest = mapper.readValue(data, AccessToFinanceRequest.class);
                    response = accessToFinanceService.update(id, accessToFinanceRequest);
                    break;
                case "accesstotechnology":
                case "access-to-technology":
                    AccessToTechnologyAndInfrastructureRequest infrastructureRequest = mapper.readValue(data, AccessToTechnologyAndInfrastructureRequest.class);
                    response = infrastructureService.update(id, infrastructureRequest);
                    break;
                case "accesstopackaginglabellingandbranding":
                case "access-to-packaging-labelling-and-branding":
                    AccessToPackagingLabellingAndBrandingRequest packagingLabellingAndBrandingRequest = mapper.readValue(data, AccessToPackagingLabellingAndBrandingRequest.class);
                    response = accessToPackagingLabellingAndBrandingService.update(id,packagingLabellingAndBrandingRequest,image1,image2,image3);
                    break;
                default:
                    return RestControllerBase.error(
                            new DataException("Unknown type: " + type, "UNKNOWN_TYPE", 400)
                    );
            }

            String user = principal != null ? principal.getName() : "anonymous";
            logService.logs(user, "UPDATE", type + " updated successfully", type, servletRequest.getRequestURI());

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return RestControllerBase.error(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> get(
            Principal principal,
            @RequestParam("type") String type,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "subActivityId", required = false) Long subActivityId,
            HttpServletRequest servletRequest) {

        try {
            WorkflowResponse response = unifiedHandholdingService.get(type, id, subActivityId);
            String user = principal != null ? principal.getName() : "anonymous";
            logService.logs(user, "FETCH", type + " fetched successfully", type, servletRequest.getRequestURI());
            if (id != null) {
                log.info("Fetched {} with ID: {}", type, id);
            } else if (subActivityId != null) {
                log.info("Fetched {} for subActivityId: {}", type, subActivityId);
            } else {
                log.info("Fetched {} (all)", type);
            }

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
            Principal principal,
            @PathVariable Long id,
            @RequestParam("type") String type,
            HttpServletRequest servletRequest) {

        try {
            WorkflowResponse response;
            String lower = type == null ? "" : type.trim().toLowerCase();

            switch (lower) {
                case "businessplan":
                case "business-plan":
                case "businessplandetails":
                    response = businessPlanService.delete(id);
                    break;

                case "counselling":
                    response = counsellingService.delete(id);
                    break;

                case "govtschemeapplication":
                case "govt-scheme-application":
                    response = govtSchemeApplicationService.delete(id);
                    break;

                case "marketstudy":
                case "market-study":
                    response = marketStudyService.delete(id);
                    break;

                case "sectoradvisory":
                case "sector-advisory":
                    response = sectorAdvisoryService.delete(id);
                    break;

                case "formalisationcompliance":
                case "formalisation-compliance":
                    response = formalisationComplianceService.delete(id);
                    break;
                case "accesstofinance":
                case "access-to-finance":
                    response = accessToFinanceService.delete(id);
                    break;
                case "accesstotechnology":
                case "access-to-technology":
                    response = infrastructureService.delete(id);
                    break;
                case "accesstopackaginglabellingandbranding":
                case "access-to-packaging-labelling-and-branding":
                    response = accessToPackagingLabellingAndBrandingService.delete(id);
                    break;
                default:
                    return RestControllerBase.error(
                            new DataException("Unknown type: " + type, "UNKNOWN_TYPE", 400)
                    );
            }

            String user = principal != null ? principal.getName() : "anonymous";
            logService.logs(user, "DELETE", type + " deleted successfully", type, servletRequest.getRequestURI());

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

}
