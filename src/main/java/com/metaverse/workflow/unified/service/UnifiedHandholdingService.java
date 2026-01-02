package com.metaverse.workflow.unified.service;

import com.metaverse.workflow.aleap_handholding.service.*;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnifiedHandholdingService {

    private static final Logger log = LoggerFactory.getLogger(UnifiedHandholdingService.class);

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
    private final FormalisationComplianceService formalisationComplianceService;

    public WorkflowResponse get(String type, Long id, Long subActivityId) throws DataException {
        String lower = type == null ? "" : type.trim().toLowerCase();
        WorkflowResponse response;

        boolean hasId = id != null;
        boolean hasSub = subActivityId != null;

        switch (lower) {
            case "aleapdesignstudio":
            case "aleap-design-studio":
                if (hasId) {
                    response = aleapService.getById(id);
                } else if (hasSub) {
                    response = aleapService.getByNonTrainingSubActivityId(subActivityId);
                } else {
                    throw new DataException("Missing id or subActivityId for aleap-design-studio", "MISSING_PARAM", 400);
                }
                break;
            case "businessplan":
            case "business-plan":
            case "businessplandetails":
                if (hasId) {
                    response = businessPlanService.getById(id);
                } else if (hasSub) {
                    response = businessPlanService.getByNonTrainingSubActivityId(subActivityId);
                } else {
                    throw new DataException("Missing id or subActivityId for business-plan", "MISSING_PARAM", 400);
                }
                break;
            case "banknbfcfinance":
            case "bank-nbfc-finance":
                if (hasId) {
                    response = bankService.getById(id);
                } else if (hasSub) {
                    response = bankService.getByNonTrainingSubActivityId(subActivityId);
                } else {
                    throw new DataException("Missing id or subActivityId for bank-nbfc-finance", "MISSING_PARAM", 400);
                }
                break;
            case "cfc-support":
            case "cfcsupport":
                if (hasId) {
                    response = cfcSupportService.getById(id);
                } else if (hasSub) {
                    response = cfcSupportService.getByNonTrainingSubActivityId(subActivityId);
                } else {
                    throw new DataException("Missing id or subActivityId for cfcsupport", "MISSING_PARAM", 400);
                }
                break;
            case "counselling":
                if (hasId) {
                    response = counsellingService.getById(id);
                } else if (hasSub) {
                    response = counsellingService.getByNonTrainingSubActivityId(subActivityId);
                } else {
                    throw new DataException("Missing id or subActivityId for counselling", "MISSING_PARAM", 400);
                }
                break;
            case "creditcounselling":
            case "credit-counselling":
                if (hasId) {
                    response = creditCounsellingService.getById(id);
                } else if (hasSub) {
                    response = creditCounsellingService.getByNonTrainingSubActivityId(subActivityId);
                } else {
                    throw new DataException("Missing id or subActivityId for credit-counselling", "MISSING_PARAM", 400);
                }
                break;
            case "govtschemeapplication":
            case "govt-scheme-application":
                if (hasId) {
                    response = govtSchemeApplicationService.getById(id);
                } else if (hasSub) {
                    response = govtSchemeApplicationService.getBySubActivityId(subActivityId);
                } else {
                    throw new DataException("Missing id or subActivityId for govt-scheme-application", "MISSING_PARAM", 400);
                }
                break;
            case "govtschemefinance":
            case "govt-scheme-finance":
                if (hasId) {
                    response = govtSchemeFinanceService.getById(id);
                } else if (hasSub) {
                    response = govtSchemeFinanceService.getByNonTrainingSubActivityId(subActivityId);
                } else {
                    throw new DataException("Missing id or subActivityId for govt-scheme-finance", "MISSING_PARAM", 400);
                }
                break;
            case "loandocumentpreparation":
            case "loan-document-preparation":
                if (hasId) {
                    response = loanDocumentPreparationService.getById(id);
                } else if (hasSub) {
                    response = loanDocumentPreparationService.getByNonTrainingSubActivityId(subActivityId);
                } else {
                    throw new DataException("Missing id or subActivityId for loan-document-preparation", "MISSING_PARAM", 400);
                }
                break;
            case "machineryidentification":
            case "machinery-identification":
                if (hasId) {
                    response = machineryIdentificationService.getById(id);
                } else if (hasSub) {
                    response = machineryIdentificationService.getByNonTrainingSubActivityId(subActivityId);
                } else {
                    throw new DataException("Missing id or subActivityId for machinery-identification", "MISSING_PARAM", 400);
                }
                break;
            case "marketstudy":
            case "market-study":
                if (hasId) {
                    response = marketStudyService.getById(id);
                } else if (hasSub) {
                    response = marketStudyService.getByNonTrainingSubActivityId(subActivityId);
                } else {
                    throw new DataException("Missing id or subActivityId for market-study", "MISSING_PARAM", 400);
                }
                break;
            case "sectoradvisory":
            case "sector-advisory":
                if (hasId) {
                    response = sectorAdvisoryService.getById(id);
                } else if (hasSub) {
                    response = sectorAdvisoryService.getByNonTrainingSubActivityId(subActivityId);
                } else {
                    throw new DataException("Missing id or subActivityId for sector-advisory", "MISSING_PARAM", 400);
                }
                break;
            case "tradefairparticipation":
            case "trade-fair-participation":
                if (hasId) {
                    response = tradeFairParticipationService.getById(id);
                } else if (hasSub) {
                    response = tradeFairParticipationService.getByNonTrainingSubActivityId(subActivityId);
                } else {
                    throw new DataException("Missing id or subActivityId for trade-fair-participation", "MISSING_PARAM", 400);
                }
                break;
            case "vendorconnection":
            case "vendor-connection":
                if (hasId) {
                    response = vendorConnectionService.getById(id);
                } else if (hasSub) {
                    response = vendorConnectionService.getByNonTrainingSubActivityId(subActivityId);
                } else {
                    throw new DataException("Missing id or subActivityId for vendor-connection", "MISSING_PARAM", 400);
                }
                break;
            case "formalisationcompliance":
            case "formalisation-compliance":
                if (hasId) {
                    response = formalisationComplianceService.getById(id);
                } else if (hasSub) {
                    response = formalisationComplianceService.getByNonTrainingSubActivityId(subActivityId);
                } else {
                    throw new DataException("Missing id or subActivityId for formalisationcompliance", "MISSING_PARAM", 400);
                }
                break;
            default:
                throw new DataException("Unknown type: " + type, "UNKNOWN_TYPE", 400);
        }

        // generic info log for GET operations
        if (id != null) {
            log.info("Fetched {} with ID: {}", type, id);
        } else if (subActivityId != null) {
            log.info("Fetched {} for subActivityId: {}", type, subActivityId);
        } else {
            log.info("Fetched {} (all)", type);
        }

        return response;
    }
}
