package com.metaverse.workflow.unified.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.aleap_handholding.request_dto.*;
import com.metaverse.workflow.aleap_handholding.service.*;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
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
}

