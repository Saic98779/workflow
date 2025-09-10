package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.NonTrainingResource;
import com.metaverse.workflow.model.NonTrainingResourceExpenditure;
import com.metaverse.workflow.nontrainingExpenditures.Dto.NonTrainingResourceExpenditureDto;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingResourceExpenditureRepo;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NonTrainingResourceExpenditureService {

    private final NonTrainingResourceExpenditureRepo expenditureRepo;
    private final NonTrainingResourceRepository resourceRepo;

    public NonTrainingResourceExpenditureDto save(NonTrainingResourceExpenditureDto dto) {
        NonTrainingResource resource = resourceRepo.findById(dto.getResourceId())
                .orElseThrow(() -> new RuntimeException("Resource not found with id " + dto.getResourceId()));

        NonTrainingResourceExpenditure entity = convertToEntity(dto);
        entity.setNonTrainingResource(resource);

        NonTrainingResourceExpenditure saved = expenditureRepo.save(entity);
        return convertToDto(saved);
    }

    private NonTrainingResourceExpenditureDto convertToDto(NonTrainingResourceExpenditure entity) {
        return NonTrainingResourceExpenditureDto.builder()
                .nonTrainingResourceExpenditureId(entity.getNonTrainingResourceExpenditureId())
                .resourceId(entity.getNonTrainingResource().getResourceId())
                .amount(entity.getAmount())
                .paymentForMonth(entity.getPaymentForMonth())
                .dateOfPayment(String.valueOf(entity.getDateOfPayment()))
                .build();
    }

    private NonTrainingResourceExpenditure convertToEntity(NonTrainingResourceExpenditureDto dto) {
        return NonTrainingResourceExpenditure.builder()
                .nonTrainingResourceExpenditureId(dto.getNonTrainingResourceExpenditureId())
                .amount(dto.getAmount())
                .paymentForMonth(dto.getPaymentForMonth())
                .dateOfPayment(DateUtil.covertStringToDate(dto.getDateOfPayment())).build();
    }

}