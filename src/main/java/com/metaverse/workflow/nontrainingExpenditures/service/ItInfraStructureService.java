package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.NonTrainingExpenditure;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.nontrainingExpenditures.Dto.NonTrainingExpenditureDto;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingExpenditureRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItInfraStructureService {

    private final NonTrainingExpenditureRepository expenditureRepo;
    private final NonTrainingSubActivityRepository subActivityRepo;

    public WorkflowResponse save(NonTrainingExpenditureDto dto) throws ParseException {

        NonTrainingSubActivity subActivity = subActivityRepo.findById(Long.valueOf(dto.getNonTrainingSubActivityId()))
                .orElseThrow(() -> new RuntimeException("SubActivity not found with id " + dto.getNonTrainingSubActivityId()));

        NonTrainingExpenditure entity = convertToEntity(dto);
//        entity.setNonTrainingSubActivity(subActivity);

        NonTrainingExpenditure saved = expenditureRepo.save(entity);
        return WorkflowResponse.builder().data(convertToDto(saved)).status(200).build();
    }

    public NonTrainingExpenditureDto getById(Long id) {
        NonTrainingExpenditure entity = expenditureRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Expenditure not found with id " + id));
        return convertToDto(entity);
    }

    public List<NonTrainingExpenditureDto> getBySubActivityId(Long subActivityId) {
        return expenditureRepo.findByNonTrainingSubActivity_SubActivityId(subActivityId)
                .stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private NonTrainingExpenditureDto convertToDto(NonTrainingExpenditure entity) {
        return NonTrainingExpenditureDto.builder()
                .id(entity.getId())
//                .nonTrainingSubActivityId(String.valueOf(entity.getNonTrainingSubActivity() != null ? entity.getNonTrainingSubActivity().getSubActivityId() : null))
                .paymentDate(String.valueOf(entity.getPaymentDate()))
                .expenditureAmount(entity.getExpenditureAmount())
                .billNo(entity.getBillNo())
                .billDate(String.valueOf(entity.getBillDate()))
                .payeeName(entity.getPayeeName())
                .accountNumber(entity.getAccountNumber())
                .bankName(entity.getBankName())
                .ifscCode(entity.getIfscCode())
                .modeOfPayment(entity.getModeOfPayment())
                .transactionId(entity.getTransactionId())
                .purpose(entity.getPurpose())
                .uploadBillUrl(entity.getUploadBillUrl())
                .build();
    }

    private NonTrainingExpenditure convertToEntity(NonTrainingExpenditureDto dto) throws ParseException {
        return NonTrainingExpenditure.builder()
                .id(dto.getId())
                .paymentDate(ItInfraStructureService.toDate(dto.getPaymentDate()))
                .expenditureAmount(dto.getExpenditureAmount())
                .billNo(dto.getBillNo())
                .billDate(ItInfraStructureService.toDate(dto.getBillDate()))
                .payeeName(dto.getPayeeName())
                .accountNumber(dto.getAccountNumber())
                .bankName(dto.getBankName())
                .ifscCode(dto.getIfscCode())
                .modeOfPayment(dto.getModeOfPayment())
                .transactionId(dto.getTransactionId())
                .purpose(dto.getPurpose())
                .uploadBillUrl(dto.getUploadBillUrl())
                .build();
    }

    public void delete(Long id) {
        if (!expenditureRepo.existsById(id)) {
            throw new RuntimeException("Expenditure not found with id " + id);
        }
        expenditureRepo.deleteById(id);
    }


    public static Date toDate(String dateStr) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.parse(dateStr);
    }
}
