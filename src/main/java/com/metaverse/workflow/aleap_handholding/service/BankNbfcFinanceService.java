package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.repository.BankNbfcFinanceRepository;
import com.metaverse.workflow.aleap_handholding.request_dto.BankNbfcFinanceRequest;
import com.metaverse.workflow.aleap_handholding.response_dto.BankNbfcFinanceResponse;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.aleap_handholding.BankNbfcFinance;
import com.metaverse.workflow.model.aleap_handholding.HandholdingSupport;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankNbfcFinanceService {

    private final HandholdingSupportService service;
    private final BankNbfcFinanceRepository repository;

    public WorkflowResponse save(BankNbfcFinanceRequest request) throws DataException {

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );

        BankNbfcFinance entity = RequestMapper.mapToBankNbfcFinance(request, support);

        BankNbfcFinance saved = repository.save(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Bank / NBFC Finance saved successfully")
                .data(ResponseMapper.mapToBankNbfcFinanceResponse(saved))
                .build();
    }

    @Transactional
    public WorkflowResponse update(Long bankNbfcId, BankNbfcFinanceRequest request) throws DataException {

        BankNbfcFinance existing = repository.findById(bankNbfcId)
                .orElseThrow(() -> new DataException(
                        "Bank/NBFC Finance not found with id " + bankNbfcId,
                        "BANK_NBFC_NOT_FOUND",
                        400
                ));

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );

        existing.setHandholdingSupport(support);

        RequestMapper.updateBankNbfcFinance(existing, request);

        BankNbfcFinance updated = repository.save(existing);

        return WorkflowResponse.builder()
                .status(200)
                .message("Bank / NBFC Finance updated successfully")
                .data(ResponseMapper.mapToBankNbfcFinanceResponse(updated))
                .build();
    }

    public WorkflowResponse getById(Long bankNbfcId) throws DataException {

        BankNbfcFinance entity = repository.findById(bankNbfcId)
                .orElseThrow(() -> new DataException(
                        "Bank/NBFC Finance not found with id " + bankNbfcId,
                        "BANK_NBFC_NOT_FOUND",
                        404
                ));

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(ResponseMapper.mapToBankNbfcFinanceResponse(entity))
                .build();
    }

    public WorkflowResponse getByNonTrainingSubActivityId(Long subActivityId) {

        List<BankNbfcFinanceResponse> response = repository
                .findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId)
                .stream()
                .map(ResponseMapper::mapToBankNbfcFinanceResponse)
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(response)
                .build();
    }

    public WorkflowResponse delete(Long bankNbfcId) throws DataException {

        BankNbfcFinance entity = repository.findById(bankNbfcId)
                .orElseThrow(() -> new DataException(
                        "Bank/NBFC Finance not found with id " + bankNbfcId,
                        "BANK_NBFC_NOT_FOUND",
                        400
                ));

        repository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Bank / NBFC Finance deleted successfully")
                .build();
    }
}
