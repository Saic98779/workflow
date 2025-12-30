package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.dto.FeasibilityInputRequest;
import com.metaverse.workflow.aleap_handholding.dto.MarketStudyRequest;
import com.metaverse.workflow.aleap_handholding.repository.FeasibilityInputRepository;
import com.metaverse.workflow.aleap_handholding.repository.MarketStudyRepository;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.aleap_handholding.FeasibilityInput;
import com.metaverse.workflow.model.aleap_handholding.HandholdingSupport;
import com.metaverse.workflow.model.aleap_handholding.MarketStudy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MarketStudyService {

    private final HandholdingSupportService service;
    private final MarketStudyRepository marketStudyRepository;
    private final FeasibilityInputRepository inputRepository;

    @Transactional
    public MarketStudy save(MarketStudyRequest request) throws DataException {

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getNonTrainingAction()
        );

        MarketStudy study = new MarketStudy();
        study.setHandholdingSupport(support);
        study.setDateOfStudy(DateUtil.covertStringToDate(request.getDateOfStudy()));

        return marketStudyRepository.save(study);
    }

    public FeasibilityInput saveFeasibilityInput(FeasibilityInputRequest request) {

        MarketStudy study = marketStudyRepository.findById(request.getMarketStudyId())
                .orElseThrow(() -> new RuntimeException("MarketStudy not found"));

        FeasibilityInput input = new FeasibilityInput();
        input.setMarketStudy(study);
        input.setInputDetails(request.getInputDetails());
        input.setSource(request.getSource());
        input.setSector(request.getSector());
        input.setFeasibilityActivity(request.getFeasibilityActivity());

        return inputRepository.save(input);
    }
}
