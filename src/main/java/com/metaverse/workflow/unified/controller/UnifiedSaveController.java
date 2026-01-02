package com.metaverse.workflow.unified.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.aleap_handholding.request_dto.*;
import com.metaverse.workflow.aleap_handholding.service.*;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.formalisationcompliance.dto.FormalisationComplianceRequest;
import com.metaverse.workflow.formalisationcompliance.service.FormalisationComplianceService;
import com.metaverse.workflow.model.aleap_handholding.FormalisationCompliance;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/unified-handholding")
@RequiredArgsConstructor
public class UnifiedSaveController {

    private final AleapDesignStudioService aleapService;
    private final BusinessPlanDetailsService businessPlanService;
    private final BankNbfcFinanceService bankService;
    private final CFCSupportService cfcSupportService;
    private final CounsellingService counsellingService;
    private final CreditCounsellingService creditCounsellingService;
    private final GovtSchemeApplicationService govtSchemeApplicationService;
    private final GovtSchemeFinanceService govtSchemeFinanceService;
    private final LoanDocumentPreparationService loanDocumentPreparationService;
    private final MachineryIdentificationService machineryIdentificationService;
    private final MarketStudyService marketStudyService;
    private final SectorAdvisoryService sectorAdvisoryService;
    private final TradeFairParticipationService tradeFairParticipationService;
    private final VendorConnectionService vendorConnectionService;
    private final ActivityLogService logService;
    private final FormalisationComplianceService formalisationComplianceService;

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
            ObjectMapper mapper = new ObjectMapper();
            WorkflowResponse response;
            String lower = type == null ? "" : type.trim().toLowerCase();

            switch (lower) {
                case "aleapdesignstudio":
                case "aleap-design-studio":
                    AleapDesignStudioRequest aleapReq = mapper.readValue(data, AleapDesignStudioRequest.class);
                    response = aleapService.save(aleapReq, image1, image2, image3);
                    break;
                case "businessplan":
                case "business-plan":
                case "businessplandetails":
                    BusinessPlanRequest bpReq = mapper.readValue(data, BusinessPlanRequest.class);
                    response = businessPlanService.save(bpReq, file);
                    break;
                case "banknbfcfinance":
                case "bank-nbfc-finance":
                    BankNbfcFinanceRequest bankReq = mapper.readValue(data, BankNbfcFinanceRequest.class);
                    response = bankService.save(bankReq);
                    break;
                case "cfc-support":
                case "cfcsupport":
                    CFCSupportRequest cfcReq = mapper.readValue(data, CFCSupportRequest.class);
                    response = cfcSupportService.save(cfcReq);
                    break;
                case "counselling":
                    CounsellingRequest counsellingReq = mapper.readValue(data, CounsellingRequest.class);
                    response = counsellingService.save(counsellingReq);
                    break;
                case "creditcounselling":
                case "credit-counselling":
                    CreditCounsellingRequest creditReq = mapper.readValue(data, CreditCounsellingRequest.class);
                    response = creditCounsellingService.save(creditReq);
                    break;
                case "govtschemeapplication":
                case "govt-scheme-application":
                    GovtSchemeApplicationRequest govAppReq = mapper.readValue(data, GovtSchemeApplicationRequest.class);
                    response = govtSchemeApplicationService.save(govAppReq);
                    break;
                case "govtschemefinance":
                case "govt-scheme-finance":
                    GovtSchemeFinanceRequest govFinReq = mapper.readValue(data, GovtSchemeFinanceRequest.class);
                    response = govtSchemeFinanceService.save(govFinReq);
                    break;
                case "loandocumentpreparation":
                case "loan-document-preparation":
                    LoanDocumentPreparationRequest loanReq = mapper.readValue(data, LoanDocumentPreparationRequest.class);
                    response = loanDocumentPreparationService.save(loanReq);
                    break;
                case "machineryidentification":
                case "machinery-identification":
                    MachineryIdentificationRequest machReq = mapper.readValue(data, MachineryIdentificationRequest.class);
                    response = machineryIdentificationService.save(machReq);
                    break;
                case "marketstudy":
                case "market-study":
                    MarketStudyRequest marketReq = mapper.readValue(data, MarketStudyRequest.class);
                    response = marketStudyService.save(marketReq);
                    break;
                case "sectoradvisory":
                case "sector-advisory":
                    SectorAdvisoryRequest sectorReq = mapper.readValue(data, SectorAdvisoryRequest.class);
                    response = sectorAdvisoryService.save(sectorReq);
                    break;
                case "tradefairparticipation":
                case "trade-fair-participation":
                    TradeFairParticipationRequest tradeReq = mapper.readValue(data, TradeFairParticipationRequest.class);
                    response = tradeFairParticipationService.save(tradeReq);
                    break;
                case "vendorconnection":
                case "vendor-connection":
                    VendorConnectionRequest vendorReq = mapper.readValue(data, VendorConnectionRequest.class);
                    response = vendorConnectionService.save(vendorReq);
                    break;
                default:
                    return RestControllerBase.error(new DataException("Unknown type: " + type, "UNKNOWN_TYPE", 400));
            }

            String user = principal != null ? principal.getName() : "anonymous";
            logService.logs(user, "SAVE", type + " saved successfully", type, servletRequest.getRequestURI());

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return RestControllerBase.error(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
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

                case "aleapdesignstudio":
                case "aleap-design-studio":
                    AleapDesignStudioRequest aleapReq = mapper.readValue(data, AleapDesignStudioRequest.class);
                    response = aleapService.update(id, aleapReq, image1, image2, image3);
                    break;

                case "businessplan":
                case "business-plan":
                case "businessplandetails":
                    BusinessPlanRequest bpReq = mapper.readValue(data, BusinessPlanRequest.class);
                    response = businessPlanService.update(id, bpReq, file);
                    break;

                case "banknbfcfinance":
                case "bank-nbfc-finance":
                    BankNbfcFinanceRequest bankReq = mapper.readValue(data, BankNbfcFinanceRequest.class);
                    response = bankService.update(id, bankReq);
                    break;

                case "cfc-support":
                case "cfcsupport":
                    CFCSupportRequest cfcReq = mapper.readValue(data, CFCSupportRequest.class);
                    response = cfcSupportService.update(id, cfcReq);
                    break;

                case "counselling":
                    CounsellingRequest counsellingReq = mapper.readValue(data, CounsellingRequest.class);
                    response = counsellingService.update(id, counsellingReq);
                    break;

                case "creditcounselling":
                case "credit-counselling":
                    CreditCounsellingRequest creditReq = mapper.readValue(data, CreditCounsellingRequest.class);
                    response = creditCounsellingService.update(id, creditReq);
                    break;

                case "govtschemeapplication":
                case "govt-scheme-application":
                    GovtSchemeApplicationRequest govAppReq = mapper.readValue(data, GovtSchemeApplicationRequest.class);
                    response = govtSchemeApplicationService.update(id, govAppReq);
                    break;

                case "govtschemefinance":
                case "govt-scheme-finance":
                    GovtSchemeFinanceRequest govFinReq = mapper.readValue(data, GovtSchemeFinanceRequest.class);
                    response = govtSchemeFinanceService.update(id, govFinReq);
                    break;

                case "loandocumentpreparation":
                case "loan-document-preparation":
                    LoanDocumentPreparationRequest loanReq = mapper.readValue(data, LoanDocumentPreparationRequest.class);
                    response = loanDocumentPreparationService.update(id, loanReq);
                    break;

                case "machineryidentification":
                case "machinery-identification":
                    MachineryIdentificationRequest machReq = mapper.readValue(data, MachineryIdentificationRequest.class);
                    response = machineryIdentificationService.update(id, machReq);
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

                case "tradefairparticipation":
                case "trade-fair-participation":
                    TradeFairParticipationRequest tradeReq = mapper.readValue(data, TradeFairParticipationRequest.class);
                    response = tradeFairParticipationService.update(id, tradeReq);
                    break;

                case "vendorconnection":
                case "vendor-connection":
                    VendorConnectionRequest vendorReq = mapper.readValue(data, VendorConnectionRequest.class);
                    response = vendorConnectionService.update(id, vendorReq);
                    break;

                case "formalisationCompliance":
                case "formalisation-compliance":
                    FormalisationComplianceRequest request = mapper.readValue(data, FormalisationComplianceRequest.class);
                    response = formalisationComplianceService.update(id, request, file);
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
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
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

                case "aleapdesignstudio":
                case "aleap-design-studio":
                    response = aleapService.delete(id);
                    break;

                case "businessplan":
                case "business-plan":
                case "businessplandetails":
                    response = businessPlanService.delete(id);
                    break;

                case "banknbfcfinance":
                case "bank-nbfc-finance":
                    response = bankService.delete(id);
                    break;

                case "cfc-support":
                case "cfcsupport":
                    response = cfcSupportService.delete(id);
                    break;

                case "counselling":
                    response = counsellingService.delete(id);
                    break;

                case "creditcounselling":
                case "credit-counselling":
                    response = creditCounsellingService.delete(id);
                    break;

                case "govtschemeapplication":
                case "govt-scheme-application":
                    response = govtSchemeApplicationService.delete(id);
                    break;

                case "govtschemefinance":
                case "govt-scheme-finance":
                    response = govtSchemeFinanceService.delete(id);
                    break;

                case "loandocumentpreparation":
                case "loan-document-preparation":
                    response = loanDocumentPreparationService.delete(id);
                    break;

                case "machineryidentification":
                case "machinery-identification":
                    response = machineryIdentificationService.delete(id);
                    break;

                case "marketstudy":
                case "market-study":
                    response = marketStudyService.delete(id);
                    break;

                case "sectoradvisory":
                case "sector-advisory":
                    response = sectorAdvisoryService.delete(id);
                    break;

                case "tradefairparticipation":
                case "trade-fair-participation":
                    response = tradeFairParticipationService.delete(id);
                    break;

                case "vendorconnection":
                case "vendor-connection":
                    response = vendorConnectionService.delete(id);
                    break;

                case "formalisationCompliance":
                case "formalisation-compliance":
                    response = formalisationComplianceService.delete(id);
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

